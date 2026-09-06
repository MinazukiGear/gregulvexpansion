package com.hoshino.gregulvexpansion;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.hoshino.gregulvexpansion.registry.GULVRegistration;
import com.mojang.logging.LogUtils;
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
        modEventBus.addListener(this::commonSetup);
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
