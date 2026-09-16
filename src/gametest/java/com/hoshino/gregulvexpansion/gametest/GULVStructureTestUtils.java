package com.hoshino.gregulvexpansion.gametest;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.BlockState;

/** Places the exact shapes consumed by EMI and checks them against the live pattern. */
public final class GULVStructureTestUtils {
    private GULVStructureTestUtils() {}

    public static void assertAllShapesForm(GameTestHelper helper, MultiblockMachineDefinition definition) {
        var shapes = definition.getMatchingShapes();
        helper.assertTrue(!shapes.isEmpty(), "No preview shape registered for " + definition.getId());
        for (int i = 0; i < shapes.size(); i++) {
            BlockPos anchor = new BlockPos(4 + i * 7, 12, 4);
            MultiblockControllerMachine controller = placeShape(helper, definition, shapes.get(i), anchor);
            helper.assertTrue(controller != null, "Preview shape has no controller: " + definition.getId());
            helper.assertTrue(controller.getPattern() != null,
                    "No pattern registered for " + definition.getId());
            helper.assertTrue(controller.getPattern().checkPatternAt(controller.getMultiblockState(), true),
                    "Preview shape " + i + " does not match the live pattern for " + definition.getId());
        }
        helper.succeed();
    }

    private static MultiblockControllerMachine placeShape(GameTestHelper helper,
                                                           MultiblockMachineDefinition definition,
                                                           MultiblockShapeInfo shape,
                                                           BlockPos anchor) {
        BlockInfo[][][] blocks = shape.getBlocks();
        int controllerX = -1;
        int controllerY = -1;
        int controllerZ = -1;
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    BlockInfo info = blocks[x][y][z];
                    if (info != null && info != BlockInfo.EMPTY && info.getBlockState() != null
                            && info.getBlockState().getBlock() == definition.getBlock()) {
                        controllerX = x;
                        controllerY = y;
                        controllerZ = z;
                    }
                }
            }
        }
        helper.assertTrue(controllerX >= 0, "Shape contains no controller for " + definition.getId());
        helper.assertTrue(controllerX == blocks.length / 2 && controllerZ == 0,
                "Controller is not centered on the front face for " + definition.getId());

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    BlockInfo info = blocks[x][y][z];
                    if (info == null || info == BlockInfo.EMPTY) continue;
                    BlockState state = info.getBlockState();
                    if (state == null || state.isAir()) continue;
                    if (state.getBlock() == definition.getBlock()) {
                        RotationState rotation = definition.getRotationState();
                        if (rotation != RotationState.NONE && state.hasProperty(rotation.property)) {
                            state = state.setValue(rotation.property, Direction.NORTH);
                        }
                    }
                    helper.setBlock(anchor.offset(x - controllerX, y - controllerY, z - controllerZ), state);
                }
            }
        }
        MetaMachine machine = MetaMachine.getMachine(helper.getLevel(), helper.absolutePos(anchor));
        return machine instanceof MultiblockControllerMachine controller ? controller : null;
    }
}
