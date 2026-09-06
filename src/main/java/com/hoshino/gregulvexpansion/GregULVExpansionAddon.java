package com.hoshino.gregulvexpansion;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.hoshino.gregulvexpansion.registry.GULVRegistration;

@GTAddon
public final class GregULVExpansionAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return GULVRegistration.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        // 首批内容定案后，在此触发语言条目与静态注册初始化。
    }

    @Override
    public String addonModId() {
        return GregULVExpansion.MOD_ID;
    }
}
