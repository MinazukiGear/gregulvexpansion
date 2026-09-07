package com.hoshino.gregulvexpansion.machine.storage;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.hoshino.gregulvexpansion.registry.GULVItems;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 铅酸蓄电墙 (lead-acid-battery-line.md，D8/D9/D10 已裁决)。
 *
 * <p>「墙上的蓄电池」：自含 24,000 EU 缓存，六面接线、1 A 进 1 A 出，
 * 与上游充电站/电池缓冲（收电池物品）语义互补不重叠——本方块不设电池槽。
 * 无自放电（D9）；过充不爆炸（教学友好），但被 LV+ 网络超压供电时
 * <b>熔断锁死</b>（停止进出）直至用扳手（非潜行）重置——给一个「哦，电压
 * 等级」的轻量教训而非炸档惩罚。
 *
 * <p>不继承 SimpleTieredMachine（无配方逻辑），能量容器为
 * {@link LeadAcidWallEnergyContainer}：在标准容器之上加入熔断判定，并放宽
 * inputsEnergy 的「输入输出面互斥」约束以支持同一面双向接线。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LeadAcidBatteryWallMachine extends TieredEnergyMachine implements IMachineLife {

    /** 自含缓存 EU（对齐总纲 §6.3 储能阶梯）。 */
    public static final long CAPACITY = 24_000;
    /** 统一电流档（D8：只做 1A）。 */
    public static final long AMPS = 1;

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER =
            new ManagedFieldHolder(LeadAcidBatteryWallMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    /** 熔断锁死：被超压供电后置位，扳手重置。 */
    @Persisted
    @DescSynced
    private boolean fused = false;

    public LeadAcidBatteryWallMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        return new LeadAcidWallEnergyContainer(this, CAPACITY,
                GTValues.V[GTValues.ULV], AMPS, GTValues.V[GTValues.ULV], AMPS);
    }

    @Override
    public boolean shouldWeatherOrTerrainExplosion() {
        // 铅酸过充不爆炸（教学友好），同样不参与雷暴/地形爆炸
        return false;
    }

    //////////////////////////////////////
    // ******* Interaction *******//
    //////////////////////////////////////

    @Override
    protected InteractionResult onWrenchClick(Player playerIn, InteractionHand hand, Direction gridSide,
                                              BlockHitResult hitResult) {
        // 非潜行 + 扳手：重置熔断；潜行 + 扳手保持上游转向语义
        if (fused && !playerIn.isShiftKeyDown()) {
            if (!isRemote()) {
                fused = false;
            }
            return InteractionResult.sidedSuccess(isRemote());
        }
        return super.onWrenchClick(playerIn, hand, gridSide, hitResult);
    }

    public boolean isFused() {
        return fused;
    }

    //////////////////////////////////////
    // ********** MISC ***********//
    //////////////////////////////////////

    @Override
    public void onMachineRemoved() {
        // 纯储能方块无内容物掉落；能量随方块移除而清空（GT 标准语义）
    }

    /**
     * 蓄电墙专用能量容器：
     * <ul>
     * <li>六面双向：覆写 {@link #inputsEnergy}，解除标准容器「输出面不收能量」的
     * 单向互斥（side 条件均默认全面允许）；</li>
     * <li>过压熔断：超压供电不再依赖上游的爆炸分支（本机不是
     * IExplosionMachine 时超压会被静默接受），而是置位熔断并完全锁死。</li>
     * </ul>
     */
    public static class LeadAcidWallEnergyContainer extends NotifiableEnergyContainer {
        private final LeadAcidBatteryWallMachine wall;

        public LeadAcidWallEnergyContainer(LeadAcidBatteryWallMachine machine, long maxCapacity,
                                           long maxInputVoltage, long maxInputAmperage,
                                           long maxOutputVoltage, long maxOutputAmperage) {
            super(machine, maxCapacity, maxInputVoltage, maxInputAmperage, maxOutputVoltage, maxOutputAmperage);
            this.wall = machine;
        }

        @Override
        public long acceptEnergyFromNetwork(Direction side, long voltage, long amperage) {
            if (wall.fused) {
                return 0;
            }
            if (voltage > getInputVoltage()) {
                wall.fused = true;
                return 0;
            }
            return super.acceptEnergyFromNetwork(side, voltage, amperage);
        }

        @Override
        public void serverTick() {
            if (wall.fused) {
                return; // 熔断期间进出全部锁死
            }
            super.serverTick();
        }

        @Override
        public boolean inputsEnergy(Direction side) {
            // 标准实现强制 inputs = !outputs（单面单向）；蓄电墙不设面条件，六面均双向
            return getInputVoltage() > 0;
        }
    }
}
