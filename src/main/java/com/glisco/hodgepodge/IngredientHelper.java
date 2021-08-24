package com.glisco.hodgepodge;

import com.glisco.hodgepodge.mixin.IngredientAccessor;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

public class IngredientHelper {

    public static Ingredient.Entry parseEntry(String descriptor) throws JsonSyntaxException {
        if (descriptor.startsWith("#")) {
            var tag = ServerTagManagerHolder.getTagManager().getTag(Registry.ITEM_KEY, new Identifier(descriptor.substring(1)), identifier -> {
                return new JsonSyntaxException("Unknown item tag '" + identifier + "'");
            });
            return new Ingredient.TagEntry(tag);
        } else {
            var item = Registry.ITEM.getOrEmpty(new Identifier(descriptor)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + descriptor + "'"));
            return new Ingredient.StackEntry(new ItemStack(item));
        }
    }

    public static Ingredient parse(String descriptor) throws JsonSyntaxException {
        if (descriptor.equals("minecraft:empty")) return Ingredient.EMPTY;
        return IngredientAccessor.invokeCreate(Stream.of(parseEntry(descriptor)));
    }

    public static boolean compareIngredients(Ingredient ingredient, Ingredient other) {
        return !(ingredient.isEmpty() || other.isEmpty()) && compareEntries(((IngredientAccessor) (Object) ingredient).getEntries()[0], ((IngredientAccessor) (Object) other).getEntries()[0]);
    }

    public static boolean compareEntries(Ingredient.Entry entry, Ingredient.Entry other) {
        if (entry.getClass() != other.getClass()) return false;
        if (entry instanceof Ingredient.StackEntry) return compareStackEntries((Ingredient.StackEntry) entry, (Ingredient.StackEntry) other);
        if (entry instanceof Ingredient.TagEntry) return compareTagEntries((Ingredient.TagEntry) entry, (Ingredient.TagEntry) other);
        return false;
    }

    private static boolean compareStackEntries(Ingredient.StackEntry stackEntry, Ingredient.StackEntry other) {
        return getStackEntryItem(stackEntry) == getStackEntryItem(other);
    }

    private static Item getStackEntryItem(Ingredient.StackEntry stackEntry) {
        return ((IngredientAccessor.StackEntryAccessor) stackEntry).getStack().getItem();
    }

    private static boolean compareTagEntries(Ingredient.TagEntry tagEntry, Ingredient.TagEntry other) {
        return getTagEntryTag(tagEntry) == getTagEntryTag(other);
    }

    private static Tag<Item> getTagEntryTag(Ingredient.TagEntry tagEntry) {
        return ((IngredientAccessor.TagEntryAccessor) tagEntry).getTag();
    }

}
