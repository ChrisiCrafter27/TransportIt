package de.chrisicrafter.transportit.entity.event;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.entity.ModEntities;
import de.chrisicrafter.transportit.entity.custom.CustomMinecartFurnace;
import de.chrisicrafter.transportit.entity.custom.TestEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransportIt.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onBlockMine(BlockEvent.BreakEvent event) {
        if(event.getLevel() instanceof ServerLevel level) {
            TestEntity sheep = new TestEntity(ModEntities.CUSTOM_MINECART_FURNACE.get(), level);
            sheep.setPos(event.getPos().getCenter());
            level.addFreshEntity(sheep);
        }
    }

}
