package com.hoshino.gregulvexpansion.registry;

import com.hoshino.gregulvexpansion.GregULVExpansion;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 本模组结构方块 (总纲 §7)。
 *
 * <p>铅衬机壳：铅室法多方块的外墙结构块，独立注册不做跨模组共享标签
 * (E2 已裁决)。方块注册沿用上游 createBrickCasingBlock 的 Registrate 形态：
 * 纯 cube 模型 + 单贴图，铁块硬度，镐可挖。
 */
public final class GULVBlocks {

    /** 蒸馏塔框架 — 原始蒸馏塔/原始裂化机/超低压流体钻井机共用的石油框架结构块。 */
    public static final BlockEntry<Block> DISTILLATION_FRAME = registerStructureBlock(
            "distillation_frame", "Distillation Frame");

    /** 铅衬机壳 — 铅室法多方块外墙（铅板衬壁的「工业遗迹」视觉载体）。 */
    public static final BlockEntry<Block> LEAD_LINED_CASING = registerStructureBlock(
            "lead_lined_casing", "Lead-Lined Casing");

    private GULVBlocks() {}

    private static BlockEntry<Block> registerStructureBlock(String name, String lang) {
        return GULVRegistration.REGISTRATE
            .block(name, Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                    prov.models().cubeAll(name, GregULVExpansion.id("block/casings/" + name))))
            .lang(lang)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(),
                    GregULVExpansion.id("block/" + name)))
            .build()
            .register();
    }
}
