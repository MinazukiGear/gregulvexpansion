package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压流体提取机 (ulv-basic-machines.md v0.9)：电力版提取机的 ULV 层级变体，
 * 橡胶链子集（粘性树脂/橡胶树部件 → 生橡胶粉，EUt 2 原样直录），生橡胶粉
 * 供蒸汽链制成橡胶板（本模组传送带/泵配方原料）。仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_EXTRACTING}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVFluidExtractorMachine extends ULVSimpleMachine {

    public ULVFluidExtractorMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
