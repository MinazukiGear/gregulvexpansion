package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.generator.HandCrankDynamoMachine;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.hoshino.gregulvexpansion.machine.generator.RedstoneGeneratorMachine;
import com.hoshino.gregulvexpansion.machine.generator.ThermoelectricGeneratorMachine;
import com.hoshino.gregulvexpansion.machine.simple.PrimitiveElectrolyzerMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVBenderMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVChemicalReactorMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVCutterMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVFluidExtractorMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVFluidSolidifierMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVLatheMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVWireMillMachine;
import com.hoshino.gregulvexpansion.machine.storage.LeadAcidBatteryWallMachine;

import net.minecraft.network.chat.Component;

/**
 * 本模组机器注册 (总纲 §7：独有机制机器不带层级前缀)。
 *
 * <p>由 RegisterEvent&lt;MachineDefinition&gt; 监听触发（沿用姊妹项目模式），
 * 晚于 GTCovers/GTItems 的上游初始化与 GT 配方类型注册事件。
 */
public final class GULVMachines {
    /** 手摇发电机 — 玩家的第一台发电设备 (P0, hand-crank-dynamo.md)。 */
    public static final MachineDefinition HAND_CRANK_DYNAMO = GULVRegistration.REGISTRATE
            .machine("hand_crank_dynamo", HandCrankDynamoMachine::new)
            .tier(GTValues.ULV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullModel("hand_crank_dynamo")
            .langValue("Hand-Crank Dynamo")
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.1"),
                    Component.translatable("gregulvexpansion.machine.hand_crank_dynamo.tooltip.crank"))
            .register();

