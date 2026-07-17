package me.mrscopes.beans.skript.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.KeyProviderExpression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.mrscopes.beans.enchantments.CustomEnchantments;
import me.mrscopes.beans.enchantments.CustomEnchantments.CustomEnchantment;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ExprCustomEnchantments extends SimpleExpression<Long> implements KeyProviderExpression<Long> {

    public static void register(SyntaxRegistry registry) {
        registry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprCustomEnchantments.class, Long.class)
                .supplier(ExprCustomEnchantments::new)
                .addPattern("custom enchantments of %itemtype%")
                .build());
    }

    Expression<ItemType> itemTypeExpression;
    Map<Event, List<CustomEnchantment>> enchantmentCache = new WeakHashMap<>();

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemTypeExpression = (Expression<ItemType>) expressions[0];

        return true;
    }

    @Override
    protected @Nullable Long[] get(Event event) {
        ItemType itemType = itemTypeExpression.getSingle(event);
        if (itemType == null)
            return null;

        List<CustomEnchantment> enchantments = CustomEnchantments.getEnchantments(itemType);
        enchantmentCache.put(event, enchantments);

        return enchantments.stream()
                .map(CustomEnchantment::level)
                .toArray(Long[]::new);
    }

    @Override
    public String @NonNull [] getArrayKeys(Event event) {
        List<CustomEnchantment> enchantments = enchantmentCache.remove(event);

        return enchantments.stream()
                .map(CustomEnchantment::enchantment)
                .toArray(String[]::new);
    }

    @Override
    public boolean isLoopOf(String input) {
        return KeyProviderExpression.super.isLoopOf(input);
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        return null;
    }

    @Override
    public Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "custom enchantments of " + itemTypeExpression.toString(event, debug);
    }
}