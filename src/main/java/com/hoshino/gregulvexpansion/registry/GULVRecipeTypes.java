package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.hoshino.gregulvexpansion.GregULVExpansion;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;


/**
 * 本模组配方类型 (primitive-electrolyzer.md)。
 *
 * <p>独立配方类型而非复用上游电解表：上游表按 LV+ 标定，ULV 机器挂表会得到
 * 空白 JEI 页；「哪些反应允许在 ULV 做」必须是一份可审阅的白名单清单（设计
 * 文档表格），代码只注册表中条目。
 *
 * <p>注册时机：由 RegisterEvent&lt;GTRecipeType&gt; 触发（CommonProxy 中
 * GTRecipeTypes.init 先于 GTMachines.init，机器定义可安全引用本类字段）。
 * 镜像上游 GTRecipeTypes.register 的三段注册：原版 RECIPE_TYPE / SERIALIZER
 * 注册表 + GTCEu 自己的配方类型注册表。
 */
public final class GULVRecipeTypes {
    /** 原型电解 (PRIMITIVE_ELECTROLYSIS)：子集准入制，首批仅水电解与 PbO₂ 阳极氧化。 */
    public static GTRecipeType PRIMITIVE_ELECTROLYSIS;

    private GULVRecipeTypes() {}

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("primitive_electrolysis");
        // IO 布局 (primitive-electrolyzer.md 注册与命名)：物品 1 进 2 出，流体 1 进 2 出
        PRIMITIVE_ELECTROLYSIS = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 2, 1, 2)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, false, GuiTextures.LIGHTNING_OVERLAY_1)
                .setSlotOverlay(false, false, true, GuiTextures.CANISTER_OVERLAY)
                .setSlotOverlay(false, true, true, GuiTextures.LIGHTNING_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.ELECTROLYZER)
                .setXEIVisible(true);

        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE, PRIMITIVE_ELECTROLYSIS.registryName, PRIMITIVE_ELECTROLYSIS);
        GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, PRIMITIVE_ELECTROLYSIS.registryName,
                new GTRecipeSerializer());
        event.register(id, PRIMITIVE_ELECTROLYSIS);
    }
}
