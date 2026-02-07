package thaumcraft.common.lib.events;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.items.consumables.ItemBathSalts;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.InventoryUtils;

public class EntityEvents {
   @SubscribeEvent
   public void itemExpire(ItemExpireEvent event) {
      if (event.getEntityItem().func_92059_d() != null && event.getEntityItem().func_92059_d().func_77973_b() != null && event.getEntityItem().func_92059_d().func_77973_b() instanceof ItemBathSalts) {
         BlockPos bp = new BlockPos(event.getEntityItem());
         IBlockState bs = event.getEntityItem().field_70170_p.func_180495_p(bp);
         if (bs.func_177230_c() == Blocks.field_150355_j && bs.func_177230_c().func_176201_c(bs) == 0) {
            event.getEntityItem().field_70170_p.func_175656_a(bp, BlocksTC.purifyingFluid.func_176223_P());
         }
      }

   }

   @SubscribeEvent
   public void livingTick(LivingUpdateEvent event) {
      if (event.getEntity() instanceof EntityMob && !event.getEntity().field_70128_L) {
         EntityMob mob = (EntityMob)event.getEntity();
         int t = (int)mob.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();

         try {
            if (t >= 0 && ChampionModifier.mods[t].type == 0) {
               ChampionModifier.mods[t].effect.performEffect(mob, (EntityLivingBase)null, (DamageSource)null, 0.0F);
            }
         } catch (Exception var5) {
            if (t >= ChampionModifier.mods.length) {
               mob.func_70106_y();
            }
         }
      }

   }

   @SubscribeEvent
   public void livingDeath(LivingDeathEvent event) {
   }

