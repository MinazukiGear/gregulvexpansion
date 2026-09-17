package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import com.gregtechceu.gtceu.api.GTValues;

import java.util.function.Consumer;

/** Registers primitive electrical and mechanical processing recipes. */
final class GULVPrimitiveRecipes {
    private GULVPrimitiveRecipes() {}

    /**
     * 原型电解配方子集 (primitive-electrolyzer.md 配方子集表，D11/B1 已裁决)。
     * 准入规则：每条必须先在文档表格中有名分，代码只注册表内条目。
     *
     * <p><b>注意：仅供运行时 {@code IGTAddon#addRecipes} 调用</b>——GT 配方图
     * 配方在上游 7.5.3 已全部改为运行时动态包注册（jar 内零配方 JSON），
     * datagen 路径的 toJson 会因无 RegistryAccess 而 NPE。
     */
    static void addElectrolysisRecipes(Consumer<FinishedRecipe> provider) {
        // 水电解：伏打电堆 (1800) 语义本体；128 t ≈ 上游 LV 同配方降档（慢一倍省一半电）
        GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS.recipeBuilder(GregULVExpansion.id("water_electrolysis"))
                .inputFluids(GTMaterials.Water.getFluid(100))
                .outputFluids(GTMaterials.Hydrogen.getFluid(100), GTMaterials.Oxygen.getFluid(50))
                .EUt(8)
                .duration(128)
                .save(provider);

        // PbO₂ 阳极氧化：B1 裁决，铅酸电池链前置；上游无对标条目（第二轮调研 B2）
        GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS.recipeBuilder(GregULVExpansion.id("lead_dioxide_oxidation"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Massicot, 1))
                .inputFluids(GTMaterials.Water.getFluid(100))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GULVMaterials.LEAD_DIOXIDE, 1))
                .EUt(8)
                .duration(256)
                .save(provider);
    }

    /**
     * ULV 轧机/切割机配方子集 (ulv-basic-machines.md 子集表 + upstream-recipe-research.md §4)。
     * 材料清单即定案对象（红合金/铜/铁/锡/铅/锌），扩表必须先改设计文档。
     * 换算规则：EUt ≤ 8 时用上游原值、时长 ×2；板材块→板 EUt 30 → 7、时长 ×2。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addUlvMachineRecipes(Consumer<FinishedRecipe> provider) {
        Material[] subsetMaterials = {
                GTMaterials.RedAlloy, GTMaterials.Copper, GTMaterials.Iron,
                GTMaterials.Tin, GTMaterials.Lead, GTMaterials.Zinc
        };

        // ---- ULV 线材轧机：锭 ×1 → 单线 ×2（上游 WireRecipeHandler，省略编程电路：
        // ---- 本机单一职能无档位歧义，降低 ULV 门槛；上游同配方 EUt 7，时长 ×2）
        for (Material material : subsetMaterials) {
            if (!material.hasProperty(PropertyKey.WIRE) ||
                    !material.shouldGenerateRecipesFor(TagPrefix.wireGtSingle)) {
                continue;
            }
            TagPrefix prefix = material.hasProperty(PropertyKey.INGOT) ? TagPrefix.ingot :
                    material.hasProperty(PropertyKey.GEM) ? TagPrefix.gem : TagPrefix.dust;
            GULVRecipeTypes.ULV_WIRE_MILLING
                    .recipeBuilder(GregULVExpansion.id("wire_" + material.getName()))
                    .inputItems(prefix, material)
                    .outputItems(TagPrefix.wireGtSingle, material, 2)
                    .duration((int) material.getMass())
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 切割机 ----
        for (Material material : subsetMaterials) {
            // 杆 ×1 → 螺栓 ×4（上游 EUt 4 / mass×2；子集 EUt 不变、时长 ×2）
            if (material.hasFlag(MaterialFlags.GENERATE_BOLT_SCREW) &&
                    material.shouldGenerateRecipesFor(TagPrefix.bolt) &&
                    material.hasProperty(PropertyKey.DUST)) {
                ItemStack boltStack = ChemicalHelper.get(TagPrefix.bolt, material);
                if (!boltStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_rod_to_bolt"))
                            .inputItems(TagPrefix.rod, material)
                            .outputItems(boltStack.copyWithCount(4))
                            .duration((int) Math.max(material.getMass() * 2L, 1L))
                            .EUt(4)
                            .save(provider);
                }
            }
            // 长杆 ×1 → 杆 ×2（上游 EUt 4 / mass；子集 EUt 不变、时长 ×2）
            if (material.hasFlag(MaterialFlags.GENERATE_ROD) &&
                    material.shouldGenerateRecipesFor(TagPrefix.rodLong)) {
                ItemStack rodStack = ChemicalHelper.get(TagPrefix.rod, material);
                if (!rodStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_long_rod_to_rod"))
                            .inputItems(TagPrefix.rodLong, material)
                            .outputItems(rodStack.copyWithCount(2))
                            .duration((int) Math.max(material.getMass(), 1L))
                            .EUt(4)
                            .save(provider);
                }
            }
            // 板材块 ×1 → 板 ×(材料量/M)（上游 EUt 30 = VA[LV]；降档为 7、时长 ×2 = mass×16）
            if (material.hasFlag(MaterialFlags.GENERATE_PLATE)) {
                ItemStack plateStack = ChemicalHelper.get(TagPrefix.plate, material);
                if (!plateStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_block_to_plate"))
                            .inputItems(TagPrefix.block, material)
                            .outputItems(plateStack.copyWithCount((int) (TagPrefix.block.getMaterialAmount(material) / GTValues.M)))
                            // 耗能不变：上游 30 × mass×8 = 240×mass → 7 × (240×mass÷7)
                            .duration((int) ((material.getMass() * 240L + 6) / 7))
                            .EUt(7)
                            .save(provider);
                }
            }
        }
    }

    /**
     * 铅室法制酸 (lead-chamber-acid-plant.md 配方草案，v0.3.1 时长拉长定案)：
     * 硫粉 ×2 + 水 500 mB + 蒸汽 6,400 mB（= 8 mB/t × 800 t，运行期一次性扣除）
     * → 硫酸 500 mB；无电（省略 EUt，焦炉同款语义）。等效 1,600 t / 1,000 mB，
     * 产能替代性已锁死。同样仅供运行时 addRecipes 调用。
     */
    static void addLeadChamberRecipe(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.LEAD_CHAMBER_RECIPES.recipeBuilder(GregULVExpansion.id("sulfuric_acid"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur, 2))
                .inputFluids(GTMaterials.Water.getFluid(500), GTMaterials.Steam.getFluid(6_400))
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(500))
                .duration(800)
                .save(provider);
    }

    /**
     * ULV 卷板机/车床配方子集 (ulv-basic-machines.md v0.5 子集表)。
     * 卷板：锭→板 1:1（上游 EUt 24 → 7，时长 mass → mass×2，省略电路）；
     * 车床：螺栓→螺丝（上游 EUt 4 / mass÷8 → 时长 ×2）与
     * 剥皮原木 → 长木杆 ×4 + 木尘（上游 EUt 7 / 160t → 时长 ×2）。
     * 材料清单同轧机/切割机六种；扩表必须先改设计文档。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addUlvBenderLatheRecipes(Consumer<FinishedRecipe> provider) {
        Material[] subsetMaterials = {
                GTMaterials.RedAlloy, GTMaterials.Copper, GTMaterials.Iron,
                GTMaterials.Tin, GTMaterials.Lead, GTMaterials.Zinc
        };

        // ---- ULV 卷板机：锭 ×1 → 板 ×1 ----
        for (Material material : subsetMaterials) {
            if (!material.hasFlag(MaterialFlags.GENERATE_PLATE)) {
                continue;
            }
            ItemStack plateStack = ChemicalHelper.get(TagPrefix.plate, material);
            if (plateStack.isEmpty()) {
                continue;
            }
            GULVRecipeTypes.ULV_BENDING
                    .recipeBuilder(GregULVExpansion.id("bend_" + material.getName() + "_to_plate"))
                    .inputItems(TagPrefix.ingot, material)
                    .outputItems(plateStack)
                    // 耗能不变：上游 24 × mass → 7 × (24×mass÷7)
                    .duration((int) ((material.getMass() * 24L + 6) / 7))
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 车床：螺栓 ×1 → 螺丝 ×1（螺丝自动化，本模组传送带/泵配方内部闭环）----
        for (Material material : subsetMaterials) {
            if (!material.shouldGenerateRecipesFor(TagPrefix.screw) ||
                    !material.shouldGenerateRecipesFor(TagPrefix.bolt)) {
                continue;
            }
            ItemStack screwStack = ChemicalHelper.get(TagPrefix.screw, material);
            if (screwStack.isEmpty()) {
                continue;
            }
            GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_" + material.getName() + "_bolt_to_screw"))
                    .inputItems(TagPrefix.bolt, material)
                    .outputItems(screwStack)
                    .duration((int) Math.max(material.getMass() / 8L, 1L))
                    .EUt(4)
                    .save(provider);
        }

        // ---- ULV 车床：锭 → 杆（跟随上游 harderRods 配置，两形态总能耗同为 32×mass：
        // ---- true（默认）杆 ×1 + 小撮粉 ×2；false 杆 ×2。EUt 16、mass×2 → EUt 7、时长 ÷7）----
        boolean harderRods = com.gregtechceu.gtceu.config.ConfigHolder.INSTANCE.recipes.harderRods;
        for (Material material : subsetMaterials) {
            if (!material.shouldGenerateRecipesFor(TagPrefix.rod) ||
                    !material.hasProperty(PropertyKey.DUST) ||
                    !(material.hasProperty(PropertyKey.INGOT) || material.hasProperty(PropertyKey.GEM))) {
                continue;
            }
            ItemStack rodStack = ChemicalHelper.get(TagPrefix.rod, material);
            if (rodStack.isEmpty()) {
                continue;
            }
            var builder = GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_" + material.getName() + "_to_rod"))
                    .inputItems(TagPrefix.ingot, material);
            if (harderRods) {
                builder.outputItems(rodStack.copyWithCount(1));
                builder.outputItems(ChemicalHelper.get(TagPrefix.dustSmall, material, 2));
            } else {
                builder.outputItems(rodStack.copyWithCount(2));
            }
            builder.duration((int) Math.max((material.getMass() * 32L + 6) / 7, 1))
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 车床：剥皮原木 ×1 → 长木杆 ×4 + 木尘 ×1（原版八种木；上游 EUt 7 已是
        // ---- ULV 档故不换算，时长 160 → 320）----
        net.minecraft.world.level.block.Block[] strippedLogs = {
                net.minecraft.world.level.block.Blocks.STRIPPED_OAK_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_SPRUCE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_BIRCH_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_JUNGLE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_ACACIA_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_DARK_OAK_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_MANGROVE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_CHERRY_LOG
        };
        for (net.minecraft.world.level.block.Block log : strippedLogs) {
            String name = log.getDescriptionId().replace("block.minecraft.stripped_", "");
            GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_stripped_" + name))
                    .inputItems(new ItemStack(log))
                    .outputItems(ChemicalHelper.get(TagPrefix.rodLong, GTMaterials.Wood, 4))
                    .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Wood, 1))
                    .duration(160)
                    .EUt(7)
                    .save(provider);
        }
    }

    /**
     * 极化白名单 (ulv-polarizer.md)：首批仅铁杆 → 磁化铁杆（马达 v0.5 前置）。
     * 上游 16 EUt × 80t = 1,280 EU → 8 EUt × 160t（总能耗不变、时长 ×2）。
     * 退磁由上游自带熔炉配方覆盖，不重复实现。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addPolarizingRecipes(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_POLARIZING
                .recipeBuilder(GregULVExpansion.id("polarize_iron_rod"))
                .inputItems(TagPrefix.rod, GTMaterials.Iron)
                .outputItems(ChemicalHelper.get(TagPrefix.rod, GTMaterials.IronMagnetic))
                .duration(160)
                .EUt(8)
                .save(provider);
    }
}
