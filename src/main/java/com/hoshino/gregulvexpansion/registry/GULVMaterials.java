package com.hoshino.gregulvexpansion.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.common.unification.material.MaterialRegistryManager;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.hoshino.gregulvexpansion.GregULVExpansion;

/**
 * 本模组新材料注册 (总纲 §7)。
 *
 * <p>时序 (CommonProxy#initMaterials)：{@link MaterialRegistryEvent}（PRE 相）
 * 创建本模组材料注册表 → GTCEu 自身材料 → {@link MaterialEvent} 注册具体材料
 * → 冻结后由上游 generateMaterialItems 自动生成粉未等形态物品
 * （走本模组 Registrate，物品 ID {@code gregulvexpansion:lead_dioxide_dust}）。
 * 事件监听在 {@code GregULVExpansion} 构造器挂接。
 */
public final class GULVMaterials {
    /** 二氧化铅 (PbO₂) — 铅酸电池线核心新材料 (lead-acid-battery-line.md / B1 裁决)。 */
    public static Material LEAD_DIOXIDE;

    private GULVMaterials() {}

    /** MaterialRegistryEvent（PRE 相）回调：创建本模组材料注册表。 */
    public static void createRegistry(MaterialRegistryEvent event) {
        MaterialRegistryManager.getInstance().createRegistry(GregULVExpansion.MOD_ID);
    }

    /** MaterialEvent 回调：注册具体材料。每条材料的准入依据须先写入设计文档。 */
    public static void init(MaterialEvent event) {
        // B1 裁决：PbO₂ 由原型电解槽阳极氧化 PbO（Massicot）制取，铅酸电池链前置；
        // 真实二氧化铅为深棕黑色粉末，与上游 Massicot（PbO，黄色 0xFFDD55）形成颜色叙事对比。
        // 注意 ID 必须用本模组命名空间：registerMaterial() 按 namespace 路由注册表。
        LEAD_DIOXIDE = new Material.Builder(GregULVExpansion.id("lead_dioxide"))
                .dust(1)
                .color(0x3E3430).secondaryColor(0x1C1712)
                .components(GTMaterials.Lead, 1, GTMaterials.Oxygen, 2)
                .buildAndRegister();
    }
}
