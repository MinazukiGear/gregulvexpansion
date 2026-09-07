package com.hoshino.gregulvexpansion.machine.generator;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * 红石发电机 (redstone-generator.md 已裁决草案 v0.3)。
 *
 * <p>ULV 层第一台可自动化燃料发电机：漏斗/传送带喂红石，机器自己烧，
 * 单台恒定 8 EU/t · 1 A，堆台数是唯一扩容方式。只烧红石系燃料
 * （红石粉 1,200 EU / 红石块 10,800 EU，C6 基准联动），禁止扩充通用燃料。
 * 环境危害强度 0（红石燃烧无烟尘叙事，且无 CO 语义）。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RedstoneGeneratorMachine extends SimpleGeneratorMachine {

    public RedstoneGeneratorMachine(IMachineBlockEntity holder, int tier,
                                    Int2IntFunction tankScalingFunction) {
        super(holder, tier, 0.0f, tankScalingFunction);
    }
}
