package me.mrscopes.beans.skript.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.mrscopes.beans.enchantments.CustomEnchantments;
import org.bukkit.event.Event;
import ch.njol.skript.aliases.ItemType;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprCustomEnchantment extends SimpleExpression<Long> {

    public static void register(SyntaxRegistry registry) {
        registry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprCustomEnchantment.class, Long.class)
                .supplier(ExprCustomEnchantment::new)
                .addPattern("level of custom enchantment %string% of %itemtype%")
                .build());
    }

    Expression<String> enchantmentExpression;
    Expression<ItemType> itemTypeExpression;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        enchantmentExpression = (Expression<String>) expressions[0];
        itemTypeExpression = (Expression<ItemType>) expressions[1];

        return true;
    }

    @Override
    protected @Nullable Long[] get(Event event) {
        ItemType itemType = itemTypeExpression.getSingle(event);
        if (itemType == null)
            return null;

        Long level = CustomEnchantments.getEnchantmentLevel(itemType, enchantmentExpression.getSingle(event));
        return new Long[]{level};
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        return switch (mode) {
            case ADD, SET, DELETE, REMOVE -> new Class[]{Long.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        if (delta == null)
            return;

        Long changeValue = (Long) delta[0];
        if (changeValue == null && mode != ChangeMode.DELETE)
            return;

        ItemType itemType = itemTypeExpression.getSingle(event);
        if (itemType == null)
            return;

        Long currentLevel = CustomEnchantments.getEnchantmentLevel(itemType, enchantmentExpression.getSingle(event));

        switch (mode) {
            case ADD:
                CustomEnchantments.setEnchantmentLevel(itemType, enchantmentExpression.getSingle(event), (currentLevel == null ? 0 : currentLevel) + changeValue);
                break;
            case REMOVE:
                CustomEnchantments.setEnchantmentLevel(itemType, enchantmentExpression.getSingle(event), (currentLevel == null ? 0 : currentLevel) - changeValue);
                break;
            case SET:
                CustomEnchantments.setEnchantmentLevel(itemType, enchantmentExpression.getSingle(event), changeValue);
                break;
            case DELETE:
                CustomEnchantments.setEnchantmentLevel(itemType, enchantmentExpression.getSingle(event), 0L);
                break;
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "level of custom enchantment " + enchantmentExpression.toString(event, debug) +  " of " + itemTypeExpression.toString(event, debug);
    }
}
