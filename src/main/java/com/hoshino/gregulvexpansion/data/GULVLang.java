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
        add("gregulvexpansion.item.ulv_electric_piston.tooltip",
                "A crude electric piston. No cover on its own — the muscle inside robot arms and reciprocating mechanisms.");

        // ---- 手摇发电机 ----
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.0",
                "Your first power source: right-click to crank it. No fuel, no steam, no prerequisites.");
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.summary.1",
                "Buffer: 480 EU. Each crank adds 24 EU (0.5 s cooldown); outputs 8 EU/t · 1 A on every side.");
        add("gregulvexpansion.machine.hand_crank_dynamo.tooltip.crank",
                "Right-click with a wooden crank (AE2's crank works too) to reinstall; wrench to detach.");

        // ---- 原型电解槽 ----
        add("gregulvexpansion.machine.primitive_electrolyzer.tooltip.summary.0",
                "Lesson one of electric chemistry: electrolysis at 8 EU/t — water in, hydrogen and oxygen out.");
        add("gregulvexpansion.machine.primitive_electrolyzer.tooltip.summary.1",
                "Runs only the hand-picked ULV reaction whitelist; buffer 240 EU. Its rectifier bridge is a pair of cat's-whisker detectors.");

        // ---- 铅酸蓄电墙 ----
        add("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.summary.0",
                "A wall-mounted battery: 24,000 EU of self-contained storage. Wire any side — 8 EU/t · 1 A in and out.");
        add("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.summary.1",
                "No battery slots (use the upstream charging station for that); no self-discharge when idle.");
        add("gregulvexpansion.machine.lead_acid_battery_wall.tooltip.fuse",
                "Overvoltage from an LV+ network trips its fuse and locks it; unsneaking wrench click resets it.");

        // ---- 铅衬机壳 / 铅室法制酸装置 ----
        // (铅衬机壳名称由 GULVBlocks 的 .lang() 生成，勿重复添加)
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.0",
                "A no-electricity multiblock that makes sulfuric acid with steam — 1850s industry at its finest.");
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.1",
                "Recipe: 2 sulfur dust + 500 mB water + steam -> 500 mB sulfuric acid in 40 s. Needs a fluid input hatch on top, plus item input, fluid input and fluid output hatches on the walls.");
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.2",
                "Far slower than LV chemical reactors — an off-grid backup, not a competitor.");

        // ---- 温差发电机 ----
        add("gregulvexpansion.machine.thermoelectric_generator.tooltip.summary.0",
                "Paste it against a heat source (lava, fire, torches) and it quietly produces 1-5 EU/t. Water or ice on any other face boosts the output.");
        add("gregulvexpansion.machine.thermoelectric_generator.tooltip.summary.1",
                "Maintenance-free but capped at 8 EU/t · 1 A — for a real power grid, move on to redstone fuel cells.");

        // ---- 超低压线材轧机 / 切割机 ----
        add("gregulvexpansion.machine.ulv_wire_mill.tooltip.summary.0",
                "Draws metal ingots into single wires at 8 EU/t — twice as slow as the LV wiremill, half the energy.");
        add("gregulvexpansion.machine.ulv_wire_mill.tooltip.summary.1",
                "Runs a hand-picked subset only (red alloy, copper, iron, tin, lead, zinc).");
        add("gregulvexpansion.machine.ulv_cutter.tooltip.summary.0",
                "A motor-driven saw blade for basic cutting: rods into bolts, long rods into rods, blocks into plates.");
        add("gregulvexpansion.machine.ulv_cutter.tooltip.summary.1",
                "Wrought-iron blade (no precision work). Runs a hand-picked subset only — no wafers, no gem cutting.");

        // ---- 红石发电机 ----
        add("gregulvexpansion.machine.redstone_generator.tooltip.summary.0",
                "The first automatable fuel generator: feed it redstone dust (1,200 EU) or redstone blocks (10,800 EU) and it burns steadily at 8 EU/t · 1 A.");
        add("gregulvexpansion.machine.redstone_generator.tooltip.summary.1",
                "Stack units to scale up — industrial scale belongs to LV combustion generators.");

        // ---- 超低压卷板机 / 车床 ----
        add("gregulvexpansion.machine.ulv_bender.tooltip.summary.0",
                "Rolls one ingot into one plate at 8 EU/t — a 50% yield boost over the forge hammer's 3-ingots-for-2-plates.");
        add("gregulvexpansion.machine.ulv_bender.tooltip.summary.1",
                "Runs a hand-picked subset only (red alloy, copper, iron, tin, lead, zinc).");
        add("gregulvexpansion.machine.ulv_lathe.tooltip.summary.0",
                "Turns bolts into screws and stripped logs into long wood rods (plus a dust bonus) — the screw supply for your conveyors and pumps.");
        add("gregulvexpansion.machine.ulv_lathe.tooltip.summary.1",
                "Runs a hand-picked subset only. 240 EU buffer, like every other ULV machine.");

        // ---- 超低压化学反应釜 / 流体固化器 ----
        add("gregulvexpansion.machine.ulv_chemical_reactor.tooltip.summary.0",
                "Electric chemistry step two: the sulfur oxidation chain — sulfur + oxygen into sulfuric acid at 8 EU/t, four times faster than the lead chamber.");
        add("gregulvexpansion.machine.ulv_chemical_reactor.tooltip.summary.1",
                "Needs oxygen from your electrolyzer. 240 EU buffer.");
        add("gregulvexpansion.machine.ulv_fluid_solidifier.tooltip.summary.0",
                "Freezes fluids into shapes with reusable molds: water into snow, lava into obsidian.");
        add("gregulvexpansion.machine.ulv_fluid_solidifier.tooltip.summary.1",
                "Runs a hand-picked subset only. 240 EU buffer.");

        // ---- 超低压流体提取机 ----
        add("gregulvexpansion.machine.ulv_fluid_extractor.tooltip.summary.0",
                "Squeezes raw rubber from sticky resin and rubber tree parts at a mere 2 EU/t — the start of the rubber chain for your conveyors and pumps.");
        add("gregulvexpansion.machine.ulv_fluid_extractor.tooltip.summary.1",
                "Runs a hand-picked subset only. 240 EU buffer.");

        // ---- 超低压微型燃气轮机 ----
        add("gregulvexpansion.machine.ulv_gas_turbine.tooltip.summary.0",
                "The oil line's payoff: burn natural gas, sulfuric gas, methane or sulfuric naphtha for a steady 8 EU/t * 1 A — the distillation tower's byproducts finally pay rent.");
        add("gregulvexpansion.machine.ulv_gas_turbine.tooltip.summary.1",
                "Fuel heat values match the upstream gas turbine, just burned 4x slower. 4,000 mB tank; stacking units is the only way to scale.");

        // ---- 超低压极化机 ----
        add("gregulvexpansion.machine.ulv_polarizer.tooltip.summary.0",
                "Electromagnetism, 1825: iron rod in, magnetized iron rod out — far cheaper than the 4-redstone hand recipe, and the new ingredient inside every ULV motor.");
        add("gregulvexpansion.machine.ulv_polarizer.tooltip.summary.1",
                "1,280 EU per rod (8 EU/t x 160t). Hook it to your microgrid to automate; hand-cranking works too (~54 cranks).");
    }

    private static void add(String key, String value) {
        GULVRegistration.REGISTRATE.addRawLang(key, value);
    }
}
