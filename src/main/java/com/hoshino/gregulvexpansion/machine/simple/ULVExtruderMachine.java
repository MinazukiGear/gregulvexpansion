package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压挤出机 (ulv-basic-machines.md v0.6)：上游挤出机的 ULV 层级变体，
 * 金属杆主产线（锭 + 杆模具 → 杆 ×2），另含螺栓/板挤出。仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_EXTRUDING}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVExtruderMachine extends ULVSimpleMachine {

    public ULVExtruderMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
