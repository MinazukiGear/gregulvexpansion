package com.hoshino.gregulvexpansion.machine.simple;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压极化机 (ulv-polarizer.md v1.0)：铁杆电力磁化的「自动化路线」。
 *
 * <p>与工作台红石粉 ×4 手工磁化（上游 iron_magnetic_stick 原配方）构成
 * 「蒸汽手工 vs 电力自动化」双路线——电力让工艺变便宜的第一课。
 * 仅运行
 * {@link com.hoshino.gregulvexpansion.registry.GULVRecipeTypes#ULV_POLARIZING}
 * 白名单表内配方；240 EU 缓存（C3/C4）。终身不做 II 档。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVPolarizerMachine extends ULVSimpleMachine {

    public ULVPolarizerMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction) {
        super(holder, tier, tankScalingFunction);
    }
}
