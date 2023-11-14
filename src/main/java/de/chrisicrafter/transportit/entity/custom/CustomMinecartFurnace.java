package de.chrisicrafter.transportit.entity.custom;

import de.chrisicrafter.transportit.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CustomMinecartFurnace extends CustomAbstractMinecart implements Container {
    private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(CustomMinecartFurnace.class, EntityDataSerializers.BOOLEAN);
    private int fuel;
    public double xPush;
    public double zPush;
    private static final Ingredient INGREDIENT = Ingredient.of(Items.BLAZE_ROD, Items.COAL_BLOCK);

    public CustomMinecartFurnace(EntityType<? extends CustomMinecartFurnace> type, Level level) {
        super(type, level);
    }

    public CustomMinecartFurnace(Level level, double x, double y, double z) {
        super(ModEntities.CUSTOM_MINECART_FURNACE.get(), level, x, y, z);
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.fuel > 0) {
                --this.fuel;
            }
            if (this.fuel <= 0) {
                this.xPush = 0.0D;
                this.zPush = 0.0D;
            }
            this.setHasFuel(this.fuel > 0);
        }
        if (this.hasFuel() && this.random.nextInt(4) == 0) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    public CustomAbstractMinecart.Type getMinecartType() {
        return CustomAbstractMinecart.Type.FURNACE;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FUEL, false);
    }

    @Override
    protected Item getDropItem() {
        return Items.FURNACE_MINECART;
    }

    @Override
    protected void moveAlongTrack(BlockPos pos, BlockState state) {
        double d0 = 1.0E-4D;
        double d1 = 0.001D;
        super.moveAlongTrack(pos, state);
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = vec3.horizontalDistanceSqr();
        double d3 = this.xPush * this.xPush + this.zPush * this.zPush;
        if (d3 > 1.0E-4D && d2 > 0.001D) {
            double d4 = Math.sqrt(d2);
            double d5 = Math.sqrt(d3);
            this.xPush = vec3.x / d4 * d5;
            this.zPush = vec3.z / d4 * d5;
        }
    }

    @Override
    protected void applyNaturalSlowdown() {
        double d0 = this.xPush * this.xPush + this.zPush * this.zPush;
        if (d0 > 1.0E-7D) {
            d0 = Math.sqrt(d0);
            this.xPush /= d0;
            this.zPush /= d0;
            Vec3 vec3 = this.getDeltaMovement().multiply(0.8D, 0.0D, 0.8D).add(this.xPush, 0.0D, this.zPush);
            if (this.isInWater()) {
                vec3 = vec3.scale(0.1D);
            }

            this.setDeltaMovement(vec3);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.98D, 0.0D, 0.98D));
        }

        super.applyNaturalSlowdown();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (INGREDIENT.test(itemstack) && this.fuel + 3600 <= 32000) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.fuel += 3600;
        }
        if (this.fuel > 0) {
            this.xPush = this.getX() - player.getX();
            this.zPush = this.getZ() - player.getZ();
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("PushX", this.xPush);
        tag.putDouble("PushZ", this.zPush);
        tag.putShort("Fuel", (short)this.fuel);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.xPush = tag.getDouble("PushX");
        this.zPush = tag.getDouble("PushZ");
        this.fuel = tag.getShort("Fuel");
    }

    protected boolean hasFuel() {
        return this.entityData.get(DATA_ID_FUEL);
    }

    protected void setHasFuel(boolean flag) {
        this.entityData.set(DATA_ID_FUEL, flag);
    }

    public BlockState getDefaultDisplayBlockState() {
        return Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH).setValue(FurnaceBlock.LIT, Boolean.valueOf(this.hasFuel()));
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.CUSTOM_MINECART_FURNACE.get();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        if(INGREDIENT.test(p_18945_)) this.fuel += 3600;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack p_18953_) {
        return INGREDIENT.test(p_18953_) && this.fuel + 3600 <= 32000;
    }

    @Override
    public boolean canTakeItem(Container p_273520_, int p_272681_, ItemStack p_273702_) {
        return false;
    }

    @Override
    public void clearContent() {}
}
