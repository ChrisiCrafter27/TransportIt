package de.chrisicrafter.transportit.screen;

import de.chrisicrafter.transportit.entity.custom.CustomMinecartCommandBlock;
import de.chrisicrafter.transportit.message.packet.CustomServerboundSetCommandMinecartPacket;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;

public class CustomMinecartCommandBlockEditScreen extends MinecartCommandBlockEditScreen {
    public CustomMinecartCommandBlockEditScreen(BaseCommandBlock p_99216_) {
        super(p_99216_);
    }

    @Override
    protected void populateAndSendPacket(BaseCommandBlock p_99218_) {
        if (p_99218_ instanceof CustomMinecartCommandBlock.MinecartCommandBase minecartcommandblock$minecartcommandbase) {
            this.minecraft.getConnection().send(new CustomServerboundSetCommandMinecartPacket(minecartcommandblock$minecartcommandbase.getMinecart().getId(), this.commandEdit.getValue(), p_99218_.isTrackOutput()));
        } else if (p_99218_ instanceof MinecartCommandBlock.MinecartCommandBase minecartcommandblock$minecartcommandbase) {
            this.minecraft.getConnection().send(new ServerboundSetCommandMinecartPacket(minecartcommandblock$minecartcommandbase.getMinecart().getId(), this.commandEdit.getValue(), p_99218_.isTrackOutput()));
        }
    }
}