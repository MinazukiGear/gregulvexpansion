package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVItems;

import net.minecraft.data.recipes.FinishedRecipe;

import com.gregtechceu.gtceu.api.GTValues;

import java.util.function.Consumer;

/** Registers component assembly and replacement circuit recipes. */
final class GULVComponentRecipes {
    private GULVComponentRecipes() {}

    /**
     * 基础构件装配机配方：输入结构、时长和电路配置与上游 LV 配方一致，
     * 仅将层级材料与工作电压下沉到 ULV。
     */
    static void addComponentAssemblerRecipes(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_electric_motor"))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.rod, GTMaterials.Iron, 2)
                .inputItems(TagPrefix.rod, GTMaterials.IronMagnetic)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.Copper, 4)
                .outputItems(GULVItems.ULV_ELECTRIC_MOTOR.asStack())
                .duration(100)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);

        addConveyorAssemblerRecipe(provider, "rubber", GTMaterials.Rubber);
        addConveyorAssemblerRecipe(provider, "silicone_rubber", GTMaterials.SiliconeRubber);
        addConveyorAssemblerRecipe(provider, "styrene_butadiene_rubber", GTMaterials.StyreneButadieneRubber);

        addPumpAssemblerRecipe(provider, "rubber", GTMaterials.Rubber);
        addPumpAssemblerRecipe(provider, "silicone_rubber", GTMaterials.SiliconeRubber);
        addPumpAssemblerRecipe(provider, "styrene_butadiene_rubber", GTMaterials.StyreneButadieneRubber);

        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_electric_piston"))
                .inputItems(TagPrefix.rod, GTMaterials.WroughtIron, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.WroughtIron, 3)
                .inputItems(TagPrefix.gearSmall, GTMaterials.Bronze)
                .inputItems(GULVItems.ULV_ELECTRIC_MOTOR)
                .outputItems(GULVItems.ULV_ELECTRIC_PISTON.asStack())
                .duration(100)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_robot_arm"))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 3)
                .inputItems(TagPrefix.rod, GTMaterials.WroughtIron, 2)
                .inputItems(GULVItems.ULV_ELECTRIC_MOTOR, 2)
                .inputItems(GULVItems.ULV_ELECTRIC_PISTON)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GULVItems.ULV_ROBOT_ARM.asStack())
                .duration(100)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_fluid_regulator"))
                .inputItems(GULVItems.ULV_ELECTRIC_PUMP)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR, 2)
                .circuitMeta(1)
                .outputItems(GULVItems.ULV_FLUID_REGULATOR.asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);
    }

    private static void addConveyorAssemblerRecipe(Consumer<FinishedRecipe> provider, String name, Material rubber) {
        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_conveyor_module_" + name))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy)
                .inputItems(GULVItems.ULV_ELECTRIC_MOTOR, 2)
                .inputFluids(rubber.getFluid(GTValues.L * 6))
                .circuitMeta(1)
                .outputItems(GULVItems.ULV_CONVEYOR_MODULE.asStack())
                .duration(100)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);
    }

    private static void addPumpAssemblerRecipe(Consumer<FinishedRecipe> provider, String name, Material rubber) {
        GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder(GregULVExpansion.id("ulv_electric_pump_" + name))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy)
                .inputItems(TagPrefix.pipeNormalFluid, GTMaterials.Bronze)
                .inputItems(TagPrefix.screw, GTMaterials.Tin)
                .inputItems(TagPrefix.rotor, GTMaterials.Tin)
                .inputItems(TagPrefix.ring, rubber, 2)
                .inputItems(GULVItems.ULV_ELECTRIC_MOTOR)
                .outputItems(GULVItems.ULV_ELECTRIC_PUMP.asStack())
                .duration(100)
                .EUt(GTValues.VA[GTValues.ULV])
                .save(provider);
    }

    /**
     * ULV 电路替代配方 (ulv-circuit-line.md，D14：探测器为 ULV 电路唯一入口)。
     * 原上游 6 条配方已经 removeRecipes 移除，本方法提供唯一制法。
     * 同样仅供运行时 addRecipes 调用。
     */
    static void addCircuitReplacementRecipes(Consumer<FinishedRecipe> provider) {
        // 真空管：保留上游配方 ID（配方书/查看器连续性）。该 ID 在 RECIPE_FILTERS
        // 中（被自身 removeRecipes 移除），故必须直写 GTDynamicDataPack 绕过过滤器
        // ——姊妹项目 GSE 焦炉替换配方的同款手法。
        // 工作台配方同样只修改成本：上游原版（钢螺栓 ×2 + 玻璃管 + 铜单线 ×3）
        // 原样保留，仅追加探测器 ×1（底部中央）
        VanillaRecipeHelper.addShapedRecipe(GTDynamicDataPack::addRecipe,
                GTCEu.id("vacuum_tube"),
                GTItems.VACUUM_TUBE.asStack(),
                "PTP",
                "WWW",
                " D ",
                'P', ChemicalHelper.get(TagPrefix.bolt, GTMaterials.Steel),
                'T', GTItems.GLASS_TUBE.asStack(),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Copper),
                'D', GULVItems.CATS_WHISKER_DETECTOR);

        // 真空管·装配机红合金流体档（上游 3/4 号配方 + 探测器成本，ID 保留、动态包直写）：
        // 工作台 ×1 → 装配机红合金 ×3 → 装配机退火铜 ×4，恢复上游「以流体育提产率」的梯度
        com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder("vacuum_tube_red_alloy")
                .EUt(GTValues.VA[GTValues.ULV]).duration(40)
                .inputItems(GTItems.GLASS_TUBE)
                .inputItems(TagPrefix.bolt, GTMaterials.Steel)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.Copper, 2)
                .inputFluids(GTMaterials.RedAlloy.getFluid(18))
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.VACUUM_TUBE, 3)
                .save(GTDynamicDataPack::addRecipe);

        com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder("vacuum_tube_red_alloy_annealed")
                .EUt(GTValues.VA[GTValues.ULV]).duration(40)
                .inputItems(GTItems.GLASS_TUBE)
                .inputItems(TagPrefix.bolt, GTMaterials.Steel)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.AnnealedCopper, 2)
                .inputFluids(GTMaterials.RedAlloy.getFluid(18))
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.VACUUM_TUBE, 4)
                .save(GTDynamicDataPack::addRecipe);

        // NAND 芯片（2026-09-08 所有者指示：仍按上游产线，仅修改成本）：完整保留上游
        // 两条电路装配机配方（MV 装配机 + 好电路板/塑料板 + SoC 晶圆链 + 红合金螺栓 +
        // 锡细线），唯一成本修改是追加猫须探测器 ×1 作为 D14 入口。ID 保留上游原 ID，
        // 经 GTDynamicDataPack 直写绕过 removeRecipes 过滤器（与真空管同手法）。
        com.gregtechceu.gtceu.common.data.GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES
                .recipeBuilder("nand_chip_ulv_good_board")
                .EUt(GTValues.VA[GTValues.MV]).duration(300)
                .inputItems(GTItems.GOOD_CIRCUIT_BOARD)
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(TagPrefix.bolt, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.wireFine, GTMaterials.Tin, 2)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.NAND_CHIP_ULV, 8)
                .save(GTDynamicDataPack::addRecipe);

        com.gregtechceu.gtceu.common.data.GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES
                .recipeBuilder("nand_chip_ulv_plastic_board")
                .EUt(GTValues.VA[GTValues.MV]).duration(300)
                .inputItems(GTItems.PLASTIC_CIRCUIT_BOARD)
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(TagPrefix.bolt, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.wireFine, GTMaterials.Tin, 2)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.NAND_CHIP_ULV, 12)
                .save(GTDynamicDataPack::addRecipe);
    }
}
