package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVBlocks;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;
import com.hoshino.gregulvexpansion.registry.GULVMultiblocks;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

/** Generates crafting recipes for components, machines, casings, and multiblock controllers. */
final class GULVCraftingRecipes {
    private GULVCraftingRecipes() {}

    static void init(Consumer<FinishedRecipe> provider) {
        addDetectorRecipe(provider);
        addWoodCrankRecipe(provider);
        addHandCrankDynamoRecipe(provider);
        addMotorRecipe(provider);
        addConveyorModuleRecipe(provider);
        addPumpRecipe(provider);
        addPistonRecipe(provider);
        addRobotArmRecipe(provider);
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
     * 手摇发电机本体：猫须探测器 + 红合金单线 ×2 + 铁板 ×4 + ULV 机械方块。
     * 曲柄独立合成，放置机器后右键安装 (hand-crank-dynamo.md)。
     */
    private static void addHandCrankDynamoRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("hand_crank_dynamo"),
                com.hoshino.gregulvexpansion.registry.GULVMachines.HAND_CRANK_DYNAMO.asStack(),
                "W W",
                "IDI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'H', GTMachines.HULL[0].asStack());
    }

    /**
     * 超低压电动马达：上游 LV 电动马达（铁变体）图案 (CWR/WMW/RWC)——红合金单股线缆 ×2 +
     * 铜单线 ×4 + 铁杆 ×2 + 磁化铁杆 ×1 (v0.6)。
     * 线缆槽 = 红合金单股线缆（上游 CABLE tier-0 基准）；绕组槽 = 铜单线（上游铜线圈原样）；
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
                'C', ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.RedAlloy),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Copper),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Iron),
                'M', ChemicalHelper.get(TagPrefix.rod, GTMaterials.IronMagnetic));

    }

    /** 超低压传送带模块：逐项下沉上游工作台与装配机配方，并保留三种橡胶变体。 */
    private static void addConveyorModuleRecipe(Consumer<FinishedRecipe> provider) {
        addConveyorModuleRecipe(provider, "rubber", GTMaterials.Rubber);
        addConveyorModuleRecipe(provider, "silicone_rubber", GTMaterials.SiliconeRubber);
        addConveyorModuleRecipe(provider, "styrene_butadiene_rubber", GTMaterials.StyreneButadieneRubber);
    }

    private static void addConveyorModuleRecipe(Consumer<FinishedRecipe> provider, String name, Material rubber) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_conveyor_module_" + name),
                GULVItems.ULV_CONVEYOR_MODULE.asStack(),
                "RRR",
                "MCM",
                "RRR",
                'R', ChemicalHelper.get(TagPrefix.plate, rubber),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'C', ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.RedAlloy));

    }

    /** 超低压电动泵：逐项下沉上游工作台与装配机配方，并保留三种橡胶变体。 */
    private static void addPumpRecipe(Consumer<FinishedRecipe> provider) {
        addPumpRecipe(provider, "rubber", GTMaterials.Rubber);
        addPumpRecipe(provider, "silicone_rubber", GTMaterials.SiliconeRubber);
        addPumpRecipe(provider, "styrene_butadiene_rubber", GTMaterials.StyreneButadieneRubber);
    }

    private static void addPumpRecipe(Consumer<FinishedRecipe> provider, String name, Material rubber) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_pump_" + name),
                GULVItems.ULV_ELECTRIC_PUMP.asStack(),
                "SXR",
                "dPw",
                "RMC",
                'S', ChemicalHelper.get(TagPrefix.screw, GTMaterials.Tin),
                'X', ChemicalHelper.get(TagPrefix.rotor, GTMaterials.Tin),
                'P', ChemicalHelper.get(TagPrefix.pipeNormalFluid, GTMaterials.Bronze),
                'R', ChemicalHelper.get(TagPrefix.ring, rubber),
                'C', ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.RedAlloy),
                'M', GULVItems.ULV_ELECTRIC_MOTOR);

    }

    /**
     * 超低压电动活塞：马达 + 锻铁板 ×3 + 红合金单股线缆 ×2 + 锻铁杆 ×2 + 小青铜齿轮 (v0.5.1)。
     * 上游 LV 电动活塞工作台图案 (PPP/CRR/CMG) 下沉：钢→锻铁、锡线缆→红合金线缆。
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
                'C', ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.WroughtIron),
                'G', ChemicalHelper.get(TagPrefix.gearSmall, GTMaterials.Bronze),
                'M', GULVItems.ULV_ELECTRIC_MOTOR);

    }

    /**
     * 超低压机械臂：红合金单股线缆 ×3 + 锻铁杆 ×2 + 马达 + 电动活塞 + 猫须探测器 (v0.5.1)。
     * 上游 LV 机械臂工作台图案 (CCC/MRM/PXR) 下沉；tier-0 电路件为猫须探测器 (D14)。
     */
    private static void addRobotArmRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_robot_arm"),
                GULVItems.ULV_ROBOT_ARM.asStack(),
                "CCC",
                "MRM",
                "PXR",
                'C', ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.WroughtIron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'P', GULVItems.ULV_ELECTRIC_PISTON,
                'X', GULVItems.CATS_WHISKER_DETECTOR);

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

    /** 铅衬机壳：铅板 ×6 + 石材基座，每次产出 2 个（铅室结构至少需要 10 块）。 */
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
                "SRS",
                "RGR",
                "SRS",
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
                "FTW",
                "FMF",
                "FTW",
                'F', GULVBlocks.DISTILLATION_FRAME.asStack(),
                'T', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Steel),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }
}
