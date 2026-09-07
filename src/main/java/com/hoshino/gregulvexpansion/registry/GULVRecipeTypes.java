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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

/**
 * 本模组配方类型 (总纲 §8)。
 *
 * <p>注册时机：由 RegisterEvent&lt;GTRecipeType&gt; 触发（CommonProxy 中
 * GTRecipeTypes.init 先于 GTMachines.init，机器定义可安全引用本类字段）。
 * 镜像上游 GTRecipeTypes.register 的三段注册：原版 RECIPE_TYPE / SERIALIZER
 * 注册表 + GTCEu 自己的配方类型注册表。
 */
public final class GULVRecipeTypes {
    /** 原型电解 (PRIMITIVE_ELECTROLYSIS)：子集准入制，首批仅水电解与 PbO₂ 阳极氧化。 */
    public static GTRecipeType PRIMITIVE_ELECTROLYSIS;
    /** 铅室法制酸 (LEAD_CHAMBER)：无电多方块，蒸汽驱动，单一一步法配方。 */
    public static GTRecipeType LEAD_CHAMBER_RECIPES;
    /** ULV 线材轧制：白名单金属锭 → 单线 ×2，时长为上游 ×2（轧机 1 进 1 出）。 */
    public static GTRecipeType ULV_WIRE_MILLING;
    /** ULV 切割：杆→螺栓、长杆→杆、板材块→板（晶圆/宝石等精密语义排除）。 */
    public static GTRecipeType ULV_CUTTING;
    /** 红石发电机燃料：红石粉/红石块 → EU（GENERATOR 组，EUt 为负）。 */
    public static GTRecipeType REDSTONE_GENERATOR_FUELS;
    /** ULV 卷板：锭 → 板 ×1（EUt 24 降档为 7，时长 ×2）。 */
    public static GTRecipeType ULV_BENDING;
    /** ULV 车削：螺栓→螺丝、剥皮原木→长木杆（EUt ≤7，时长 ×2）。 */
    public static GTRecipeType ULV_TURNING;

    private GULVRecipeTypes() {}

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        PRIMITIVE_ELECTROLYSIS = registerPrimitiveElectrolysis(event);
        LEAD_CHAMBER_RECIPES = registerLeadChamber(event);
        ULV_WIRE_MILLING = registerUlvWireMilling(event);
        ULV_CUTTING = registerUlvCutting(event);
        REDSTONE_GENERATOR_FUELS = registerRedstoneGeneratorFuels(event);
        ULV_BENDING = registerUlvBending(event);
        ULV_TURNING = registerUlvTurning(event);
    }

    private static GTRecipeType registerUlvBending(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_bending");
        // IO 布局：卷板 1 进 1 出（ulv-basic-machines.md v0.5）
        ULV_BENDING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 1, 0, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.BENDER_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_BENDING, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MOTOR)
                .setXEIVisible(true);

        return register(id, ULV_BENDING, event);
    }

    private static GTRecipeType registerUlvTurning(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_turning");
        // IO 布局：车削 1 进 2 出（长木杆 + 木尘）
        ULV_TURNING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 2, 0, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.CUTTER_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_SLICE, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MOTOR)
                .setXEIVisible(true);

        return register(id, ULV_TURNING, event);
    }

    private static GTRecipeType registerRedstoneGeneratorFuels(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("redstone_generator");
        // IO 布局 (redstone-generator.md)：固体燃料 1 进；GENERATOR 组，EUt 为负（发电）
        REDSTONE_GENERATOR_FUELS = new GTRecipeType(id, GTRecipeTypes.GENERATOR)
                .setMaxIOSize(1, 0, 0, 0)
                .setEUIO(IO.OUT)
                .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.COMBUSTION)
                .setXEIVisible(true);

        return register(id, REDSTONE_GENERATOR_FUELS, event);
    }

    private static GTRecipeType registerPrimitiveElectrolysis(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
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

        return register(id, PRIMITIVE_ELECTROLYSIS, event);
    }

    private static GTRecipeType registerLeadChamber(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("lead_chamber");
        // IO 布局 (lead-chamber-acid-plant.md 配方草案)：物品 1 进(硫粉)、流体 2 进
        // (水 + 蒸汽)、流体 1 出(硫酸)。无电：省略 EUt（焦炉同款语义）。
        LEAD_CHAMBER_RECIPES = new GTRecipeType(id, GTRecipeTypes.MULTIBLOCK)
                .setMaxIOSize(1, 0, 2, 1)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.FIRE)
                .setXEIVisible(true);

        return register(id, LEAD_CHAMBER_RECIPES, event);
    }

    private static GTRecipeType registerUlvWireMilling(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_wire_milling");
        // IO 布局 (ulv-basic-machines.md 数值草案)：轧机 1 进 1 出
        ULV_WIRE_MILLING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 1, 0, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.WIREMILL_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MOTOR)
                .setXEIVisible(true);

        return register(id, ULV_WIRE_MILLING, event);
    }

    private static GTRecipeType registerUlvCutting(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_cutting");
        // IO 布局 (ulv-basic-machines.md 数值草案)：切割 1 进 2 出
        ULV_CUTTING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 2, 0, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.SAWBLADE_OVERLAY)
                .setSlotOverlay(true, false, false, GuiTextures.CUTTER_OVERLAY)
                .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_SLICE, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.CUT)
                .setXEIVisible(true);

        return register(id, ULV_CUTTING, event);
    }

    private static GTRecipeType register(ResourceLocation id, GTRecipeType recipeType,
                                         GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE, recipeType.registryName, recipeType);
        GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, recipeType.registryName,
                new GTRecipeSerializer());
        event.register(id, recipeType);
        return recipeType;
    }
}
