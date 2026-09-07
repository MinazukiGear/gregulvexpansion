package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压卷板机 (ulv-basic-machines.md v0.5，所有者指示「能提升产能的机器」)：
 * 锭 ×1 → 板 ×1（上游卷板 1:1 语义），对比锻锤机 3 锭 → 2 板的 0.67 板/锭
 * **+50% 产能**。仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_BENDING}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVBenderMachine extends ULVSimpleMachine {

    public ULVBenderMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
