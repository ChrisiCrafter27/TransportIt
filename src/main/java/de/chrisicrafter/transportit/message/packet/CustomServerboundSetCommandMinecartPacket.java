package de.chrisicrafter.transportit.message.packet;

import de.chrisicrafter.transportit.entity.custom.CustomMinecartCommandBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CustomServerboundSetCommandMinecartPacket extends ServerboundSetCommandMinecartPacket {
    private final int entity;
    private final String command;
    private final boolean trackOutput;

    public CustomServerboundSetCommandMinecartPacket(int p_134534_, String p_134535_, boolean p_134536_) {
        super(p_134534_, p_134535_, p_134536_);
        this.entity = p_134534_;
        this.command = p_134535_;
        this.trackOutput = p_134536_;
    }

    public CustomServerboundSetCommandMinecartPacket(FriendlyByteBuf p_179758_) {
        super(p_179758_);
        this.entity = p_179758_.readVarInt();
        this.command = p_179758_.readUtf();
        this.trackOutput = p_179758_.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf p_134547_) {
        p_134547_.writeVarInt(this.entity);
        p_134547_.writeUtf(this.command);
        p_134547_.writeBoolean(this.trackOutput);
    }

    @Override
    public void handle(ServerGamePacketListener p_134544_) {
        p_134544_.handleSetCommandMinecart(this);
    }

    @Override
    @Nullable
    public BaseCommandBlock getCommandBlock(Level p_134538_) {
        Entity entity = p_134538_.getEntity(this.entity);
        return entity instanceof CustomMinecartCommandBlock ? ((CustomMinecartCommandBlock) entity).getCommandBlock() : null;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public boolean isTrackOutput() {
        return this.trackOutput;
    }
}