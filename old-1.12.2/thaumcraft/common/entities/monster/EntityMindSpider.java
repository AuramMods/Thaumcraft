package thaumcraft.common.entities.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import thaumcraft.api.entities.IEldritchMob;

public class EntityMindSpider extends EntitySpider implements IEldritchMob {
   private int lifeSpan = Integer.MAX_VALUE;
   private static final DataParameter<Boolean> HARMLESS;
   private static final DataParameter<String> VIEWER;

   public EntityMindSpider(World par1World) {
      super(par1World);
      this.func_70105_a(0.7F, 0.5F);
      this.field_70728_aV = 1;
   }

   @Nullable
   protected ResourceLocation func_184647_J() {
      return null;
   }

   public float func_70047_e() {
      return 0.45F;
   }

   protected int func_70693_a(EntityPlayer p_70693_1_) {
      return this.isHarmless() ? 0 : super.func_70693_a(p_70693_1_);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(1.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(1.0D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(HARMLESS, false);
      this.func_184212_Q().func_187214_a(VIEWER, String.valueOf(""));
   }

   public String getViewer() {
      return (String)this.func_184212_Q().func_187225_a(VIEWER);
   }

   public void setViewer(String player) {
      this.func_184212_Q().func_187227_b(VIEWER, String.valueOf(player));
   }

   public boolean isHarmless() {
      return (Boolean)this.func_184212_Q().func_187225_a(HARMLESS);
   }

   public void setHarmless(boolean h) {
      if (h) {
         this.lifeSpan = 1200;
      }

      this.func_184212_Q().func_187227_b(HARMLESS, h);
   }

   protected float func_70647_i() {
      return 0.7F;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K && this.field_70173_aa > this.lifeSpan) {
         this.func_70106_y();
      }

   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
   }

   public boolean func_145773_az() {
      return true;
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public boolean func_70652_k(Entity p_70652_1_) {
      return this.isHarmless() ? false : super.func_70652_k(p_70652_1_);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setHarmless(nbt.func_74767_n("harmless"));
      this.setViewer(nbt.func_74779_i("viewer"));
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74757_a("harmless", this.isHarmless());
      nbt.func_74778_a("viewer", this.getViewer());
   }

   public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
      return p_180482_2_;
   }

   static {
      HARMLESS = EntityDataManager.func_187226_a(EntityMindSpider.class, DataSerializers.field_187198_h);
      VIEWER = EntityDataManager.func_187226_a(EntityMindSpider.class, DataSerializers.field_187194_d);
   }
}
