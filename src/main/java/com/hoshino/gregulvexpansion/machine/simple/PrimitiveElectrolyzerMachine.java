package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeAmperageEnergyContainer;

import net.minecraft.MethodsReturnNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 原型电解槽 (primitive-electrolyzer.md 已裁决草案 v0.3)。
 *
 * <p>「电力化学的第一课」：8 EU/t 做最基础的电解反应。基于
 * {@link SimpleTieredMachine}（标准工作机器语义： RecipeLogic + 比较器 +
 * 电池充电槽），唯一差异点是能量缓存——C3 已裁决 240 EU（与轧机/切割机
 * 共用常量，小于上游默认的 V×64=512），输入 8 EU/t · 1 A。
 * 配方子集准入制：仅 {@link GULVRecipeTypes#PRIMITIVE_ELECTROLYSIS} 表内反应。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveElectrolyzerMachine extends SimpleTieredMachine {

    /** 能量缓存 EU（C3 已裁决；轧机/切割机共用同一常量口径）。 */
    public static final long ENERGY_CAPACITY = 240;

    public PrimitiveElectrolyzerMachine(IMachineBlockEntity holder, int tier,
                                        Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        // 与上游工作机器一致使用 RecipeAmperageEnergyContainer（按配方安培取电），
        // 缓存从默认 512 收窄到 240（C3）：一次手摇充满、略有盈余的节奏锚点
        return RecipeAmperageEnergyContainer.makeReceiverContainer(this, ENERGY_CAPACITY,
                GTValues.V[GTValues.ULV], 1);
    }
}
