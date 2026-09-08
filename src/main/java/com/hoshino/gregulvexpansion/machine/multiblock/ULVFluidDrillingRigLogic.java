package com.hoshino.gregulvexpansion.machine.multiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;
import com.gregtechceu.gtceu.common.machine.trait.FluidDrillLogic;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

/**
 * 超低压流体钻井机逻辑（primitive-distillation-tower.md v1.1，所有者指示）：
 * 相对上游 LV 流体钻井机——**产出减半、单位能耗加倍**。
 *
 * <p>上游钻井周期 20t @ EUt VA[LV]=30 产 P mB（600 EU/mB·P）。本泵：产出 P/2、
 * 周期 86t @ EUt 7（=1,202 EU）→ 单位能耗 2,404/P ≈ LV 的 2 倍（取整），
 * 实际产率 ≈ LV 钻机的 11.6%。矿脉枯竭机制与上游一致。
 */
public class ULVFluidDrillingRigLogic extends FluidDrillLogic {

    public static final int MAX_PROGRESS = 86;

    @Nullable
    private Fluid veinFluid;

    public ULVFluidDrillingRigLogic(FluidDrillMachine machine) {
        super(machine);
    }

    @Override
    public ULVFluidDrillingRigMachine getMachine() {
        return (ULVFluidDrillingRigMachine) super.getMachine();
    }

    @Override
    public void findAndHandleRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            lastRecipe = null;
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            if (veinFluid == null) {
                this.veinFluid = data.getFluidInChunk(getChunkX(), getChunkZ());
                if (this.veinFluid == null) {
                    if (subscription != null) {
                        subscription.unsubscribe();
                        subscription = null;
                    }
                    return;
                }
            }
            var match = getFluidPumpRecipe();
            if (match != null) {
                if (RecipeHelper.matchContents(this.machine, match).isSuccess()) {
                    setupRecipe(match);
                }
            }
        }
    }

    @Nullable
    private GTRecipe getFluidPumpRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            var recipe = GTRecipeBuilder.ofRaw()
                    .duration(MAX_PROGRESS)
                    .EUt(GTValues.VA[GTValues.ULV])
                    .outputFluids(new FluidStack(veinFluid,
                            getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ()))))
                    .buildRawRecipe();
            if (RecipeHelper.matchContents(getMachine(), recipe).isSuccess()) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public int getFluidToProduce() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            return getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ()));
        }
        return 0;
    }

    private int getFluidToProduce(FluidVeinWorldEntry entry) {
        var definition = entry.getDefinition();
        if (definition == null) {
            return 0;
        }
        int produced = Math.max(definition.getDepletedYield(),
                entry.getFluidYield() * entry.getOperationsRemaining()
                        / BedrockFluidVeinSavedData.MAXIMUM_VEIN_OPERATIONS);
        produced *= FluidDrillMachine.getRigMultiplier(getMachine().getTier());
        // 产出减半（相对 LV 钻机）；EUt 7 下单位能耗相应加倍
        return produced / 2;
    }

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            RecipeHelper.handleRecipeIO(this.machine, lastRecipe, IO.OUT, this.chanceCaches);
        }
        depleteVein();
        var match = getFluidPumpRecipe();
        if (match != null) {
            if (RecipeHelper.matchContents(this.machine, match).isSuccess()) {
                setupRecipe(match);
                return;
            }
        }
        if (suspendAfterFinish) {
            setStatus(Status.SUSPEND);
            suspendAfterFinish = false;
        } else {
            setStatus(Status.IDLE);
        }
        progress = 0;
        duration = 0;
    }

    protected void depleteVein() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            // 枯竭概率与上游 LV 钻机一致（chance 1）
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            data.depleteVein(getChunkX(), getChunkZ(), 0, false);
        }
    }

    private int getChunkX() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getX());
    }

    private int getChunkZ() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getZ());
    }
}
