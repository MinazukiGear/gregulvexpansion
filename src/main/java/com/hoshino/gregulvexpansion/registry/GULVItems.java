package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.common.item.CoverPlaceBehavior;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.hoshino.gregulvexpansion.GregULVExpansion;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.network.chat.Component;

/**
 * 本模组全部新物品 (总纲 §7 命名规范；ID 无材料名、小写下划线)。
 *
 * <p>静态初始化由 {@link #init()} (initializeAddon 阶段) 触发，晚于
 * {@link GULVCovers#init()}，cover 定义已存在，物品侧引用安全。
 * 构件物品一律不带 ElectricStats (与上游构件一致，调研 B1)。
 */
public final class GULVItems {
    static {
        // 构件物品进 GT「物品」标签页；REGISTRATE 默认标签页 (MACHINE) 留给机器。
        GULVRegistration.REGISTRATE.creativeModeTab(GTCreativeModeTabs.ITEM);
    }

    /** 猫须探测器 — ULV 元件线起点，手摇发电机与马达的合成前置 (ulv-circuit-line.md)。 */
    public static final ItemEntry<ComponentItem> CATS_WHISKER_DETECTOR = GULVRegistration.REGISTRATE
            .item("cats_whisker_detector", ComponentItem::create)
            .lang("Cat's-Whisker Detector")
            .onRegister(GTItems.attach(new TooltipBehavior(lines -> lines.add(Component.translatable(
                    "gregulvexpansion.item.cats_whisker_detector.tooltip")))))
            .register();

    /** 木质曲柄 — 独立物品 (D2)，可扳手拆装，兼容 AE2 曲柄驱动 (hand-crank-dynamo.md)。 */
    public static final ItemEntry<ComponentItem> WOOD_CRANK = GULVRegistration.REGISTRATE
            .item("wood_crank", ComponentItem::create)
            .lang("Wooden Crank")
            .onRegister(GTItems.attach(new TooltipBehavior(lines -> lines.add(Component.translatable(
                    "gregulvexpansion.item.wood_crank.tooltip")))))
            .register();

    /** 超低压电动马达 — 全部 ULV 机器与温差/红石发电机的公共前置 (D15)。 */
    public static final ItemEntry<ComponentItem> ULV_ELECTRIC_MOTOR = GULVRegistration.REGISTRATE
            .item("ulv_electric_motor", ComponentItem::create)
            .lang("ULV Electric Motor")
            .onRegister(GTItems.attach(new TooltipBehavior(lines -> lines.add(Component.translatable(
                    "gregulvexpansion.item.ulv_electric_motor.tooltip")))))
            .register();

    /** 超低压传送带模块 — cover 物品，2 件/t (C5 公式值)。 */
    public static final ItemEntry<ComponentItem> ULV_CONVEYOR_MODULE = GULVRegistration.REGISTRATE
            .item("ulv_conveyor_module", ComponentItem::create)
            .lang("ULV Conveyor Module")
            .onRegister(GTItems.attach(new CoverPlaceBehavior(GULVCovers.CONVEYOR_ULV)))
            .onRegister(GTItems.attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate", 2));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();

    /** 超低压电动泵 — cover 物品，16 mB/t (C5 公式值)。 */
    public static final ItemEntry<ComponentItem> ULV_ELECTRIC_PUMP = GULVRegistration.REGISTRATE
            .item("ulv_electric_pump", ComponentItem::create)
            .lang("ULV Electric Pump")
            .onRegister(GTItems.attach(new CoverPlaceBehavior(GULVCovers.PUMP_ULV)))
            .onRegister(GTItems.attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.pump.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.pump_rate", 16));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();

    private GULVItems() {}

    public static void init() {
        // 仅为触发上方静态初始化
    }
}
