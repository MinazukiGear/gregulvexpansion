package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压流体固化器 (ulv-basic-machines.md v0.8)：水/岩浆 + 模具固化成型
 * （雪球/雪块/黑曜石），模具不消耗。仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_FLUID_SOLIDFICATION}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVFluidSolidifierMachine extends ULVSimpleMachine {

    public ULVFluidSolidifierMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
