package com.hoshino.gregulvexpansion.data;

import com.hoshino.gregulvexpansion.registry.GULVRegistration;

/**
 * en_us 语言条目 (addRawLang)；zh_cn 为手维护静态文件
 * {@code assets/gregulvexpansion/lang/zh_cn.json}，两处键面必须同步。
 */
public final class GULVLang {
    private GULVLang() {}

    public static void init() {
        // ---- EMI / JEI recipe category names ----
        // GTRecipeCategory derives these keys directly from the recipe type ID.
        add("gregulvexpansion.primitive_electrolysis", "Electrolyzer");
        add("gregulvexpansion.lead_chamber", "Lead Chamber");
        add("gregulvexpansion.ulv_wire_milling", "Wiremill");
        add("gregulvexpansion.ulv_cutting", "Cutter");
        add("gregulvexpansion.redstone_generator", "Redstone Generator");
        add("gregulvexpansion.ulv_bending", "Bender");
        add("gregulvexpansion.ulv_turning", "Lathe");
        add("gregulvexpansion.ulv_chemical_reacting", "Chemical Reactor");
        add("gregulvexpansion.ulv_fluid_solidification", "Fluid Solidifier");
        add("gregulvexpansion.ulv_extracting", "Extractor");
        add("gregulvexpansion.primitive_distillation", "Primitive Distillation Tower");
        add("gregulvexpansion.primitive_cracking", "Primitive Cracker");
        add("gregulvexpansion.ulv_polarizing", "Polarizer");
        add("gregulvexpansion.ulv_gas_turbine", "Gas Turbine");

        // ---- 铅衬机壳 / 铅室法制酸装置 ----
        // (铅衬机壳名称由 GULVBlocks 的 .lang() 生成，勿重复添加)
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.0",
                "A no-electricity multiblock that makes sulfuric acid with steam — 1850s industry at its finest.");
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.1",
                "Uses the upstream Large Chemical Reactor's 3 x 3 x 3 layout with lead-lined casing, a bronze pipe casing and one steel firebox; fit the required input and output hatches into casing positions.");
        add("gregulvexpansion.machine.lead_chamber.tooltip.summary.2",
                "Far slower than LV chemical reactors — an off-grid backup, not a competitor.");

        // ---- Primitive oil-processing multiblocks ----
        add("gregulvexpansion.machine.primitive_distillation_tower.tooltip.summary.0",
                "A steam-driven, no-electricity tower that separates crude oil into four sulfuric fractions.");
        add("gregulvexpansion.machine.primitive_distillation_tower.tooltip.summary.1",
                "Uses the upstream Distillation Tower's repeatable 3 x 3 layout, capped at 6 layers. Supply oil and steam at the base and install no more than one fluid output hatch on each upper layer.");
        add("gregulvexpansion.machine.primitive_cracker.tooltip.summary.0",
                "A steam-driven, no-electricity cracker that turns desulfurized naphtha or light fuel into ethylene.");
        add("gregulvexpansion.machine.primitive_cracker.tooltip.summary.1",
                "Uses the upstream Cracker's 5 x 3 x 3 layout, replacing its coils with steel fireboxes. Needs fluid input and output hatches plus an item output hatch for the carbon byproduct.");

        // ---- 超低压流体钻井机 ----
        add("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.0",
                "Pumps crude oil, light oil, heavy oil or natural gas from bedrock veins at half the LV rig's yield and twice its energy cost per unit. Veins gradually deplete.");
        add("gregulvexpansion.machine.ulv_fluid_drilling_rig.tooltip.summary.1",
                "Runs on ULV power (8 EU/t), uses a bronze drill frame and provides your first source of oil on the way into the electric age.");

    }

    private static void add(String key, String value) {
        GULVRegistration.REGISTRATE.addRawLang(key, value);
    }
}
