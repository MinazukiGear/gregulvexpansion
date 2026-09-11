package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVBlocks;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;
import com.hoshino.gregulvexpansion.registry.GULVMultiblocks;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

import com.gregtechceu.gtceu.api.GTValues;
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
 *
 * <p>ULV 机器获取配方 (v0.10) 统一采用<b>上游机器样式</b>：图案逐字镜像上游
 * 同型 LV 机器 (MetaTileEntityLoader 机器配方表)，组件按上游 CraftingComponent
 * 的 tier-0 基准解析——机壳 = ULV 机械方块；电路 = 猫须探测器 (上游 CIRCUIT
 * tier-0 为 circuits/ulv 标签，D14 后探测器为唯一成员故直引物品)；线缆 = 红合金
 * 单线 (上游 CABLE tier-0 基准)；板材 = 铁板 (上游 PLATE tier-0 基准)；锯片 =
 * 青铜圆锯头 (上游 SAWBLADE 显式 ULV 条目，工作台配方)；研磨件 = 钻石 (上游
 * GRINDER tier-0 基准)；转子 = 锡转子 (上游 ROTOR tier-0 基准，工作台配方)；
 * 反应管 = 玻璃 (上游 PIPE_REACTOR 全层级)；电动构件 (马达/活塞/泵/传送带) 为
 * 本模组 ULV 构件 (上游无 tier-0 条目)。全材料工作台/蒸汽可达。
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
        addPistonRecipe(provider);
        addRobotArmRecipe(provider);
        addFluidRegulatorRecipe(provider);
        addPrimitiveElectrolyzerRecipe(provider);
        addLeadAcidCellRecipe(provider);
        addBatteryPackRecipe(provider);
        addBatteryWallRecipe(provider);
        addLeadLinedCasingRecipe(provider);
        addThermoelectricGeneratorRecipe(provider);
        addWireMillRecipe(provider);
        addCutterRecipe(provider);
        addRedstoneGeneratorRecipe(provider);
        addGasTurbineRecipe(provider);
        addPolarizerRecipe(provider);
        addBenderRecipe(provider);
        addLatheRecipe(provider);
        addChemicalReactorRecipe(provider);
        addFluidSolidifierRecipe(provider);
        addFluidExtractorRecipe(provider);
        addLeadChamberControllerRecipe(provider);
        addDistillationFrameRecipe(provider);
        addPrimitiveDistillationTowerController(provider);
        addPrimitiveCrackerController(provider);
        addOilPumpController(provider);
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

    /**
     * 超低压电动马达：上游 LV 电动马达（铁变体）图案 (CWR/WMW/RWC)——红合金单线 ×2 +
     * 铜单线 ×4 + 铁杆 ×2 + 磁化铁杆 ×1 (v0.6)。
     * 线缆槽 = 红合金单线（上游 CABLE tier-0 基准）；绕组槽 = 铜单线（上游铜线圈原样）；
     * 磁化铁杆 = 铁杆 + 红石粉 ×4 工作台（上游 iron_magnetic_stick 原配方），或极化机电力磁化。
     * 上游马达本就无电路件，探测器不再入马达（D14/D15 语义不变：探测器仍是手摇机、
     * 电解槽、机械臂与全部机器获取配方的电路入口）。
     */
    private static void addMotorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_motor"),
                GULVItems.ULV_ELECTRIC_MOTOR.asStack(),
                "CWR",
                "WMW",
                "RWC",
                'C', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Copper),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Iron),
                'M', ChemicalHelper.get(TagPrefix.rod, GTMaterials.IronMagnetic));
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
     * 超低压电动活塞：马达 + 锻铁板 ×3 + 红合金单线 ×2 + 锻铁杆 ×2 + 小青铜齿轮 (v0.5.1)。
     * 上游 LV 电动活塞工作台图案 (PPP/CRR/CMG) 下沉：钢→锻铁、锡线缆→红合金线。
     * 锻铁 = 熔炉烧铁粒（煤火零电力）；齿轮槽取小青铜齿轮——锻铁无小齿轮物品形态
     * （上游未启用 GENERATE_SMALL_GEAR），青铜与切割机锯片同族、工作台可达。
     */
    private static void addPistonRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_piston"),
                GULVItems.ULV_ELECTRIC_PISTON.asStack(),
                "PPP",
                "CRR",
                "CMG",
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.WroughtIron),
                'C', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.WroughtIron),
                'G', ChemicalHelper.get(TagPrefix.gearSmall, GTMaterials.Bronze),
                'M', GULVItems.ULV_ELECTRIC_MOTOR);
    }

    /**
     * 超低压机械臂：红合金单线 ×3 + 锻铁杆 ×2 + 马达 + 电动活塞 + 猫须探测器 (v0.5.1)。
     * 上游 LV 机械臂工作台图案 (CCC/MRM/PXR) 下沉；tier-0 电路件为猫须探测器 (D14)。
     */
    private static void addRobotArmRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_robot_arm"),
                GULVItems.ULV_ROBOT_ARM.asStack(),
                "CCC",
                "MRM",
                "PXR",
                'C', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.WroughtIron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'P', GULVItems.ULV_ELECTRIC_PISTON,
                'X', GULVItems.CATS_WHISKER_DETECTOR);
    }

    /**
     * 超低压流体调节器：泵 + 猫须探测器 ×2 + 玻璃 ×3 (v0.7)。
     * 上游调节器为装配机专属（泵 + 电路 ×2），无工作台对标——工作台配方按其
     * 材料清单自设计（泵 + tier-0 电路 + 玻璃壳体），零电力门槛不变。
     */
    private static void addFluidRegulatorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_fluid_regulator"),
                GULVItems.ULV_FLUID_REGULATOR.asStack(),
                "DPD",
                "GGG",
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'P', GULVItems.ULV_ELECTRIC_PUMP,
                'G', Tags.Items.GLASS);
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
     * ULV 轧机/切割机配方子集 (ulv-basic-machines.md 子集表 + upstream-recipe-research.md §4)。
     * 材料清单即定案对象（红合金/铜/铁/锡/铅/锌），扩表必须先改设计文档。
     * 换算规则：EUt ≤ 8 时用上游原值、时长 ×2；板材块→板 EUt 30 → 7、时长 ×2。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvMachineRecipes(Consumer<FinishedRecipe> provider) {
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
    public static void addLeadChamberRecipe(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.LEAD_CHAMBER_RECIPES.recipeBuilder(GregULVExpansion.id("sulfuric_acid"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur, 2))
                .inputFluids(GTMaterials.Water.getFluid(500), GTMaterials.Steam.getFluid(6_400))
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(500))
                .duration(800)
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

    /** 铅衬机壳：铅板 ×6 + 石材基座，每次产出 2 个（铅室法 3×4×3 结构约需 22 块）。 */
    private static void addLeadLinedCasingRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_lined_casing"),
                GULVBlocks.LEAD_LINED_CASING.asStack(2),
                "PPP",
                " S ",
                "PPP",
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'S', Tags.Items.STONE);
    }

    /** 温差发电机：马达 + 红合金单线 ×2 + 铁板 ×3 + 铜板 ×2 + ULV 机械方块（3×3 收纳，铁板 4→3）。 */
    private static void addThermoelectricGeneratorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("thermoelectric_generator"),
                GULVMachines.THERMOELECTRIC_GENERATOR.asStack(),
                "WCW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Copper),
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压线材轧机：上游 WIREMILL 图案 (EWE/CMC/EWE)——马达 ×4 + 探测器 ×2 + 红合金单线 ×2 + ULV 机械方块。 */
    private static void addWireMillRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_wire_mill"),
                GULVMachines.ULV_WIRE_MILL.asStack(),
                "EWE",
                "CMC",
                "EWE",
                'E', GULVItems.ULV_ELECTRIC_MOTOR,
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'M', GTMachines.HULL[0].asStack());
    }

    /**
     * 超低压切割机：上游 CUTTER 图案 (WCG/VMB/CWE)——单线 ×2 + 探测器 ×2 + 玻璃 +
     * 传送带 + 青铜圆锯头 + 马达 + ULV 机械方块。
     * D12 实现修订（v0.10）：锯片由锻铁板改为上游 SAWBLADE 的 ULV 组件条目
     * （青铜圆锯头，GTCEu 有独立物品且为工作台配方）。
     */
    private static void addCutterRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_cutter"),
                GULVMachines.ULV_CUTTER.asStack(),
                "WCG",
                "VMB",
                "CWE",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'G', Tags.Items.GLASS,
                'V', GULVItems.ULV_CONVEYOR_MODULE,
                'M', GTMachines.HULL[0].asStack(),
                'B', ChemicalHelper.get(TagPrefix.toolHeadBuzzSaw, GTMaterials.Bronze),
                'E', GULVItems.ULV_ELECTRIC_MOTOR);
    }

    /** 红石发电机：马达 + 红合金单线 ×2 + 铁板 ×4 + 活塞 ×1 + ULV 机械方块。 */
    private static void addRedstoneGeneratorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("redstone_generator"),
                GULVMachines.REDSTONE_GENERATOR.asStack(),
                "WPW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'P', Items.PISTON,
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }

    /**
     * 超低压微型燃气轮机：上游 GAS_TURBINE LV 图案 (CRC/RMR/EWE)——锡转子 ×4 +
     * 探测器 ×2 + 马达 ×2 + 红合金单线 + ULV 机械方块 (gas-turbine.md)。
     */
    private static void addGasTurbineRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_gas_turbine"),
                GULVMachines.ULV_GAS_TURBINE.asStack(),
                "CRC",
                "RMR",
                "EWE",
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'R', ChemicalHelper.get(TagPrefix.rotor, GTMaterials.Tin),
                'M', GTMachines.HULL[0].asStack(),
                'E', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

    /**
     * 超低压极化机：上游 POLARIZER LV 图案 (ZSZ/WMW/ZSZ)——铁杆 ×4（电磁杆基准）+
     * 锡单线 ×4（线圈，上游 COIL_ELECTRIC tier-0 基准）+ 红合金单线 + ULV 机械方块。
     * 上游本就无电路槽，忠实镜像 (ulv-polarizer.md)。
     */
    private static void addPolarizerRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_polarizer"),
                GULVMachines.ULV_POLARIZER.asStack(),
                "ZSZ",
                "WMW",
                "ZSZ",
                'Z', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Tin),
                'S', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Iron),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'M', GTMachines.HULL[0].asStack());
    }


    /**
     * ULV 卷板机/车床配方子集 (ulv-basic-machines.md v0.5 子集表)。
     * 卷板：锭→板 1:1（上游 EUt 24 → 7，时长 mass → mass×2，省略电路）；
     * 车床：螺栓→螺丝（上游 EUt 4 / mass÷8 → 时长 ×2）与
     * 剥皮原木 → 长木杆 ×4 + 木尘（上游 EUt 7 / 160t → 时长 ×2）。
     * 材料清单同轧机/切割机六种；扩表必须先改设计文档。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvBenderLatheRecipes(Consumer<FinishedRecipe> provider) {
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
     * ULV 化学反应釜配方子集 (ulv-basic-machines.md v0.8 子集表，酸链三条)。
     * 上游 AcidRecipes（EUt VA[ULV]=7 原样直录）；硫化氢路线不收录。
     * 编程电路省略（单一职能无歧义）。同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvChemicalReactorRecipes(Consumer<FinishedRecipe> provider) {
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
    }

    /**
     * ULV 流体固化器配方子集 (ulv-basic-machines.md v0.8 子集表)。
     * 雪球/雪块（EUt 4 原样直录）；黑曜石（上游 EUt 16、1024t，总 EU 16,384
     * → EUt 7、2,341t，v0.7 耗能不变）。模具 notConsumable 不消耗。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvFluidSolidifierRecipes(Consumer<FinishedRecipe> provider) {
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
    }


    /**
     * ULV 流体提取机配方子集 (ulv-basic-machines.md v0.9 子集表，橡胶链五条)。
     * 上游 MachineRecipeLoader.registerDecompositionRecipes（全部 EUt 2 = ULV
     * 能量档，原样直录）。同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvExtractorRecipes(Consumer<FinishedRecipe> provider) {
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
    public static void addOilLineRecipes(Consumer<FinishedRecipe> provider) {
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

    /**
     * 红石发电机燃料表 (redstone-generator.md 燃料表，C6 基准联动)：
     * 红石粉 1,200 EU ≈ 煤蒸汽链的 1/4；红石块 9 倍、无压缩奖励。
     * EUt 为负 = 发电。同样仅供运行时 addRecipes 调用。
     */
    public static void addRedstoneGeneratorFuels(Consumer<FinishedRecipe> provider) {
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
    public static void addGasTurbineFuels(Consumer<FinishedRecipe> provider) {
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

    /**
     * 极化白名单 (ulv-polarizer.md)：首批仅铁杆 → 磁化铁杆（马达 v0.5 前置）。
     * 上游 16 EUt × 80t = 1,280 EU → 8 EUt × 160t（总能耗不变、时长 ×2）。
     * 退磁由上游自带熔炉配方覆盖，不重复实现。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addPolarizingRecipes(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.ULV_POLARIZING
                .recipeBuilder(GregULVExpansion.id("polarize_iron_rod"))
                .inputItems(TagPrefix.rod, GTMaterials.Iron)
                .outputItems(ChemicalHelper.get(TagPrefix.rod, GTMaterials.IronMagnetic))
                .duration(160)
                .EUt(8)
                .save(provider);
    }

    /**
     * ULV 电路替代配方 (ulv-circuit-line.md，D14：探测器为 ULV 电路唯一入口)。
     * 原上游 6 条配方已经 removeRecipes 移除，本方法提供唯一制法。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addCircuitReplacementRecipes(Consumer<FinishedRecipe> provider) {
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

    /** 超低压卷板机：上游 BENDER 图案 (PBP/CMC/EWE)——活塞 ×2 + 铁板 + 探测器 + ULV 机械方块 + 马达 ×2 + 单线。 */
    private static void addBenderRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_bender"),
                GULVMachines.ULV_BENDER.asStack(),
                "PBP",
                "CMC",
                "EWE",
                'P', GULVItems.ULV_ELECTRIC_PISTON,
                'B', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'M', GTMachines.HULL[0].asStack(),
                'E', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

    /** 超低压车床：上游 LATHE 图案 (WCW/EMD/CWP)——单线 ×3 + 探测器 ×2 + 马达 + ULV 机械方块 + 钻石 + 活塞。 */
    private static void addLatheRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_lathe"),
                GULVMachines.ULV_LATHE.asStack(),
                "WCW",
                "EMD",
                "CWP",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'E', GULVItems.ULV_ELECTRIC_MOTOR,
                'M', GTMachines.HULL[0].asStack(),
                'D', ChemicalHelper.get(TagPrefix.gem, GTMaterials.Diamond),
                'P', GULVItems.ULV_ELECTRIC_PISTON);
    }

    /** 超低压化学反应釜：上游 CHEMICAL_REACTOR 图案 (GRG/WEW/CMC)——玻璃（反应管）×2 + 锡转子 ×2 + 单线 + 马达 + 探测器 ×2 + ULV 机械方块。 */
    private static void addChemicalReactorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_chemical_reactor"),
                GULVMachines.ULV_CHEMICAL_REACTOR.asStack(),
                "GRG",
                "WEW",
                "CMC",
                'G', Tags.Items.GLASS,
                'R', ChemicalHelper.get(TagPrefix.rotor, GTMaterials.Tin),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'E', GULVItems.ULV_ELECTRIC_MOTOR,
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'M', GTMachines.HULL[0].asStack());
    }

    /** 超低压流体固化器：上游 FLUID_SOLIDIFIER 图案 (PGP/WMW/CBC)——泵 ×2 + 玻璃 + 单线 + ULV 机械方块 + 探测器 ×2 + 木箱。 */
    private static void addFluidSolidifierRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_fluid_solidifier"),
                GULVMachines.ULV_FLUID_SOLIDIFIER.asStack(),
                "PGP",
                "WMW",
                "CBC",
                'P', GULVItems.ULV_ELECTRIC_PUMP,
                'G', Tags.Items.GLASS,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'M', GTMachines.HULL[0].asStack(),
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'B', Tags.Items.CHESTS_WOODEN);
    }

    /** 超低压流体提取机：上游 EXTRACTOR 图案 (GCG/EMP/WCW)——玻璃 ×2 + 探测器 ×2 + 活塞 + ULV 机械方块 + 泵 + 单线 ×2。 */
    private static void addFluidExtractorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_fluid_extractor"),
                GULVMachines.ULV_FLUID_EXTRACTOR.asStack(),
                "GCG",
                "EMP",
                "WCW",
                'G', Tags.Items.GLASS,
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'E', GULVItems.ULV_ELECTRIC_PISTON,
                'M', GTMachines.HULL[0].asStack(),
                'P', GULVItems.ULV_ELECTRIC_PUMP,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

    /** 铅室控制器：铅衬机壳 ×4(角) + 玻璃 ×2 + 猫须探测器 ×1 + 铅板 ×2。 */
    private static void addLeadChamberControllerRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_chamber"),
                GULVMultiblocks.LEAD_CHAMBER.asStack(),
                "ILI",
                "GCG",
                "ILI",
                'I', GULVBlocks.LEAD_LINED_CASING.asStack(),
                'G', net.minecraftforge.common.Tags.Items.GLASS,
                'C', GULVItems.CATS_WHISKER_DETECTOR,
                'L', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead));
    }

    /** 蒸馏塔框架：钢板 ×4(角) + 钢杆 ×4(边) + 玻璃 ×1(中) → ×2。 */
    private static void addDistillationFrameRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("distillation_frame"),
                GULVBlocks.DISTILLATION_FRAME.asStack(2),
                "S S",
                "GSG",
                "S S",
                'S', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Steel),
                'G', net.minecraftforge.common.Tags.Items.GLASS,
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Steel));
    }

    /** 原始蒸馏塔控制器：蒸馏塔框架 ×4 + 玻璃 ×2 + 红合金线 ×2 + 猫须探测器 ×1。 */
    private static void addPrimitiveDistillationTowerController(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("primitive_distillation_tower"),
                GULVMultiblocks.PRIMITIVE_DISTILLATION_TOWER.asStack(),
                "WFW",
                "GDG",
                "WFW",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'F', GULVBlocks.DISTILLATION_FRAME.asStack(),
                'G', net.minecraftforge.common.Tags.Items.GLASS,
                'D', GULVItems.CATS_WHISKER_DETECTOR);
    }

    /** 原始裂化机控制器：蒸馏塔框架 ×4 + 钢板 ×2 + 红合金线 ×2 + 猫须探测器 ×1。 */
    private static void addPrimitiveCrackerController(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("primitive_cracker"),
                GULVMultiblocks.PRIMITIVE_CRACKER.asStack(),
                "FSF",
                "WDW",
                "FSF",
                'F', GULVBlocks.DISTILLATION_FRAME.asStack(),
                'S', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Steel),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'D', GULVItems.CATS_WHISKER_DETECTOR);
    }

    /** 超低压流体钻井机控制器：蒸馏塔框架 ×4 + 马达 ×1 + 钢杆 ×2 + 红合金线 ×2。 */
    private static void addOilPumpController(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_fluid_drilling_rig"),
                GULVMultiblocks.ULV_FLUID_DRILLING_RIG.asStack(),
                "TFR",
                "TMT",
                "TFR",
                'F', GULVBlocks.DISTILLATION_FRAME.asStack(),
                'T', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Steel),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

}
