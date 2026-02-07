package thaumcraft.client.renderers.block;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import thaumcraft.client.lib.obj.MeshLoader;
import thaumcraft.client.lib.obj.MeshModel;
import thaumcraft.client.lib.obj.MeshPart;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.blocks.world.ore.BlockCrystal;

public class CrystalModel implements IBakedModel {
   ResourceLocation model = new ResourceLocation("thaumcraft", "models/obj/crystal.obj");
   MeshModel sourceMesh;
   TextureAtlasSprite tex;

   public CrystalModel(TextureAtlasSprite tex2) {
      this.tex = tex2;

      try {
         this.sourceMesh = (new MeshLoader()).loadFromResource(this.model);

         MeshPart mp;
         for(Iterator var2 = this.sourceMesh.parts.iterator(); var2.hasNext(); mp.tintIndex = 0) {
            mp = (MeshPart)var2.next();
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public List<BakedQuad> func_188616_a(IBlockState state, EnumFacing side, long rand) {
      if (side == null && state instanceof IExtendedBlockState) {
         List<BakedQuad> ret = new ArrayList();
         IExtendedBlockState es = (IExtendedBlockState)state;
         int m = ((BlockCrystal)state.func_177230_c()).getGrowth(state) + 1;
         MeshModel mm = this.sourceMesh.clone();
         if (!(Boolean)es.getValue(BlockCrystal.UP) || !(Boolean)es.getValue(BlockCrystal.DOWN) || !(Boolean)es.getValue(BlockCrystal.EAST) || !(Boolean)es.getValue(BlockCrystal.WEST) || !(Boolean)es.getValue(BlockCrystal.NORTH) || !(Boolean)es.getValue(BlockCrystal.SOUTH)) {
            List c;
            int a;
            Iterator var11;
            BakedQuad quad;
            MeshModel mod;
            if ((Boolean)es.getValue(BlockCrystal.UP)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               mod = mm.clone();
               mod.rotate(Math.toRadians(180.0D), new Vector3(1.0D, 0.0D, 0.0D), new Vector3(0.0D, 1.0D, 1.0D));
               var11 = mod.bakeModel(this.func_177554_e()).iterator();

               while(var11.hasNext()) {
                  quad = (BakedQuad)var11.next();
                  ret.add(quad);
               }
            }

            if ((Boolean)es.getValue(BlockCrystal.DOWN)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand + 5L));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               Iterator var15 = mm.bakeModel(this.func_177554_e()).iterator();

               while(var15.hasNext()) {
                  BakedQuad quad = (BakedQuad)var15.next();
                  ret.add(quad);
               }
            }

            if ((Boolean)es.getValue(BlockCrystal.EAST)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand + 10L));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               mod = mm.clone();
               mod.rotate(Math.toRadians(90.0D), new Vector3(1.0D, 0.0D, 0.0D), new Vector3(0.0D, 0.0D, 0.0D));
               mod.rotate(Math.toRadians(270.0D), new Vector3(0.0D, 1.0D, 0.0D), new Vector3(1.0D, 1.0D, 0.0D));
               var11 = mod.bakeModel(this.func_177554_e()).iterator();

               while(var11.hasNext()) {
                  quad = (BakedQuad)var11.next();
                  ret.add(quad);
               }
            }

            if ((Boolean)es.getValue(BlockCrystal.WEST)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand + 15L));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               mod = mm.clone();
               mod.rotate(Math.toRadians(90.0D), new Vector3(1.0D, 0.0D, 0.0D), new Vector3(0.0D, 0.0D, 0.0D));
               mod.rotate(Math.toRadians(90.0D), new Vector3(0.0D, 1.0D, 0.0D), new Vector3(0.0D, 1.0D, 1.0D));
               var11 = mod.bakeModel(this.func_177554_e()).iterator();

               while(var11.hasNext()) {
                  quad = (BakedQuad)var11.next();
                  ret.add(quad);
               }
            }

            if ((Boolean)es.getValue(BlockCrystal.NORTH)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand + 20L));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               mod = mm.clone();
               mod.rotate(Math.toRadians(90.0D), new Vector3(1.0D, 0.0D, 0.0D), new Vector3(0.0D, 1.0D, 0.0D));
               var11 = mod.bakeModel(this.func_177554_e()).iterator();

               while(var11.hasNext()) {
                  quad = (BakedQuad)var11.next();
                  ret.add(quad);
               }
            }

            if ((Boolean)es.getValue(BlockCrystal.SOUTH)) {
               c = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
               Collections.shuffle(c, new Random(rand + 25L));
               mm.parts.clear();

               for(a = 0; a < m; ++a) {
                  mm.parts.add(this.sourceMesh.parts.get((Integer)c.get(a)));
               }

               mod = mm.clone();
               mod.rotate(Math.toRadians(90.0D), new Vector3(1.0D, 0.0D, 0.0D), new Vector3(0.0D, 0.0D, 0.0D));
               mod.rotate(Math.toRadians(180.0D), new Vector3(0.0D, 1.0D, 0.0D), new Vector3(1.0D, 1.0D, 1.0D));
               var11 = mod.bakeModel(this.func_177554_e()).iterator();

               while(var11.hasNext()) {
                  quad = (BakedQuad)var11.next();
                  ret.add(quad);
               }
            }
         }

         return ret;
      } else {
         return ImmutableList.of();
      }
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
      return this.tex;
   }

   public ItemCameraTransforms func_177552_f() {
      return ItemCameraTransforms.field_178357_a;
   }

   public ItemOverrideList func_188617_f() {
      return ItemOverrideList.field_188022_a;
   }
}
