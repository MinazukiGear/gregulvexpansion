package com.hoshino.gregulvexpansion;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.hoshino.gregulvexpansion.data.GULVLang;
import com.hoshino.gregulvexpansion.registry.GULVCovers;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVRegistration;

@GTAddon
public final class GregULVExpansionAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return GULVRegistration.REGISTRATE;
    }

    @Override
    public void registerCovers() {
        // GTCEu 在 GTCovers.init() 内调用，早于物品注册与 initializeAddon：
        // cover 定义必须先于引用它们的 cover 物品存在
        GULVCovers.init();
    }

    @Override
    public void initializeAddon() {
        // 时序 (CommonProxy)：registerCovers → GTMachines.init → GTItems.init → 本方法。
        // 静态注册（物品构建队列）在此触发；机器定义由 RegisterEvent 监听器触发。
        GULVItems.init();
        GULVLang.init();
    }

    @Override
    public String addonModId() {
        return GregULVExpansion.MOD_ID;
    }
}
