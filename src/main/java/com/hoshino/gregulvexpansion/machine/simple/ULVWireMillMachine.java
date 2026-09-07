package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压线材轧机 (ulv-basic-machines.md)：上游轧机的 ULV 层级变体，
 * 仅运行 {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_WIRE_MILLING}
 * 白名单表内配方（常用金属锭 → 单线 ×2，时长为上游 ×2）。辊轮靠马达转
 * （D15 公共前置）；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVWireMillMachine extends ULVSimpleMachine {

    public ULVWireMillMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
