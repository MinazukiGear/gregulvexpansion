package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压化学反应釜 (ulv-basic-machines.md v0.8)：酸链子集（硫氧化/三氧化硫/成酸，
 * EUt 7 原样直录）。与铅室法分工：反应釜快 4 倍但需电解供氧与电力，
 * 铅室无电无氧仍是起步首选。240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVChemicalReactorMachine extends ULVSimpleMachine {

    public ULVChemicalReactorMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
