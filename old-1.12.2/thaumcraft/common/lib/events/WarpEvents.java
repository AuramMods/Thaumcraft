package thaumcraft.common.lib.events;

import baubles.api.BaublesApi;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.potions.PotionBlurredVision;
import thaumcraft.common.lib.potions.PotionDeathGaze;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;
import thaumcraft.common.lib.potions.PotionSunScorned;
import thaumcraft.common.lib.potions.PotionThaumarhia;
import thaumcraft.common.lib.potions.PotionUnnaturalHunger;
import thaumcraft.common.lib.utils.EntityUtils;

public class WarpEvents {
   public static void checkWarpEvent(EntityPlayer player) {
      int tw = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
      int nw = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL);
      int pw = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT);
      int warp = tw + nw + pw;
      int actualwarp = pw + nw;
      int gearWarp = getWarpFromGear(player);
      warp += gearWarp;
      int warpCounter = ThaumcraftCapabilities.getWarp(player).getCounter();
      int r = player.field_70170_p.field_73012_v.nextInt(100);
      if (warpCounter > 0 && warp > 0 && (double)r <= Math.sqrt((double)warpCounter)) {
         warp = Math.min(100, (warp + warp + warpCounter) / 3);
         warpCounter = (int)((double)warpCounter - Math.max(5.0D, Math.sqrt((double)warpCounter) * 2.0D - (double)(gearWarp * 2)));
         ThaumcraftCapabilities.getWarp(player).setCounter(warpCounter);
         int eff = player.field_70170_p.field_73012_v.nextInt(warp);
         ItemStack helm = player.field_71071_by.field_70460_b[3];
         if (helm != null && helm.func_77973_b() instanceof ItemFortressArmor && helm.func_77942_o() && helm.func_77978_p().func_74764_b("mask") && helm.func_77978_p().func_74762_e("mask") == 0) {
            eff -= 2 + player.field_70170_p.field_73012_v.nextInt(4);
         }

         PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte)0), (EntityPlayerMP)player);
         if (eff > 0) {
            if (eff <= 4) {
               if (!Config.nostress) {
                  player.func_184185_a(SoundEvents.field_187572_ar, 1.0F, 0.5F);
               }
            } else if (eff <= 8) {
               if (!Config.nostress) {
                  player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t + (double)((player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 10.0F), player.field_70163_u + (double)((player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 10.0F), player.field_70161_v + (double)((player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 10.0F), SoundEvents.field_187539_bB, SoundCategory.AMBIENT, 4.0F, (1.0F + (player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 0.2F) * 0.7F);
               }
            } else if (eff <= 12) {
               player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.11")));
            } else {
               PotionEffect pe;
               if (eff <= 16) {
                  pe = new PotionEffect(PotionVisExhaust.instance, 5000, Math.min(3, warp / 15), true, true);
                  pe.getCurativeItems().clear();

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var22) {
                     var22.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.1")));
               } else if (eff <= 20) {
                  pe = new PotionEffect(PotionThaumarhia.instance, Math.min(32000, 10 * warp), 0, true, true);
                  pe.getCurativeItems().clear();

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var21) {
                     var21.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.15")));
               } else if (eff <= 24) {
                  pe = new PotionEffect(PotionUnnaturalHunger.instance, 5000, Math.min(3, warp / 15), true, true);
                  pe.getCurativeItems().clear();
                  pe.addCurativeItem(new ItemStack(Items.field_151078_bh));
                  pe.addCurativeItem(new ItemStack(ItemsTC.brain));

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var20) {
                     var20.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.2")));
               } else if (eff <= 28) {
                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.12")));
               } else if (eff <= 32) {
                  spawnMist(player, warp, 1);
               } else if (eff <= 36) {
                  try {
                     player.func_70690_d(new PotionEffect(PotionBlurredVision.instance, Math.min(32000, 10 * warp), 0, true, true));
                  } catch (Exception var19) {
                     var19.printStackTrace();
                  }
               } else if (eff <= 40) {
                  pe = new PotionEffect(PotionSunScorned.instance, 5000, Math.min(3, warp / 15), true, true);
                  pe.getCurativeItems().clear();

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var18) {
                     var18.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.5")));
               } else if (eff <= 44) {
                  try {
                     player.func_70690_d(new PotionEffect(MobEffects.field_76419_f, 1200, Math.min(3, warp / 15), true, true));
                  } catch (Exception var17) {
                     var17.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.9")));
               } else if (eff <= 48) {
                  pe = new PotionEffect(PotionInfectiousVisExhaust.instance, 6000, Math.min(3, warp / 15));
                  pe.getCurativeItems().clear();

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var16) {
                     var16.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.1")));
               } else if (eff <= 52) {
                  player.func_70690_d(new PotionEffect(MobEffects.field_76439_r, Math.min(40 * warp, 6000), 0, true, true));
                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.10")));
               } else if (eff <= 56) {
                  pe = new PotionEffect(PotionDeathGaze.instance, 6000, Math.min(3, warp / 15), true, true);
                  pe.getCurativeItems().clear();

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var15) {
                     var15.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.4")));
               } else if (eff <= 60) {
                  suddenlySpiders(player, warp, false);
               } else if (eff <= 64) {
                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.13")));
               } else if (eff <= 68) {
                  spawnMist(player, warp, warp / 30);
               } else if (eff <= 72) {
                  try {
                     player.func_70690_d(new PotionEffect(MobEffects.field_76440_q, Math.min(32000, 5 * warp), 0, true, true));
                  } catch (Exception var14) {
                     var14.printStackTrace();
                  }
               } else if (eff == 76) {
                  if (nw > 0) {
                     ThaumcraftApi.internalMethods.addWarpToPlayer(player, -1, IPlayerWarp.EnumWarpType.NORMAL);
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.14")));
               } else if (eff <= 80) {
                  pe = new PotionEffect(PotionUnnaturalHunger.instance, 6000, Math.min(3, warp / 15), true, true);
                  pe.getCurativeItems().clear();
                  pe.addCurativeItem(new ItemStack(Items.field_151078_bh));
                  pe.addCurativeItem(new ItemStack(ItemsTC.brain));

                  try {
                     player.func_70690_d(pe);
                  } catch (Exception var13) {
                     var13.printStackTrace();
                  }

                  player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.2")));
               } else if (eff <= 88) {
                  spawnPortal(player);
               } else if (eff <= 92) {
                  suddenlySpiders(player, warp, true);
               } else {
                  spawnMist(player, warp, warp / 15);
               }
            }
         }

         if (actualwarp > 10 && !ThaumcraftCapabilities.knowsResearch(player, "BATHSALTS") && !ThaumcraftCapabilities.knowsResearch(player, "!BATHSALTS")) {
            player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.8")));
            ThaumcraftApi.internalMethods.completeResearch(player, "!BATHSALTS");
         }

         if (actualwarp > 25 && !ThaumcraftCapabilities.knowsResearch(player, "ELDRITCHMINOR")) {
            ThaumcraftApi.internalMethods.completeResearch(player, "ELDRITCHMINOR");
         }

         if (actualwarp > 50 && !ThaumcraftCapabilities.knowsResearch(player, "ELDRITCHMAJOR")) {
            ThaumcraftApi.internalMethods.completeResearch(player, "ELDRITCHMAJOR");
         }
      }

      ThaumcraftApi.internalMethods.addWarpToPlayer(player, -1, IPlayerWarp.EnumWarpType.TEMPORARY);
   }

   private static void spawnMist(EntityPlayer player, int warp, int guardian) {
      PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte)1), (EntityPlayerMP)player);
      if (guardian > 0) {
         guardian = Math.min(8, guardian);

         for(int a = 0; a < guardian; ++a) {
            spawnGuardian(player);
         }
      }

      player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.6")));
   }

   private static void spawnPortal(EntityPlayer player) {
      EntityCultistPortalLesser eg = new EntityCultistPortalLesser(player.field_70170_p);
      int i = MathHelper.func_76128_c(player.field_70165_t);
      int j = MathHelper.func_76128_c(player.field_70163_u);
      int k = MathHelper.func_76128_c(player.field_70161_v);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         int j1 = j + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         int k1 = k + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         eg.func_70107_b((double)i1 + 0.5D, (double)j1 + 1.0D, (double)k1 + 0.5D);
         if (player.field_70170_p.func_180495_p(new BlockPos(i1, j1 - 1, k1)).func_185896_q() && player.field_70170_p.func_72855_b(eg.func_174813_aQ()) && player.field_70170_p.func_147461_a(eg.func_174813_aQ()).isEmpty() && !player.field_70170_p.func_72953_d(eg.func_174813_aQ())) {
            eg.func_180482_a(player.field_70170_p.func_175649_E(new BlockPos(eg)), (IEntityLivingData)null);
            player.field_70170_p.func_72838_d(eg);
            player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.16")));
            break;
         }
      }

   }

   private static void spawnGuardian(EntityPlayer player) {
      EntityEldritchGuardian eg = new EntityEldritchGuardian(player.field_70170_p);
      int i = MathHelper.func_76128_c(player.field_70165_t);
      int j = MathHelper.func_76128_c(player.field_70163_u);
      int k = MathHelper.func_76128_c(player.field_70161_v);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         int j1 = j + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         int k1 = k + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
         if (player.field_70170_p.func_180495_p(new BlockPos(i1, j1 - 1, k1)).func_185896_q()) {
            eg.func_70107_b((double)i1, (double)j1, (double)k1);
            if (player.field_70170_p.func_72855_b(eg.func_174813_aQ()) && player.field_70170_p.func_147461_a(eg.func_174813_aQ()).isEmpty() && !player.field_70170_p.func_72953_d(eg.func_174813_aQ())) {
               eg.func_70624_b(player);
               player.field_70170_p.func_72838_d(eg);
               break;
            }
         }
      }

   }

   private static void suddenlySpiders(EntityPlayer player, int warp, boolean real) {
      int spawns = Math.min(50, warp);

      for(int a = 0; a < spawns; ++a) {
         EntityMindSpider spider = new EntityMindSpider(player.field_70170_p);
         int i = MathHelper.func_76128_c(player.field_70165_t);
         int j = MathHelper.func_76128_c(player.field_70163_u);
         int k = MathHelper.func_76128_c(player.field_70161_v);
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
            int j1 = j + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
            int k1 = k + MathHelper.func_76136_a(player.field_70170_p.field_73012_v, 7, 24) * MathHelper.func_76136_a(player.field_70170_p.field_73012_v, -1, 1);
            if (player.field_70170_p.func_180495_p(new BlockPos(i1, j1 - 1, k1)).func_185896_q()) {
               spider.func_70107_b((double)i1, (double)j1, (double)k1);
               if (player.field_70170_p.func_72855_b(spider.func_174813_aQ()) && player.field_70170_p.func_147461_a(spider.func_174813_aQ()).isEmpty() && !player.field_70170_p.func_72953_d(spider.func_174813_aQ())) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.func_70624_b(player);
            if (!real) {
               spider.setViewer(player.func_70005_c_());
               spider.setHarmless(true);
            }

            player.field_70170_p.func_72838_d(spider);
         }
      }

      player.func_145747_a(new TextComponentString("§5§o" + I18n.func_74838_a("warp.text.7")));
   }

   public static void checkDeathGaze(EntityPlayer player) {
      PotionEffect pe = player.func_70660_b(PotionDeathGaze.instance);
      if (pe != null) {
         int level = pe.func_76458_c();
         int range = Math.min(8 + level * 3, 24);
         List list = player.field_70170_p.func_72839_b(player, player.func_174813_aQ().func_72314_b((double)range, (double)range, (double)range));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (entity.func_70067_L() && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70089_S() && EntityUtils.isVisibleTo(0.75F, player, entity, (float)range) && entity != null && player.func_70685_l(entity) && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W()) && !((EntityLivingBase)entity).func_70644_a(MobEffects.field_82731_v)) {
               ((EntityLivingBase)entity).func_70604_c(player);
               ((EntityLivingBase)entity).func_130011_c(player);
               if (entity instanceof EntityCreature) {
                  ((EntityCreature)entity).func_70624_b(player);
               }

               ((EntityLivingBase)entity).func_70690_d(new PotionEffect(MobEffects.field_82731_v, 80));
            }
         }

      }
   }

   private static int getWarpFromGear(EntityPlayer player) {
      int w = PlayerEvents.getFinalWarp(player.func_184614_ca(), player);

      for(int a = 0; a < 4; ++a) {
         w += PlayerEvents.getFinalWarp(player.field_71071_by.field_70460_b[a], player);
      }

      IInventory baubles = BaublesApi.getBaubles(player);

      for(int a = 0; a < baubles.func_70302_i_(); ++a) {
         w += PlayerEvents.getFinalWarp(baubles.func_70301_a(a), player);
      }

      return w;
   }
}
