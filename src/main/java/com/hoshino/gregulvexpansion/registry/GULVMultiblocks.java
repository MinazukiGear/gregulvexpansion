package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.multiblock.LeadChamberMachine;

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

    private GULVMultiblocks() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
