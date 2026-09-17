package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.generator.HandCrankDynamoMachine;
import com.hoshino.gregulvexpansion.machine.generator.ThermoelectricGeneratorMachine;
import com.hoshino.gregulvexpansion.machine.simple.ULVSimpleMachine;
import com.hoshino.gregulvexpansion.machine.storage.LeadAcidBatteryWallMachine;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

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
            .langValue(generatorName("Hand-Crank"))
            .tooltips(
                    voltageOut(GTValues.V[GTValues.ULV]),
                    energyCapacity(HandCrankDynamoMachine.CAPACITY))
            .register();

    /**
     * 原型电解槽 — 电力化学的第一课，P1 首位 (B1: 储能链前置)。
     * 仅运行 {@link GULVRecipeTypes#PRIMITIVE_ELECTROLYSIS} 白名单表内反应。
     */
    public static final MachineDefinition PRIMITIVE_ELECTROLYZER = registerUlvSimpleMachine(
            "primitive_electrolyzer", "Electrolyzer", GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS, "electrolyzer");

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
            .langValue(simpleMachineName("Lead-Acid Battery Wall"))
            .tooltips(
                    energyCapacity(LeadAcidBatteryWallMachine.CAPACITY),
                    Component.translatable("gtceu.universal.tooltip.voltage_in_out",
                            FormattingUtil.formatNumbers(GTValues.V[GTValues.ULV]), GTValues.VNF[GTValues.ULV]),
                    Component.translatable("gtceu.universal.tooltip.amperage_in_out",
                            LeadAcidBatteryWallMachine.AMPS))
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
            .langValue(generatorName("Thermoelectric"))
            .tooltips(
                    voltageOut(GTValues.V[GTValues.ULV]),
                    energyCapacity(ThermoelectricGeneratorMachine.CAPACITY))
            .register();

    /** 超低压线材轧机 — 金属锭→单线子集下沉（P1, ulv-basic-machines.md）。 */
    public static final MachineDefinition ULV_WIRE_MILL = registerUlvSimpleMachine(
            "ulv_wire_mill", "Wiremill", GULVRecipeTypes.ULV_WIRE_MILLING, "wiremill");

    /** 原始切割机 — 基础切割件子集下沉，配方使用 ULV 青铜圆锯头（P1, D12）。 */
    public static final MachineDefinition ULV_CUTTER = registerUlvSimpleMachine(
            "ulv_cutter", "Cutter", GULVRecipeTypes.ULV_CUTTING, "cutter");

    /**
     * 红石发电机 — 可堆叠燃料电（P2, redstone-generator.md，C6 基准联动）。
     * 单台恒定 8 EU/t，只烧红石系燃料；堆台数是唯一扩容方式。
     */
    public static final MachineDefinition REDSTONE_GENERATOR = registerUlvGenerator(
            "redstone_generator", "Redstone", GULVRecipeTypes.REDSTONE_GENERATOR_FUELS,
            GregULVExpansion.id("block/generators/redstone_generator"), GTMachineUtils.defaultTankSizeFunction);

    /** 超低压卷板机 — 锭→板 1:1，对比锻锤 0.67 板/锭 +50% 产能（v0.5）。 */
    public static final MachineDefinition ULV_BENDER = registerUlvSimpleMachine(
            "ulv_bender", "Bender", GULVRecipeTypes.ULV_BENDING, "bender");

    /** 超低压车床 — 螺栓→螺丝自动化与木材链（剥皮原木→长木杆），（v0.5）。 */
    public static final MachineDefinition ULV_LATHE = registerUlvSimpleMachine(
            "ulv_lathe", "Lathe", GULVRecipeTypes.ULV_TURNING, "lathe");

    /** 原始化学反应釜 — 酸链、石油脱硫、聚乙烯与橡胶反应子集（v0.11）。 */
    public static final MachineDefinition ULV_CHEMICAL_REACTOR = registerUlvSimpleMachine(
            "ulv_chemical_reactor", "Chemical Reactor", GULVRecipeTypes.ULV_CHEMICAL_REACTING, "chemical_reactor");

    /** 原始流体固化器 — 水、岩浆、聚乙烯与橡胶的模具固化子集（v0.11）。 */
    public static final MachineDefinition ULV_FLUID_SOLIDIFIER = registerUlvSimpleMachine(
            "ulv_fluid_solidifier", "Fluid Solidifier", GULVRecipeTypes.ULV_FLUID_SOLIDFICATION, "fluid_solidifier");

    /** 超低压流体提取机 — 橡胶链子集（树脂/橡胶树部件→生橡胶粉），电力版（v0.9）。 */
    public static final MachineDefinition ULV_FLUID_EXTRACTOR = registerUlvSimpleMachine(
            "ulv_fluid_extractor", "Extractor", GULVRecipeTypes.ULV_EXTRACTING, "extractor");

    /**
     * 超低压微型燃气轮机 — 气体燃料电（P2, gas-turbine.md）。
     * 只烧白名单四种流体（天然气/含硫气体/甲烷/含硫石脑油），热值与上游一致、
     * 恒定 8 EU/t · 1 A；闭合石油线天然气/含硫气体/甲烷死端。罐容 4,000 mB 定容
     * （上游 genericGeneratorTankSizeFunction 在 tier 0 会算出负值，故自定义）。
     */
    public static final MachineDefinition ULV_GAS_TURBINE = registerUlvGenerator(
            "ulv_gas_turbine", "Gas Turbine", GULVRecipeTypes.ULV_GAS_TURBINE_FUELS,
            GTCEu.id("block/generators/gas_turbine"), tier -> 4 * FluidType.BUCKET_VOLUME);

    /** 超低压极化机 — 磁化铁杆电力磁化，双路线的自动化侧（P2, ulv-polarizer.md；马达 v0.5）。 */
    public static final MachineDefinition ULV_POLARIZER = registerUlvSimpleMachine(
            "ulv_polarizer", "Polarizer", GULVRecipeTypes.ULV_POLARIZING, "polarizer");

    private static MachineDefinition registerUlvSimpleMachine(String id, String name,
                                                              GTRecipeType recipeType, String model) {
        return GULVRegistration.REGISTRATE
                .machine(id, holder -> new ULVSimpleMachine(holder, GTValues.ULV,
                        GTMachineUtils.defaultTankSizeFunction))
                .tier(GTValues.ULV)
                .rotationState(RotationState.NON_Y_AXIS)
                .recipeType(recipeType)
                .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
                .langValue(simpleMachineName(name))
                .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(
                        GregULVExpansion.id(id), recipeType))
                .workableTieredHullModel(GTCEu.id("block/machines/" + model))
                .tooltips(workableTooltip(recipeType))
                .register();
    }

    private static MachineDefinition registerUlvGenerator(String id, String name,
                                                           GTRecipeType recipeType, ResourceLocation model,
                                                           Int2IntFunction tankScalingFunction) {
        return GULVRegistration.REGISTRATE
                .machine(id, holder -> new SimpleGeneratorMachine(holder, GTValues.ULV, 0.0f,
                        tankScalingFunction))
                .tier(GTValues.ULV)
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
                .addOutputLimit(ItemRecipeCapability.CAP, 0)
                .addOutputLimit(FluidRecipeCapability.CAP, 0)
                .langValue(generatorName(name))
                .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(
                        GregULVExpansion.id(id), recipeType))
                .workableTieredHullModel(model)
                .tooltips(GTMachineUtils.workableTiered(GTValues.ULV, GTValues.V[GTValues.ULV],
                        GTValues.V[GTValues.ULV] * 64, recipeType,
                        tankScalingFunction.applyAsInt(GTValues.ULV), false))
                .register();
    }

    private static String simpleMachineName(String name) {
        return "%s %s %s".formatted(GTValues.VLVH[GTValues.ULV], name, GTValues.VLVT[GTValues.ULV]);
    }

    private static String generatorName(String name) {
        return "%s %s Generator %s".formatted(
                GTValues.VLVH[GTValues.ULV], name, GTValues.VLVT[GTValues.ULV]);
    }

    private static Component[] workableTooltip(GTRecipeType recipeType) {
        return GTMachineUtils.workableTiered(GTValues.ULV, GTValues.V[GTValues.ULV],
                ULVSimpleMachine.ENERGY_CAPACITY, recipeType,
                GTMachineUtils.defaultTankSizeFunction.applyAsInt(GTValues.ULV), true);
    }

    private static Component voltageOut(long voltage) {
        return Component.translatable("gtceu.universal.tooltip.voltage_out",
                FormattingUtil.formatNumbers(voltage), GTValues.VNF[GTValues.ULV]);
    }

    private static Component energyCapacity(long capacity) {
        return Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                FormattingUtil.formatNumbers(capacity));
    }

    private GULVMachines() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
