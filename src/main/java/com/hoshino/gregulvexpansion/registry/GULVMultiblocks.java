package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.multiblock.LeadChamberMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveCrackerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.PrimitiveDistillationTowerMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.ULVFluidDrillingRigMachine;
import net.minecraft.network.chat.Component;

import java.util.Comparator;

import static com.gregtechceu.gtceu.api.pattern.Predicates.air;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.controller;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.BACK;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.RIGHT;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.UP;

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
     * <p>结构沿用上游大型化学反应釜的 3×3×3 骨架，以铅衬机壳、青铜管道机壳
     * 和钢制燃烧室替换高阶构件。机器仍为无电、蒸汽驱动。
     */
    public static final MultiblockMachineDefinition LEAD_CHAMBER = GULVRegistration.REGISTRATE
            .multiblock("lead_chamber", LeadChamberMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GULVRecipeTypes.LEAD_CHAMBER_RECIPES)
            .appearanceBlock(GULVBlocks.LEAD_LINED_CASING)
            .pattern(definition -> {
                TraceabilityPredicate casing = blocks(GULVBlocks.LEAD_LINED_CASING.get())
                        .setMinGlobalLimited(10);
                TraceabilityPredicate abilities = Predicates.autoAbilities(definition.getRecipeTypes(),
                        false, false, true, false, true, true);
                return FactoryBlockPattern.start()
                        .aisle("XXX", "XCX", "XXX")
                        .aisle("XCX", "CPC", "XCX")
                        .aisle("XXX", "XSX", "XXX")
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('X', casing.or(abilities))
                        .where('P', blocks(GTBlocks.CASING_BRONZE_PIPE.get()))
                        .where('C', blocks(GTBlocks.FIREBOX_STEEL.get()).setExactLimit(1)
                                .or(abilities)
                                .or(casing))
                        .build();
            })
            .workableCasingModel(GregULVExpansion.id("block/casings/lead_lined_casing"),
                    GTCEu.id("block/multiblock/large_chemical_reactor"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.1"),
                    Component.translatable("gregulvexpansion.machine.lead_chamber.tooltip.summary.2"))
            .register();

    
    
    
    /**
     * 原始蒸馏塔 — 无电多方块，结构与上游蒸馏塔一致，原油分馏 ×16 拉伸
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
            .pattern(definition -> {
                TraceabilityPredicate exportPredicate = abilities(PartAbility.EXPORT_FLUIDS_1X)
                        .setMaxLayerLimited(1);
                return FactoryBlockPattern.start(RIGHT, BACK, UP)
                        .aisle("YSY", "YYY", "YYY")
                        .aisle("ZZZ", "Z#Z", "ZZZ")
                        .aisle("XXX", "X#X", "XXX").setRepeatable(0, 3)
                        .aisle("XXX", "XXX", "XXX")
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('Y', blocks(GULVBlocks.DISTILLATION_FRAME.get())
                                .or(abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                                .or(abilities(PartAbility.IMPORT_FLUIDS).setMinGlobalLimited(1)))
                        .where('Z', blocks(GULVBlocks.DISTILLATION_FRAME.get()).or(exportPredicate))
                        .where('X', blocks(GULVBlocks.DISTILLATION_FRAME.get()).or(exportPredicate))
                        .where('#', air())
                        .build();
            })
            .allowExtendedFacing(false)
            .partSorter(Comparator.comparingInt(part -> part.self().getPos().getY()))
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GTCEu.id("block/multiblock/distillation_tower"))
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
            .rotationState(RotationState.ALL)
            .recipeType(GULVRecipeTypes.PRIMITIVE_CRACKING)
            .appearanceBlock(GULVBlocks.DISTILLATION_FRAME)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("HCHCH", "HCHCH", "HCHCH")
                    .aisle("HCHCH", "H###H", "HCHCH")
                    .aisle("HCHCH", "HCOCH", "HCHCH")
                    .where('H', blocks(GULVBlocks.DISTILLATION_FRAME.get()).setMinGlobalLimited(12)
                            .or(Predicates.autoAbilities(definition.getRecipeTypes(),
                                    false, false, true, true, true, true)))
                    .where('#', air())
                    .where('C', blocks(GTBlocks.FIREBOX_STEEL.get()))
                    .where('O', controller(blocks(definition.getBlock())))
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GTCEu.id("block/multiblock/cracking_unit"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.primitive_cracker.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.primitive_cracker.tooltip.summary.1"))
            .register();

    /**
     * 超低压流体钻井机 — 仿上游流体钻井机的 ULV 多方块（抽取基岩流体油田，带枯竭机制），
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
                    .aisle("XXX", "#F#", "#F#", "#F#", "###", "###", "###")
                    .aisle("XXX", "FCF", "FCF", "FCF", "#F#", "#F#", "#F#")
                    .aisle("XSX", "#F#", "#F#", "#F#", "###", "###", "###")
                    .where('X', blocks(GULVBlocks.DISTILLATION_FRAME.get()).setMinGlobalLimited(3)
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where('C', blocks(GULVBlocks.DISTILLATION_FRAME.get()))
                    .where('F', blocks(GTMaterialBlocks.MATERIAL_BLOCKS
                            .get(TagPrefix.frameGt, GTMaterials.Bronze).get()))
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('#', Predicates.any())
                    .build())
            .workableCasingModel(GregULVExpansion.id("block/casings/distillation_frame"),
                    GTCEu.id("block/multiblock/fluid_drilling_rig"))
            .tooltips(
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.0"),
                    Component.translatable("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.1"))
            .register();

    private GULVMultiblocks() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
