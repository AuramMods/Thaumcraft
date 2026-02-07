package thaumcraft.client.renderers.block;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.property.IExtendedBlockState;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;

public class TaintFibreModel implements IBakedModel {
   static final ResourceLocation FIBRE_MODEL = new ResourceLocation("thaumcraft:block/taint_fibre");
   static final ResourceLocation GROWTH_MODEL_1 = new ResourceLocation("thaumcraft:block/taint_growth_1");
   static final ResourceLocation GROWTH_MODEL_2 = new ResourceLocation("thaumcraft:block/taint_growth_2");
   static final ResourceLocation GROWTH_MODEL_3 = new ResourceLocation("thaumcraft:block/taint_growth_3");
   static final ResourceLocation GROWTH_MODEL_4 = new ResourceLocation("thaumcraft:block/taint_growth_4");
   final TextureAtlasSprite fibreTexture;

   public TaintFibreModel(TextureAtlasSprite fibreTexture) {
      this.fibreTexture = fibreTexture;
   }

   public List<BakedQuad> func_188616_a(IBlockState state, EnumFacing side, long rand) {
      if (state instanceof IExtendedBlockState) {
         try {
            IExtendedBlockState es = (IExtendedBlockState)state;
            List<BakedQuad> ret = new ArrayList();
            IModel model_feature = null;
            IModel model_fibre = ModelLoaderRegistry.getModel(FIBRE_MODEL);
            Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
               public TextureAtlasSprite apply(ResourceLocation location) {
                  return Minecraft.func_71410_x().func_147117_R().func_110572_b(location.toString());
               }
            };
            IBakedModel bm;
            List list;
            Iterator var12;
            BakedQuad bq;
            if (!(Boolean)es.getValue(BlockTaintFibre.UP) || !(Boolean)es.getValue(BlockTaintFibre.DOWN) || !(Boolean)es.getValue(BlockTaintFibre.EAST) || !(Boolean)es.getValue(BlockTaintFibre.WEST) || !(Boolean)es.getValue(BlockTaintFibre.NORTH) || !(Boolean)es.getValue(BlockTaintFibre.SOUTH)) {
               if ((Boolean)es.getValue(BlockTaintFibre.UP)) {
                  bm = model_fibre.bake(ModelRotation.X180_Y0, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }

               if ((Boolean)es.getValue(BlockTaintFibre.DOWN)) {
                  bm = model_fibre.bake(model_fibre.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }

               if ((Boolean)es.getValue(BlockTaintFibre.EAST)) {
                  bm = model_fibre.bake(ModelRotation.X270_Y90, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }

               if ((Boolean)es.getValue(BlockTaintFibre.WEST)) {
                  bm = model_fibre.bake(ModelRotation.X90_Y90, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }

               if ((Boolean)es.getValue(BlockTaintFibre.NORTH)) {
                  bm = model_fibre.bake(ModelRotation.X90_Y180, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }

               if ((Boolean)es.getValue(BlockTaintFibre.SOUTH)) {
                  bm = model_fibre.bake(ModelRotation.X90_Y0, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
                  list = bm.func_188616_a(state, side, rand);
                  var12 = list.iterator();

                  while(var12.hasNext()) {
                     bq = (BakedQuad)var12.next();
                     ret.add(bq);
                  }
               }
            }

            switch((Integer)es.getValue(BlockTaintFibre.GROWTH)) {
            case 1:
               model_feature = ModelLoaderRegistry.getModel(GROWTH_MODEL_1);
               break;
            case 2:
               model_feature = ModelLoaderRegistry.getModel(GROWTH_MODEL_2);
               break;
            case 3:
               model_feature = ModelLoaderRegistry.getModel(GROWTH_MODEL_3);
               break;
            case 4:
               model_feature = ModelLoaderRegistry.getModel(GROWTH_MODEL_4);
            }

            if (model_feature != null) {
               bm = model_feature.bake(model_feature.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
               list = bm.func_188616_a(state, side, rand);
               var12 = list.iterator();

               while(var12.hasNext()) {
                  bq = (BakedQuad)var12.next();
                  ret.add(bq);
               }
            }

            return ret;
         } catch (Exception var14) {
         }
      }

      return ImmutableList.of();
   }

   public boolean func_177555_b() {
      return true;
   }

   public boolean func_177556_c() {
      return false;
   }

   public boolean func_188618_c() {
      return false;
   }

   public TextureAtlasSprite func_177554_e() {
      return this.fibreTexture;
   }

   public ItemCameraTransforms func_177552_f() {
      return ItemCameraTransforms.field_178357_a;
   }

   public ItemOverrideList func_188617_f() {
      return ItemOverrideList.field_188022_a;
   }
}
