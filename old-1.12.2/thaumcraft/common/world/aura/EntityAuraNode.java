package thaumcraft.common.world.aura;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.world.aura.nodetypes.NodeType;

public class EntityAuraNode extends Entity {
   public int tickCounter = -1;
   int checkDelay = -1;
   List<EntityAuraNode> neighbours = null;
   public boolean stablized = false;
   private static final DataParameter<Byte> TYPE;
   private static final DataParameter<Integer> SIZE;
   private static final DataParameter<String> ASPECT;

   public EntityAuraNode(World worldIn) {
      super(worldIn);
      this.func_70105_a(0.5F, 0.5F);
      this.field_70178_ae = true;
      this.field_70145_X = true;
   }

   public void func_70071_h_() {
      if (this.getNodeSize() == 0 || this.getAspect() == null) {
         this.randomizeNode();
      }

      if (this.tickCounter < 0) {
         if (this.field_70170_p.field_72995_K) {
            func_184227_b(4.0D);
         }

         this.tickCounter = this.field_70146_Z.nextInt(200);
      }

      this.field_70170_p.field_72984_F.func_76320_a("entityBaseTick");
      this.field_70169_q = this.field_70165_t;
      this.field_70167_r = this.field_70163_u;
      this.field_70166_s = this.field_70161_v;
      this.tickCounter += this.getNodeSize();
      if (this.tickCounter > 1000) {
         boolean a = NodeType.nodeTypes[this.getNodeType()].performDischargeEvent(this);
         this.tickCounter -= 1000;
      }

      this.checkAdjacentNodes();
      if (this.field_70159_w != 0.0D || this.field_70181_x != 0.0D || this.field_70179_y != 0.0D) {
         this.field_70159_w *= 0.8D;
         this.field_70181_x *= 0.8D;
         this.field_70179_y *= 0.8D;
         super.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
      }

      this.field_70170_p.field_72984_F.func_76319_b();
   }

   private void checkAdjacentNodes() {
      if (this.neighbours == null || this.checkDelay < this.field_70173_aa) {
         this.neighbours = EntityUtils.getEntitiesInRange(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this, EntityAuraNode.class, 32.0D);
         this.checkDelay = this.field_70173_aa + 750;
      }

      if (!this.stablized) {
         try {
            Iterator i = this.neighbours.iterator();

            while(true) {
               while(true) {
                  EntityAuraNode an;
                  label51:
                  do {
                     while(i.hasNext()) {
                        an = (EntityAuraNode)i.next();
                        if (an != null && !an.field_70128_L) {
                           continue label51;
                        }

                        i.remove();
                     }

                     return;
                  } while(an.stablized);

                  double xd = this.field_70165_t - an.field_70165_t;
                  double yd = this.field_70163_u - an.field_70163_u;
                  double zd = this.field_70161_v - an.field_70161_v;
                  double d = xd * xd + yd * yd + zd * zd;
                  if (d < (double)(this.getNodeSize() + an.getNodeSize()) && d > 0.1D) {
                     float tq = (float)(this.getNodeSize() + an.getNodeSize()) * 1.5F;
                     float xm = (float)(-xd / d / (double)tq * ((double)an.getNodeSize() / 50.0D));
                     float ym = (float)(-yd / d / (double)tq * ((double)an.getNodeSize() / 50.0D));
                     float zm = (float)(-zd / d / (double)tq * ((double)an.getNodeSize() / 50.0D));
                     this.field_70159_w += (double)xm;
                     this.field_70181_x += (double)ym;
                     this.field_70179_y += (double)zm;
                  } else if (d <= 0.1D && this.getNodeSize() >= an.getNodeSize() && !this.field_70170_p.field_72995_K) {
                     int bonus = (int)Math.sqrt((double)an.getNodeSize());
                     int n = this.getNodeSize() + bonus;
                     this.setNodeSize(n);
                     if ((double)this.field_70146_Z.nextInt(100) < Math.sqrt((double)an.getNodeSize())) {
                        Aspect a = AspectHelper.getCombinationResult(this.getAspect(), an.getAspect());
                        if (a != null) {
                           this.setAspectTag(a.getTag());
                        }
                     }

                     if (this.getNodeType() == 0 && an.getNodeType() != 0 && this.field_70146_Z.nextInt(3) == 0 || this.getNodeType() != 0 && an.getNodeType() != 0 && (double)this.field_70146_Z.nextInt(100) < Math.sqrt((double)(an.getNodeSize() / 2))) {
                        this.setNodeType(an.getNodeType());
                     }

                     an.func_70106_y();
                  }
               }
            }
         } catch (Exception var15) {
            var15.printStackTrace();
         }
      }

   }

