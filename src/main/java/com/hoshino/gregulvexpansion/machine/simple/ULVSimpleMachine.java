package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeAmperageEnergyContainer;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 本模组 ULV 用电机器公共基类。
 *
 * <p>与上游 {@link SimpleTieredMachine} 唯一差异：能量缓存收窄为
 * **240 EU**（约 30 t 缓冲，C3/C4 已裁决，原型电解槽 / ULV 线材轧机 /
 * 超低压切割机共用同一常量）——手摇发电机一次充满略有盈余的节奏锚点。
 * 输入 8 EU/t · 1 A。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ULVSimpleMachine extends SimpleTieredMachine {

    /** 能量缓存 EU（C3/C4 已裁决；本模组 ULV 用电机器统一口径）。 */
    public static final long ENERGY_CAPACITY = 240;

    protected ULVSimpleMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        // 与上游工作机器一致使用 RecipeAmperageEnergyContainer（按配方安培取电）
        return RecipeAmperageEnergyContainer.makeReceiverContainer(this, ENERGY_CAPACITY,
                GTValues.V[GTValues.ULV], 1);
    }
}
