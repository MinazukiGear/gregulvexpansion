package com.hoshino.gregulvexpansion.machine.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 温差发电机 (thermal-generator.md 已裁决草案 v0.3)。
 *
 * <p>免维护基础负荷：不烧燃料、不吃蒸汽，每 20 t 扫描六个相邻面，
 * 按「最高热源档位 × 最高冷面系数」计算输出，封顶 8 EU/t、1 A（全面发射）。
 * 热源档位（D5）：岩浆源 4 / 火 2 / 篝火 1.5 / 火把·灯笼 1；冷面系数：
 * 无 0.5 / 水 1.0 / 冰雪 1.25；流动岩浆不计数（不可靠）。
 *
 * <p>输出必须显著低于 8 EU/t（岩浆+贴水 5 EU/t 封顶，C2）——免费永动电
 * 只做保底，满功率需求请转红石发电机。篝火档以 0.75 权重参与计算
 * （1.5 EU/t 草案值 × 计算精度折算，见 HEAT_CAMPFIRE）。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThermoelectricGeneratorMachine extends TieredEnergyMachine {

    /** 内置缓存 EU：平滑 20 t 扫描周期的输出注入。 */
    public static final long CAPACITY = 64;
    /** 输出封顶 EU/t（发射容器电压上限，C2）。 */
    public static final long MAX_OUTPUT = 8;
    /** 热源档位（×10 定点表示，避免浮点）。 */
    private static final int HEAT_LAVA_SOURCE = 40, HEAT_FIRE = 20, HEAT_CAMPFIRE = 15, HEAT_TORCH = 10;
    /** 冷面系数（×100 定点表示）。 */
    private static final int COLD_NONE = 50, COLD_WATER = 100, COLD_ICE_SNOW = 125;

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER =
            new ManagedFieldHolder(ThermoelectricGeneratorMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    /** 当前输出 EU/t（供 Jade/tooltip 展示）。 */
    @Persisted
    @DescSynced
    private long currentOutput = 0;

    /** 每 tick 注入任务订阅。 */
    private TickableSubscription tickSub;

    public ThermoelectricGeneratorMachine(IMachineBlockEntity holder) {
        super(holder, GTValues.ULV);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        // 8 EU/t · 1 A · 全面发射；容器自身向电网推送
        return NotifiableEnergyContainer.emitterContainer(this, CAPACITY, GTValues.V[GTValues.ULV], 1);
    }

    @Override
    public boolean shouldWeatherOrTerrainExplosion() {
        return false;
    }

    //////////////////////////////////////
    // *******  Scan & Generate  *******//
    //////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            rescanNeighbors();
            tickSub = subscribeServerTick(tickSub, this::generatorTick);
        }
    }

    @Override
    public void onUnload() {
        if (tickSub != null) {
            tickSub.unsubscribe();
            tickSub = null;
        }
        currentOutput = 0;
        super.onUnload();
    }

    /** 邻方块变更 → 立即重扫（实现要点：方块更新触发即时重扫）。 */
    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        if (!isRemote() && getLevel() != null && fromPos.distSqr(getPos()) == 1) {
            rescanNeighbors();
        }
    }

    /** 每 20 t 扫描一次，其余 tick 按当前输出向缓存注入；发射容器自行推送电网。 */
    private void generatorTick() {
        if (getOffsetTimer() % 20 == 0) {
            rescanNeighbors();
        }
        if (currentOutput > 0) {
            energyContainer.addEnergy(currentOutput);
        }
    }

    private void rescanNeighbors() {
        Level level = getLevel();
        if (level == null) {
            currentOutput = 0;
            return;
        }
        BlockPos pos = getPos();
        int maxHeat = 0;
        int coldFactor = COLD_NONE;
        for (Direction side : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(side));
            maxHeat = Math.max(maxHeat, heatTier(state));
            coldFactor = Math.max(coldFactor, coldFactor(state));
        }
        // 定点数：heat(×10) × cold(×100) / 1000 = EU/t
        currentOutput = Math.min(maxHeat * (long) coldFactor / 1000, MAX_OUTPUT);
    }

    /** 热源档位 ×10（D5 表）；流动岩浆返回 0（不可靠，不计数）。 */
    private static int heatTier(BlockState state) {
        if (state.is(Blocks.LAVA)) {
            return state.getFluidState().isSource() ? HEAT_LAVA_SOURCE : 0;
        }
        if (state.is(Blocks.FIRE)) {
            return HEAT_FIRE;
        }
        if (state.is(Blocks.CAMPFIRE) && state.getValue(CampfireBlock.LIT)) {
            return HEAT_CAMPFIRE;
        }
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH) || state.is(Blocks.LANTERN)) {
            return HEAT_TORCH;
        }
        return 0;
    }

    /** 冷面系数 ×100（草案表）；非冷源返回 COLD_NONE 兜底。 */
    private static int coldFactor(BlockState state) {
        if (!state.getFluidState().isEmpty() && state.getFluidState().is(Fluids.WATER)) {
            return COLD_WATER;
        }
        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE) ||
                state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.SNOW) || state.is(Blocks.POWDER_SNOW)) {
            return COLD_ICE_SNOW;
        }
        return COLD_NONE;
    }
}
