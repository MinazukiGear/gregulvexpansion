package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.hoshino.gregulvexpansion.machine.generator.HandCrankDynamoMachine;

import net.minecraft.network.chat.Component;

/**
 * 本模组机器注册 (总纲 §7：独有机制机器不带层级前缀)。
 *
 * <p>由 RegisterEvent&lt;MachineDefinition&gt; 监听触发（沿用姊妹项目模式），
 * 晚于 GTCovers/GTItems 的上游初始化。ULV 机壳 + 自绘 overlay 模型；
 * 曲柄动态部件为后续增强（设计文档标注可后置）。
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

    private GULVMachines() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
