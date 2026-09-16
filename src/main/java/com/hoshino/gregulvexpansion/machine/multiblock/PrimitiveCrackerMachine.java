package com.hoshino.gregulvexpansion.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 原始裂化机：无电多方块（完整沿用上游裂化机的 5×3×3
 * 机壳/燃烧室交错结构，蒸汽驱动），乙烯产量为上游裂化+蒸馏全链的 1/6。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveCrackerMachine extends WorkableMultiblockMachine {

    public PrimitiveCrackerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
}
