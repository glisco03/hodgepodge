package com.glisco.hodgepodge.mixin.compat;

import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = CuttingBoardRecipe.class, remap = false)
public interface CuttingBoardRecipeAccessor {

    @Accessor("input")
    Ingredient hodgepodge$getInput();

    @Mutable
    @Accessor("input")
    void hodgepodge$setInput(Ingredient ingredient);

    @Mutable
    @Accessor("tool")
    void hodgepodge$setTool(Ingredient tool);

}
