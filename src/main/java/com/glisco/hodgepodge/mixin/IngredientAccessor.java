package com.glisco.hodgepodge.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public interface IngredientAccessor {

    @Invoker("<init>")
    static Ingredient invokeCreate(Stream<? extends Ingredient.Entry> entries) {
        throw new AssertionError();
    }

    @Accessor
    Ingredient.Entry[] getEntries();

    @Mixin(Ingredient.TagEntry.class)
    interface TagEntryAccessor {
        @Accessor
        Tag<Item> getTag();
    }

    @Mixin(Ingredient.StackEntry.class)
    interface StackEntryAccessor {
        @Accessor
        ItemStack getStack();
    }

}
