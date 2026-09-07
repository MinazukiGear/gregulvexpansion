package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.multiblock.LeadChamberMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveCrackerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveDistillationTowerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.ULVFluidDrillingRigMachine;
import net.minecraft.world.level.block.Blocks;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveCrackerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveDistillationTowerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.ULVFluidDrillingRigMachine;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.pattern.Predicates.air;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.controller;

/**
 * 本模组多方块注册 (总纲 §7)。
 *
 * <p>注册时机与机器一致：RegisterEvent&lt;MachineDefinition&gt; 监听内触发
 * （见主类 registerMachines）。
 */
public final class GULVMultiblocks {
    /**
     * 铅室法制酸装置 — 无电多方块 (P1, lead-chamber-acid-plant.md)。
     *
     * <p>结构 3×4×3（宽×高×深）：铅衬机壳外墙 + 中空铅室；顶面中央为蒸汽
     * 进气口（必须放一个流体输入仓），其余墙面允许 GTCEu 标准仓
     * （物品输入 / 流体输入 / 流体输出——上游 ULV 层无标准仓，实际用 LV 档
     * 仓室，蒸汽时代钢链可造，与文档「不新增仓室类」一致）。
     * F2 已裁决：仓室嵌入地形不影响成型由空位谓词自然满足。
     */
    public static final MultiblockMachineDefinition LEAD_CHAMBER = GULVRegistration.REGISTRATE
            .multiblock("lead_chamber", LeadChamberMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GULVRecipeTypes.LEAD_CHAMBER_RECIPES)
            .appearanceBlock(GULVBlocks.LEAD_LINED_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XSX", "XXX")
                    .aisle("XXX", "X#X", "XXX")
                    .aisle("XXX", "X#X", "XXX")
                    .aisle("XXX", "X#X", "XYX")
                    .where('S', abilities(PartAbility.IMPORT_FLUIDS))
                    .where('X', blocks(GULVBlocks.LEAD_LINED_CASING.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes(),
                                    false, false, true, false, true, true)))
                    .where('#', air())
                    .where('Y', controller(blocks(definition.getBlock())))
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/lead_lined_casing"),
                    GregULVExpansion.id("block/multiblock/lead_chamber"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.1"),
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.2"))
            .register();

    
    
    
    /**
     * 原始蒸馏塔 — 无电多方块（3×6×3 固定，最高 6 层），原油分馏 ×16 拉伸
     * (v1.1, primitive-distillation-tower.md)：HV 级配方的无电原始多方块特例首例。
     * 只接受流体输入（与上游蒸馏塔一致）。
     */
    public static final MultiblockMachineDefinition PRIMITIVE_DISTILLATION_TOWER = GULVRegistration.REGISTRATE
            .multiblock("primitive_distillation_tower",
                    holder -> new PrimitiveDistillationTowerMachine(holder))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.PRIMITIVE_DISTILLATION)
            .appearanceBlock(GULVBlocks.DISTILLATION_FRAME)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA", "AAA", "AAA")
                    .aisle("AAA", "A#A", "AAA")
                    .aisle("AAA", "A#A", "AAA")
                    .aisle("AAA", "A#A", "AAA")
                    .aisle("AAA", "A#A", "AAA")
                    .aisle("AAA", "ASA", "AAA")
                    .where('A', blocks(GULVBlocks.DISTILLATION_FRAME.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes(),
                                    false, false, false, false, true, true)))
                    .where('#', air())
                    .where('S', controller(blocks(definition.getBlock())))
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GregULVExpansion.id("block/multiblock/primitive_distillation_tower"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.primitive_distillation_tower.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.primitive_distillation_tower.tooltip.summary.1"))
            .register();

    /**
     * 原始裂化机 — 无电多方块（5×3×3，仿上游裂化机结构原始化），
     * 乙烯产量为上游裂化+蒸馏全链的 1/6 (v1.1)。
     */
    public static final MultiblockMachineDefinition PRIMITIVE_CRACKER = GULVRegistration.REGISTRATE
            .multiblock("primitive_cracker",
                    holder -> new PrimitiveCrackerMachine(holder))
            .tier(GTValues.ULV)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GULVRecipeTypes.PRIMITIVE_CRACKING)
            .appearanceBlock(GULVBlocks.DISTILLATION_FRAME)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFFFF", "FFFFF", "FFFFF")
                    .aisle("FFFFF", "F###F", "FFFFF")
                    .aisle("FFFFF", "F#O#F", "FFFFF")
                    .where('F', blocks(GULVBlocks.DISTILLATION_FRAME.get())
                            .or(Predicates.autoAbilities(definition.getRecipeTypes(),
                                    false, false, false, false, true, true)))
                    .where('#', air())
                    .where('O', controller(blocks(definition.getBlock())))
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GregULVExpansion.id("block/multiblock/primitive_cracker"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.primitive_cracker.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.primitive_cracker.tooltip.summary.1"))
            .register();

    /**
     * ULV 油田泵 — 仿上游流体钻井机的 ULV 多方块（抽取基岩流体油田，带枯竭机制），
     * 相对 LV 钻井机：产出减半、单位能耗加倍（v1.1）。
     */
    public static final MultiblockMachineDefinition ULV_FLUID_DRILLING_RIG = GULVRegistration.REGISTRATE
            .multiblock("ulv_fluid_drilling_rig",
                    holder -> new ULVFluidDrillingRigMachine(holder, GTValues.ULV))
            .tier(GTValues.ULV)
            .rotationState(RotationState.ALL)
            .recipeType(com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(GULVBlocks.DISTILLATION_FRAME)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "#F#", "#F#", "###")
                    .aisle("XXX", "FCF", "FCF", "###")
                    .aisle("XSX", "#F#", "#F#", "###")
                    .where('X', blocks(GULVBlocks.DISTILLATION_FRAME.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where('C', blocks(GULVBlocks.DISTILLATION_FRAME.get()))
                    .where('F', blocks(FluidDrillMachine.getFrameState(GTValues.ULV)))
                    .where('S', controller(blocks(definition.getBlock())))
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GregULVExpansion.id("block/multiblock/ulv_fluid_drilling_rig"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.1"))
            .register();

    private GULVMultiblocks() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
