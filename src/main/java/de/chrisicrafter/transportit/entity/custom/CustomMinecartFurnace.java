package de.chrisicrafter.transportit.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CustomMinecartFurnace extends MinecartFurnace {
    private int fuel;
    private static final Ingredient INGREDIENT = Ingredient.of(Items.COAL_BLOCK, Items.BLAZE_ROD);

    public CustomMinecartFurnace(EntityType<? extends MinecartFurnace> type, Level level) {
        super(type, level);
    }

    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult ret = super.interact(player, hand);
        if (ret.consumesAction()) return ret;
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
    protected double getMaxSpeed() {
        return (this.isInWater() ? 3.0D / 20.0D : 4.0D);
    }

    @Override
    public float getMaxCartSpeedOnRail() {
        return 4.0f;
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
}
