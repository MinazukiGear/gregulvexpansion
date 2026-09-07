package com.hoshino.gregulvexpansion.machine.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.hoshino.gregulvexpansion.registry.GULVItems;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 手摇发电机 (hand-crank-dynamo.md 已裁决草案 v0.3)。
 *
 * <p>零门槛第一桶 EU：不烧燃料、不接蒸汽、无配方表；玩家右键摇动向内置缓存
 * 注能，缓存以 8 EU/t · 1 A 自动向任意一面的 GT 电网输出（发射容器语义，
 * sideOutputCondition 默认全面允许）。节奏 (C1 已裁决)：单击 +24 EU、
 * 冷却 10 t ⇒ 摇 10 秒（缓存充满）≈ 供 1 台 ULV 机器运行 1 分钟。
 *
 * <p>木质曲柄为独立物品 (D2)：工作台合成产物默认已安装；空机可用曲柄右键
 * 装回，扳手（非潜行，潜行+扳手仍是转向）拆下掉落。AE2 存在时其木质曲柄
 * 亦可驱动 (D2 软兼容，仅运行时探测)。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HandCrankDynamoMachine extends TieredEnergyMachine implements IInteractedMachine, IMachineLife {

    /** 内置缓存 EU (草案 480，允许 ±30% 实测微调)。 */
    public static final int CAPACITY = 480;
    /** 单次摇动注入 EU (C1)。 */
    public static final int EU_PER_CRANK = 24;
    /** 摇动冷却 tick (C1，手速上限 ≈ 2 次/秒)。 */
    public static final int CRANK_COOLDOWN_TICKS = 10;
    /** AE2 木质曲柄的物品 ID (D2 软兼容)。 */
    private static final ResourceLocation AE2_CRANK_ID = ResourceLocation.fromNamespaceAndPath("appeng", "crank");

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER =
            new ManagedFieldHolder(HandCrankDynamoMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    /** 曲柄是否已安装；拆卸后机器不响应摇动。 */
    @Persisted
    @DescSynced
    private boolean hasCrank = true;
    /** 上次成功摇动的游戏刻（用于冷却判定）。 */
    @Persisted
    private long lastCrankTime = Long.MIN_VALUE / 4;

    public HandCrankDynamoMachine(IMachineBlockEntity holder) {
        super(holder, GTValues.ULV);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        // 8 EU/t · 1 A · 全面发射：不设 sideOutputCondition，任意一面均可对接导线
        return NotifiableEnergyContainer.emitterContainer(this, CAPACITY, GTValues.V[GTValues.ULV], 1);
    }

    @Override
    public boolean shouldWeatherOrTerrainExplosion() {
        // 手摇发电机是木质低风险设备 (设计红线：低成本低风险)，不参与雷暴/地形爆炸
        return false;
    }

    //////////////////////////////////////
    // ******* Interaction *******//
    //////////////////////////////////////

    @Override
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                   BlockHitResult hit) {
        if (world.isClientSide) {
            return canAcceptCrank(player.getItemInHand(hand)) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        ItemStack held = player.getItemInHand(hand);
        // 空机装曲柄（合成产物默认已装，此处覆盖拆装循环）
        if (!hasCrank) {
            if (isCrankItem(held)) {
                hasCrank = true;
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
                playCrankSound((ServerLevel) world);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
            return InteractionResult.PASS;
        }
        // 冷却中：静默忽略，防止快速连点突破手速上限 (D3)
        long now = world.getGameTime();
        if (now - lastCrankTime < CRANK_COOLDOWN_TICKS) {
            return InteractionResult.PASS;
        }
        // 缓存已满：继续摇动无效
        if (energyContainer.getEnergyStored() >= energyContainer.getEnergyCapacity()) {
            return InteractionResult.PASS;
        }
        lastCrankTime = now;
        energyContainer.addEnergy(EU_PER_CRANK);
        playCrankSound((ServerLevel) world);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    /**
     * 扳手交互：非潜行 + 扳手拆下曲柄并掉落（重复安装语义，D2）；
     * 潜行 + 扳手保持上游转向语义。曲柄未安装时回落上游逻辑（仅转向）。
     */
    @Override
    protected InteractionResult onWrenchClick(Player playerIn, InteractionHand hand, Direction gridSide,
                                              BlockHitResult hitResult) {
        if (hasCrank && !playerIn.isShiftKeyDown()) {
            if (!isRemote()) {
                hasCrank = false;
                Block.popResource(getLevel(), getPos(), GULVItems.WOOD_CRANK.asStack());
            }
            return InteractionResult.sidedSuccess(isRemote());
        }
        return super.onWrenchClick(playerIn, hand, gridSide, hitResult);
    }

    @Override
    public void onMachineRemoved() {
        if (hasCrank && !isRemote() && getLevel() instanceof ServerLevel serverLevel) {
            Block.popResource(serverLevel, getPos(), GULVItems.WOOD_CRANK.asStack());
        }
    }

    //////////////////////////////////////
    // ********** MISC ***********//
    //////////////////////////////////////

    public boolean hasCrank() {
        return hasCrank;
    }

    public static boolean isCrankItem(ItemStack stack) {
        if (stack.is(GULVItems.WOOD_CRANK.get())) {
            return true;
        }
        // D2 软兼容：AE2 木质曲柄；AE2 不是前置，仅运行时探测
        return ModList.get().isLoaded("ae2") &&
                stack.is(ForgeRegistries.ITEMS.getValue(AE2_CRANK_ID));
    }

    private boolean canAcceptCrank(ItemStack stack) {
        return !hasCrank && isCrankItem(stack);
    }

    private void playCrankSound(ServerLevel level) {
        level.playSound(null, getPos(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.4f,
                0.7f + level.random.nextFloat() * 0.3f);
    }
}
