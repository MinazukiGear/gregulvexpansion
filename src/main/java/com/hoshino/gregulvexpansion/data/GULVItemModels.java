package com.hoshino.gregulvexpansion.data;

import com.hoshino.gregulvexpansion.GregULVExpansion;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;

/**
 * 物品模型：全部为平面贴图 (item/generated)，贴图位于
 * {@code assets/gregulvexpansion/textures/item/}。
 * 机器方块/物品模型由 MachineBuilder 的 tiered hull 模型自动生成。
 */
public final class GULVItemModels {
    private GULVItemModels() {}

    public static void init(RegistrateItemModelProvider provider) {
        generated(provider, "cats_whisker_detector");
        generated(provider, "wood_crank");
        generated(provider, "ulv_electric_motor");
        generated(provider, "ulv_conveyor_module");
        generated(provider, "ulv_electric_pump");
    }

    private static void generated(RegistrateItemModelProvider provider, String name) {
        provider.withExistingParent(name, "minecraft:item/generated")
                .texture("layer0", GregULVExpansion.id("item/" + name));
    }
}
