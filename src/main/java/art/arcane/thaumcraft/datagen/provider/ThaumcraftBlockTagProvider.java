package art.arcane.thaumcraft.datagen.provider;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ThaumcraftBlockTagProvider extends BlockTagsProvider {

    public ThaumcraftBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Thaumcraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Phase 1+ will map block tags from migrated content.
    }
}
