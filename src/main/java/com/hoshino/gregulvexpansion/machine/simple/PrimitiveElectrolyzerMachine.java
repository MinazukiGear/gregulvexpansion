package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 原型电解槽 (primitive-electrolyzer.md 已裁决草案 v0.3)。
 *
 * <p>「电力化学的第一课」：8 EU/t 做最基础的电解反应。基于
 * {@link ULVSimpleMachine}（240 EU 缓存 C3，输入 8 EU/t · 1 A）。
 * 配方子集准入制：仅 {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#PRIMITIVE_ELECTROLYSIS}
 * 表内反应。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveElectrolyzerMachine extends ULVSimpleMachine {

    public PrimitiveElectrolyzerMachine(IMachineBlockEntity holder, int tier,
                                        Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
