package com.hoshino.gregulvexpansion.data;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

/** Coordinates crafting data generation and runtime recipe registration. */
public final class GULVRecipes {
    private GULVRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        GULVCraftingRecipes.init(provider);
    }

    public static void addRuntimeRecipes(Consumer<FinishedRecipe> provider) {
        GULVComponentRecipes.addComponentAssemblerRecipes(provider);
        GULVPrimitiveRecipes.addElectrolysisRecipes(provider);
        GULVPrimitiveRecipes.addLeadChamberRecipe(provider);
        GULVPrimitiveRecipes.addUlvMachineRecipes(provider);
        GULVPrimitiveRecipes.addUlvBenderLatheRecipes(provider);
        GULVChemistryRecipes.addUlvChemicalReactorRecipes(provider);
        GULVChemistryRecipes.addUlvFluidSolidifierRecipes(provider);
        GULVChemistryRecipes.addUlvExtractorRecipes(provider);
        GULVChemistryRecipes.addOilLineRecipes(provider);
        GULVFuelRecipes.addRedstoneGeneratorFuels(provider);
        GULVFuelRecipes.addGasTurbineFuels(provider);
        GULVPrimitiveRecipes.addPolarizingRecipes(provider);
        GULVComponentRecipes.addCircuitReplacementRecipes(provider);
    }
}
