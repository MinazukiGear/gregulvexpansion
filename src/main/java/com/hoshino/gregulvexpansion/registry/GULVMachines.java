package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.generator.HandCrankDynamoMachine;
import com.hoshino.gregulvexpansion.machine.simple.PrimitiveElectrolyzerMachine;
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

    private GULVMachines() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}

