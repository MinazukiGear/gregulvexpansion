package com.hoshino.gregulvexpansion.gametest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.cover.ConveyorCover;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.hoshino.gregulvexpansion.GregULVExpansion;
import com.hoshino.gregulvexpansion.machine.generator.HandCrankDynamoMachine;
import com.hoshino.gregulvexpansion.machine.generator.ThermoelectricGeneratorMachine;
import com.hoshino.gregulvexpansion.machine.multiblock.ULVFluidDrillingRigLogic;
import com.hoshino.gregulvexpansion.machine.simple.ULVSimpleMachine;
import com.hoshino.gregulvexpansion.machine.storage.LeadAcidBatteryWallMachine;
import com.hoshino.gregulvexpansion.registry.GULVItems;
import com.hoshino.gregulvexpansion.registry.GULVCovers;
import com.hoshino.gregulvexpansion.registry.GULVMachines;
import com.hoshino.gregulvexpansion.registry.GULVMultiblocks;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GameTestHolder(GregULVExpansion.MOD_ID)
@PrefixGameTestTemplate(false)
public final class GULVGameTests {
    private static final BlockPos MACHINE_POS = new BlockPos(2, 2, 2);

    private GULVGameTests() {}

    @GameTest(template = "empty_32x32x32", timeoutTicks = 80)
    public static void handCrankRequiresInstallationAndRespectsCooldown(GameTestHelper helper) {
        HandCrankDynamoMachine machine = placeHandCrank(helper);
        var player = helper.makeMockSurvivalPlayer();
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        use(machine, helper, player);
        helper.assertTrue(!machine.hasCrank(), "A newly placed generator must not contain a crank");
        helper.assertTrue(machine.energyContainer.getEnergyStored() == 0,
                "Right-clicking a generator without a crank produced energy");

        player.setItemInHand(InteractionHand.MAIN_HAND, GULVItems.WOOD_CRANK.asStack());
        use(machine, helper, player);
        helper.assertTrue(machine.hasCrank(), "Wooden crank did not install");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "Installing the crank did not consume exactly one item");

        use(machine, helper, player);
        helper.assertTrue(machine.energyContainer.getEnergyStored() == HandCrankDynamoMachine.EU_PER_CRANK,
                "First valid crank did not produce the configured energy");
        use(machine, helper, player);
        helper.assertTrue(machine.energyContainer.getEnergyStored() == HandCrankDynamoMachine.EU_PER_CRANK,
                "Cooldown allowed an immediate second crank");

