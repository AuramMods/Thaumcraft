package art.arcane.thaumcraft.datagen;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.datagen.provider.ThaumcraftBlockStateProvider;
import art.arcane.thaumcraft.datagen.provider.ThaumcraftBlockTagProvider;
import art.arcane.thaumcraft.datagen.provider.ThaumcraftItemModelProvider;
import art.arcane.thaumcraft.datagen.provider.ThaumcraftLanguageProvider;
import art.arcane.thaumcraft.datagen.provider.ThaumcraftRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ThaumcraftDataGenerators {

    private ThaumcraftDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new ThaumcraftBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ThaumcraftItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ThaumcraftLanguageProvider(output, "en_us"));

        ThaumcraftBlockTagProvider blockTagProvider = new ThaumcraftBlockTagProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagProvider);
        generator.addProvider(event.includeServer(), new ThaumcraftRecipeProvider(output));
    }
}
