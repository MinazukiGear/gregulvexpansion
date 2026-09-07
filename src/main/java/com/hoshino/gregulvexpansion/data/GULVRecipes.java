package com.hoshino.gregulvexpansion.data;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVBlocks;
import com.hoshino.gregulvexpansion.registry.GULVRecipeTypes;
import com.hoshino.gregulvexpansion.registry.GULVMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

import com.gregtechceu.gtceu.api.GTValues;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

/**
 * 首批工作台配方 (P0)。
 *
 * <p>零电力门槛自检 (各设计文档「合成草案」)：红合金线 = 铜+红石蒸汽合金炉 +
 * 剪线钳手工裁切；纯净方铅矿矿石 = 方铅矿经蒸汽磨机 + 蒸汽洗矿获得
 * （GTCEu 蒸汽磨机 / 姊妹项目 GSE 大型蒸汽洗矿机，实现期修订 2026-09-07
 * 所有者指定输入），橡胶/螺丝/铁杆均为蒸汽时代已有形态 —— 整条链无电力机器。
 */
public final class GULVRecipes {
    private GULVRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        addDetectorRecipe(provider);
        addWoodCrankRecipe(provider);
        addHandCrankDynamoRecipe(provider);
        addMotorRecipe(provider);
        addConveyorModuleRecipe(provider);
        addPumpRecipe(provider);
        addPrimitiveElectrolyzerRecipe(provider);
        addLeadAcidCellRecipe(provider);
        addBatteryPackRecipe(provider);
        addBatteryWallRecipe(provider);
        addLeadLinedCasingRecipe(provider);
        addThermoelectricGeneratorRecipe(provider);
        addWireMillRecipe(provider);
        addCutterRecipe(provider);
        addRedstoneGeneratorRecipe(provider);
        addBenderRecipe(provider);
        addLatheRecipe(provider);
    }

    /** 猫须探测器：纯净方铅矿矿石 + 红合金单线 → ×2 (D13 产出翻倍)。 */
    private static void addDetectorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("cats_whisker_detector"),
                GULVItems.CATS_WHISKER_DETECTOR.asStack(2),
                "W",
                "O",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'O', ChemicalHelper.get(TagPrefix.crushedPurified, GTMaterials.Galena));
    }

    /** 木质曲柄：木板 ×2 + 木棍 ×1 (D2 独立物品)。 */
    private static void addWoodCrankRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("wood_crank"),
                GULVItems.WOOD_CRANK.asStack(),
                "PP",
                " S",
                'P', ItemTags.PLANKS,
                'S', Items.STICK);
    }

    /**
     * 手摇发电机：木质曲柄 + 猫须探测器 + 红合金单线 ×2 + 铁板 ×4 + ULV 机械方块。
     * 曲柄为合成材料 → 合成产物默认已安装 (hand-crank-dynamo.md)。
     */
    private static void addHandCrankDynamoRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("hand_crank_dynamo"),
                com.hoshino.gregulvexpansion.registry.GULVMachines.HAND_CRANK_DYNAMO.asStack(),
                "WCW",
                "IDI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', GULVItems.WOOD_CRANK,
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压电动马达：红合金单线 ×4 + 铁杆 ×2 + 铁板 ×2 + 猫须探测器。 */
    private static void addMotorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_motor"),
                GULVItems.ULV_ELECTRIC_MOTOR.asStack(),
                "WRW",
                "PDP",
                "WRW",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Iron),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'D', GULVItems.CATS_WHISKER_DETECTOR);
    }

    /** 超低压传送带模块：马达 + 橡胶板 ×2 + 铁螺丝 ×2。 */
    private static void addConveyorModuleRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_conveyor_module"),
                GULVItems.ULV_CONVEYOR_MODULE.asStack(),
                "SMS",
                "RR ",
                'S', ChemicalHelper.get(TagPrefix.screw, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'R', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Rubber));
    }

    /** 超低压电动泵：马达 + 铁板 ×2 + 玻璃 ×2。 */
    private static void addPumpRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_electric_pump"),
                GULVItems.ULV_ELECTRIC_PUMP.asStack(),
                "G G",
                "PMP",
                'G', Tags.Items.GLASS,
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR);
    }

    /**
     * 原型电解槽工作台配方 (primitive-electrolyzer.md 合成草案，v0.3.1 收纳为 3×3)：
     * 猫须探测器 ×2（整流桥语义） + 红合金单线 ×2 + 铅板 ×2 + 玻璃 ×2 + ULV 机械方块 ×1。
     */
    private static void addPrimitiveElectrolyzerRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("primitive_electrolyzer"),
                GULVMachines.PRIMITIVE_ELECTROLYZER.asStack(),
                "WDW",
                "GHG",
                "PDP",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'D', GULVItems.CATS_WHISKER_DETECTOR,
                'G', Tags.Items.GLASS,
                'H', GTMachines.HULL[0].asStack(),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead));
    }

    /**
     * 原型电解配方子集 (primitive-electrolyzer.md 配方子集表，D11/B1 已裁决)。
     * 准入规则：每条必须先在文档表格中有名分，代码只注册表内条目。
     *
     * <p><b>注意：仅供运行时 {@code IGTAddon#addRecipes} 调用</b>——GT 配方图
     * 配方在上游 7.5.3 已全部改为运行时动态包注册（jar 内零配方 JSON），
     * datagen 路径的 toJson 会因无 RegistryAccess 而 NPE。
     */
    public static void addElectrolysisRecipes(Consumer<FinishedRecipe> provider) {
        // 水电解：伏打电堆 (1800) 语义本体；128 t ≈ 上游 LV 同配方降档（慢一倍省一半电）
        GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS.recipeBuilder(GregULVExpansion.id("water_electrolysis"))
                .inputFluids(GTMaterials.Water.getFluid(100))
                .outputFluids(GTMaterials.Hydrogen.getFluid(100), GTMaterials.Oxygen.getFluid(50))
                .EUt(8)
                .duration(128)
                .save(provider);

        // PbO₂ 阳极氧化：B1 裁决，铅酸电池链前置；上游无对标条目（第二轮调研 B2）
        GULVRecipeTypes.PRIMITIVE_ELECTROLYSIS.recipeBuilder(GregULVExpansion.id("lead_dioxide_oxidation"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Massicot, 1))
                .inputFluids(GTMaterials.Water.getFluid(100))
                .outputItems(ChemicalHelper.get(TagPrefix.dust, GULVMaterials.LEAD_DIOXIDE, 1))
                .EUt(8)
                .duration(256)
                .save(provider);
    }


    /**
     * ULV 轧机/切割机配方子集 (ulv-basic-machines.md 子集表 + upstream-recipe-research.md §4)。
     * 材料清单即定案对象（红合金/铜/铁/锡/铅/锌），扩表必须先改设计文档。
     * 换算规则：EUt ≤ 8 时用上游原值、时长 ×2；板材块→板 EUt 30 → 7、时长 ×2。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvMachineRecipes(Consumer<FinishedRecipe> provider) {
        Material[] subsetMaterials = {
                GTMaterials.RedAlloy, GTMaterials.Copper, GTMaterials.Iron,
                GTMaterials.Tin, GTMaterials.Lead, GTMaterials.Zinc
        };

        // ---- ULV 线材轧机：锭 ×1 → 单线 ×2（上游 WireRecipeHandler，省略编程电路：
        // ---- 本机单一职能无档位歧义，降低 ULV 门槛；上游同配方 EUt 7，时长 ×2）
        for (Material material : subsetMaterials) {
            if (!material.hasProperty(PropertyKey.WIRE) ||
                    !material.shouldGenerateRecipesFor(TagPrefix.wireGtSingle)) {
                continue;
            }
            TagPrefix prefix = material.hasProperty(PropertyKey.INGOT) ? TagPrefix.ingot :
                    material.hasProperty(PropertyKey.GEM) ? TagPrefix.gem : TagPrefix.dust;
            GULVRecipeTypes.ULV_WIRE_MILLING
                    .recipeBuilder(GregULVExpansion.id("wire_" + material.getName()))
                    .inputItems(prefix, material)
                    .outputItems(TagPrefix.wireGtSingle, material, 2)
                    .duration((int) material.getMass())
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 切割机 ----
        for (Material material : subsetMaterials) {
            // 杆 ×1 → 螺栓 ×4（上游 EUt 4 / mass×2；子集 EUt 不变、时长 ×2）
            if (material.hasFlag(MaterialFlags.GENERATE_BOLT_SCREW) &&
                    material.shouldGenerateRecipesFor(TagPrefix.bolt) &&
                    material.hasProperty(PropertyKey.DUST)) {
                ItemStack boltStack = ChemicalHelper.get(TagPrefix.bolt, material);
                if (!boltStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_rod_to_bolt"))
                            .inputItems(TagPrefix.rod, material)
                            .outputItems(boltStack.copyWithCount(4))
                            .duration((int) Math.max(material.getMass() * 2L, 1L))
                            .EUt(4)
                            .save(provider);
                }
            }
            // 长杆 ×1 → 杆 ×2（上游 EUt 4 / mass；子集 EUt 不变、时长 ×2）
            if (material.hasFlag(MaterialFlags.GENERATE_ROD) &&
                    material.shouldGenerateRecipesFor(TagPrefix.rodLong)) {
                ItemStack rodStack = ChemicalHelper.get(TagPrefix.rod, material);
                if (!rodStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_long_rod_to_rod"))
                            .inputItems(TagPrefix.rodLong, material)
                            .outputItems(rodStack.copyWithCount(2))
                            .duration((int) Math.max(material.getMass(), 1L))
                            .EUt(4)
                            .save(provider);
                }
            }
            // 板材块 ×1 → 板 ×(材料量/M)（上游 EUt 30 = VA[LV]；降档为 7、时长 ×2 = mass×16）
            if (material.hasFlag(MaterialFlags.GENERATE_PLATE)) {
                ItemStack plateStack = ChemicalHelper.get(TagPrefix.plate, material);
                if (!plateStack.isEmpty()) {
                    GULVRecipeTypes.ULV_CUTTING
                            .recipeBuilder(GregULVExpansion.id("cut_" + material.getName() + "_block_to_plate"))
                            .inputItems(TagPrefix.block, material)
                            .outputItems(plateStack.copyWithCount((int) (TagPrefix.block.getMaterialAmount(material) / GTValues.M)))
                            // 耗能不变：上游 30 × mass×8 = 240×mass → 7 × (240×mass÷7)
                            .duration((int) ((material.getMass() * 240L + 6) / 7))
                            .EUt(7)
                            .save(provider);
                }
            }
        }
    }

    /**
     * 铅室法制酸 (lead-chamber-acid-plant.md 配方草案，v0.3.1 时长拉长定案)：
     * 硫粉 ×2 + 水 500 mB + 蒸汽 6,400 mB（= 8 mB/t × 800 t，运行期一次性扣除）
     * → 硫酸 500 mB；无电（省略 EUt，焦炉同款语义）。等效 1,600 t / 1,000 mB，
     * 产能替代性已锁死。同样仅供运行时 addRecipes 调用。
     */
    public static void addLeadChamberRecipe(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.LEAD_CHAMBER_RECIPES.recipeBuilder(GregULVExpansion.id("sulfuric_acid"))
                .inputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur, 2))
                .inputFluids(GTMaterials.Water.getFluid(500), GTMaterials.Steam.getFluid(6_400))
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(500))
                .duration(800)
                .save(provider);
    }

    /**
     * 铅酸单格电池 (lead-acid-battery-line.md 化学链)：PbO₂ 板 + 铅板 + 硫酸 100 mB +
     * 木箱。硫酸走 {@link FluidContainerIngredient}：匹配任意盛有 ≥100 mB 硫酸的
     * 流体容器（GT 单元等），合成时精确抽出 100 mB 并返还余量容器。
     */
    private static void addLeadAcidCellRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedFluidContainerRecipe(provider, false,
                GregULVExpansion.id("lead_acid_cell"),
                GULVItems.LEAD_ACID_CELL.asStack(),
                "DP",
                "BC",
                'D', ChemicalHelper.get(TagPrefix.plate, GULVMaterials.LEAD_DIOXIDE),
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'B', new FluidContainerIngredient(GTMaterials.SulfuricAcid.getFluid(100)),
                'C', net.minecraftforge.common.Tags.Items.CHESTS_WOODEN);
    }

    /** 铅酸电池组：单格 ×4 + 铅板外壳 + 红合金单线；容量严格等于组成之和。 */
    private static void addBatteryPackRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_acid_battery_pack"),
                GULVItems.LEAD_ACID_BATTERY_PACK.asStack(),
                " C ",
                "CPC",
                " W ",
                'C', GULVItems.LEAD_ACID_CELL,
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy));
    }

    /** 铅酸蓄电墙：电池组 + 铅板 ×4 + ULV 机械方块 + 红合金单线 ×2。 */
    private static void addBatteryWallRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_acid_battery_wall"),
                GULVMachines.LEAD_ACID_BATTERY_WALL.asStack(),
                "PWP",
                "PHP",
                "WCW",
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'H', GTMachines.HULL[0].asStack(),
                'C', GULVItems.LEAD_ACID_BATTERY_PACK);
    }

    /** 铅衬机壳：铅板 ×6 + 石材基座，每次产出 2 个（铅室法 3×4×3 结构约需 22 块）。 */
    private static void addLeadLinedCasingRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("lead_lined_casing"),
                GULVBlocks.LEAD_LINED_CASING.asStack(2),
                "PPP",
                " S ",
                "PPP",
                'P', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Lead),
                'S', Tags.Items.STONE);
    }

    /** 温差发电机：马达 + 红合金单线 ×2 + 铁板 ×3 + 铜板 ×2 + ULV 机械方块（3×3 收纳，铁板 4→3）。 */
    private static void addThermoelectricGeneratorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("thermoelectric_generator"),
                GULVMachines.THERMOELECTRIC_GENERATOR.asStack(),
                "WCW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'C', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Copper),
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压线材轧机：马达 + 红合金单线 ×2 + 铁板 ×3 + 钢杆 ×2 + ULV 机械方块。 */
    private static void addWireMillRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_wire_mill"),
                GULVMachines.ULV_WIRE_MILL.asStack(),
                "WRW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Steel),
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压切割机：马达 + 红合金单线 ×2 + 铁板 ×4 + 锻铁锯片 + ULV 机械方块（D12）。 */
    private static void addCutterRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_cutter"),
                GULVMachines.ULV_CUTTER.asStack(),
                "ISI",
                "IMI",
                "WHW",
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'S', ChemicalHelper.get(TagPrefix.plate, GTMaterials.WroughtIron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'H', GTMachines.HULL[0].asStack());
    }

    /** 红石发电机：马达 + 红合金单线 ×2 + 铁板 ×4 + 活塞 ×1 + ULV 机械方块。 */
    private static void addRedstoneGeneratorRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("redstone_generator"),
                GULVMachines.REDSTONE_GENERATOR.asStack(),
                "WPW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'P', Items.PISTON,
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }


    /**
     * ULV 卷板机/车床配方子集 (ulv-basic-machines.md v0.5 子集表)。
     * 卷板：锭→板 1:1（上游 EUt 24 → 7，时长 mass → mass×2，省略电路）；
     * 车床：螺栓→螺丝（上游 EUt 4 / mass÷8 → 时长 ×2）与
     * 剥皮原木 → 长木杆 ×4 + 木尘（上游 EUt 7 / 160t → 时长 ×2）。
     * 材料清单同轧机/切割机六种；扩表必须先改设计文档。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addUlvBenderLatheRecipes(Consumer<FinishedRecipe> provider) {
        Material[] subsetMaterials = {
                GTMaterials.RedAlloy, GTMaterials.Copper, GTMaterials.Iron,
                GTMaterials.Tin, GTMaterials.Lead, GTMaterials.Zinc
        };

        // ---- ULV 卷板机：锭 ×1 → 板 ×1 ----
        for (Material material : subsetMaterials) {
            if (!material.hasFlag(MaterialFlags.GENERATE_PLATE)) {
                continue;
            }
            ItemStack plateStack = ChemicalHelper.get(TagPrefix.plate, material);
            if (plateStack.isEmpty()) {
                continue;
            }
            GULVRecipeTypes.ULV_BENDING
                    .recipeBuilder(GregULVExpansion.id("bend_" + material.getName() + "_to_plate"))
                    .inputItems(TagPrefix.ingot, material)
                    .outputItems(plateStack)
                    // 耗能不变：上游 24 × mass → 7 × (24×mass÷7)
                    .duration((int) ((material.getMass() * 24L + 6) / 7))
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 车床：螺栓 ×1 → 螺丝 ×1（螺丝自动化，本模组传送带/泵配方内部闭环）----
        for (Material material : subsetMaterials) {
            if (!material.shouldGenerateRecipesFor(TagPrefix.screw) ||
                    !material.shouldGenerateRecipesFor(TagPrefix.bolt)) {
                continue;
            }
            ItemStack screwStack = ChemicalHelper.get(TagPrefix.screw, material);
            if (screwStack.isEmpty()) {
                continue;
            }
            GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_" + material.getName() + "_bolt_to_screw"))
                    .inputItems(TagPrefix.bolt, material)
                    .outputItems(screwStack)
                    .duration((int) Math.max(material.getMass() / 8L, 1L))
                    .EUt(4)
                    .save(provider);
        }

        // ---- ULV 车床：锭 ×1 → 杆 ×1 + 小撮粉 ×2（上游 processRod 默认形态
        // ---- harderRods=true：EUt 16、mass×2 → 总能耗 32×mass 不变：EUt 7、时长 ÷7）----
        for (Material material : subsetMaterials) {
            if (!material.shouldGenerateRecipesFor(TagPrefix.rod) ||
                    !material.hasProperty(PropertyKey.DUST) ||
                    !(material.hasProperty(PropertyKey.INGOT) || material.hasProperty(PropertyKey.GEM))) {
                continue;
            }
            ItemStack rodStack = ChemicalHelper.get(TagPrefix.rod, material);
            if (rodStack.isEmpty()) {
                continue;
            }
            GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_" + material.getName() + "_to_rod"))
                    .inputItems(TagPrefix.ingot, material)
                    .outputItems(rodStack.copyWithCount(1))
                    .outputItems(ChemicalHelper.get(TagPrefix.dustSmall, material, 2))
                    .duration((int) Math.max((material.getMass() * 32L + 6) / 7, 1))
                    .EUt(7)
                    .save(provider);
        }

        // ---- ULV 车床：剥皮原木 ×1 → 长木杆 ×4 + 木尘 ×1（原版八种木；上游 EUt 7 已是
        // ---- ULV 档故不换算，时长 160 → 320）----
        net.minecraft.world.level.block.Block[] strippedLogs = {
                net.minecraft.world.level.block.Blocks.STRIPPED_OAK_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_SPRUCE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_BIRCH_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_JUNGLE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_ACACIA_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_DARK_OAK_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_MANGROVE_LOG,
                net.minecraft.world.level.block.Blocks.STRIPPED_CHERRY_LOG
        };
        for (net.minecraft.world.level.block.Block log : strippedLogs) {
            String name = log.getDescriptionId().replace("block.minecraft.stripped_", "");
            GULVRecipeTypes.ULV_TURNING
                    .recipeBuilder(GregULVExpansion.id("lathe_stripped_" + name))
                    .inputItems(new ItemStack(log))
                    .outputItems(ChemicalHelper.get(TagPrefix.rodLong, GTMaterials.Wood, 4))
                    .outputItems(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Wood, 1))
                    .duration(160)
                    .EUt(7)
                    .save(provider);
        }
    }



    /**
     * 红石发电机燃料表 (redstone-generator.md 燃料表，C6 基准联动)：
     * 红石粉 1,200 EU ≈ 煤蒸汽链的 1/4；红石块 9 倍、无压缩奖励。
     * EUt 为负 = 发电。同样仅供运行时 addRecipes 调用。
     */
    public static void addRedstoneGeneratorFuels(Consumer<FinishedRecipe> provider) {
        GULVRecipeTypes.REDSTONE_GENERATOR_FUELS
                .recipeBuilder(GregULVExpansion.id("redstone_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Redstone, 1)
                .duration(150)
                .EUt(-8)
                .save(provider);

        GULVRecipeTypes.REDSTONE_GENERATOR_FUELS
                .recipeBuilder(GregULVExpansion.id("redstone_block"))
                .inputItems(TagPrefix.block, GTMaterials.Redstone, 1)
                .duration(1350)
                .EUt(-8)
                .save(provider);
    }

    /**
     * ULV 电路替代配方 (ulv-circuit-line.md，D14：探测器为 ULV 电路唯一入口)。
     * 原上游 6 条配方已经 removeRecipes 移除，本方法提供唯一制法。
     * 同样仅供运行时 addRecipes 调用。
     */
    public static void addCircuitReplacementRecipes(Consumer<FinishedRecipe> provider) {
        // 真空管：保留上游配方 ID（配方书/查看器连续性）。该 ID 在 RECIPE_FILTERS
        // 中（被自身 removeRecipes 移除），故必须直写 GTDynamicDataPack 绕过过滤器
        // ——姊妹项目 GSE 焦炉替换配方的同款手法。
        // 工作台配方同样只修改成本：上游原版（钢螺栓 ×2 + 玻璃管 + 铜单线 ×3）
        // 原样保留，仅追加探测器 ×1（底部中央）
        VanillaRecipeHelper.addShapedRecipe(GTDynamicDataPack::addRecipe,
                GTCEu.id("vacuum_tube"),
                GTItems.VACUUM_TUBE.asStack(),
                "PTP",
                "WWW",
                " D ",
                'P', ChemicalHelper.get(TagPrefix.bolt, GTMaterials.Steel),
                'T', GTItems.GLASS_TUBE.asStack(),
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Copper),
                'D', GULVItems.CATS_WHISKER_DETECTOR);

        // 真空管·装配机红合金流体档（上游 3/4 号配方 + 探测器成本，ID 保留、动态包直写）：
        // 工作台 ×1 → 装配机红合金 ×3 → 装配机退火铜 ×4，恢复上游「以流体育提产率」的梯度
        com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder("vacuum_tube_red_alloy")
                .EUt(GTValues.VA[GTValues.ULV]).duration(40)
                .inputItems(GTItems.GLASS_TUBE)
                .inputItems(TagPrefix.bolt, GTMaterials.Steel)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.Copper, 2)
                .inputFluids(GTMaterials.RedAlloy.getFluid(18))
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.VACUUM_TUBE, 3)
                .save(GTDynamicDataPack::addRecipe);

        com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES
                .recipeBuilder("vacuum_tube_red_alloy_annealed")
                .EUt(GTValues.VA[GTValues.ULV]).duration(40)
                .inputItems(GTItems.GLASS_TUBE)
                .inputItems(TagPrefix.bolt, GTMaterials.Steel)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.AnnealedCopper, 2)
                .inputFluids(GTMaterials.RedAlloy.getFluid(18))
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.VACUUM_TUBE, 4)
                .save(GTDynamicDataPack::addRecipe);

        // NAND 芯片（2026-09-08 所有者指示：仍按上游产线，仅修改成本）：完整保留上游
        // 两条电路装配机配方（MV 装配机 + 好电路板/塑料板 + SoC 晶圆链 + 红合金螺栓 +
        // 锡细线），唯一成本修改是追加猫须探测器 ×1 作为 D14 入口。ID 保留上游原 ID，
        // 经 GTDynamicDataPack 直写绕过 removeRecipes 过滤器（与真空管同手法）。
        com.gregtechceu.gtceu.common.data.GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES
                .recipeBuilder("nand_chip_ulv_good_board")
                .EUt(GTValues.VA[GTValues.MV]).duration(300)
                .inputItems(GTItems.GOOD_CIRCUIT_BOARD)
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(TagPrefix.bolt, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.wireFine, GTMaterials.Tin, 2)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.NAND_CHIP_ULV, 8)
                .save(GTDynamicDataPack::addRecipe);

        com.gregtechceu.gtceu.common.data.GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES
                .recipeBuilder("nand_chip_ulv_plastic_board")
                .EUt(GTValues.VA[GTValues.MV]).duration(300)
                .inputItems(GTItems.PLASTIC_CIRCUIT_BOARD)
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(TagPrefix.bolt, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.wireFine, GTMaterials.Tin, 2)
                .inputItems(GULVItems.CATS_WHISKER_DETECTOR)
                .outputItems(GTItems.NAND_CHIP_ULV, 12)
                .save(GTDynamicDataPack::addRecipe);
    }

    /** 超低压卷板机：马达 + 红合金单线 ×2 + 铁板 ×3 + 钢辊（钢杆）×2 + ULV 机械方块（v0.5）。 */
    private static void addBenderRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_bender"),
                GULVMachines.ULV_BENDER.asStack(),
                "WRW",
                "IMI",
                "IHI",
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'R', ChemicalHelper.get(TagPrefix.rod, GTMaterials.Steel),
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'H', GTMachines.HULL[0].asStack());
    }

    /** 超低压车床：马达 + 红合金单线 ×2 + 铁板 ×4 + 锻铁车刀（锻铁板）×1 + ULV 机械方块（v0.5）。 */
    private static void addLatheRecipe(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider,
                GregULVExpansion.id("ulv_lathe"),
                GULVMachines.ULV_LATHE.asStack(),
                "ISI",
                "IMI",
                "WHW",
                'I', ChemicalHelper.get(TagPrefix.plate, GTMaterials.Iron),
                'S', ChemicalHelper.get(TagPrefix.plate, GTMaterials.WroughtIron),
                'M', GULVItems.ULV_ELECTRIC_MOTOR,
                'W', ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.RedAlloy),
                'H', GTMachines.HULL[0].asStack());
    }

}
