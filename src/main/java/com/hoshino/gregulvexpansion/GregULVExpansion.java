package com.hoshino.gregulvexpansion;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.hoshino.gregulvexpansion.data.GULVItemModels;
import com.hoshino.gregulvexpansion.data.GULVRecipes;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;
import com.hoshino.gregulvexpansion.registry.GULVRegistration;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GregULVExpansion.MOD_ID)
public final class GregULVExpansion {
    public static final String MOD_ID = "gregulvexpansion";
    public static final String MOD_NAME = "Greg ULV Expansion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GregULVExpansion(final FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        GULVRegistration.REGISTRATE.registerEventListeners(modEventBus);
        GULVRegistration.REGISTRATE.creativeModeTab(GTCreativeModeTabs.MACHINE);
        GULVRegistration.REGISTRATE.addDataGenerator(ProviderType.RECIPE, GULVRecipes::init);
        GULVRegistration.REGISTRATE.addDataGenerator(ProviderType.ITEM_MODEL, GULVItemModels::init);
        // GTCEu 注册阶段钩子（时序见 CommonProxy：covers → 配方类型 → 机器 → 物品 → 材料）
        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addListener(GULVMaterials::createRegistry);
        modEventBus.addListener(GULVMaterials::init);
        modEventBus.addListener(this::commonSetup);
    }

    private void registerRecipeTypes(final GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GULVRecipeTypes.init(event);
    }

    private void registerMachines(final GTCEuAPI.RegisterEvent<?, MachineDefinition> event) {
        GULVMachines.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info(
                "{} initialized with GTCEu {}.",
                MOD_NAME,
                loadedVersion("gtceu")
        );
    }

    private static String loadedVersion(final String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("not loaded on this side");
    }
}
