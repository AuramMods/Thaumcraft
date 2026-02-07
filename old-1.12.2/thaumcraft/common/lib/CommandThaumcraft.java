package thaumcraft.common.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;
import thaumcraft.common.lib.research.ResearchManager;

public class CommandThaumcraft extends CommandBase {
   private List aliases = new ArrayList();

   public CommandThaumcraft() {
      this.aliases.add("thaumcraft");
      this.aliases.add("thaum");
      this.aliases.add("tc");
   }

   public String func_71517_b() {
      return "thaumcraft";
   }

   public List<String> func_71514_a() {
      return this.aliases;
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/thaumcraft <action> [<player> [<params>]]";
   }

   public int func_82362_a() {
      return 2;
   }

   public boolean func_82358_a(String[] astring, int i) {
      return i == 1;
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         sender.func_145747_a(new TextComponentTranslation("§cInvalid arguments", new Object[0]));
         sender.func_145747_a(new TextComponentTranslation("§cUse /thaumcraft help to get help", new Object[0]));
      } else {
         if (args[0].equalsIgnoreCase("reload")) {
            Iterator var4 = ResearchCategories.researchCategories.values().iterator();

            while(var4.hasNext()) {
               ResearchCategory rc = (ResearchCategory)var4.next();
               rc.research.clear();
            }

            ResearchManager.parseAllResearch();
            sender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
         } else if (args[0].equalsIgnoreCase("help")) {
            sender.func_145747_a(new TextComponentTranslation("§3You can also use /thaum or /tc instead of /thaumcraft.", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("§3Use this to give research to a player.", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("  /thaumcraft research <list|player> <list|all|reset|<research>>", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("§3Use this to give set a players warp level.", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("  /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("  not specifying perm or temp will just add normal warp", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("§3Use this to reload json research data", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("  /thaumcraft reload", new Object[0]));
         } else if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("research") && args[1].equalsIgnoreCase("list")) {
               this.listResearch(sender);
            } else {
               EntityPlayerMP entityplayermp = func_184888_a(server, sender, args[1]);
               if (args[0].equalsIgnoreCase("research")) {
                  if (args.length == 3) {
                     if (args[2].equalsIgnoreCase("list")) {
                        this.listAllResearch(sender, entityplayermp);
                     } else if (args[2].equalsIgnoreCase("all")) {
                        this.giveAllResearch(sender, entityplayermp);
                     } else if (args[2].equalsIgnoreCase("reset")) {
                        this.resetResearch(sender, entityplayermp);
                     } else {
                        this.giveResearch(sender, entityplayermp, args[2]);
                     }
                  } else {
                     sender.func_145747_a(new TextComponentTranslation("§cInvalid arguments", new Object[0]));
                     sender.func_145747_a(new TextComponentTranslation("§cUse /thaumcraft research <list|player> <list|all|reset|<research>>", new Object[0]));
                  }
               } else if (args[0].equalsIgnoreCase("warp")) {
                  int i;
                  if (args.length >= 4 && args[2].equalsIgnoreCase("set")) {
                     i = func_180528_a(args[3], 0);
                     this.setWarp(sender, entityplayermp, i, args.length == 5 ? args[4] : "");
                  } else if (args.length >= 4 && args[2].equalsIgnoreCase("add")) {
                     i = func_175764_a(args[3], -100, 100);
                     this.addWarp(sender, entityplayermp, i, args.length == 5 ? args[4] : "");
                  } else {
                     sender.func_145747_a(new TextComponentTranslation("§cInvalid arguments", new Object[0]));
                     sender.func_145747_a(new TextComponentTranslation("§cUse /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>", new Object[0]));
                  }
               } else {
                  sender.func_145747_a(new TextComponentTranslation("§cInvalid arguments", new Object[0]));
                  sender.func_145747_a(new TextComponentTranslation("§cUse /thaumcraft help to get help", new Object[0]));
               }
            }
         } else {
            sender.func_145747_a(new TextComponentTranslation("§cInvalid arguments", new Object[0]));
            sender.func_145747_a(new TextComponentTranslation("§cUse /thaumcraft help to get help", new Object[0]));
         }

      }
   }

   private void setWarp(ICommandSender icommandsender, EntityPlayerMP player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         ThaumcraftCapabilities.getWarp(player).set(IPlayerWarp.EnumWarpType.PERMANENT, i);
      } else if (type.equalsIgnoreCase("TEMP")) {
         ThaumcraftCapabilities.getWarp(player).set(IPlayerWarp.EnumWarpType.TEMPORARY, i);
      } else {
         ThaumcraftCapabilities.getWarp(player).set(IPlayerWarp.EnumWarpType.NORMAL, i);
      }

      ThaumcraftCapabilities.getWarp(player).sync(player);
      player.func_145747_a(new TextComponentTranslation("§5" + icommandsender.func_70005_c_() + " set your warp to " + i, new Object[0]));
      icommandsender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
   }

   private void addWarp(ICommandSender icommandsender, EntityPlayerMP player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.PERMANENT, i);
      } else if (type.equalsIgnoreCase("TEMP")) {
         ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.TEMPORARY, i);
      } else {
         ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.NORMAL, i);
      }

      ThaumcraftCapabilities.getWarp(player).sync(player);
      PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)0, i), player);
      player.func_145747_a(new TextComponentTranslation("§5" + icommandsender.func_70005_c_() + " added " + i + " warp to your total.", new Object[0]));
      icommandsender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
   }

   private void listResearch(ICommandSender icommandsender) {
      Collection<ResearchCategory> rc = ResearchCategories.researchCategories.values();
      Iterator var3 = rc.iterator();

      while(var3.hasNext()) {
         ResearchCategory cat = (ResearchCategory)var3.next();
         Collection<ResearchEntry> rl = cat.research.values();
         Iterator var6 = rl.iterator();

         while(var6.hasNext()) {
            ResearchEntry ri = (ResearchEntry)var6.next();
            icommandsender.func_145747_a(new TextComponentTranslation("§5" + ri.getKey(), new Object[0]));
         }
      }

   }

   void giveResearch(ICommandSender icommandsender, EntityPlayerMP player, String research) {
      if (ResearchCategories.getResearch(research) != null) {
         giveRecursiveResearch(player, research);
         ThaumcraftCapabilities.getKnowledge(player).sync(player);
         player.func_145747_a(new TextComponentTranslation("§5" + icommandsender.func_70005_c_() + " gave you " + research + " research and its requisites.", new Object[0]));
         icommandsender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
      } else {
         icommandsender.func_145747_a(new TextComponentTranslation("§cResearch does not exist.", new Object[0]));
      }

   }

   public static void giveRecursiveResearch(EntityPlayer player, String research) {
      if (research.contains("@")) {
         int i = research.indexOf("@");
         research = research.substring(0, i);
      }

      ResearchEntry res = ResearchCategories.getResearch(research);
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
      if (!knowledge.isResearchComplete(research)) {
         String[] var4;
         int var5;
         int var6;
         String rsi;
         if (res != null && res.getParents() != null) {
            var4 = res.getParentsStripped();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               rsi = var4[var6];
               giveRecursiveResearch(player, rsi);
            }
         }

         int var9;
         int var10;
         if (res != null && res.getStages() != null) {
            ResearchStage[] var13 = res.getStages();
            var5 = var13.length;

            for(var6 = 0; var6 < var5; ++var6) {
               ResearchStage page = var13[var6];
               if (page.getResearch() != null) {
                  String[] var8 = page.getResearch();
                  var9 = var8.length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     String gr = var8[var10];
                     ResearchManager.completeResearch(player, gr);
                  }
               }
            }
         }

         ResearchManager.completeResearch(player, research);
         Iterator var14 = ResearchCategories.researchCategories.keySet().iterator();

         label70:
         while(var14.hasNext()) {
            String rc = (String)var14.next();
            Iterator var17 = ResearchCategories.getResearchCategory(rc).research.values().iterator();

            while(true) {
               while(true) {
                  ResearchEntry ri;
                  do {
                     if (!var17.hasNext()) {
                        continue label70;
                     }

                     ri = (ResearchEntry)var17.next();
                  } while(ri.getStages() == null);

                  ResearchStage[] var19 = ri.getStages();
                  var9 = var19.length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     ResearchStage stage = var19[var10];
                     if (stage.getResearch() != null && Arrays.asList(stage.getResearch()).contains(research)) {
                        ThaumcraftCapabilities.getKnowledge(player).setResearchFlag(ri.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE);
                        break;
                     }
                  }
               }
            }
         }

         if (res != null && res.getSiblings() != null) {
            var4 = res.getSiblings();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               rsi = var4[var6];
               giveRecursiveResearch(player, rsi);
            }
         }
      }

   }

   void listAllResearch(ICommandSender icommandsender, EntityPlayerMP player) {
      String ss = "";

      String key;
      for(Iterator var4 = ThaumcraftCapabilities.getKnowledge(player).getResearchList().iterator(); var4.hasNext(); ss = ss + key) {
         key = (String)var4.next();
         if (ss.length() != 0) {
            ss = ss + ", ";
         }
      }

      icommandsender.func_145747_a(new TextComponentTranslation("§5Research for " + player.func_70005_c_(), new Object[0]));
      icommandsender.func_145747_a(new TextComponentTranslation("§5" + ss, new Object[0]));
   }

   void giveAllResearch(ICommandSender icommandsender, EntityPlayerMP player) {
      Collection<ResearchCategory> rc = ResearchCategories.researchCategories.values();
      Iterator var4 = rc.iterator();

      while(var4.hasNext()) {
         ResearchCategory cat = (ResearchCategory)var4.next();
         Collection<ResearchEntry> rl = cat.research.values();
         Iterator var7 = rl.iterator();

         while(var7.hasNext()) {
            ResearchEntry ri = (ResearchEntry)var7.next();
            giveRecursiveResearch(player, ri.getKey());
         }
      }

      player.func_145747_a(new TextComponentTranslation("§5" + icommandsender.func_70005_c_() + " has given you all research.", new Object[0]));
      icommandsender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
   }

   void resetResearch(ICommandSender icommandsender, EntityPlayerMP player) {
      ThaumcraftCapabilities.getKnowledge(player).clear();
      Collection<ResearchCategory> rc = ResearchCategories.researchCategories.values();
      Iterator var4 = rc.iterator();

      while(var4.hasNext()) {
         ResearchCategory cat = (ResearchCategory)var4.next();
         Collection<ResearchEntry> res = cat.research.values();
         Iterator var7 = res.iterator();

         while(var7.hasNext()) {
            ResearchEntry ri = (ResearchEntry)var7.next();
            if (ri.hasMeta(ResearchEntry.EnumResearchMeta.AUTOUNLOCK)) {
               ResearchManager.completeResearch(player, ri.getKey(), false);
            }
         }
      }

      player.func_145747_a(new TextComponentTranslation("§5" + icommandsender.func_70005_c_() + " has reset all your research.", new Object[0]));
      icommandsender.func_145747_a(new TextComponentTranslation("§5Success!", new Object[0]));
      ThaumcraftCapabilities.getKnowledge(player).sync(player);
   }
}
