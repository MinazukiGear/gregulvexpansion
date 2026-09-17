package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/** Registers chemical, solidification, extraction, and oil processing recipes. */
final class GULVChemistryRecipes {
    private GULVChemistryRecipes() {}

    /**
     * ULV 化学反应釜配方子集（ulv-basic-machines.md v0.11）：酸链、石油脱硫、
     * 聚乙烯聚合及橡胶聚合/硫化。
     * 上游 ULV 配方原样直录；高于 ULV 的配方按总能耗不降低原则换算为 7 EU/t。
     * 编程电路省略（单一职能无歧义）。同样仅供运行时 addRecipes 调用。
     */
    static void addUlvChemicalReactorRecipes(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("sulfur_dioxide_from_sulfur"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur, 1))
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000))
                .duration(60)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("sulfur_trioxide"))
                .inputFluids(GTMaterials.SulfurDioxide.getFluid(1000), GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.SulfurTrioxide.getFluid(1000))
                .duration(200)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("sulfuric_acid_from_trioxide"))
                .inputFluids(GTMaterials.SulfurTrioxide.getFluid(1000), GTMaterials.Water.getFluid(1000))
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .duration(160)
                .EUt(7)
                .save(provider);

        // ---- 石油线子集 (primitive-distillation-tower.md §6；2026-09-08 补录：
        // ---- 提交 2fda8e2 信息声称落地但代码缺失，本次按文档定稿值恢复) ----
        // 脱硫（轻燃料/石脑油）：上游 desulfurizationRecipes（硫化油 12,000 + 氢 2,000 →
        // 油 12,000 + H₂S 1,000，30 EUt × 160t）；耗能不变：7 × 686t
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("desulfurize_light_fuel"))
                .inputFluids(GTMaterials.SulfuricLightFuel.getFluid(12_000),
                        GTMaterials.Hydrogen.getFluid(2_000))
                .outputFluids(GTMaterials.LightFuel.getFluid(12_000),
                        GTMaterials.HydrogenSulfide.getFluid(1_000))
                .duration(686)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("desulfurize_naphtha"))
                .inputFluids(GTMaterials.SulfuricNaphtha.getFluid(12_000),
                        GTMaterials.Hydrogen.getFluid(2_000))
                .outputFluids(GTMaterials.Naphtha.getFluid(12_000),
                        GTMaterials.HydrogenSulfide.getFluid(1_000))
                .duration(686)
                .EUt(7)
                .save(provider);

        // 脱硫（重燃料）——2026-09-09 补录：上游 desulfurizationRecipes 第三条 v1.1 漏抄，
        // 蒸馏塔第一股馏分（含硫重燃料）由此闭合。口径同表：硫化重燃料 8,000（注意基数
        // 与轻燃料/石脑油的 12,000 不同）+ 氢 2,000 → 重燃料 8,000 + H₂S 1,000：7 × 686t。
        // 脱硫后重燃料为 LV 内燃机/锅炉燃料（GSE 锅炉白名单提案另议）。
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("desulfurize_heavy_fuel"))
                .inputFluids(GTMaterials.SulfuricHeavyFuel.getFluid(8_000),
                        GTMaterials.Hydrogen.getFluid(2_000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(8_000),
                        GTMaterials.HydrogenSulfide.getFluid(1_000))
                .duration(686)
                .EUt(7)
                .save(provider);

        // H₂S 反哺酸产（自定义，上游无对标条目）：H₂S 1,000 + 氧 4,000 → 硫酸 1,000；
        // 口径同表：7 × 1,372t
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("sulfuric_acid_from_h2s"))
                .inputFluids(GTMaterials.HydrogenSulfide.getFluid(1_000),
                        GTMaterials.Oxygen.getFluid(4_000))
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(1_000))
                .duration(1372)
                .EUt(7)
                .save(provider);

        // 聚合 ×2（上游 PolymerRecipes，编程电路省略——空气/氧输入互斥无歧义）：
        // 空气版 乙烯 144 + 空气 1,000 → PE 144；氧版产 216（上游 1.5× 氧化奖励）。
        // 耗能不变：30 × 160t → 7 × 686t
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("polyethylene_from_air"))
                .inputFluids(GTMaterials.Air.getFluid(1_000), GTMaterials.Ethylene.getFluid(144))
                .outputFluids(GTMaterials.Polyethylene.getFluid(144))
                .duration(686)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("polyethylene_from_oxygen"))
                .inputFluids(GTMaterials.Oxygen.getFluid(1_000), GTMaterials.Ethylene.getFluid(144))
                .outputFluids(GTMaterials.Polyethylene.getFluid(216))
                .duration(686)
                .EUt(7)
                .save(provider);

        // 橡胶化学链：完整下放上游化学反应釜中直接生成生橡胶/橡胶的三条配方。
        // 生橡胶聚合：30 EU/t × 160t → 7 EU/t × 686t；氧气路线保持 3 倍产出。
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_air"))
                .inputFluids(GTMaterials.Isoprene.getFluid(144), GTMaterials.Air.getFluid(2_000))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber))
                .duration(686)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_oxygen"))
                .inputFluids(GTMaterials.Isoprene.getFluid(144), GTMaterials.Oxygen.getFluid(2_000))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 3))
                .duration(686)
                .EUt(7)
                .save(provider);

        // 硫化：16 EU/t × 600t → 7 EU/t × 1,372t（向上取整，避免降低总能耗）。
        GULVRecipeTypes.ULV_CHEMICAL_REACTING
                .recipeBuilder(GregULVExpansion.id("rubber"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 9),
                        ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur))
                .outputFluids(GTMaterials.Rubber.getFluid(1_296))
                .duration(1_372)
                .EUt(7)
                .save(provider);
    }

    /**
     * ULV 流体固化器配方子集（ulv-basic-machines.md v0.11）：基础冷却配方，
     * 以及聚乙烯/橡胶的全部上游可固化形态。
     * 雪球/雪块（EUt 4 原样直录）；黑曜石（上游 EUt 16、1024t，总 EU 16,384
     * → EUt 7、2,341t，v0.7 耗能不变）。模具 notConsumable 不消耗。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addUlvFluidSolidifierRecipes(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id("snowball"))
                .inputFluids(GTMaterials.Water.getFluid(250))
                .notConsumable(GTItems.SHAPE_MOLD_BALL)
                .outputItems(new ItemStack(net.minecraft.world.item.Items.SNOWBALL))
                .duration(128)
                .EUt(4)
                .save(provider);

        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id("snow_block"))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                .outputItems(new ItemStack(net.minecraft.world.level.block.Blocks.SNOW_BLOCK))
                .duration(512)
                .EUt(4)
                .save(provider);

        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id("obsidian"))
                .inputFluids(GTMaterials.Lava.getFluid(1000))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                .outputItems(new ItemStack(net.minecraft.world.level.block.Blocks.OBSIDIAN))
                .duration(2341)
                .EUt(7)
                .save(provider);

        // 聚乙烯板（石油线终点，primitive-distillation-tower.md §6）：
        // PE 144 mB + 板模具 → 板 ×1（EUt 7 / 40t 原样直录，模具不消耗）
        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id("polyethylene_plate"))
                .inputFluids(GTMaterials.Polyethylene.getFluid(144))
                .notConsumable(GTItems.SHAPE_MOLD_PLATE)
                .outputItems(ChemicalHelper.get(TagPrefix.plate, GTMaterials.Polyethylene))
                .duration(40)
                .EUt(7)
                .save(provider);

        // 聚乙烯与橡胶完整固化覆盖：锭、粒、块、板；聚乙烯另含其全部五档流体管。
        addPolymerSolidificationRecipes(provider, GTMaterials.Polyethylene, "polyethylene", false);
        addPolymerSolidificationRecipes(provider, GTMaterials.Rubber, "rubber", true);

        addPolyethylenePipeSolidificationRecipes(provider);
    }

    private static void addPolymerSolidificationRecipes(Consumer<FinishedRecipe> provider, Material material,
                                                         String recipePrefix, boolean includePlate) {
        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id(recipePrefix + "_ingot"))
                .inputFluids(material.getFluid(144))
                .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                .outputItems(ChemicalHelper.get(TagPrefix.ingot, material))
                .duration(20)
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id(recipePrefix + "_nuggets"))
                .inputFluids(material.getFluid(144))
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET)
                .outputItems(ChemicalHelper.get(TagPrefix.nugget, material, 9))
                .duration((int) material.getMass())
                .EUt(7)
                .save(provider);

        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id(recipePrefix + "_block"))
                .inputFluids(material.getFluid(1_296))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                .outputItems(ChemicalHelper.get(TagPrefix.block, material))
                .duration((int) material.getMass())
                .EUt(7)
                .save(provider);

        if (includePlate) {
            GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                    .recipeBuilder(GregULVExpansion.id(recipePrefix + "_plate"))
                    .inputFluids(material.getFluid(144))
                    .notConsumable(GTItems.SHAPE_MOLD_PLATE)
                    .outputItems(ChemicalHelper.get(TagPrefix.plate, material))
                    .duration(40)
                    .EUt(7)
                    .save(provider);
        }
    }

    private static void addPolyethylenePipeSolidificationRecipes(Consumer<FinishedRecipe> provider) {
        addPolyethylenePipeSolidificationRecipe(provider, "tiny", GTItems.SHAPE_MOLD_TINY_PIPE,
                TagPrefix.pipeTinyFluid, 72, 14);
        addPolyethylenePipeSolidificationRecipe(provider, "small", GTItems.SHAPE_MOLD_SMALL_PIPE,
                TagPrefix.pipeSmallFluid, 144, 28);
        addPolyethylenePipeSolidificationRecipe(provider, "normal", GTItems.SHAPE_MOLD_NORMAL_PIPE,
                TagPrefix.pipeNormalFluid, 432, 84);
        addPolyethylenePipeSolidificationRecipe(provider, "large", GTItems.SHAPE_MOLD_LARGE_PIPE,
                TagPrefix.pipeLargeFluid, 864, 168);
        addPolyethylenePipeSolidificationRecipe(provider, "huge", GTItems.SHAPE_MOLD_HUGE_PIPE,
                TagPrefix.pipeHugeFluid, 1_728, 672);
    }

    private static void addPolyethylenePipeSolidificationRecipe(Consumer<FinishedRecipe> provider, String size,
                                                                 com.tterrag.registrate.util.entry.ItemEntry<?> mold,
                                                                 TagPrefix outputPrefix, int fluidAmount,
                                                                 int duration) {
        GULVRecipeTypes.ULV_FLUID_SOLIDFICATION
                .recipeBuilder(GregULVExpansion.id("polyethylene_" + size + "_fluid_pipe"))
                .inputFluids(GTMaterials.Polyethylene.getFluid(fluidAmount))
                .notConsumable(mold)
                .outputItems(ChemicalHelper.get(outputPrefix, GTMaterials.Polyethylene))
                .duration(duration)
                .EUt(6)
                .save(provider);
    }

    /**
     * ULV 流体提取机配方子集 (ulv-basic-machines.md v0.9 子集表，橡胶链五条)。
     * 上游 MachineRecipeLoader.registerDecompositionRecipes（全部 EUt 2 = ULV
     * 能量档，原样直录）。同样仅供运行时 addRecipes 调用。
     */
    static void addUlvExtractorRecipes(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_EXTRACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_resin"))
                .inputItems(GTItems.STICKY_RESIN)
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 3))
                .duration(150)
                .EUt(2)
                .save(provider);

        GULVRecipeTypes.ULV_EXTRACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_log"))
                .inputItems(com.gregtechceu.gtceu.common.data.GTBlocks.RUBBER_LOG.asStack())
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 1))
                .duration(300)
                .EUt(2)
                .save(provider);

        GULVRecipeTypes.ULV_EXTRACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_leaves"))
                .inputItems(com.gregtechceu.gtceu.common.data.GTBlocks.RUBBER_LEAVES.asStack(16))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 1))
                .duration(300)
                .EUt(2)
                .save(provider);

        GULVRecipeTypes.ULV_EXTRACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_sapling"))
                .inputItems(com.gregtechceu.gtceu.common.data.GTBlocks.RUBBER_SAPLING.asStack())
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 1))
                .duration(300)
                .EUt(2)
                .save(provider);

        GULVRecipeTypes.ULV_EXTRACTING
                .recipeBuilder(GregULVExpansion.id("raw_rubber_from_slime"))
                .inputItems(new ItemStack(net.minecraft.world.item.Items.SLIME_BALL))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.RawRubber, 2))
                .duration(150)
                .EUt(2)
                .save(provider);
    }

    /**
     * 石油线无电多方块本体配方 (primitive-distillation-tower.md §3/§5，v1.1 所有者逐条修正；
     * 2026-09-08 补录：提交 890a559 时序中声明但代码缺失，本次按文档定稿值恢复)。
     * 无电：省略 EUt（铅室同款语义）；蒸汽按运行时长一次扣除（写入配方流体输入）。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addOilLineRecipes(Consumer<FinishedRecipe> provider) {
        // 原始蒸馏塔：原油 50 + 蒸汽 2,560（8 mB/t × 320t 一次扣除）
        // → 四硫化组分（产出比例与上游 distill_oil 完全一致），时长 ×16（320t）
        GULVRecipeTypes.PRIMITIVE_DISTILLATION
                .recipeBuilder(GregULVExpansion.id("distill_oil"))
                .inputFluids(GTMaterials.Oil.getFluid(50), GTMaterials.Steam.getFluid(2_560))
                .outputFluids(GTMaterials.SulfuricHeavyFuel.getFluid(15),
                        GTMaterials.SulfuricLightFuel.getFluid(50),
                        GTMaterials.SulfuricNaphtha.getFluid(20),
                        GTMaterials.SulfuricGas.getFluid(60))
                .duration(320)
                .save(provider);

        // 原始裂化机 ×2：石脑油/轻燃料 1,000 + 蒸汽 4,000（一次扣除）
        // → 乙烯 250 + 甲烷 500 + 碳粉 ×1（上游裂化+蒸馏全链产量的 1/6），时长 640t
        GULVRecipeTypes.PRIMITIVE_CRACKING
                .recipeBuilder(GregULVExpansion.id("crack_naphtha"))
                .inputFluids(GTMaterials.Naphtha.getFluid(1_000), GTMaterials.Steam.getFluid(4_000))
                .outputFluids(GTMaterials.Ethylene.getFluid(250), GTMaterials.Methane.getFluid(500))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Carbon, 1))
                .duration(640)
                .save(provider);

        GULVRecipeTypes.PRIMITIVE_CRACKING
                .recipeBuilder(GregULVExpansion.id("crack_light_fuel"))
                .inputFluids(GTMaterials.LightFuel.getFluid(1_000), GTMaterials.Steam.getFluid(4_000))
                .outputFluids(GTMaterials.Ethylene.getFluid(250), GTMaterials.Methane.getFluid(500))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Carbon, 1))
                .duration(640)
                .save(provider);
    }
}
