package com.glisco.hodgepodge.patching;

import net.minecraft.recipe.Recipe;

public class PatchPreconditions {

    public static void checkInputIndex(Recipe<?> recipe, int index, int max) {
        if (index < 0) {
            throw new IllegalArgumentException("Input index must not be negative");
        } else if (index > max) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of bounds for recipe '" + recipe.getId() + "' of class '"
                            + recipe.getClass() + "' which only provides " + (max + 1) + " inputs"
            );
        }
    }

    public static void checkOutputIndex(Recipe<?> recipe, int index, int max) {
        if (index < 0) {
            throw new IllegalArgumentException("Output index must not be negative");
        } else if (index > max) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of bounds for recipe '" + recipe.getId() + "' of class '"
                            + recipe.getClass() + "' which only provides " + (max + 1) + " outputs"
            );
        }
    }

}
