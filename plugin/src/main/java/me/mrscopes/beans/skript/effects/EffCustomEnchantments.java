package me.mrscopes.beans.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import me.mrscopes.beans.enchantments.CustomEnchantments;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffCustomEnchantments extends Effect {
    public static void register(SyntaxRegistry registry) {
        registry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(EffCustomEnchantments.class)
                .supplier(EffCustomEnchantments::new)
                .addPattern("store custom enchantments of %itemstack% in %objects%")
                .build());
    }

    private Expression<ItemStack> itemstack;
    private Variable<?> variableStorage;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,  SkriptParser.ParseResult parseResult) {
        itemstack = (Expression<ItemStack>) exprs[0];

        if (!(exprs[1] instanceof Variable<?> variable)) {
            Skript.error("The custom enchantments must be stored in a variable.");
            return false;
        }

        if (!variable.isList()) {
            Skript.error("The custom enchantments must be stored in a list variable, such as {_enchantments::*}.");
            return false;
        }

        variableStorage = variable;
        return true;
    }

    @Override
    protected void execute(Event event) {
        ItemStack item = itemstack.getSingle(event);
        if (item == null)
            return;

        String variableName = variableStorage.getName().toString(event);

        String variablePrefix = variableName.substring(0, variableName.length() - 1);

        for (CustomEnchantments.Enchantment enchantment : CustomEnchantments.getEnchantments(item)) {
            Variables.setVariable(
                    variablePrefix + enchantment.enchantment(),
                    enchantment.level(),
                    event,
                    variableStorage.isLocal()
            );
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "store custom enchantments of " + itemstack.toString(event, b) + " in " + variableStorage.toString(event, b);
    }
}