   @SubscribeEvent
   public void entityHurt(LivingHurtEvent event) {
      if (event.getSource().func_76347_k() && event.getEntity() instanceof EntityPlayer && ThaumcraftCapabilities.knowsResearchStrict((EntityPlayer)event.getEntity(), "BASETHAUMATURGY@2") && !ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), "f_onfire")) {
         IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge((EntityPlayer)event.getEntity());
         knowledge.addResearch("f_onfire");
         knowledge.sync((EntityPlayerMP)event.getEntity());
         ((EntityPlayer)event.getEntity()).func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.onfire")));
      }

      EntityPlayer player;
      if (event.getSource().func_76364_f() != null && event.getSource().func_76364_f() instanceof EntityPlayer) {
         player = (EntityPlayer)event.getSource().func_76364_f();
         ItemStack helm = player.field_71071_by.field_70460_b[3];
         if (helm != null && helm.func_77973_b() instanceof ItemFortressArmor && helm.func_77942_o() && helm.func_77978_p().func_74764_b("mask") && helm.func_77978_p().func_74762_e("mask") == 2 && player.field_70170_p.field_73012_v.nextFloat() < event.getAmount() / 12.0F) {
            player.func_70691_i(1.0F);
         }
      }

      if (event.getEntity() instanceof EntityPlayer) {
         player = (EntityPlayer)event.getEntity();
         if (event.getSource().func_76364_f() != null && event.getSource().func_76364_f() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase)event.getSource().func_76364_f();
            ItemStack helm = player.field_71071_by.field_70460_b[3];
            if (helm != null && helm.func_77973_b() instanceof ItemFortressArmor && helm.func_77942_o() && helm.func_77978_p().func_74764_b("mask") && helm.func_77978_p().func_74762_e("mask") == 1 && player.field_70170_p.field_73012_v.nextFloat() < event.getAmount() / 10.0F) {
               try {
                  attacker.func_70690_d(new PotionEffect(MobEffects.field_82731_v, 80));
               } catch (Exception var7) {
               }
            }
         }

         int charge = (int)player.func_110139_bj();
         if (charge > 0 && PlayerEvents.runicInfo.containsKey(player.func_145782_y()) && PlayerEvents.lastMaxCharge.containsKey(player.func_145782_y())) {
            long time = System.currentTimeMillis();
            int target = -1;
            if (event.getSource().func_76346_g() != null) {
               target = event.getSource().func_76346_g().func_145782_y();
            }

            if (event.getSource() == DamageSource.field_76379_h) {
               target = -2;
            }

            if (event.getSource() == DamageSource.field_82729_p) {
               target = -3;
            }

            PacketHandler.INSTANCE.sendToAllAround(new PacketFXShield(event.getEntity().func_145782_y(), target), new TargetPoint(event.getEntity().field_70170_p.field_73011_w.getDimension(), event.getEntity().field_70165_t, event.getEntity().field_70163_u, event.getEntity().field_70161_v, 32.0D));
         }
      } else if (event.getEntity() instanceof EntityMob) {
         IAttributeInstance cai = ((EntityMob)event.getEntity()).func_110148_a(EntityUtils.CHAMPION_MOD);
         EntityMob mob;
         int t;
         if (cai != null && cai.func_111126_e() >= 0.0D || event.getEntity() instanceof IEldritchMob) {
            mob = (EntityMob)event.getEntity();
            t = (int)cai.func_111126_e();
            if ((t == 5 || event.getEntity() instanceof IEldritchMob) && mob.func_110139_bj() > 0.0F) {
               int target = -1;
               if (event.getSource().func_76346_g() != null) {
                  target = event.getSource().func_76346_g().func_145782_y();
               }

               if (event.getSource() == DamageSource.field_76379_h) {
                  target = -2;
               }

               if (event.getSource() == DamageSource.field_82729_p) {
                  target = -3;
               }

               PacketHandler.INSTANCE.sendToAllAround(new PacketFXShield(mob.func_145782_y(), target), new TargetPoint(event.getEntity().field_70170_p.field_73011_w.getDimension(), event.getEntity().field_70165_t, event.getEntity().field_70163_u, event.getEntity().field_70161_v, 32.0D));
               event.getEntity().func_184185_a(SoundsTC.runicShieldCharge, 0.66F, 1.1F + event.getEntity().field_70170_p.field_73012_v.nextFloat() * 0.1F);
            } else if (t >= 0 && ChampionModifier.mods[t].type == 2 && event.getSource().func_76364_f() != null && event.getSource().func_76364_f() instanceof EntityLivingBase) {
               EntityLivingBase attacker = (EntityLivingBase)event.getSource().func_76364_f();
               event.setAmount(ChampionModifier.mods[t].effect.performEffect(mob, attacker, event.getSource(), event.getAmount()));
            }
         }

         if (event.getAmount() > 0.0F && event.getSource().func_76364_f() != null && event.getEntity() instanceof EntityLivingBase && event.getSource().func_76364_f() instanceof EntityMob && ((EntityMob)event.getSource().func_76364_f()).func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e() >= 0.0D) {
            mob = (EntityMob)event.getSource().func_76364_f();
            t = (int)mob.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();
            if (ChampionModifier.mods[t].type == 1) {
               event.setAmount(ChampionModifier.mods[t].effect.performEffect(mob, (EntityLivingBase)event.getEntity(), event.getSource(), event.getAmount()));
            }
         }
      }

   }

   @SubscribeEvent
   public void itemPickup(EntityItemPickupEvent event) {
      if (event.getEntityPlayer().func_70005_c_().startsWith("FakeThaumcraft")) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void entityConstuct(EntityConstructing event) {
      if (event.getEntity() instanceof EntityMob) {
         EntityMob mob = (EntityMob)event.getEntity();
         mob.func_110140_aT().func_111150_b(EntityUtils.CHAMPION_MOD).func_111128_a(-2.0D);
      }

   }

   @SubscribeEvent
   public void livingDrops(LivingDropsEvent event) {
      boolean fakeplayer = event.getSource().func_76346_g() != null && event.getSource().func_76346_g() instanceof FakePlayer;
      int c;
      if (!event.getEntity().field_70170_p.field_72995_K && event.isRecentlyHit() && !fakeplayer && event.getEntity() instanceof EntityMob && !(event.getEntity() instanceof EntityThaumcraftBoss) && ((EntityMob)event.getEntity()).func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e() >= 0.0D) {
         c = 5 + event.getEntity().field_70170_p.field_73012_v.nextInt(3);

         int lb;
         while(c > 0) {
            lb = EntityXPOrb.func_70527_a(c);
            c -= lb;
            event.getEntity().field_70170_p.func_72838_d(new EntityXPOrb(event.getEntity().field_70170_p, event.getEntity().field_70165_t, event.getEntity().field_70163_u, event.getEntity().field_70161_v, lb));
         }

         lb = Math.min(2, MathHelper.func_76141_d((float)(event.getEntity().field_70170_p.field_73012_v.nextInt(9) + event.getLootingLevel()) / 5.0F));
         event.getDrops().add(new EntityItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, new ItemStack(ItemsTC.lootBag, 1, lb)));
      }

      if (event.getEntityLiving() instanceof EntityZombie && !(event.getEntityLiving() instanceof EntityBrainyZombie) && event.isRecentlyHit() && event.getEntity().field_70170_p.field_73012_v.nextInt(10) - event.getLootingLevel() < 1) {
         event.getDrops().add(new EntityItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, new ItemStack(ItemsTC.brain)));
      }

      if (event.getEntityLiving() instanceof EntityCultist && !fakeplayer && event.getSource().func_76346_g() != null && event.getSource().func_76346_g() instanceof EntityPlayer) {
         c = !ThaumcraftCapabilities.getKnowledge((EntityPlayer)event.getSource().func_76346_g()).isResearchKnown("!CrimsonCultist@2") ? 4 : 50;
         if (InventoryUtils.isPlayerCarrying((EntityPlayer)event.getSource().func_76346_g(), new ItemStack(ItemsTC.curio, 1, 6)) > 0) {
            c = 100;
         }

         if (event.getEntity().field_70170_p.field_73012_v.nextInt(c) == 0) {
            event.getDrops().add(new EntityItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, new ItemStack(ItemsTC.curio, 1, 6)));
         }
      }

      if (event.getSource() == DamageSourceThaumcraft.dissolve) {
         AspectList aspects = AspectHelper.getEntityAspects(event.getEntityLiving());
         if (aspects != null && aspects.size() > 0) {
            Aspect[] var11 = aspects.getAspects();
            int var5 = var11.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Aspect aspect = var11[var6];
               if (!event.getEntity().field_70170_p.field_73012_v.nextBoolean()) {
                  int size = 1 + event.getEntity().field_70170_p.field_73012_v.nextInt(aspects.getAmount(aspect));
                  size = Math.max(1, size / 2);
                  ItemStack stack = new ItemStack(ItemsTC.crystalEssence, size, 0);
                  ((ItemGenericEssentiaContainer)stack.func_77973_b()).setAspects(stack, (new AspectList()).add(aspect, 1));
                  event.getDrops().add(new EntityItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, stack));
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void entitySpawns(EntityJoinWorldEvent event) {
      if (!event.getWorld().field_72995_K && event.getEntity() instanceof EntityMob) {
         EntityMob mob = (EntityMob)event.getEntity();
         if (mob.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e() < -1.0D) {
            int c = event.getWorld().field_73012_v.nextInt(100);
            if (event.getWorld().func_175659_aa() == EnumDifficulty.EASY || !Config.championMobs) {
               c += 2;
            }

            if (event.getWorld().func_175659_aa() == EnumDifficulty.HARD) {
               c -= Config.championMobs ? 2 : 0;
            }

            if (event.getWorld().field_73011_w.getDimension() == Config.dimensionOuterId) {
               c -= 3;
            }

            Biome bg = mob.field_70170_p.func_180494_b(new BlockPos(mob));
            if (BiomeDictionary.isBiomeOfType(bg, Type.SPOOKY) || BiomeDictionary.isBiomeOfType(bg, Type.NETHER) || BiomeDictionary.isBiomeOfType(bg, Type.END)) {
               c -= Config.championMobs ? 2 : 1;
            }

            if (this.isDangerousLocation(mob.field_70170_p, MathHelper.func_76143_f(mob.field_70165_t), MathHelper.func_76143_f(mob.field_70163_u), MathHelper.func_76143_f(mob.field_70161_v))) {
               c -= Config.championMobs ? 10 : 3;
            }

            int cc = 0;
            boolean whitelisted = false;
            Iterator var7 = ConfigEntities.championModWhitelist.keySet().iterator();

            while(true) {
               Class clazz;
               do {
                  do {
                     if (!var7.hasNext()) {
                        c -= cc;
                        if (whitelisted && c <= 0 && mob.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() >= 10.0D) {
                           EntityUtils.makeChampion(mob, false);
                        } else {
                           IAttributeInstance modai = mob.func_110148_a(EntityUtils.CHAMPION_MOD);
                           modai.func_111124_b(ChampionModifier.ATTRIBUTE_MOD_NONE);
                           modai.func_111121_a(ChampionModifier.ATTRIBUTE_MOD_NONE);
                        }

                        return;
                     }

                     clazz = (Class)var7.next();
                  } while(!clazz.isAssignableFrom(event.getEntity().getClass()));

                  whitelisted = true;
               } while(!Config.championMobs && !(event.getEntity() instanceof EntityThaumcraftBoss));

               cc = Math.max(cc, (Integer)ConfigEntities.championModWhitelist.get(clazz) - 1);
            }
         }
      }

   }

   private boolean isDangerousLocation(World world, int x, int y, int z) {
      return false;
   }
}
