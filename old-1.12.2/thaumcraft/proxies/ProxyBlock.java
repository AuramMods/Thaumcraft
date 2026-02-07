package thaumcraft.proxies;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.renderers.block.CrystalModel;
import thaumcraft.client.renderers.block.TaintFibreModel;
import thaumcraft.common.blocks.world.ore.ShardType;

public class ProxyBlock {
   static ModelResourceLocation[] crystals = new ModelResourceLocation[ShardType.values().length];
   static ModelResourceLocation[] jars = new ModelResourceLocation[4];
   static ModelResourceLocation[] jarsVoid = new ModelResourceLocation[4];
   static ModelResourceLocation fibres;
   private static ModelResourceLocation fluidGooLocation = new ModelResourceLocation("thaumcraft:flux_goo", "fluid");
   private static ModelResourceLocation taintDustLocation = new ModelResourceLocation("thaumcraft:taint_dust", "fluid");
   private static ModelResourceLocation fluidDeathLocation = new ModelResourceLocation("thaumcraft:liquid_death", "fluid");
   private static ModelResourceLocation fluidPureLocation = new ModelResourceLocation("thaumcraft:purifying_fluid", "fluid");

   public void setupBlocks() {
      MinecraftForge.EVENT_BUS.register(BlocksTC.levitator);
      MinecraftForge.EVENT_BUS.register(BlocksTC.tube);
      MinecraftForge.EVENT_BUS.register(BlocksTC.redstoneRelay);
      MinecraftForge.EVENT_BUS.register(BlocksTC.patternCrafter);

      for(int a = 0; a < ShardType.values().length; ++a) {
         crystals[a] = new ModelResourceLocation(GameData.getBlockRegistry().getNameForObject(ShardType.values()[a].getOre()), "normal");
         final ModelResourceLocation mrl = crystals[a];
         ModelLoader.setCustomStateMapper(ShardType.values()[a].getOre(), new StateMapperBase() {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_) {
               return mrl;
            }
         });
      }

      fibres = new ModelResourceLocation(GameData.getBlockRegistry().getNameForObject(BlocksTC.taintFibre), "normal");
      ModelLoader.setCustomStateMapper(BlocksTC.taintFibre, new StateMapperBase() {
         protected ModelResourceLocation func_178132_a(IBlockState state) {
            return ProxyBlock.fibres;
         }
      });
      ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(BlocksTC.banner), 1, new ModelResourceLocation(new ResourceLocation("thaumcraft:banner_cultist"), "inventory"));
      ModelBakery.registerItemVariants(Item.func_150898_a(BlocksTC.mirror), new ResourceLocation[]{new ResourceLocation("thaumcraft:mirror"), new ResourceLocation("thaumcraft:mirror_on")});
      ModelBakery.registerItemVariants(Item.func_150898_a(BlocksTC.mirrorEssentia), new ResourceLocation[]{new ResourceLocation("thaumcraft:mirror_essentia"), new ResourceLocation("thaumcraft:mirror_essentia_on")});
      ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(BlocksTC.mirror), 1, new ModelResourceLocation(new ResourceLocation("thaumcraft:mirror_on"), "inventory"));
      ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(BlocksTC.mirrorEssentia), 1, new ModelResourceLocation(new ResourceLocation("thaumcraft:mirror_essentia_on"), "inventory"));
      Item fluxGooItem = Item.func_150898_a(BlocksTC.fluxGoo);
      ModelBakery.registerItemVariants(fluxGooItem, new ResourceLocation[0]);
      ModelLoader.setCustomMeshDefinition(fluxGooItem, new ItemMeshDefinition() {
         public ModelResourceLocation func_178113_a(ItemStack stack) {
            return ProxyBlock.fluidGooLocation;
         }
      });
      ModelLoader.setCustomStateMapper(BlocksTC.fluxGoo, new StateMapperBase() {
         protected ModelResourceLocation func_178132_a(IBlockState state) {
            return ProxyBlock.fluidGooLocation;
         }
      });
      Item liquidDeathItem = Item.func_150898_a(BlocksTC.liquidDeath);
      ModelBakery.registerItemVariants(liquidDeathItem, new ResourceLocation[0]);
      ModelLoader.setCustomMeshDefinition(liquidDeathItem, new ItemMeshDefinition() {
         public ModelResourceLocation func_178113_a(ItemStack stack) {
            return ProxyBlock.fluidDeathLocation;
         }
      });
      ModelLoader.setCustomStateMapper(BlocksTC.liquidDeath, new StateMapperBase() {
         protected ModelResourceLocation func_178132_a(IBlockState state) {
            return ProxyBlock.fluidDeathLocation;
         }
      });
      Item purifyingFluidItem = Item.func_150898_a(BlocksTC.purifyingFluid);
      ModelBakery.registerItemVariants(purifyingFluidItem, new ResourceLocation[0]);
      ModelLoader.setCustomMeshDefinition(purifyingFluidItem, new ItemMeshDefinition() {
         public ModelResourceLocation func_178113_a(ItemStack stack) {
            return ProxyBlock.fluidPureLocation;
         }
      });
      ModelLoader.setCustomStateMapper(BlocksTC.purifyingFluid, new StateMapperBase() {
         protected ModelResourceLocation func_178132_a(IBlockState state) {
            return ProxyBlock.fluidPureLocation;
         }
      });
   }

   public static class BakeBlockEventHandler {
      public static final ProxyBlock.BakeBlockEventHandler INSTANCE = new ProxyBlock.BakeBlockEventHandler();

      private BakeBlockEventHandler() {
      }

      @SubscribeEvent
      public void onModelBakeEvent(ModelBakeEvent event) {
         TextureAtlasSprite crystalTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("thaumcraft:blocks/crystal");
         IBakedModel customCrystalModel = new CrystalModel(crystalTexture);

         for(int a = 0; a < ShardType.values().length; ++a) {
            event.getModelRegistry().func_82595_a(ProxyBlock.crystals[a], customCrystalModel);
         }

         event.getModelRegistry().func_82595_a(ProxyBlock.fibres, new TaintFibreModel(Minecraft.func_71410_x().func_147117_R().func_110572_b("thaumcraft:blocks/taint_fibres")));
      }
   }
}