        helper.runAfterDelay(HandCrankDynamoMachine.CRANK_COOLDOWN_TICKS, () -> {
            use(machine, helper, player);
            helper.assertTrue(machine.energyContainer.getEnergyStored() == 2L * HandCrankDynamoMachine.EU_PER_CRANK,
                    "Crank did not work after the cooldown elapsed");
            helper.succeed();
        });
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 40)
    public static void breakingHandCrankDropsOnlyAnInstalledCrank(GameTestHelper helper) {
        HandCrankDynamoMachine installed = placeHandCrank(helper);
        var player = helper.makeMockSurvivalPlayer();
        player.setItemInHand(InteractionHand.MAIN_HAND, GULVItems.WOOD_CRANK.asStack());
        use(installed, helper, player);
        helper.destroyBlock(MACHINE_POS);

        BlockPos emptyPos = MACHINE_POS.offset(5, 0, 0);
        helper.setBlock(emptyPos, GULVMachines.HAND_CRANK_DYNAMO.defaultBlockState());
        helper.destroyBlock(emptyPos);

        helper.runAfterDelay(1, () -> {
            long crankDrops = helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                            new AABB(helper.absolutePos(MACHINE_POS)).inflate(8))
                    .stream().filter(entity -> entity.getItem().is(GULVItems.WOOD_CRANK.get()))
                    .mapToInt(entity -> entity.getItem().getCount()).sum();
            helper.assertTrue(crankDrops == 1,
                    "Breaking one installed and one empty generator must drop exactly one crank; got " + crankDrops);
            helper.succeed();
        });
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 40)
    public static void recipesCoverComponentsPolyethyleneAndRubber(GameTestHelper helper) {
        String[] crafting = {
                "shaped/ulv_electric_motor", "shaped/ulv_electric_piston", "shaped/ulv_robot_arm",
                "shaped/hand_crank_dynamo",
                "shaped/ulv_conveyor_module_rubber", "shaped/ulv_conveyor_module_silicone_rubber",
                "shaped/ulv_conveyor_module_styrene_butadiene_rubber",
                "shaped/ulv_electric_pump_rubber", "shaped/ulv_electric_pump_silicone_rubber",
                "shaped/ulv_electric_pump_styrene_butadiene_rubber"
        };
        String[] processing = {
                "polyethylene_from_air", "polyethylene_from_oxygen",
                "raw_rubber_from_air", "raw_rubber_from_oxygen", "rubber",
                "polyethylene_ingot", "polyethylene_nuggets", "polyethylene_block", "polyethylene_plate",
                "rubber_ingot", "rubber_nuggets", "rubber_block", "rubber_plate",
                "polyethylene_tiny_fluid_pipe", "polyethylene_small_fluid_pipe",
                "polyethylene_normal_fluid_pipe", "polyethylene_large_fluid_pipe",
                "polyethylene_huge_fluid_pipe"
        };
        for (String id : crafting) assertRecipe(helper, id);
        for (String id : processing) assertRuntimeRecipe(helper, id);
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(GregULVExpansion.id("shaped/ulv_conveyor_module")).isEmpty(),
                "Obsolete single-material conveyor recipe is still loaded");
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(GregULVExpansion.id("shaped/ulv_electric_pump")).isEmpty(),
                "Obsolete single-material pump recipe is still loaded");
        helper.succeed();
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 40)
    public static void namesRecipeCategoriesAndTooltipsAreLocalized(GameTestHelper helper) {
        Map<String, String> en = language("en_us");
        Map<String, String> zh = language("zh_cn");
        helper.assertTrue(en.keySet().equals(zh.keySet()), "English and Chinese language keys differ");
        helper.assertTrue("化学反应釜".equals(zh.get("gregulvexpansion.ulv_chemical_reacting")),
                "Chemical reactor recipe category is not localized");
        helper.assertTrue("流体固化器".equals(zh.get("gregulvexpansion.ulv_fluid_solidification")),
                "Fluid solidifier recipe category is not localized");

        List<MachineDefinition> singleBlocks = List.of(
                GULVMachines.HAND_CRANK_DYNAMO, GULVMachines.PRIMITIVE_ELECTROLYZER,
                GULVMachines.LEAD_ACID_BATTERY_WALL, GULVMachines.THERMOELECTRIC_GENERATOR,
                GULVMachines.ULV_WIRE_MILL, GULVMachines.ULV_CUTTER, GULVMachines.REDSTONE_GENERATOR,
                GULVMachines.ULV_BENDER, GULVMachines.ULV_LATHE, GULVMachines.ULV_CHEMICAL_REACTOR,
                GULVMachines.ULV_FLUID_SOLIDIFIER, GULVMachines.ULV_FLUID_EXTRACTOR,
                GULVMachines.ULV_GAS_TURBINE, GULVMachines.ULV_POLARIZER);
        for (MachineDefinition definition : singleBlocks) {
            String localized = zh.get(definition.getDescriptionId());
            helper.assertTrue(localized != null && localized.startsWith("原始"),
                    "Single-block machine name does not use the Primitive prefix: " + definition.getId());
            List<Component> tooltip = new ArrayList<>();
            definition.getTooltipBuilder().accept(definition.asStack(), tooltip);
            helper.assertTrue(!tooltip.isEmpty(), "Machine tooltip is empty: " + definition.getId());
        }

        Set<String> keys = new HashSet<>();
        List<Component> tooltip = new ArrayList<>();
        GULVMachines.ULV_CHEMICAL_REACTOR.getTooltipBuilder()
                .accept(GULVMachines.ULV_CHEMICAL_REACTOR.asStack(), tooltip);
        tooltip.forEach(component -> collectTranslationKeys(component, keys));
        helper.assertTrue(keys.contains("gtceu.universal.tooltip.voltage_in"),
                "Basic machine tooltip does not use the upstream voltage template");
        helper.assertTrue(keys.contains("gtceu.universal.tooltip.energy_storage_capacity"),
                "Basic machine tooltip does not use the upstream capacity template");
        helper.succeed();
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 40)
    public static void configuredUlvValuesRemainPinned(GameTestHelper helper) {
        helper.assertTrue(ConveyorCover.CONVEYOR_SCALING.applyAsInt(GTValues.ULV) == 2,
                "ULV conveyor scaling changed");
        helper.assertTrue(GULVCovers.ULV_FLUID_TRANSFER_RATE == 16,
                "ULV pump and fluid-regulator rate changed");
        helper.assertTrue(ULVSimpleMachine.ENERGY_CAPACITY == 240, "ULV machine capacity changed");
        helper.assertTrue(HandCrankDynamoMachine.CAPACITY == 4_800
                        && HandCrankDynamoMachine.EU_PER_CRANK == 120
                        && HandCrankDynamoMachine.CRANK_COOLDOWN_TICKS == 10,
                "Hand-crank balance changed");
        helper.assertTrue(ThermoelectricGeneratorMachine.CAPACITY == 64
                        && ThermoelectricGeneratorMachine.MAX_OUTPUT == 8,
                "Thermoelectric generator balance changed");
        helper.assertTrue(LeadAcidBatteryWallMachine.CAPACITY == 24_000
                        && LeadAcidBatteryWallMachine.AMPS == 1,
                "Lead-acid wall balance changed");
        helper.assertTrue(ULVFluidDrillingRigLogic.MAX_PROGRESS == 86,
                "Fluid drilling duration changed");
        helper.succeed();
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 300)
    public static void leadChamberFormsFromEmiShape(GameTestHelper helper) {
        var shapes = GULVMultiblocks.LEAD_CHAMBER.getMatchingShapes();
        helper.assertTrue(shapes.size() == 1, "Lead chamber should have one preview shape");
        helper.assertTrue(countBlock(shapes.get(0).getBlocks(), GTBlocks.FIREBOX_STEEL.get()) == 1,
                "Lead chamber preview must contain exactly one steel firebox");
        helper.assertTrue(countBlock(shapes.get(0).getBlocks(), GTBlocks.CASING_BRONZE_PIPE.get()) == 1,
                "Lead chamber preview must contain the bronze pipe casing");
        GULVStructureTestUtils.assertAllShapesForm(helper, GULVMultiblocks.LEAD_CHAMBER);
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 300)
    public static void distillationTowerFormsUpToSixLayers(GameTestHelper helper) {
        var shapes = GULVMultiblocks.PRIMITIVE_DISTILLATION_TOWER.getMatchingShapes();
        helper.assertTrue(shapes.size() == 4, "Distillation tower should expose four heights (3-6 layers)");
        int largestDimension = shapes.stream().mapToInt(shape -> maxDimension(shape.getBlocks())).max().orElse(0);
        helper.assertTrue(largestDimension == 6, "Distillation tower preview exceeds or misses the 6-layer cap");
        GULVStructureTestUtils.assertAllShapesForm(helper, GULVMultiblocks.PRIMITIVE_DISTILLATION_TOWER);
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 300)
    public static void primitiveCrackerFormsWithSteelFireboxes(GameTestHelper helper) {
        var shape = GULVMultiblocks.PRIMITIVE_CRACKER.getMatchingShapes().get(0);
        helper.assertTrue(countBlock(shape.getBlocks(), GTBlocks.FIREBOX_STEEL.get()) > 0,
                "Primitive cracker preview contains no steel fireboxes");
        GULVStructureTestUtils.assertAllShapesForm(helper, GULVMultiblocks.PRIMITIVE_CRACKER);
    }

    @GameTest(template = "empty_32x32x32", timeoutTicks = 300)
    public static void fluidDrillingRigFormsWithBronzeFrame(GameTestHelper helper) {
        var shape = GULVMultiblocks.ULV_FLUID_DRILLING_RIG.getMatchingShapes().get(0);
        var bronze = GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Bronze).get();
        var steel = GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Steel).get();
        helper.assertTrue(countBlock(shape.getBlocks(), bronze) > 0,
                "Fluid drilling rig preview contains no bronze frames");
        helper.assertTrue(countBlock(shape.getBlocks(), steel) == 0,
                "Fluid drilling rig preview still contains steel frames");
        GULVStructureTestUtils.assertAllShapesForm(helper, GULVMultiblocks.ULV_FLUID_DRILLING_RIG);
    }

    private static HandCrankDynamoMachine placeHandCrank(GameTestHelper helper) {
        helper.setBlock(MACHINE_POS, GULVMachines.HAND_CRANK_DYNAMO.defaultBlockState());
        MetaMachine machine = MetaMachine.getMachine(helper.getLevel(), helper.absolutePos(MACHINE_POS));
        helper.assertTrue(machine instanceof HandCrankDynamoMachine,
                "Placed block did not create the hand-crank generator");
        return (HandCrankDynamoMachine) machine;
    }

    private static void use(HandCrankDynamoMachine machine, GameTestHelper helper,
                            net.minecraft.world.entity.player.Player player) {
        BlockPos absolute = helper.absolutePos(MACHINE_POS);
        machine.onUse(helper.getLevel().getBlockState(absolute), helper.getLevel(), absolute, player,
                InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false));
    }

    private static void assertRecipe(GameTestHelper helper, String path) {
        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(GregULVExpansion.id(path)).isPresent(),
                "Required recipe was not loaded: " + path);
    }

    private static void assertRuntimeRecipe(GameTestHelper helper, String path) {
        boolean loaded = helper.getLevel().getRecipeManager().getRecipes().stream()
                .anyMatch(recipe -> GregULVExpansion.MOD_ID.equals(recipe.getId().getNamespace())
                        && (recipe.getId().getPath().equals(path)
                        || recipe.getId().getPath().endsWith("/" + path)));
        helper.assertTrue(loaded, "Required runtime recipe was not loaded: " + path);
    }

    private static Map<String, String> language(String locale) {
        String path = "/assets/gregulvexpansion/lang/" + locale + ".json";
        var stream = GregULVExpansion.class.getResourceAsStream(path);
        if (stream == null) throw new AssertionError("Missing language resource " + path);
        return new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                new TypeToken<Map<String, String>>() {}.getType());
    }

    private static void collectTranslationKeys(Component component, Set<String> keys) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            keys.add(translatable.getKey());
        }
        component.getSiblings().forEach(sibling -> collectTranslationKeys(sibling, keys));
    }

    private static long countBlock(BlockInfo[][][] blocks, net.minecraft.world.level.block.Block target) {
        long count = 0;
        for (BlockInfo[][] plane : blocks) for (BlockInfo[] row : plane) for (BlockInfo info : row) {
            if (info != null && info != BlockInfo.EMPTY && info.getBlockState() != null
                    && info.getBlockState().is(target)) count++;
        }
        return count;
    }

    private static int maxDimension(BlockInfo[][][] blocks) {
        return Math.max(blocks.length, Math.max(blocks[0].length, blocks[0][0].length));
    }
}