    /**
     * 原型电解槽 — 电力化学的第一课，P1 首位 (B1: 储能链前置)。
     * 仅运行 {@link GULVRecipeTypes#PRIMITIVE_ELECTROLYSIS} 白名单表内反应。
     */
    public static final MachineDefinition PRIMITIVE_ELECTROLYZER = GULVRegistration.REGISTRATE
            .machine("primitive_electrolyzer",
                    holder -> new PrimitiveElectrolyzerMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("Primitive Electrolyzer")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("primitive_electrolyzer"), GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS))
            .workableTieredHullModel(GTCEu.id("block/machines/primitive_electrolyzer"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.primitive_electrolyzer.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.primitive_electrolyzer.tooltip.summary.1"))
            .register();

    /**
     * 铅酸蓄电墙 — 自含 24,000 EU 储能方块，六面接线 1A 进出（D8/D10），
     * 过压熔断 + 扳手重置，无电池物品槽（与上游充电站语义互补）。
     */
    public static final MachineDefinition LEAD_ACID_BATTERY_WALL = GULVRegistration.REGISTRATE
            .machine("lead_acid_battery_wall",
                    holder -> new LeadAcidBatteryWallMachine(holder, GTValues.ULV))
            .tier(GTValues.ULV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullModel("lead_acid_battery_wall")
            .langValue("Lead-Acid Battery Wall")
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.summary.1"),
                    Component.translatable("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.fuse"))
            .register();

    /**
     * 温差发电机 — 贴热产电的免维护基础负荷（P1, D5/C2/D15）。
     * 岩浆源+贴水 5 EU/t 封顶，满功率需求转红石发电机。
     */
    public static final MachineDefinition THERMOELECTRIC_GENERATOR = GULVRegistration.REGISTRATE
            .machine("thermoelectric_generator", ThermoelectricGeneratorMachine::new)
            .tier(GTValues.ULV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullModel("thermoelectric_generator")
            .langValue("Thermoelectric Generator")
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.thermoelectric_generator.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.thermoelectric_generator.tooltip.summary.1"))
            .register();

    /** 超低压线材轧机 — 金属锭→单线子集下沉（P1, ulv-basic-machines.md）。 */
    public static final MachineDefinition ULV_WIRE_MILL = GULVRegistration.REGISTRATE
            .machine("ulv_wire_mill",
                    holder -> new ULVWireMillMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_WIRE_MILLING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Wire Mill")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_wire_mill"), GULVRecipeTypes.ULV_WIRE_MILLING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_wire_mill"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_wire_mill.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_wire_mill.tooltip.summary.1"))
            .register();

    /** 超低压切割机 — 基础切割件子集下沉，锯片锻铁（P1, D12）。 */
    public static final MachineDefinition ULV_CUTTER = GULVRegistration.REGISTRATE
            .machine("ulv_cutter",
                    holder -> new ULVCutterMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_CUTTING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Cutter")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_cutter"), GULVRecipeTypes.ULV_CUTTING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_cutter"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_cutter.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_cutter.tooltip.summary.1"))
            .register();

    /**
     * 红石发电机 — 可堆叠燃料电（P2, redstone-generator.md，C6 基准联动）。
     * 单台恒定 8 EU/t，只烧红石系燃料；堆台数是唯一扩容方式。
     */
    public static final MachineDefinition REDSTONE_GENERATOR = GULVRegistration.REGISTRATE
            .machine("redstone_generator",
                    holder -> new RedstoneGeneratorMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.ALL)
            .recipeType(GULVRecipeTypes.REDSTONE_GENERATOR_FUELS)
            .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
            .addOutputLimit(ItemRecipeCapability.CAP, 0)
            .addOutputLimit(FluidRecipeCapability.CAP, 0)
            .langValue("Redstone Generator")
            .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("redstone_generator"), GULVRecipeTypes.REDSTONE_GENERATOR_FUELS))
            .workableTieredHullModel(GTCEu.id("block/generators/redstone_generator"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.redstone_generator.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.redstone_generator.tooltip.summary.1"))
            .register();

    /** 超低压卷板机 — 锭→板 1:1，对比锻锤 0.67 板/锭 +50% 产能（v0.5）。 */
    public static final MachineDefinition ULV_BENDER = GULVRegistration.REGISTRATE
            .machine("ulv_bender",
                    holder -> new ULVBenderMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_BENDING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Bender")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_bender"), GULVRecipeTypes.ULV_BENDING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_bender"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_bender.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_bender.tooltip.summary.1"))
            .register();

    /** 超低压车床 — 螺栓→螺丝自动化与木材链（剥皮原木→长木杆），（v0.5）。 */
    public static final MachineDefinition ULV_LATHE = GULVRegistration.REGISTRATE
            .machine("ulv_lathe",
                    holder -> new ULVLatheMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_TURNING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Lathe")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_lathe"), GULVRecipeTypes.ULV_TURNING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_lathe"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_lathe.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_lathe.tooltip.summary.1"))
            .register();

    /** 超低压化学反应釜 — 酸链子集（硫氧化/三氧化硫/成酸），电动提效第二级（v0.8）。 */
    public static final MachineDefinition ULV_CHEMICAL_REACTOR = GULVRegistration.REGISTRATE
            .machine("ulv_chemical_reactor",
                    holder -> new ULVChemicalReactorMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_CHEMICAL_REACTING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Chemical Reactor")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_chemical_reactor"), GULVRecipeTypes.ULV_CHEMICAL_REACTING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_chemical_reactor"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_chemical_reactor.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_chemical_reactor.tooltip.summary.1"))
            .register();

    /** 超低压流体固化器 — 水/岩浆 + 模具固化成型（v0.8），模具不消耗。 */
    public static final MachineDefinition ULV_FLUID_SOLIDIFIER = GULVRegistration.REGISTRATE
            .machine("ulv_fluid_solidifier",
                    holder -> new ULVFluidSolidifierMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_FLUID_SOLIDFICATION)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Fluid Solidifier")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_fluid_solidifier"), GULVRecipeTypes.ULV_FLUID_SOLIDFICATION))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_fluid_solidifier"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_solidifier.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_solidifier.tooltip.summary.1"))
            .register();

    /** 超低压流体提取机 — 橡胶链子集（树脂/橡胶树部件→生橡胶粉），电力版（v0.9）。 */
    public static final MachineDefinition ULV_FLUID_EXTRACTOR = GULVRegistration.REGISTRATE
            .machine("ulv_fluid_extractor",
                    holder -> new ULVFluidExtractorMachine(holder, GTValues.ULV,
                            GTMachineUtils.defaultTankSizeFunction))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.ULV_EXTRACTING)
            .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
            .langValue("ULV Fluid Extractor")
            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                    GregULVExpansion.id("ulv_fluid_extractor"), GULVRecipeTypes.ULV_EXTRACTING))
            .workableTieredHullModel(GTCEu.id("block/machines/ulv_fluid_extractor"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_extractor.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_extractor.tooltip.summary.1"))
            .register();

    private GULVMachines() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}

