package me.mrscopes.beans.skript;

import me.mrscopes.beans.skript.effects.EffCustomEnchantments;
import me.mrscopes.beans.skript.expressions.*;

import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.addon.AddonModule;

public class BeansSkript implements AddonModule {
    private final SkriptAddon skriptAddon;
    public SkriptAddon getSkriptAddon() {
        return skriptAddon;
    }

    public BeansSkript() {
        skriptAddon = Skript.instance().registerAddon(BeansSkript.class, "Beans");
        skriptAddon.loadModules(this);
    }

    @Override
    public void load(SkriptAddon skriptAddon) {
        ExprBeans.register(skriptAddon.syntaxRegistry());
        ExprCustomEnchantment.register(skriptAddon.syntaxRegistry());
        EffCustomEnchantments.register(skriptAddon.syntaxRegistry());
    }

    @Override
    public String name() {
        return "beans";
    }
}
