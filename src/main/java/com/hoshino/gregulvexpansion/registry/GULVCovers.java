package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.client.renderer.cover.IOCoverRenderer;
import com.gregtechceu.gtceu.common.cover.ConveyorCover;
import com.gregtechceu.gtceu.common.cover.PumpCover;
import com.gregtechceu.gtceu.common.data.GTCovers;
import com.hoshino.gregulvexpansion.GregULVExpansion;

/**
 * ULV 覆盖板定义 (ulv-components.md 已裁决草案 v0.3)。
 *
 * <p>上游电动构件最低注册于 LV (调研 F12)，tier 0 行为由上游速率公式的层级闭包
 * 自然给出 (调研 B2)：传送带 {@code 2 × 4^min(tier, LuV)} 件/t → 2 件/t，
 * 泵 {@code 64 × 4^min(tier−1, IV)} mB/t → 16 mB/t，行为类零修改。
 * 渲染器全层级共用同一贴图，直接复用上游构造 (调研 F1)。
 *
 * <p>注册时机：本类 {@link #init()} 由 {@code IGTAddon#registerCovers()} 在
 * {@code GTCovers.init()} 内调用，早于物品注册 (CommonProxy 中 GTCovers.init
 * 先于 GTItems.init 与 initializeAddon)，物品侧引用本类字段安全。
 */
public final class GULVCovers {
    /** 超低压传送带覆盖板 gregulvexpansion:conveyor.ulv — 2 件/t。 */
    public static CoverDefinition CONVEYOR_ULV;
    /** 超低压电动泵覆盖板 gregulvexpansion:pump.ulv — 16 mB/t。 */
    public static CoverDefinition PUMP_ULV;

    private GULVCovers() {}

    public static void init() {
        CONVEYOR_ULV = GTCovers.register(
                GregULVExpansion.id("conveyor.ulv"),
                (definition, coverable, attachedSide) ->
                        new ConveyorCover(definition, coverable, attachedSide, GTValues.ULV),
                // 与上游 CONVEYORS 相同的贴图构造，仅层级名不同
                () -> () -> new IOCoverRenderer(
                        GTCEu.id("block/cover/conveyor"),
                        null,
                        GTCEu.id("block/cover/conveyor_emissive"),
                        GTCEu.id("block/cover/conveyor_inverted_emissive")));
        PUMP_ULV = GTCovers.register(
                GregULVExpansion.id("pump.ulv"),
                (definition, coverable, attachedSide) ->
                        new PumpCover(definition, coverable, attachedSide, GTValues.ULV),
                // 上游泵全层级共用常量渲染器
                () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);
    }
}
