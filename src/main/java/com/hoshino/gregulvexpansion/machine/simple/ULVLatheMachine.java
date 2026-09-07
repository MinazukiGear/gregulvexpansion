package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压车床 (ulv-basic-machines.md v0.5)：螺栓→螺丝自动化（本模组传送带/
 * 泵配方的螺丝需求内部闭环）与木材链（剥皮原木 → 长木杆 ×4 + 木尘）。仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_TURNING}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVLatheMachine extends ULVSimpleMachine {

    public ULVLatheMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
