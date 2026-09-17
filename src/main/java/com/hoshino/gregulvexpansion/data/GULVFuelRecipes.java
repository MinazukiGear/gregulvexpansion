package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

/** Registers solid and fluid generator fuels. */
final class GULVFuelRecipes {
    private GULVFuelRecipes() {}

    /**
     * 红石发电机燃料表 (redstone-generator.md 燃料表，C6 基准联动)：
     * 红石粉 1,200 EU ≈ 煤蒸汽链的 1/4；红石块 9 倍、无压缩奖励。
     * EUt 为负 = 发电。同样仅供运行时 addRecipes 调用。
     */
    static void addRedstoneGeneratorFuels(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.REDSTONE_GENERATOR_FUELS
                .recipeBuilder(GregULVExpansion.id("redstone_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Redstone, 1)
                .duration(150)
                .EUt(-8)
                .save(provider);

        GULVRecipeTypes.REDSTONE_GENERATOR_FUELS
                .recipeBuilder(GregULVExpansion.id("redstone_block"))
                .inputItems(TagPrefix.block, GTMaterials.Redstone, 1)
                .duration(1350)
                .EUt(-8)
                .save(provider);
    }

    /**
     * 微型燃气轮机燃料表 (gas-turbine.md)：白名单四种流体全部来自本模组石油线/钻井。
     * 热值与上游 GAS_TURBINE_FUELS 一致（总 EU 不变），输出降为 8 EU/t → 时长 ×4。
     * 乙烯不上表（聚合叙事优先）；木煤气/煤气留待 GSE 侧联动扩展。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addGasTurbineFuels(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_GAS_TURBINE_FUELS
                .recipeBuilder(GregULVExpansion.id("natural_gas"))
                .inputFluids(GTMaterials.NaturalGas.getFluid(8))
                .duration(20)
                .EUt(-8)
                .save(provider);

        GULVRecipeTypes.ULV_GAS_TURBINE_FUELS
                .recipeBuilder(GregULVExpansion.id("sulfuric_gas"))
                .inputFluids(GTMaterials.SulfuricGas.getFluid(32))
                .duration(100)
                .EUt(-8)
                .save(provider);

        GULVRecipeTypes.ULV_GAS_TURBINE_FUELS
                .recipeBuilder(GregULVExpansion.id("methane"))
                .inputFluids(GTMaterials.Methane.getFluid(2))
                .duration(28)
                .EUt(-8)
                .save(provider);

        GULVRecipeTypes.ULV_GAS_TURBINE_FUELS
                .recipeBuilder(GregULVExpansion.id("sulfuric_naphtha"))
                .inputFluids(GTMaterials.SulfuricNaphtha.getFluid(4))
                .duration(20)
                .EUt(-8)
                .save(provider);
    }
}
