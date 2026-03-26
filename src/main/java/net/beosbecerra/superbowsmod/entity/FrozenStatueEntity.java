package net.beosbecerra.superbowsmod.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class FrozenStatueEntity extends LivingEntity {

    private static final EntityDataAccessor<String> TRAPPED_ENTITY_TYPE =
            SynchedEntityData.defineId(FrozenStatueEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<CompoundTag> TRAPPED_ENTITY_DATA =
            SynchedEntityData.defineId(FrozenStatueEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Float> TRAPPED_WIDTH =
            SynchedEntityData.defineId(FrozenStatueEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TRAPPED_HEIGHT =
            SynchedEntityData.defineId(FrozenStatueEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TRAPPED_SCALE =
            SynchedEntityData.defineId(FrozenStatueEntity.class, EntityDataSerializers.FLOAT);

    private EntityDimensions statueSize = EntityDimensions.fixed(0.5F, 0.5F);

    public FrozenStatueEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    public static FrozenStatueEntity buildFromEntity(LivingEntity entity) {
        FrozenStatueEntity statue = ModEntities.FROZEN_STATUE.get().create(entity.level());
        CompoundTag tag = new CompoundTag();
        try {
            if (!(entity instanceof Player)) {
                entity.saveWithoutId(tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        statue.setTrappedTag(tag);
        statue.setTrappedEntityTypeString(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
        statue.setTrappedWidth(entity.getBbWidth());
        statue.setTrappedHeight(entity.getBbHeight());
        statue.setTrappedScale(entity.getScale());
        return statue;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRAPPED_ENTITY_TYPE, "minecraft:pig");
        this.entityData.define(TRAPPED_ENTITY_DATA, new CompoundTag());
        this.entityData.define(TRAPPED_WIDTH, 0.5F);
        this.entityData.define(TRAPPED_HEIGHT, 0.5F);
        this.entityData.define(TRAPPED_SCALE, 1.0F);
    }

    // getters and setters
    public String getTrappedEntityTypeString() { return this.entityData.get(TRAPPED_ENTITY_TYPE); }
    public void setTrappedEntityTypeString(String s) { this.entityData.set(TRAPPED_ENTITY_TYPE, s); }
    public CompoundTag getTrappedTag() { return this.entityData.get(TRAPPED_ENTITY_DATA); }
    public void setTrappedTag(CompoundTag tag) { this.entityData.set(TRAPPED_ENTITY_DATA, tag); }
    public float getTrappedWidth() { return this.entityData.get(TRAPPED_WIDTH); }
    public void setTrappedWidth(float w) { this.entityData.set(TRAPPED_WIDTH, w); }
    public float getTrappedHeight() { return this.entityData.get(TRAPPED_HEIGHT); }
    public void setTrappedHeight(float h) { this.entityData.set(TRAPPED_HEIGHT, h); }
    public float getTrappedScale() { return this.entityData.get(TRAPPED_SCALE); }
    public void setTrappedScale(float s) { this.entityData.set(TRAPPED_SCALE, s); }

    @Override
    public void baseTick() {} // statue does nothing

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // only takes damage from players hitting it
        if (!this.level().isClientSide && source.getEntity() instanceof Player) {
            // drop packed ice and discard
            this.spawnAtLocation(new ItemStack(Items.PACKED_ICE, 1));
            this.discard();
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setYRot(this.yBodyRot);
        this.yHeadRot = this.getYRot();

        // resize bounding box to match trapped entity
        if (Math.abs(this.getBbWidth() - getTrappedWidth()) > 0.01
                || Math.abs(this.getBbHeight() - getTrappedHeight()) > 0.01) {
            double prevX = this.getX();
            double prevZ = this.getZ();
            this.statueSize = EntityDimensions.scalable(getTrappedWidth(), getTrappedHeight());
            refreshDimensions();
            this.setPos(prevX, this.getY(), prevZ);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) { return statueSize; }

    @Override
    public float getScale() { return getTrappedScale(); }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("StatueWidth", getTrappedWidth());
        tag.putFloat("StatueHeight", getTrappedHeight());
        tag.putFloat("StatueScale", getTrappedScale());
        tag.putString("StatueEntityType", getTrappedEntityTypeString());
        tag.put("StatueEntityTag", getTrappedTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTrappedWidth(tag.getFloat("StatueWidth"));
        setTrappedHeight(tag.getFloat("StatueHeight"));
        setTrappedScale(tag.getFloat("StatueScale"));
        setTrappedEntityTypeString(tag.getString("StatueEntityType"));
        if (tag.contains("StatueEntityTag")) setTrappedTag(tag.getCompound("StatueEntityTag"));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        // invulnerable to everything except player hits
        if (source.getEntity() instanceof Player) return false;
        return true;
    }

    @Override public Iterable<ItemStack> getArmorSlots() { return ImmutableList.of(); }
    @Override public ItemStack getItemBySlot(EquipmentSlot slot) { return ItemStack.EMPTY; }
    @Override public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}
    @Override public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
    @Override public boolean canBreatheUnderwater() { return true; }

    public EntityType<?> getTrappedEntityType() {
        return EntityType.byString(getTrappedEntityTypeString()).orElse(EntityType.PIG);
    }
}