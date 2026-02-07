package thaumcraft.common.entities.projectile;

import io.netty.buffer.ByteBuf;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.lib.utils.Utils;

public class EntityGenericProjectile extends EntityThrowable implements IEntityAdditionalSpawnData {
   FocusCore core;
   ItemStack casterStack;
   float charge = 1.0F;

   public EntityGenericProjectile(World par1World) {
      super(par1World);
   }

   public EntityGenericProjectile(World par1World, EntityLivingBase p, ItemStack s, float speed, float scatter, FocusCore core, float charge, boolean mainhand) {
      super(par1World, p);
      this.core = core;
      this.func_184538_a(p, p.field_70125_A, p.field_70177_z, -1.0F, speed, scatter);
      this.field_70165_t += (double)(-MathHelper.func_76134_b((p.field_70177_z - 0.5F) / 180.0F * 3.141593F) * 0.2F * (float)(mainhand ? 1 : -1));
      this.field_70163_u += -0.4000000059604645D;
      this.field_70161_v += (double)(-MathHelper.func_76126_a((p.field_70177_z - 0.5F) / 180.0F * 3.141593F) * 0.3F * (float)(mainhand ? 1 : -1));
      this.casterStack = s;
      this.charge = charge;
   }

   protected float func_70185_h() {
      return 0.01F;
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeFloat(this.charge);
      Utils.writeNBTTagCompoundToBuffer(data, this.core.serialize());
      if (this.casterStack != null) {
         Utils.writeNBTTagCompoundToBuffer(data, this.casterStack.serializeNBT());
      }

   }

   public void readSpawnData(ByteBuf data) {
      this.charge = data.readFloat();
      this.core = new FocusCore();
      this.core.deserialize(Utils.readNBTTagCompoundFromBuffer(data));

      try {
         this.casterStack = ItemStack.func_77949_a(Utils.readNBTTagCompoundFromBuffer(data));
      } catch (Exception var3) {
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74782_a("core", this.core.serialize());
      if (this.casterStack != null) {
         nbt.func_74782_a("casterstack", this.casterStack.serializeNBT());
      }

      nbt.func_74776_a("charge", this.charge);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.charge = nbt.func_74760_g("charge");
      this.core = new FocusCore();
      this.core.deserialize(nbt.func_74775_l("core"));
      this.casterStack = ItemStack.func_77949_a(nbt.func_74775_l("casterstack"));
   }

   protected void func_70184_a(RayTraceResult mop) {
      if (mop != null) {
         boolean unhurt = mop.field_72308_g == null || mop.field_72308_g.field_70172_ad <= 0;
         int resetHurt = 0;
         FocusCore.FocusEffect[] var4 = this.core.effects;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            FocusCore.FocusEffect ef = var4[var6];
            if (mop.field_72308_g != null && unhurt && mop.field_72308_g.field_70172_ad > 0) {
               resetHurt = mop.field_72308_g.field_70172_ad;
               mop.field_72308_g.field_70172_ad = 0;
            }

            ef.effect.onEffectTrigger(this.field_70170_p, mop, this.func_85052_h(), this.casterStack, this, ef, this.charge);
         }

         if (!this.field_70170_p.field_72995_K) {
            CasterManager.sendImpact(this.field_70170_p, mop.field_72307_f.field_72450_a, Math.max(mop.field_72307_f.field_72448_b, this.field_70163_u), mop.field_72307_f.field_72449_c, this.core);
         }

         if (resetHurt > 0) {
            mop.field_72308_g.field_70172_ad = resetHurt;
         }

         if (!this.field_70170_p.field_72995_K) {
            this.func_70106_y();
         }
      }

   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70173_aa > 1200) {
         this.func_70106_y();
      }

      if (this.field_70170_p.field_72995_K) {
         for(double i = 0.0D; i < 3.0D; ++i) {
            FocusCore.FocusEffect eff = this.core.effects[this.field_70146_Z.nextInt(this.core.effects.length)];
            float scale = 1.0F * eff.effectMultipler * this.charge;
            Color c1 = new Color(eff.effect.getGemColor());
            Color c2 = new Color(eff.effect.getIconColor());
            double coeff = i / 3.0D;
            FXDispatcher.INSTANCE.drawFireMote((float)(this.field_70169_q + (this.field_70165_t - this.field_70169_q) * coeff), (float)(this.field_70167_r + (this.field_70163_u - this.field_70167_r) * coeff) + this.field_70131_O / 2.0F, (float)(this.field_70166_s + (this.field_70161_v - this.field_70166_s) * coeff), 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F) * scale, 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F) * scale, 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F) * scale, (float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, 0.5F, 4.0F * scale);
            if (i == 0.0D) {
               ArrayList<String> hp = new ArrayList();
               Iterator var10 = this.core.partsRaw.keySet().iterator();

               while(var10.hasNext()) {
                  String p = (String)var10.next();
                  if (((IFocusPart)this.core.partsRaw.get(p)).hasCustomParticle()) {
                     hp.add(p);
                  }
               }

               if (hp.isEmpty()) {
                  FXDispatcher.INSTANCE.drawGenericParticles(this.field_70169_q + (this.field_70165_t - this.field_70169_q) * coeff + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70167_r + (this.field_70163_u - this.field_70167_r) * coeff + (double)(this.field_70131_O / 2.0F) + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70166_s + (this.field_70161_v - this.field_70166_s) * coeff + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D, (float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, 0.8F, false, 448, 8, 1, 20 + this.field_70146_Z.nextInt(10), 0, 0.3F * scale, 0.0F, 0);
               } else {
                  IFocusPart p = (IFocusPart)this.core.partsRaw.get(hp.get(this.field_70146_Z.nextInt(hp.size())));
                  if (p != null) {
                     p.drawCustomParticle(this.field_70170_p, this.field_70169_q + (this.field_70165_t - this.field_70169_q) * coeff + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70167_r + (this.field_70163_u - this.field_70167_r) * coeff + (double)(this.field_70131_O / 2.0F) + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70166_s + (this.field_70161_v - this.field_70166_s) * coeff + this.field_70170_p.field_73012_v.nextGaussian() * 0.10000000149011612D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D, this.field_70170_p.field_73012_v.nextGaussian() * 0.009999999776482582D);
                  }
               }
            }
         }
      }

   }

   public void func_70088_a() {
      super.func_70088_a();
   }
}
