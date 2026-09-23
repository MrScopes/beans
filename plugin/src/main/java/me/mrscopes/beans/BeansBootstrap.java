package me.mrscopes.beans;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import io.papermc.paper.registry.keys.tags.DialogTagKeys;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class BeansBootstrap implements PluginBootstrap {

    private static final TypedKey<Dialog> MENU = DialogKeys.create(Key.key("beans", "menu"));
    private static final TypedKey<Dialog> LINKS = DialogKeys.create(Key.key("beans", "links"));

    @Override
    public void bootstrap(BootstrapContext context) {

        // Dialogs
        context.getLifecycleManager().registerEventHandler(
                RegistryEvents.DIALOG.compose(),
                event -> {

                    // Main menu
                    event.registry().register(
                            MENU,
                            builder -> builder
                                    .base(DialogBase.builder(Component.text("beans"))
                                            .externalTitle(Component.text("beans").color(NamedTextColor.AQUA))
                                            .canCloseWithEscape(true)
                                            .pause(false)
                                            .build())
                                    .type(DialogType.multiAction(List.of(
                                            ActionButton.builder(Component.text("Settings"))
                                                    .tooltip(Component.text("Manage your settings"))
                                                    .width(150)
                                                    .action(DialogAction.customClick(
                                                            Key.key("beans", "settings"),
                                                            null
                                                    ))
                                                    .build(),

                                            ActionButton.builder(Component.text("Links"))
                                                    .tooltip(Component.text("Useful links"))
                                                    .width(150)
                                                    .action(DialogAction.customClick(
                                                            Key.key("beans", "links"),
                                                            null
                                                    ))
                                                    .build()
                                    )).build())
                    );

                    // Links
                    event.registry().register(
                            LINKS,
                            builder -> builder
                                    .base(DialogBase.builder(Component.text("Links"))
                                            .externalTitle(Component.text("Links"))
                                            .canCloseWithEscape(true)
                                            .pause(false)
                                            .build())
                                    .type(DialogType.multiAction(List.of(
                                            ActionButton.builder(Component.text("Discord"))
                                                    .tooltip(Component.text("Join the beans Discord"))
                                                    .width(150)
                                                    .action(DialogAction.staticAction(
                                                            ClickEvent.openUrl(
                                                                    "https://discord.gg/wjQz6ea6JV"
                                                            )
                                                    ))
                                                    .build(),

                                            ActionButton.builder(Component.text("GitHub"))
                                                    .tooltip(Component.text("View beans on GitHub"))
                                                    .width(150)
                                                    .action(DialogAction.staticAction(
                                                            ClickEvent.openUrl(
                                                                    "https://github.com/MrScopes/beans"
                                                            )
                                                    ))
                                                    .build()
                                    )).build())
                    );
                }
        );

        // ESC -> Custom Options -> beans menu
        context.getLifecycleManager().registerEventHandler(
                LifecycleEvents.TAGS.preFlatten(RegistryKey.DIALOG),
                event -> event.registrar().setTag(
                        DialogTagKeys.PAUSE_SCREEN_ADDITIONS,
                        Set.of(TagEntry.valueEntry(MENU))
                )
        );
    }
}