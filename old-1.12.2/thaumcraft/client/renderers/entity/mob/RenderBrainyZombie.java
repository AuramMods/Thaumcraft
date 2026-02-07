package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;

@SideOnly(Side.CLIENT)
public class RenderBrainyZombie extends RenderZombie {
   private static final ResourceLocation field_110865_p = new ResourceLocation("thaumcraft", "textures/models/creature/bzombie.png");
   private static final ResourceLocation field_110864_q = new ResourceLocation("thaumcraft", "textures/models/creature/bzombievil.png");
   private ModelBiped field_82434_o;
   private ModelZombieVillager field_82432_p;
   private int field_82431_q = 1;

   public RenderBrainyZombie(RenderManager rm) {
      super(rm);
   }

   protected ResourceLocation func_110775_a(EntityZombie entity) {
      return entity.func_82231_m() ? field_110864_q : field_110865_p;
   }

   protected void preRenderScale(EntityGiantBrainyZombie z, float par2) {
      GL11.glScalef(1.0F + z.getAnger(), 1.0F + z.getAnger(), 1.0F + z.getAnger());
      float q = Math.min(1.0F, z.getAnger()) / 2.0F;
      GL11.glColor3f(1.0F, 1.0F - q, 1.0F - q);
   }

   protected void func_77041_b(EntityZombie par1EntityLiving, float par2) {
      if (par1EntityLiving instanceof EntityGiantBrainyZombie) {
         this.preRenderScale((EntityGiantBrainyZombie)par1EntityLiving, par2);
      }

   }
}
