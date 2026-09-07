package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压切割机 (ulv-basic-machines.md)：上游切割机的 ULV 层级变体，
 * 仅运行 {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_CUTTING}
 * 白名单表内配方（杆→螺栓、长杆→杆、板材块→板；晶圆/宝石等精密语义排除）。
 * 锯片材质锻铁（D12）；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVCutterMachine extends ULVSimpleMachine {

    public ULVCutterMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
