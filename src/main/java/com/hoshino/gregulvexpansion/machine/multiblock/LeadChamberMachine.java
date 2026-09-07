package com.hoshino.gregulvexpansion.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 铅室法制酸装置控制器 (lead-chamber-acid-plant.md 已裁决草案 v0.3.1)。
 *
 * <p>「电力时代之前的硫酸就该有电力时代之前的造法」：无电多方块，蒸汽驱动。
 * 标准工作多方块语义（结构成型检查 + 配方逻辑 + 成品输出），无任何自定义
 * 能量/ Maintenance 逻辑——配方的全部成本就是硫粉、水与蒸汽（v0.3.1 时长
 * 800 t，产能替代性已锁死，仅无电备援价值）。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LeadChamberMachine extends WorkableMultiblockMachine {

    public LeadChamberMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }
}
