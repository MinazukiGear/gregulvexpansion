package com.hoshino.gregulvexpansion.data;

import com.hoshino.gregulvexpansion.registry.GULVRegistration;

/**
 * en_us 语言条目 (addRawLang)；zh_cn 为手维护静态文件
 * {@code assets/gregulvexpansion/lang/zh_cn.json}，两处键面必须同步。
 */
public final class GULVLang {
    private GULVLang() {}

    public static void init() {
        // ---- 物品提示 ----
        add("gregulvexpansion.item.cats_whisker_detector.tooltip",
                "A purified galena crystal with a fine red alloy whisk — historically the first semiconductor device.");
        add("gregulvexpansion.item.wood_crank.tooltip",
                "A detachable crank. Right-click a hand-crank dynamo to install; wrench to detach.");
        add("gregulvexpansion.item.ulv_electric_motor.tooltip",
                "A crude 8 EU motor. The common prerequisite of every ULV machine and generator.");

        // ---- 手摇发电机 ----
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.0",
                "Your first power source: right-click to crank it. No fuel, no steam, no prerequisites.");
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.1",
                "Buffer: 480 EU. Each crank adds 24 EU (0.5 s cooldown); outputs 8 EU/t · 1 A on every side.");
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.crank",
                "Right-click with a wooden crank (AE2's crank works too) to reinstall; wrench to detach.");
    }

    private static void add(String key, String value) {
        GULVRegistration.REGISTRATE.addRawLang(key, value);
    }
}
