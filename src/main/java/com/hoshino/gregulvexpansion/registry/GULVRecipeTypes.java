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
    /** ULV 化学反应：硫氧化/三氧化硫/成酸（酸链子集，EUt 7 原样直录）。 */
    public static GTRecipeType ULV_CHEMICAL_REACTING;
    /** ULV 流体固化：雪球/雪块/黑曜石（模具不消耗）。 */
    public static GTRecipeType ULV_FLUID_SOLIDFICATION;
    /** ULV 流体提取：橡胶链（粘性树脂/橡胶树部件 → 生橡胶粉，EUt 2 原样直录）。 */
    public static GTRecipeType ULV_EXTRACTING;

    /** 原始蒸馏：原油分馏（无电多方块，蒸汽驱动，产出比例与上游一致）。 */
    public static GTRecipeType PRIMITIVE_DISTILLATION;
    /** 原始裂化：石脑油/轻燃料 + 蒸汽 → 乙烯（无电多方块，产量 1/6）。 */
    public static GTRecipeType PRIMITIVE_CRACKING;
    /** ULV 卷板：锭 → 板 ×1（EUt 24 降档为 7，时长 ×2）。 */
    public static GTRecipeType ULV_BENDING;
    /** ULV 车削：螺栓→螺丝、剥皮原木→长木杆（EUt ≤7，时长 ×2）。 */
    public static GTRecipeType ULV_TURNING;
    /** ULV 极化：铁杆 → 磁化铁杆（白名单单项，时长 ×2 总能耗不变）。 */
    public static GTRecipeType ULV_POLARIZING;
    /** ULV 微型燃气轮机燃料：天然气/含硫气体/甲烷/含硫石脑油 → EU（GENERATOR 组，EUt 为负）。 */
    public static GTRecipeType ULV_GAS_TURBINE_FUELS;

    private GULVRecipeTypes() {}

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        PRIMITIVE_ELECTROLYSIS = registerPrimitiveElectrolysis(event);
        LEAD_CHAMBER_RECIPES = registerLeadChamber(event);
        ULV_WIRE_MILLING = registerUlvWireMilling(event);
        ULV_CUTTING = registerUlvCutting(event);
        REDSTONE_GENERATOR_FUELS = registerRedstoneGeneratorFuels(event);
        ULV_BENDING = registerUlvBending(event);
        ULV_TURNING = registerUlvTurning(event);
        ULV_CHEMICAL_REACTING = registerUlvChemicalReacting(event);
        ULV_FLUID_SOLIDFICATION = registerUlvFluidSolidification(event);
        ULV_EXTRACTING = registerUlvExtracting(event);
        PRIMITIVE_DISTILLATION = registerPrimitiveDistillation(event);
        PRIMITIVE_CRACKING = registerPrimitiveCracking(event);
        ULV_POLARIZING = registerUlvPolarizing(event);
        ULV_GAS_TURBINE_FUELS = registerUlvGasTurbineFuels(event);
    }

    private static GTRecipeType registerUlvPolarizing(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_polarizing");
        // IO 布局 (ulv-polarizer.md)：镜像上游极化机 1 进 1 出
        ULV_POLARIZING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 1, 0, 0)
                .setEUIO(IO.IN)
                .setProgressBar(GuiTextures.PROGRESS_BAR_MAGNET, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.ARC)
                .setXEIVisible(true);

        return register(id, ULV_POLARIZING, event);
    }

    private static GTRecipeType registerUlvGasTurbineFuels(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_gas_turbine");
        // IO 布局 (gas-turbine.md)：流体燃料 1 进；GENERATOR 组，EUt 为负（发电）
        ULV_GAS_TURBINE_FUELS = new GTRecipeType(id, GTRecipeTypes.GENERATOR)
                .setMaxIOSize(0, 0, 1, 0)
                .setEUIO(IO.OUT)
                .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.COMBUSTION)
                .setXEIVisible(true);

        return register(id, ULV_GAS_TURBINE_FUELS, event);
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

    private static GTRecipeType registerUlvChemicalReacting(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_chemical_reacting");
        // IO 布局：镜像上游化学反应机 2/2/3/2，为石油线脱硫/聚合子集预留槽位
        // (ulv-basic-machines.md v0.8 子集表 + primitive-distillation-tower.md §6)
        ULV_CHEMICAL_REACTING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(2, 2, 3, 2)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, false, GuiTextures.MOLECULAR_OVERLAY_1)
                .setSlotOverlay(false, false, true, GuiTextures.MOLECULAR_OVERLAY_2)
                .setSlotOverlay(false, true, false, GuiTextures.MOLECULAR_OVERLAY_3)
                .setSlotOverlay(false, true, true, GuiTextures.MOLECULAR_OVERLAY_4)
                .setSlotOverlay(true, false, GuiTextures.VIAL_OVERLAY_1)
                .setSlotOverlay(true, true, GuiTextures.VIAL_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.CHEMICAL)
                .setXEIVisible(true);

        return register(id, ULV_CHEMICAL_REACTING, event);
    }

    private static GTRecipeType registerUlvFluidSolidification(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_fluid_solidification");
        // IO 布局 (ulv-basic-machines.md v0.8 子集表)：固化 1 流体进 + 模具（notConsumable）
        // → 1 物品出，镜像上游 FLUID_SOLIDFICATION_RECIPES
        ULV_FLUID_SOLIDFICATION = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 1, 1, 0)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.COOLING)
                .setXEIVisible(true);

        return register(id, ULV_FLUID_SOLIDFICATION, event);
    }

    private static GTRecipeType registerUlvExtracting(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("ulv_extracting");
        // IO 布局 (ulv-basic-machines.md v0.9 子集表)：镜像上游提取机 1/1/0/1，
        // 流体输出槽为油砂榨取（原油 250 mB，primitive-distillation-tower.md §2）预留
        ULV_EXTRACTING = new GTRecipeType(id, GTRecipeTypes.ELECTRIC)
                .setMaxIOSize(1, 1, 0, 1)
                .setEUIO(IO.IN)
                .setSlotOverlay(false, false, GuiTextures.EXTRACTOR_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MOTOR)
                .setXEIVisible(true);

        return register(id, ULV_EXTRACTING, event);
    }

    private static GTRecipeType registerPrimitiveDistillation(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("primitive_distillation");
        // IO 布局 (primitive-distillation-tower.md §3)：流体 2 进（原油 + 蒸汽）、
        // 4 出（硫化重/轻燃料、硫化石脑油、硫化气体）；无电：省略 EUt（铅室同款语义）
        PRIMITIVE_DISTILLATION = new GTRecipeType(id, GTRecipeTypes.MULTIBLOCK)
                .setMaxIOSize(0, 0, 2, 4)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.CHEMICAL)
                .setXEIVisible(true);

        return register(id, PRIMITIVE_DISTILLATION, event);
    }

    private static GTRecipeType registerPrimitiveCracking(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        ResourceLocation id = GregULVExpansion.id("primitive_cracking");
        // IO 布局 (primitive-distillation-tower.md §5)：镜像上游裂化机 1/0/2/2，
        // 物品输出 +1（碳粉副产）；无电：省略 EUt
        PRIMITIVE_CRACKING = new GTRecipeType(id, GTRecipeTypes.MULTIBLOCK)
                .setMaxIOSize(1, 1, 2, 2)
                .setSlotOverlay(false, true, GuiTextures.CRACKING_OVERLAY_1)
                .setSlotOverlay(true, true, GuiTextures.CRACKING_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, LEFT_TO_RIGHT)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.FIRE)
                .setXEIVisible(true);

        return register(id, PRIMITIVE_CRACKING, event);
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
