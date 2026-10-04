package me.mrscopes.beans.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.LoopSection;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

/**
 * simple thread switching
 *
 * async:
 *     # runs asynchronously
 *
 * sync:
 *     # runs on the main server thread
 * 
 * async:
 *     # off thread
 *     sync:
 *         # back on the main server thread
 */
public class SecThread extends LoopSection {

    public static void register(SyntaxRegistry registry) {
        registry.register(
            SyntaxRegistry.SECTION,
            SyntaxInfo.simple(
                SecThread.class,
                SecThread::new,
                "async",
                "sync"
            )
        );
    }

    private boolean async;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        this.async = matchedPattern == 0;

        ParserInstance parserInstance = ParserInstance.get();
        Kleenean hasDelayBefore = parserInstance.getHasDelayBefore();
        parserInstance.setHasDelayBefore(Kleenean.TRUE);
        loadCode(sectionNode);
        parserInstance.setHasDelayBefore(hasDelayBefore);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        Object localVariables = Variables.copyLocalVariables(event);

        Runnable runnable = () -> {
            assert this.first != null;
            Variables.setLocalVariables(event, localVariables);
            TriggerItem.walk(this.first, event);
            Variables.removeLocals(event);
        };

        if (this.async) {
            Bukkit.getScheduler().runTaskAsynchronously(Skript.getInstance(), runnable);
        } else {
            Bukkit.getScheduler().runTask(Skript.getInstance(), runnable);
        }

        // Keep the scheduled section body from walking into code after the section.
        if (last != null) last.setNext(null);
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return this.async ? "async" : "sync";
    }

    @Override
    public TriggerItem getActualNext() {
        return null;
    }
}
