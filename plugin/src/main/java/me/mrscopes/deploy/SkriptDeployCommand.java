package me.mrscopes.deploy;

import io.papermc.paper.command.brigadier.Commands;
import me.mrscopes.MrScopes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static me.mrscopes.Utilities.hasPermission;

public class SkriptDeployCommand {
    private final MrScopes plugin;

    public SkriptDeployCommand(Commands commands) {
        this.plugin = MrScopes.getInstance();

        commands.register(
                Commands.literal("skriptdeploy")
                        .requires(hasPermission("beans.admin"))
                        .executes(context -> {
                            deploy(context.getSource().getSender());
                            return 1;
                        })
                        .build()
        );
    }

    private void deploy(CommandSender sender) {
        sender.sendMessage(Component.text("Pulling Skript scripts from Git...", NamedTextColor.YELLOW));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                DeployResult result = pullCopyAndDetectChanges(sender);

                Bukkit.getScheduler().runTask(plugin, () -> reloadChangedScripts(sender, result));
            } catch (Exception exception) {
                sender.sendMessage(Component.text("Skript deploy failed: " + exception.getMessage(), NamedTextColor.RED));
                plugin.getLogger().severe("Skript deploy failed");
                exception.printStackTrace();
            }
        });
    }

    private DeployResult pullCopyAndDetectChanges(CommandSender sender) throws IOException, InterruptedException {
        Path repoFolder = Path.of("plugins/Skript");
        String repoScriptsFolderName = "scripts";
        Path repoScriptsFolder = repoFolder.resolve(repoScriptsFolderName).normalize();
        Path skriptScriptsFolder = Path.of("plugins/Skript/scripts");
        Path backupsFolder = Path.of("plugins/beans/skript-backups");

        if (!Files.isDirectory(repoFolder)) {
            throw new IOException("Repo folder does not exist: " + repoFolder);
        }

        if (!Files.isDirectory(repoScriptsFolder)) {
            throw new IOException("Repo scripts folder does not exist: " + repoScriptsFolder);
        }

        String beforeCommit = firstLine(runCommandCapture(repoFolder, "git", "rev-parse", "HEAD"));

        List<String> pullOutput = runCommandCapture(repoFolder, "git", "pull", "--ff-only");
        sendLines(sender, "git", pullOutput);

        String afterCommit = firstLine(runCommandCapture(repoFolder, "git", "rev-parse", "HEAD"));

        if (beforeCommit.equals(afterCommit)) {
            return new DeployResult(List.of(), false, true);
        }

        List<String> changedFiles = runCommandCapture(repoFolder, "git", "diff", "--name-status", beforeCommit, afterCommit);

        List<String> scriptsToReload = new ArrayList<>();
        boolean needsFullReload = false;

        backupCurrentScripts(skriptScriptsFolder, backupsFolder);

        for (String changedFile : changedFiles) {
            ChangedScript changedScript = parseChangedScript(changedFile, repoScriptsFolderName);

            if (changedScript == null) {
                continue;
            }

            Path target = skriptScriptsFolder.resolve(changedScript.relativeScriptPath()).normalize();

            if (changedScript.deleted()) {
                Files.deleteIfExists(target);
                needsFullReload = true;
                continue;
            }

            Path source = repoScriptsFolder.resolve(changedScript.relativeScriptPath()).normalize();

            if (!Files.exists(source)) {
                needsFullReload = true;
                continue;
            }

            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            scriptsToReload.add(toSkriptPath(changedScript.relativeScriptPath()));
        }

        return new DeployResult(scriptsToReload, needsFullReload, false);
    }

    private ChangedScript parseChangedScript(String gitDiffLine, String repoScriptsFolderName) {
        String[] parts = gitDiffLine.split("\\t");

        if (parts.length < 2) {
            return null;
        }

        String status = parts[0];
        String file = parts[parts.length - 1].replace('\\', '/');
        String prefix = repoScriptsFolderName.replace('\\', '/').replaceAll("/+$", "") + "/";

        if (!file.startsWith(prefix) || !file.endsWith(".sk")) {
            return null;
        }

        Path relativeScriptPath = Paths.get(file.substring(prefix.length()));
        boolean deleted = status.startsWith("D");

        return new ChangedScript(relativeScriptPath, deleted);
    }

    private void reloadChangedScripts(CommandSender sender, DeployResult result) {
        if (result.noChanges()) {
            sender.sendMessage(Component.text("No Git changes found. Nothing to reload.", NamedTextColor.GRAY));
            return;
        }

        if (result.needsFullReload()) {
            sender.sendMessage(Component.text("Deleted or missing script detected. Reloading all scripts...", NamedTextColor.YELLOW));
            Bukkit.dispatchCommand(sender, "skript reload scripts");
            sender.sendMessage(Component.text("Skript deploy complete with full reload.", NamedTextColor.GREEN));
            return;
        }

        if (result.scriptsToReload().isEmpty()) {
            sender.sendMessage(Component.text("Git pulled successfully, but no .sk files changed.", NamedTextColor.GRAY));
            return;
        }

        for (String script : result.scriptsToReload()) {
            sender.sendMessage(Component.text("Reloading " + script + "...", NamedTextColor.YELLOW));
            Bukkit.dispatchCommand(sender, "skript reload " + script);
        }

        sender.sendMessage(Component.text("Skript deploy complete. Reloaded " + result.scriptsToReload().size() + " changed script(s).", NamedTextColor.GREEN));
    }

    private void backupCurrentScripts(Path skriptScriptsFolder, Path backupsFolder) throws IOException {
        if (!Files.isDirectory(skriptScriptsFolder)) {
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path backupTarget = backupsFolder.resolve(timestamp);

        try (var paths = Files.walk(skriptScriptsFolder)) {
            for (Path source : paths.toList()) {
                Path relative = skriptScriptsFolder.relativize(source);
                Path target = backupTarget.resolve(relative);

                if (Files.isDirectory(source)) {
                    Files.createDirectories(target);
                } else {
                    Files.createDirectories(target.getParent());
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private List<String> runCommandCapture(Path workingDirectory, String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDirectory.toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        List<String> output = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException(String.join("\n", output));
        }

        return output;
    }

    private void sendLines(CommandSender sender, String prefix, List<String> lines) {
        for (String line : lines) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    sender.sendMessage(Component.text("[" + prefix + "] " + line, NamedTextColor.GRAY))
            );
        }
    }

    private String firstLine(List<String> lines) throws IOException {
        if (lines.isEmpty()) {
            throw new IOException("Command returned no output.");
        }

        return lines.getFirst();
    }

    private String toSkriptPath(Path path) {
        return path.toString().replace('\\', '/');
    }

    private record ChangedScript(Path relativeScriptPath, boolean deleted) {
    }

    private record DeployResult(List<String> scriptsToReload, boolean needsFullReload, boolean noChanges) {
    }
}
