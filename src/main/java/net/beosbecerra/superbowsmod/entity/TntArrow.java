package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class TntArrow extends Arrow {

    private static final int FUSE_TICKS = 0;

    public TntArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public TntArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super( level, shooter);
    }

    private void explode() {
        PrimedTnt tnt = new PrimedTnt(level(), this.getX(), this.getY(), this.getZ(), null);
        tnt.setFuse(FUSE_TICKS);
        level().addFreshEntity(tnt);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        explode();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        explode();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}