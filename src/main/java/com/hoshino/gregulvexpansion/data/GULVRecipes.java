package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

/**
 * 首批工作台配方 (P0)。
 *
 * <p>零电力门槛自检 (各设计文档「合成草案」)：红合金线 = 铜+红石蒸汽合金炉 +
 * 剪线钳手工裁切；纯净方铅矿矿石 = 方铅矿经蒸汽磨机 + 蒸汽洗矿获得
 * （GTCEu 蒸汽磨机 / 姊妹项目 GSE 大型蒸汽洗矿机，实现期修订 2026-09-07
 * 所有者指定输入），橡胶/螺丝/铁杆均为蒸汽时代已有形态 —— 整条链无电力机器。
 */
public final class GULVRecipes {
    private GULVRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        addDetectorRecipe(provider);
        addWoodCrankRecipe(provider);
        addHandCrankDynamoRecipe(provider);
        addMotorRecipe(provider);
        addConveyorModuleRecipe(provider);
        addPumpRecipe(provider);
        addPrimitiveElectrolyzerRecipe(provider);
        addLeadAcidCellRecipe(provider);
        addBatteryPackRecipe(provider);
        addBatteryWallRecipe(provider);
    }

    /** 猫须探测器：纯净方铅矿矿石 + 红合金单线 → ×2 (D13 产出翻倍)。 */
    private static void addDetectorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("cats_whisker_detector"),
                GULVItems.CATS_WHISKER_DETECTOR.asStack(2),
                "W",
                "O",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'O', ChemicalHelper.get(TagPrefix.crushedPurified, GTMaterials.Galena));
    }

    /** 木质曲柄：木板 ×2 + 木棍 ×1 (D2 独立物品)。 */
    private static void addWoodCrankRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("wood_crank"),
                GULVItems.WOOD_CRANK.asStack(),
                "PP",
                " S",
                'P', ItemTags.PLANKS,
                'S', Items.STICK);
    }

    /**
     * 手摇发电机：木质曲柄 + 猫须探测器 + 红合金单线 ×2 + 铁板 ×4 + ULV 机械方块。
     * 曲柄为合成材料 → 合成产物默认已安装 (hand-crank-dynamo.md)。
     */
    private static void addHandCrankDynamoRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("hand_crank_dynamo"),
                com.hoshino.gregulvexpansion.registry.GULVMachines.HAND_CRANK_DYNAMO.asStack(),
                "WCW",
                "IDI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', GULVItems.WOOD_CRANK,
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压电动马达：红合金单线 ×4 + 铁杆 ×2 + 铁板 ×2 + 猫须探测器。 */
    private static void addMotorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_motor"),
                GULVItems.ULV_ELECTRIC_MOTOR.asStack(),
                "WRW",
                "PDP",
                "WRW",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Iron),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'D', GULVItems.CATS_WHISKER_DETECTOR);
    }

    /** 超低压传送带模块：马达 + 橡胶板 ×2 + 铁螺丝 ×2。 */
    private static void addConveyorModuleRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_conveyor_module"),
                GULVItems.ULV_CONVEYOR_MODULE.asStack(),
                "SMS",
                "RR ",
                'S', ChemicalHelper.get(TagPrefix.screw, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'R', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Rubber));
    }

    /** 超低压电动泵：马达 + 铁板 ×2 + 玻璃 ×2。 */
    private static void addPumpRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_pump"),
                GULVItems.ULV_ELECTRIC_PUMP.asStack(),
                "G G",
                "PMP",
                'G', Tags.Items.GLASS,
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR);
    }

    /**
     * 原型电解槽工作台配方 (primitive-electrolyzer.md 合成草案，v0.3.1 收纳为 3×3)：
     * 猫须探测器 ×2（整流桥语义） + 红合金单线 ×2 + 铅板 ×2 + 玻璃 ×2 + ULV 机械方块 ×1。
     */
    private static void addPrimitiveElectrolyzerRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("primitive_electrolyzer"),
                GULVMachines.PRIMITIVE_ELECTROLYZER.asStack(),
                "WDW",
                "GHG",
                "PDP",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'G', Tags.Items.GLASS,
                'H', GTMachines.HULL[0].asStack(),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead));
    }

    /**
     * 原型电解配方子集 (primitive-electrolyzer.md 配方子集表，D11/B1 已裁决)。
     * 准入规则：每条必须先在文档表格中有名分，代码只注册表内条目。
     *
     * <p><b>注意：仅供运行时 {@code IGTAddon#addRecipes} 调用</b>——GT 配方图
     * 配方在上游 7.5.3 已全部改为运行时动态包注册（jar 内零配方 JSON），
     * datagen 路径的 toJson 会因无 RegistryAccess 而 NPE。
     */
    public static void addElectrolysisRecipes(Consumer<FinishedRecipe> provider) {
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
     * 铅酸单格电池 (lead-acid-battery-line.md 化学链)：PbO₂ 板 + 铅板 + 硫酸 100 mB +
     * 木箱。硫酸走 {@link FluidContainerIngredient}：匹配任意盛有 ≥100 mB 硫酸的
     * 流体容器（GT 单元等），合成时精确抽出 100 mB 并返还余量容器。
     */
    private static void addLeadAcidCellRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedFluidContainerRecipe(provider, false,
                GregULVExpansion.id("lead_acid_cell"),
                GULVItems.LEAD_ACID_CELL.asStack(),
                "DP",
                "BC",
                'D', ChemicalHelper.get(TagPrefix.plate, GULVMaterials.LEAD_DIOXIDE),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'B', new FluidContainerIngredient(GTMaterials.SulfuricAcid.getFluid(100)),
                'C', net.minecraftforge.common.Tags.Items.CHESTS_WOODEN);
    }

    /** 铅酸电池组：单格 ×4 + 铅板外壳 + 红合金单线；容量严格等于组成之和。 */
    private static void addBatteryPackRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_acid_battery_pack"),
                GULVItems.LEAD_ACID_BATTERY_PACK.asStack(),
                " C ",
                "CPC",
                " W ",
                'C', GULVItems.LEAD_ACID_CELL,
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

    /** 铅酸蓄电墙：电池组 + 铅板 ×4 + ULV 机械方块 + 红合金单线 ×2。 */
    private static void addBatteryWallRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_acid_battery_wall"),
                GULVMachines.LEAD_ACID_BATTERY_WALL.asStack(),
                "PWP",
                "PHP",
                "WCW",
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'H', GTMachines.HULL[0].asStack(),
                'C', GULVItems.LEAD_ACID_BATTERY_PACK);
    }
}
