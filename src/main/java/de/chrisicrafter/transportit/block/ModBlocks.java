package de.chrisicrafter.transportit.block;

import de.chrisicrafter.transportit.TransportIt;
import de.chrisicrafter.transportit.block.custom.AdvancedPoweredRailBlock;
import de.chrisicrafter.transportit.block.custom.CustomDetectorRailBlock;
import de.chrisicrafter.transportit.block.custom.CustomPoweredRailBlock;
import de.chrisicrafter.transportit.block.custom.CustomRailBlock;
import de.chrisicrafter.transportit.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TransportIt.MOD_ID);
    public static final DeferredRegister<Block> VANILLA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft");

    public static final RegistryObject<Block> POWERED_POWERED_RAIL = registerBlock("powered_powered_rail", () -> new CustomPoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL), CustomPoweredRailBlock.PoweredRailType.POWERED_POWER));
    public static final RegistryObject<Block> STATION_RAIL = registerBlock("station_rail", () -> new AdvancedPoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.ACTIVATOR_RAIL), AdvancedPoweredRailBlock.PoweredRailType.STATION));
    public static final RegistryObject<Block> BRAKE_RAIL = registerBlock("brake_rail", () -> new AdvancedPoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.ACTIVATOR_RAIL), AdvancedPoweredRailBlock.PoweredRailType.BRAKE));
    public static final RegistryObject<Block> POWERED_RAIL = VANILLA_BLOCKS.register("powered_rail", () -> new CustomPoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL), CustomPoweredRailBlock.PoweredRailType.POWER));
    public static final RegistryObject<Block> ACTIVATOR_RAIL = VANILLA_BLOCKS.register("activator_rail", () -> new CustomPoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.ACTIVATOR_RAIL), CustomPoweredRailBlock.PoweredRailType.ACTIVATOR));
    public static final RegistryObject<Block> DETECTOR_RAIL = VANILLA_BLOCKS.register("detector_rail", () -> new CustomDetectorRailBlock(BlockBehaviour.Properties.copy(Blocks.DETECTOR_RAIL)));
    public static final RegistryObject<Block> RAIL = VANILLA_BLOCKS.register("rail", () -> new CustomRailBlock(BlockBehaviour.Properties.copy(Blocks.RAIL)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        VANILLA_BLOCKS.register(eventBus);
    }
}
