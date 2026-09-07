package com.hoshino.gregulvexpansion.machine.multiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * ULV 油田泵 (primitive-distillation-tower.md v1.1)：仿上游流体钻井机的 ULV 多方块，
 * 抽取基岩流体油田（原油/轻油/重油/天然气矿脉，带枯竭机制）。
 * 相对 LV 钻井机：产出减半、单位能耗加倍（ULVFluidDrillingRigLogic 实现，所有者指示）。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVFluidDrillingRigMachine extends FluidDrillMachine {

    public ULVFluidDrillingRigMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Override
    protected ULVFluidDrillingRigLogic createRecipeLogic(Object... args) {
        return new ULVFluidDrillingRigLogic(this);
    }

    @Override
    public ULVFluidDrillingRigLogic getRecipeLogic() {
        return (ULVFluidDrillingRigLogic) super.getRecipeLogic();
    }
}
