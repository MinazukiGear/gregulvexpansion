package com.hoshino.gregulvexpansion.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 原始蒸馏塔 (primitive-distillation-tower.md v1.1)：无电多方块（3×6×3 固定，
 * 蒸汽驱动，仅接受流体），原油分馏 ×16 时长拉伸——HV 级配方的无电原始多方块
 * 特例首例。「前电力时代工业遗迹」第五件套。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveDistillationTowerMachine extends WorkableMultiblockMachine {

    public PrimitiveDistillationTowerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
}
