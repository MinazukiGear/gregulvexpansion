package com.hoshino.gregulvexpansion.machine.generator;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 超低压微型燃气轮机 (gas-turbine.md v1.0)。
 *
 * <p>常设微电第三台：流体燃料发电（温差 = 环境热、红石 = 固体燃料、燃气 = 流体燃料）。
 * 只烧白名单四种流体（天然气/含硫气体/甲烷/含硫石脑油），热值与上游燃气轮机一致、
 * 输出恒定 8 EU/t · 1 A，闭合石油线天然气/含硫气体/甲烷死端。堆台数是唯一扩容方式。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVGasTurbineMachine extends SimpleGeneratorMachine {

    public ULVGasTurbineMachine(IMachineBlockEntity holder, int tier,
                                Int2IntFunction tankScalingFunction) {
        super(holder, tier, 0.0f, tankScalingFunction);
    }
}
