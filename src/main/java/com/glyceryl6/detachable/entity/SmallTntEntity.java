package com.glyceryl6.detachable.entity;

import com.glyceryl6.detachable.DetachableTNT;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallTntEntity extends AbstractArrow {

    public SmallTntEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public SmallTntEntity(double x, double y, double z, Level level) {
        super(RegistryEntity.SMALL_TNT.get(), x, y, z, level);
    }

    public SmallTntEntity(LivingEntity entity, Level level) {
        super(RegistryEntity.SMALL_TNT.get(), entity, level);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 80) {
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        super.tick();
    }

    @Override
    public ItemStack getPickupItem() {
        return DetachableTNT.SMALL_TNT_ITEM.get().getDefaultInstance();
    }

    public boolean tryPickup(Player player) {
        return false;
    }

    public boolean isInGround() {
        return this.inGround;
    }

    protected void onHitBlock(BlockHitResult result) {
        BlockState blockstate = this.level.getBlockState(result.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, result, this);
        Vec3 vec3 = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setShotFromCrossbow(false);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    }

}