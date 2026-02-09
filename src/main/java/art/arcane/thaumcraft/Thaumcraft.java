package art.arcane.thaumcraft;

import com.mojang.logging.LogUtils;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModCreativeTabs;
import art.arcane.thaumcraft.common.registry.ModItems;
import art.arcane.thaumcraft.common.registry.ModMenus;
import art.arcane.thaumcraft.common.registry.ModMobEffects;
import art.arcane.thaumcraft.common.registry.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Thaumcraft.MODID)
public class Thaumcraft {

    public static final String MODID = "thaumcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Thaumcraft(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModMobEffects.register(modEventBus);

        LOGGER.info("Thaumcraft bootstrap initialized");
    }
}
