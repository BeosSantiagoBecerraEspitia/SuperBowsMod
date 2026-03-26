package net.beosbecerra.superbowsmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class BeeArrow extends Arrow {

    // ── Tweak ──────────────────────────────────────────────────────────
    private static final int BEE_COUNT = 6;
    // ───────────────────────────────────────────────────────────────────

    public BeeArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public BeeArrow(EntityType<? extends Arrow> type, Level level, LivingEntity shooter) {
        super( level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // deal normal arrow damage first
        super.onHitEntity(result);

        if (!level().isClientSide && result.getEntity() instanceof LivingEntity target) {
            for (int i = 0; i < BEE_COUNT; i++) {
                Bee bee = EntityType.BEE.create(level());
                if (bee == null) continue;

                // spawn around the impact point
                bee.setPos(
                        this.getX() + (level().random.nextDouble() - 0.5) * 2,
                        this.getY() + 0.5,
                        this.getZ() + (level().random.nextDouble() - 0.5) * 2
                );

                // make bee angry at the target
                bee.setTarget(target);
                bee.setRemainingPersistentAngerTime(400); // 20 seconds of anger
                bee.setPersistentAngerTarget(target.getUUID());

                level().addFreshEntity(bee);
            }
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}