   public int getNodeSize() {
      return (Integer)this.func_184212_Q().func_187225_a(SIZE);
   }

   public void setNodeSize(int p) {
      this.func_184212_Q().func_187227_b(SIZE, p);
   }

   public String getAspectTag() {
      return (String)this.func_184212_Q().func_187225_a(ASPECT);
   }

   public Aspect getAspect() {
      return Aspect.getAspect(this.getAspectTag());
   }

   public void setAspectTag(String t) {
      this.func_184212_Q().func_187227_b(ASPECT, String.valueOf(t));
   }

   public int getNodeType() {
      return (Byte)this.func_184212_Q().func_187225_a(TYPE);
   }

   public void setNodeType(int p) {
      this.func_184212_Q().func_187227_b(TYPE, (byte)MathHelper.func_76125_a(p, 0, NodeType.nodeTypes.length - 1));
   }

   protected void func_70088_a() {
      this.func_184212_Q().func_187214_a(TYPE, (byte)0);
      this.func_184212_Q().func_187214_a(ASPECT, String.valueOf(""));
      this.func_184212_Q().func_187214_a(SIZE, 0);
   }

   public void randomizeNode() {
      this.setNodeSize(10 + this.field_70146_Z.nextInt(30));
      if (this.field_70146_Z.nextInt(Config.specialNodeRarity) == 0 && NodeType.nodeTypes.length > 1) {
         this.setNodeType(1 + this.field_70146_Z.nextInt(NodeType.nodeTypes.length - 1));
         if ((this.getNodeType() == 2 || this.getNodeType() == 4) && (double)this.field_70146_Z.nextFloat() < 0.33D) {
            this.setNodeType(0);
         }
      } else {
         this.setNodeType(0);
      }

      ArrayList<Aspect> al = Aspect.getPrimalAspects();
      if (this.field_70146_Z.nextInt(20) == 0) {
         al = Aspect.getCompoundAspects();
      }

      this.setAspectTag(((Aspect)al.get(this.field_70146_Z.nextInt(al.size()))).getTag());
   }

   public boolean func_96092_aw() {
      return false;
   }

   public boolean func_180427_aV() {
      return true;
   }

   public boolean func_85031_j(Entity entityIn) {
      return false;
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      return false;
   }

   public void func_70024_g(double x, double y, double z) {
   }

   public void func_70091_d(double x, double y, double z) {
   }

   protected boolean func_142008_O() {
      return false;
   }

   private void onBroken(Entity entity) {
   }

   protected void func_70037_a(NBTTagCompound tagCompound) {
      this.setNodeSize(tagCompound.func_74762_e("size"));
      this.setNodeType(tagCompound.func_74771_c("type"));
      this.setAspectTag(tagCompound.func_74779_i("aspect"));
   }

   protected void func_70014_b(NBTTagCompound tagCompound) {
      tagCompound.func_74768_a("size", this.getNodeSize());
      tagCompound.func_74774_a("type", (byte)this.getNodeType());
      tagCompound.func_74778_a("aspect", this.getAspectTag());
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
      this.func_70107_b(x, y, z);
      this.func_70101_b(yaw, pitch);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_90999_ad() {
      return false;
   }

   static {
      TYPE = EntityDataManager.func_187226_a(EntityAuraNode.class, DataSerializers.field_187191_a);
      SIZE = EntityDataManager.func_187226_a(EntityAuraNode.class, DataSerializers.field_187192_b);
      ASPECT = EntityDataManager.func_187226_a(EntityAuraNode.class, DataSerializers.field_187194_d);
   }
}
