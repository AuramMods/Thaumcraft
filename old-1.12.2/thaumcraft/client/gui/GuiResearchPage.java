package thaumcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.Part;
import thaumcraft.api.crafting.RecipeMisc;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.api.research.ResearchAddendum;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.lib.events.HudHandler;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.crafting.Matrix;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncProgressToServer;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.PosXY;

@SideOnly(Side.CLIENT)
public class GuiResearchPage extends GuiScreen {
   public static LinkedList<String> history = new LinkedList();
   protected int paneWidth = 256;
   protected int paneHeight = 181;
   protected double guiMapX;
   protected double guiMapY;
   protected int mouseX = 0;
   protected int mouseY = 0;
   private GuiButton button;
   private ResearchEntry research;
   private int currentStage = 0;
   int lastStage = 0;
   boolean hold = false;
   private int page = 0;
   private int maxPages = 0;
   private int maxAspectPages = 0;
   AspectList knownPlayerAspects;
   IPlayerKnowledge playerKnowledge;
   int rhash = 0;
   float transX = 0.0F;
   float transY = 0.0F;
   float rotX = 0.0F;
   float rotY = 0.0F;
   float rotZ = 0.0F;
   long lastCheck = 0L;
   float pt;
   ResourceLocation tex1 = new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook.png");
   ResourceLocation tex2 = new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png");
   ResourceLocation tex3 = new ResourceLocation("thaumcraft", "textures/aspects/_back.png");
   ResourceLocation tex4 = new ResourceLocation("thaumcraft", "textures/gui/paper.png");
   ResourceLocation dummyResearch = new ResourceLocation("thaumcraft", "textures/aspects/_unknown.png");
   ResourceLocation dummyMap = new ResourceLocation("thaumcraft", "textures/research/rd_map.png");
   ResourceLocation dummyFlask = new ResourceLocation("thaumcraft", "textures/research/rd_flask.png");
   ResourceLocation dummyChest = new ResourceLocation("thaumcraft", "textures/research/rd_chest.png");
   int hrx = 0;
   int hry = 0;
   static String shownRecipe;
   int recipePage = 0;
   int recipePageMax = 0;
   private long lastCycle = 0L;
   private boolean showingAspects = false;
   private static int aspectsPage = 0;
   LinkedHashMap<String, ArrayList> recipeLists = new LinkedHashMap();
   LinkedHashMap<String, ArrayList> recipeOutputs = new LinkedHashMap();
   LinkedHashMap<String, ArrayList> drilldownLists = new LinkedHashMap();
   boolean hasRecipePages;
   boolean renderingCompound = false;
   static boolean cycleMultiblockLines = false;
   GuiResearchPage.BlueprintBlockAccess blockAccess = null;
   HashMap<String, GuiResearchPage.BlueprintBlockAccess> blockAccessIcons = new HashMap();
   ArrayList<List> reference = new ArrayList();
   private int cycle = -1;
   boolean allowWithPagePopup = false;
   List tipText = null;
   private static final int PAGEWIDTH = 140;
   private static final int PAGEHEIGHT = 210;
   private static final GuiResearchPage.PageImage PILINE = GuiResearchPage.PageImage.parse("thaumcraft:textures/gui/gui_researchbook.png:24:184:95:6:1");
   private static final GuiResearchPage.PageImage PIDIV = GuiResearchPage.PageImage.parse("thaumcraft:textures/gui/gui_researchbook.png:28:192:140:6:1");
   private ArrayList<GuiResearchPage.Page> pages = new ArrayList();
   boolean isComplete = false;
   boolean hasAllRequisites = false;
   boolean[] hasItem = null;
   boolean[] hasCraft = null;
   boolean[] hasResearch = null;
   boolean[] hasKnow = null;
   boolean[] hasStats = null;
   public HashMap<Integer, String> keyCache = new HashMap();

   public GuiResearchPage(ResearchEntry research, String recipe, double x, double y) {
      this.research = research;
      this.guiMapX = x;
      this.guiMapY = y;
      this.field_146297_k = Minecraft.func_71410_x();
      this.playerKnowledge = ThaumcraftCapabilities.getKnowledge(this.field_146297_k.field_71439_g);
      this.parsePages();
      this.maxPages = this.pages.size();
      this.knownPlayerAspects = new AspectList();
      Iterator var7 = Aspect.aspects.values().iterator();

      while(var7.hasNext()) {
         Aspect a = (Aspect)var7.next();
         if (ThaumcraftCapabilities.knowsResearch(this.field_146297_k.field_71439_g, "!" + a.getTag().toLowerCase())) {
            this.knownPlayerAspects.add(a, 1);
         }
      }

      this.maxAspectPages = this.knownPlayerAspects != null ? MathHelper.func_76123_f((float)this.knownPlayerAspects.size() / 5.0F) : 0;
      this.page = 0;
      if (recipe != null) {
         shownRecipe = recipe;
      }

   }

   public void func_73866_w_() {
      this.rotX = 25.0F;
      this.rotY = -45.0F;
   }

   public void func_73863_a(int par1, int par2, float par3) {
      this.hasRecipePages = false;
      long nano = System.nanoTime();
      if (nano > this.lastCheck) {
         this.parsePages();
         if (this.hold) {
            this.lastCheck = nano + 250000000L;
         } else {
            this.lastCheck = nano + 2000000000L;
         }

         if (this.currentStage > this.lastStage) {
            this.hold = false;
         }
      }

      this.pt = par3;
      this.func_146276_q_();
      this.genResearchBackground(par1, par2, par3);
      int sw = (this.field_146294_l - this.paneWidth) / 2;
      int sh = (this.field_146295_m - this.paneHeight) / 2;
      if (!history.isEmpty()) {
         int mx = par1 - (sw + 118);
         int my = par2 - (sh + 190);
         if (mx >= 0 && my >= 0 && mx < 20 && my < 12) {
            this.field_146297_k.field_71466_p.func_175063_a(I18n.func_74838_a("recipe.return"), (float)par1, (float)par2, 16777215);
         }
      }

   }

   protected void genResearchBackground(int par1, int par2, float par3) {
      int sw = (this.field_146294_l - this.paneWidth) / 2;
      int sh = (this.field_146295_m - this.paneHeight) / 2;
      float var10 = ((float)this.field_146294_l - (float)this.paneWidth * 1.3F) / 2.0F;
      float var11 = ((float)this.field_146295_m - (float)this.paneHeight * 1.3F) / 2.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glPushMatrix();
      GL11.glTranslatef(var10, var11, 0.0F);
      GL11.glScalef(1.3F, 1.3F, 1.0F);
      this.func_73729_b(0, 0, 0, 0, this.paneWidth, this.paneHeight);
      GL11.glPopMatrix();
      this.reference.clear();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      int current = 0;

      for(int a = 0; a < this.pages.size(); ++a) {
         if ((current == this.page || current == this.page + 1) && current < this.maxPages) {
            this.drawPage((GuiResearchPage.Page)this.pages.get(a), current % 2, sw, sh - 10, par1, par2);
         }

         ++current;
         if (current > this.page + 1) {
            break;
         }
      }

      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
      float bob = MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 3.0F) * 0.2F + 0.1F;
      if (!history.isEmpty()) {
         this.drawTexturedModalRectScaled(sw + 118, sh + 190, 38, 202, 20, 12, bob);
      }

      if (this.page > 0 && shownRecipe == null) {
         this.drawTexturedModalRectScaled(sw - 16, sh + 190, 0, 184, 12, 8, bob);
      }

      if (this.page < this.maxPages - 2 && shownRecipe == null) {
         this.drawTexturedModalRectScaled(sw + 262, sh + 190, 12, 184, 12, 8, bob);
      }

      if (this.tipText != null) {
         UtilsFX.drawCustomTooltip(this, this.field_146297_k.field_71466_p, this.tipText, par1, par2 + 12, 11);
         this.tipText = null;
      }

   }

   private void drawPage(GuiResearchPage.Page pageParm, int side, int x, int y, int mx, int my) {
      if (this.lastCycle < System.currentTimeMillis()) {
         ++this.cycle;
         this.lastCycle = System.currentTimeMillis() + 1000L;
         if (cycleMultiblockLines && this.blockAccess != null) {
            ++this.blockAccess.sliceLine;
         }
      }

      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      int le;
      if (this.page == 0 && side == 0) {
         this.func_73729_b(x + 4, y - 7, 24, 184, 96, 4);
         this.func_73729_b(x + 4, y + 10, 24, 184, 96, 4);
         le = this.field_146297_k.field_71466_p.func_78256_a(this.research.getLocalizedName());
         if (le <= 140) {
            this.field_146297_k.field_71466_p.func_78276_b(this.research.getLocalizedName(), x - 15 + 140 / 2 - le / 2, y, 2105376);
         } else {
            float vv = 140.0F / (float)le;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x - 15 + 140 / 2) - (float)(le / 2) * vv, (float)y + 1.0F * vv, 0.0F);
            GL11.glScalef(vv, vv, vv);
            this.field_146297_k.field_71466_p.func_78276_b(this.research.getLocalizedName(), 0, 0, 2105376);
            GL11.glPopMatrix();
         }

         y += 28;
      }

      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      Iterator var11 = pageParm.contents.iterator();

      String s;
      while(var11.hasNext()) {
         Object content = var11.next();
         if (content instanceof String) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            s = ((String)content).replace("ï¿½", "");
            this.field_146297_k.field_71466_p.func_78276_b(s, x - 15 + side * 152, y - 6, 0);
            y += this.field_146297_k.field_71466_p.field_78288_b;
            if (((String)content).endsWith("ï¿½")) {
               y = (int)((double)y + (double)this.field_146297_k.field_71466_p.field_78288_b * 0.66D);
            }
         } else if (content instanceof GuiResearchPage.PageImage) {
            GuiResearchPage.PageImage pi = (GuiResearchPage.PageImage)content;
            GL11.glPushMatrix();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146297_k.field_71446_o.func_110577_a(pi.loc);
            int pad = (140 - pi.aw) / 2;
            GL11.glTranslatef((float)(x - 15 + side * 152 + pad), (float)(y - 5), 0.0F);
            GL11.glScalef(pi.scale, pi.scale, pi.scale);
            this.func_73729_b(0, 0, pi.x, pi.y, pi.w, pi.h);
            GL11.glPopMatrix();
            y += pi.ah + 2;
         }
      }

      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (this.playerKnowledge.isResearchComplete("FIRSTSTEPS")) {
         y = (this.field_146295_m - this.paneHeight) / 2 + 9;
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         le = this.mouseInside(x - 48, y, 25, 16, mx, my) ? 0 : 3;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawPopupAt(x - 48, y, 25, 16, mx, my, "tc.aspect.name");
         this.func_73729_b(x - 48 + le, y, 76, 232, 24 - le, 16);
         this.func_73729_b(x - 28, y, 100, 232, 4, 16);
      }

      ResearchStage stage = this.research.getStages()[this.currentStage];
      if (stage.getRecipes() != null) {
         this.drawRecipeBookmarks(x, mx, my, stage);
      }

      if (this.page == 0 && side == 0 && !this.isComplete) {
         this.drawRequirements(x, mx, my, stage);
      }

      if (this.playerKnowledge.isResearchComplete("KNOWLEDGETYPES") && this.research.getKey().equals("KNOWLEDGETYPES")) {
         this.drawKnowledges(x, mx, my);
      }

      this.renderingCompound = false;
      if (this.showingAspects) {
         this.drawAspects(mx, my);
      } else if (shownRecipe != null) {
         this.drawRecipe(mx, my);
      } else if (stage.getWarp() > 0 && !this.isComplete) {
         int warp = stage.getWarp();
         if (warp > 5) {
            warp = 5;
         }

         GuiResearchBrowser.drawForbidden((double)(x - 45), (double)(y - 20));
         s = I18n.func_74838_a("tc.forbidden.level." + warp);
         this.field_146297_k.field_71466_p.func_78276_b(s, x - 44 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, y - 23, 11180543);
         String text = I18n.func_74838_a("tc.warp.warn");
         this.drawPopupAt(x - 55, y - 30, 20, 20, mx, my, text.replaceAll("%n", s));
      }

   }

   private void drawKnowledges(int x, int mx, int my) {
      int y = (this.field_146295_m - this.paneHeight) / 2 - 16 + 210;
      y -= 18;
      boolean drewSomething = false;
      int tc = 0;
      IPlayerKnowledge.EnumKnowledgeType[] var7 = IPlayerKnowledge.EnumKnowledgeType.values();
      int var8 = var7.length;

      label61:
      for(int var9 = 0; var9 < var8; ++var9) {
         IPlayerKnowledge.EnumKnowledgeType type = var7[var9];
         int fc = 0;
         boolean b = false;
         Iterator var13 = ResearchCategories.researchCategories.values().iterator();

         while(true) {
            ResearchCategory category;
            int amt;
            int par;
            do {
               do {
                  if (!var13.hasNext()) {
                     if (b) {
                        ++tc;
                     }
                     continue label61;
                  }

                  category = (ResearchCategory)var13.next();
               } while(!type.hasFields() && category != null);

               amt = this.playerKnowledge.getKnowledge(type, category);
               par = this.playerKnowledge.getKnowledgeRaw(type, category) % type.getProgression();
            } while(amt <= 0 && par <= 0);

            drewSomething = true;
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            this.field_146297_k.field_71446_o.func_110577_a(HudHandler.KNOW_TYPE[type.ordinal()]);
            GL11.glTranslatef((float)(x - 10 + 18 * fc), (float)(y - tc * 20), 0.0F);
            GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
            this.func_73729_b(0, 0, 0, 0, 255, 255);
            if (type.hasFields() && category != null) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.75F);
               this.field_146297_k.field_71446_o.func_110577_a(category.icon);
               GL11.glTranslatef(0.0F, 0.0F, 1.0F);
               GL11.glScaled(0.66D, 0.66D, 0.66D);
               this.func_73729_b(66, 66, 0, 0, 255, 255);
            }

            GL11.glPopMatrix();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(0.0F, 0.0F, 5.0F);
            String s = "" + amt;
            int m = this.field_146297_k.field_71466_p.func_78256_a(s);
            this.field_146297_k.field_71466_p.func_175063_a(s, (float)(x - 10 + 16 - m + 18 * fc), (float)(y - tc * 20 + 8), 16777215);
            s = I18n.func_74838_a("tc.type." + type.toString().toLowerCase());
            if (type.hasFields() && category != null) {
               s = s + ": " + ResearchCategories.getCategoryName(category.key);
            }

            this.drawPopupAt(x - 10 + 18 * fc, y - tc * 20, mx, my, s);
            if (par > 0) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.75F);
               this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
               int l = (int)((float)par / (float)type.getProgression() * 16.0F);
               this.func_73729_b(x - 10 + 18 * fc, y + 17 - tc * 20, 0, 232, l, 2);
               this.func_73729_b(x - 10 + 18 * fc + l, y + 17 - tc * 20, l, 234, 16 - l, 2);
            }

            GL11.glTranslatef(0.0F, 0.0F, -5.0F);
            ++fc;
            b = true;
         }
      }

      if (drewSomething) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x + 4, y - tc * 20 + 12, 24, 184, 96, 8);
      }

   }

   private void drawRequirements(int x, int mx, int my, ResearchStage stage) {
      int y = (this.field_146295_m - this.paneHeight) / 2 - 16 + 210;
      GL11.glPushMatrix();
      boolean b = false;
      int shift;
      int ss;
      String key;
      String s;
      if (stage.getResearch() != null) {
         y -= 18;
         b = true;
         shift = 24;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.25F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x - 12, y - 1, 200, 232, 56, 16);
         this.drawPopupAt(x - 15, y, mx, my, "tc.need.research");
         Object loc = null;
         if (this.hasResearch != null) {
            if (this.hasResearch.length != stage.getResearch().length) {
               this.hasResearch = new boolean[stage.getResearch().length];
            }

            ss = 18;
            if (stage.getResearch().length > 6) {
               ss = 110 / stage.getResearch().length;
            }

            for(int a = 0; a < stage.getResearch().length; ++a) {
               key = stage.getResearch()[a];
               loc = this.dummyResearch;
               s = I18n.func_74838_a("research." + key + ".text");
               if (key.startsWith("!")) {
                  String k = key.replaceAll("!", "");
                  Aspect as = (Aspect)Aspect.aspects.get(k);
                  if (as != null) {
                     loc = as;
                     s = as.getName();
                  }
               }

               ResearchEntry re = ResearchCategories.getResearch(key);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (re != null && re.getIcons() != null) {
                  int idx = (int)(System.currentTimeMillis() / 1000L % (long)re.getIcons().length);
                  loc = re.getIcons()[idx];
                  s = re.getLocalizedName();
               } else if (key.startsWith("m_")) {
                  loc = this.dummyMap;
               } else if (key.startsWith("c_")) {
                  loc = this.dummyChest;
               } else if (key.startsWith("f_")) {
                  loc = this.dummyFlask;
               } else {
                  GlStateManager.func_179131_c(0.5F, 0.75F, 1.0F, 1.0F);
               }

               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               if (loc instanceof Aspect) {
                  this.field_146297_k.field_71446_o.func_110577_a(((Aspect)loc).getImage());
                  Color cc = new Color(((Aspect)loc).getColor());
                  GlStateManager.func_179131_c((float)cc.getRed() / 255.0F, (float)cc.getGreen() / 255.0F, (float)cc.getBlue() / 255.0F, 1.0F);
                  UtilsFX.drawTexturedQuadFull((float)(x - 15 + shift), (float)y, (double)this.field_73735_i);
               } else if (loc instanceof ResourceLocation) {
                  this.field_146297_k.field_71446_o.func_110577_a((ResourceLocation)loc);
                  UtilsFX.drawTexturedQuadFull((float)(x - 15 + shift), (float)y, (double)this.field_73735_i);
               } else if (loc instanceof ItemStack) {
                  RenderHelper.func_74520_c();
                  GL11.glDisable(2896);
                  GL11.glEnable(32826);
                  GL11.glEnable(2903);
                  GL11.glEnable(2896);
                  this.field_146296_j.func_180450_b(InventoryUtils.cycleItemStack(loc), x - 15 + shift, y);
                  GL11.glDisable(2896);
                  GL11.glDepthMask(true);
                  GL11.glEnable(2929);
               }

               GL11.glPopMatrix();
               if (this.hasResearch[a]) {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
                  GlStateManager.func_179097_i();
                  this.func_73729_b(x - 15 + shift + 8, y, 159, 207, 10, 10);
                  GlStateManager.func_179126_j();
               }

               this.drawPopupAt(x - 15 + shift, y, mx, my, s);
               shift += ss;
            }
         }
      }

      int ss;
      ItemStack stack;
      if (stage.getObtain() != null) {
         y -= 18;
         b = true;
         shift = 24;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.25F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x - 12, y - 1, 200, 216, 56, 16);
         this.drawPopupAt(x - 15, y, mx, my, "tc.need.obtain");
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.hasItem != null) {
            if (this.hasItem.length != stage.getObtain().length) {
               this.hasItem = new boolean[stage.getObtain().length];
            }

            ss = 18;
            if (stage.getObtain().length > 6) {
               ss = 110 / stage.getObtain().length;
            }

            for(ss = 0; ss < stage.getObtain().length; ++ss) {
               stack = stage.getObtain()[ss];
               this.drawStackAt(stack, x - 15 + shift, y, mx, my, true);
               if (this.hasItem[ss]) {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
                  GlStateManager.func_179097_i();
                  this.func_73729_b(x - 15 + shift + 8, y, 159, 207, 10, 10);
                  GlStateManager.func_179126_j();
               }

               shift += ss;
            }
         }
      }

      if (stage.getCraft() != null) {
         y -= 18;
         b = true;
         shift = 24;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.25F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x - 12, y - 1, 200, 200, 56, 16);
         this.drawPopupAt(x - 15, y, mx, my, "tc.need.craft");
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.hasCraft != null) {
            if (this.hasCraft.length != stage.getCraft().length) {
               this.hasCraft = new boolean[stage.getCraft().length];
            }

            ss = 18;
            if (stage.getCraft().length > 6) {
               ss = 110 / stage.getCraft().length;
            }

            for(ss = 0; ss < stage.getCraft().length; ++ss) {
               stack = stage.getCraft()[ss];
               this.drawStackAt(stack, x - 15 + shift, y, mx, my, true);
               if (this.hasCraft[ss]) {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
                  GlStateManager.func_179097_i();
                  this.func_73729_b(x - 15 + shift + 8, y, 159, 207, 10, 10);
                  GlStateManager.func_179126_j();
               }

               shift += ss;
            }
         }
      }

      if (stage.getKnow() != null) {
         y -= 18;
         b = true;
         shift = 24;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.25F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x - 12, y - 1, 200, 184, 56, 16);
         this.drawPopupAt(x - 15, y, mx, my, "tc.need.know");
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.hasKnow != null) {
            if (this.hasKnow.length != stage.getKnow().length) {
               this.hasKnow = new boolean[stage.getKnow().length];
            }

            ss = 18;
            if (stage.getKnow().length > 6) {
               ss = 110 / stage.getKnow().length;
            }

            for(ss = 0; ss < stage.getKnow().length; ++ss) {
               ResearchStage.Knowledge kn = stage.getKnow()[ss];
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glPushMatrix();
               this.field_146297_k.field_71446_o.func_110577_a(HudHandler.KNOW_TYPE[kn.type.ordinal()]);
               GL11.glTranslatef((float)(x - 15 + shift), (float)y, 0.0F);
               GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
               this.func_73729_b(0, 0, 0, 0, 255, 255);
               if (kn.type.hasFields() && kn.category != null) {
                  this.field_146297_k.field_71446_o.func_110577_a(kn.category.icon);
                  GL11.glTranslatef(32.0F, 32.0F, 1.0F);
                  GL11.glPushMatrix();
                  GL11.glScaled(0.75D, 0.75D, 0.75D);
                  this.func_73729_b(0, 0, 0, 0, 255, 255);
                  GL11.glPopMatrix();
               }

               GL11.glPopMatrix();
               key = "" + (!this.hasKnow[ss] ? TextFormatting.RED : "") + kn.amount;
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glPushMatrix();
               GL11.glTranslatef((float)(x - 15 + shift + 16 - this.field_146297_k.field_71466_p.func_78256_a(key) / 2), (float)(y + 12), 5.0F);
               GL11.glScaled(0.5D, 0.5D, 0.5D);
               this.field_146297_k.field_71466_p.func_175063_a(key, 0.0F, 0.0F, 16777215);
               GL11.glPopMatrix();
               if (this.hasKnow[ss]) {
                  GL11.glPushMatrix();
                  GL11.glTranslatef(0.0F, 0.0F, 1.0F);
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
                  this.func_73729_b(x - 15 + shift + 8, y, 159, 207, 10, 10);
                  GL11.glPopMatrix();
               }

               s = I18n.func_74838_a("tc.type." + kn.type.toString().toLowerCase());
               if (kn.type.hasFields() && kn.category != null) {
                  s = s + ": " + ResearchCategories.getCategoryName(kn.category.key);
               }

               this.drawPopupAt(x - 15 + shift, y, mx, my, s);
               shift += ss;
            }
         }
      }

      if (b) {
         y -= 12;
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         this.func_73729_b(x + 4, y - 2, 24, 184, 96, 8);
         if (this.hasAllRequisites) {
            this.hrx = x + 20;
            this.hry = y - 6;
            String s;
            if (this.hold) {
               s = I18n.func_74838_a("tc.stage.hold");
               ss = this.field_146297_k.field_71466_p.func_78256_a(s);
               this.field_146297_k.field_71466_p.func_175063_a(s, (float)(x + 52) - (float)ss / 2.0F, (float)(y - 4), 16777215);
            } else {
               if (this.mouseInside(this.hrx, this.hry, 64, 12, mx, my)) {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               } else {
                  GlStateManager.func_179131_c(0.8F, 0.8F, 0.9F, 1.0F);
               }

               this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
               this.func_73729_b(this.hrx, this.hry, 84, 216, 64, 12);
               s = I18n.func_74838_a("tc.stage.complete");
               ss = this.field_146297_k.field_71466_p.func_78256_a(s);
               this.field_146297_k.field_71466_p.func_175063_a(s, (float)(x + 52) - (float)ss / 2.0F, (float)(y - 4), 16777215);
            }
         }
      }

      GL11.glPopMatrix();
   }

   private void drawRecipeBookmarks(int x, int mx, int my, ResearchStage stage) {
      Random rng = new Random((long)this.rhash);
      GL11.glPushMatrix();
      int y = (this.field_146295_m - this.paneHeight) / 2 + 4;
      this.allowWithPagePopup = true;
      Iterator var7 = this.recipeOutputs.keySet().iterator();

      while(var7.hasNext()) {
         String rk = (String)var7.next();
         List list = (List)this.recipeOutputs.get(rk);
         if (list != null && list.size() > 0) {
            int i = this.cycle % list.size();
            if (list.get(i) != null) {
               int sh = rng.nextInt(3);
               int le = rng.nextInt(3) + (this.mouseInside(x + 280, y - 1, 30, 16, mx, my) ? 0 : 3);
               this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
               if (rk.equals(shownRecipe)) {
                  GlStateManager.func_179131_c(1.0F, 0.5F, 0.5F, 1.0F);
               } else {
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               }

               this.func_73729_b(x + 280 + sh, y - 1, 120 + le, 232, 28, 16);
               this.func_73729_b(x + 280 + sh, y - 1, 116, 232, 4, 16);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               UtilsFX.hideStackOverlay = true;
               if (list.get(i) instanceof ItemStack) {
                  this.drawStackAt((ItemStack)list.get(i), x + 287 + sh - le, y - 1, mx, my, false);
               } else if (list.get(i) instanceof Part[][][]) {
                  GuiResearchPage.BlueprintBlockAccess ba = (GuiResearchPage.BlueprintBlockAccess)this.blockAccessIcons.get(rk + i);
                  if (ba == null) {
                     this.blockAccessIcons.put(rk + i, ba = new GuiResearchPage.BlueprintBlockAccess((Part[][][])((Part[][][])list.get(i)), true));
                  }

                  int h = ((Part[][][])((Part[][][])list.get(i))).length;
                  this.renderBluePrint(ba, x + 295 + sh - le, y + 6 + h, 4.0F, (Part[][][])((Part[][][])list.get(i)), -5000, -5000, (ItemStack[])null);
               }

               UtilsFX.hideStackOverlay = false;
               y += 24;
            }
         }
      }

      this.allowWithPagePopup = false;
      GL11.glPopMatrix();
   }

   private void generateRecipesLists(ResearchStage stage, ResearchAddendum[] addenda) {
      this.recipeLists.clear();
      this.recipeOutputs.clear();
      if (stage != null && stage.getRecipes() != null) {
         String[] var3 = stage.getRecipes();
         int var4 = var3.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            String rk = var3[var5];
            this.addRecipesToList(rk, this.recipeLists, this.recipeOutputs);
         }

         if (addenda != null) {
            ResearchAddendum[] var11 = addenda;
            var4 = addenda.length;

            for(var5 = 0; var5 < var4; ++var5) {
               ResearchAddendum addendum = var11[var5];
               if (addendum.getRecipes() != null && ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, addendum.getResearch())) {
                  String[] var7 = addendum.getRecipes();
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     String rk = var7[var9];
                     this.addRecipesToList(rk, this.recipeLists, this.recipeOutputs);
                  }
               }
            }

         }
      }
   }

   private void addRecipesToList(String rk, LinkedHashMap<String, ArrayList> recipeLists2, LinkedHashMap<String, ArrayList> recipeOutputs2) {
      Object recipes = CommonInternals.getCatalogRecipe(rk);
      if (recipes != null) {
         ArrayList list = new ArrayList();
         ArrayList outputs = new ArrayList();
         int var9;
         int var10;
         if (recipes instanceof ThaumcraftApi.BluePrint[]) {
            ThaumcraftApi.BluePrint[] r = (ThaumcraftApi.BluePrint[])((ThaumcraftApi.BluePrint[])recipes);
            ThaumcraftApi.BluePrint[] var23 = r;
            var9 = r.length;

            for(var10 = 0; var10 < var9; ++var10) {
               ThaumcraftApi.BluePrint re = var23[var10];
               if (ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, re.getResearch())) {
                  list.add(re);
                  if (re.getDisplayStack() != null) {
                     outputs.add(re.getDisplayStack());
                  } else {
                     outputs.add(re.getParts());
                  }
               }
            }
         } else {
            ItemStack is;
            if (recipes instanceof CrucibleRecipe[]) {
               CrucibleRecipe[] r = (CrucibleRecipe[])((CrucibleRecipe[])recipes);
               CrucibleRecipe[] var22 = r;
               var9 = r.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  CrucibleRecipe re = var22[var10];
                  is = InventoryUtils.cycleItemStack(re.catalyst, 0);
                  if (is != null && ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, re.research)) {
                     list.add(re);
                     outputs.add(re.getRecipeOutput());
                  }
               }
            } else if (recipes instanceof InfusionRecipe[]) {
               InfusionRecipe[] r = (InfusionRecipe[])((InfusionRecipe[])recipes);
               InfusionRecipe[] var21 = r;
               var9 = r.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  InfusionRecipe re = var21[var10];
                  is = null;
                  ArrayList cl;
                  if (re instanceof InfusionEnchantmentRecipe) {
                     is = InventoryUtils.cycleItemStack(re.getRecipeOutput(this.field_146297_k.field_71439_g, (ItemStack)re.getRecipeInput(), (ArrayList)null), 0);
                  } else if (re instanceof InfusionRunicAugmentRecipe) {
                     cl = new ArrayList(Arrays.asList(((InfusionRunicAugmentRecipe)re).getComponents((ItemStack)re.getRecipeInput())));
                     is = InventoryUtils.cycleItemStack(re.getRecipeOutput(this.field_146297_k.field_71439_g, (ItemStack)re.getRecipeInput(), cl), 0);
                  } else {
                     is = InventoryUtils.cycleItemStack(re.getRecipeOutput(), 0);
                  }

                  if (is != null && ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, re.research)) {
                     list.add(re);
                     if (re instanceof InfusionEnchantmentRecipe) {
                        outputs.add(re.getRecipeOutput(this.field_146297_k.field_71439_g, (ItemStack)re.getRecipeInput(), (ArrayList)null));
                     } else if (re instanceof InfusionRunicAugmentRecipe) {
                        cl = new ArrayList(Arrays.asList(((InfusionRunicAugmentRecipe)re).getComponents((ItemStack)re.getRecipeInput())));
                        outputs.add(re.getRecipeOutput(this.field_146297_k.field_71439_g, (ItemStack)re.getRecipeInput(), cl));
                     } else {
                        outputs.add(re.getRecipeOutput());
                     }
                  }
               }
            } else if (recipes instanceof IArcaneRecipe[]) {
               IArcaneRecipe[] r = (IArcaneRecipe[])((IArcaneRecipe[])recipes);
               IArcaneRecipe[] var20 = r;
               var9 = r.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  IArcaneRecipe re = var20[var10];
                  is = InventoryUtils.cycleItemStack(re.func_77571_b(), 0);
                  if (is != null && ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, re.getResearch())) {
                     list.add(re);
                     outputs.add(re.func_77571_b());
                  }
               }
            } else if (recipes instanceof IRecipe[]) {
               IRecipe[] r = (IRecipe[])((IRecipe[])recipes);
               IRecipe[] var18 = r;
               var9 = r.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  IRecipe re = var18[var10];
                  list.add(re);
                  outputs.add(re.func_77571_b());
               }
            } else if (recipes instanceof RecipeMisc[]) {
               RecipeMisc[] r = (RecipeMisc[])((RecipeMisc[])recipes);
               RecipeMisc[] var8 = r;
               var9 = r.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  RecipeMisc re = var8[var10];
                  list.add(re);
                  outputs.add(re.getOutput());
               }
            }
         }

         if (list.size() > 0) {
            recipeLists2.put(rk, list);
            if (recipeOutputs2 != null) {
               recipeOutputs2.put(rk, outputs);
            }
         }
      }

   }

   private void drawRecipe(int mx, int my) {
      this.allowWithPagePopup = true;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex4);
      int x = (this.field_146294_l - 256) / 2;
      int y = (this.field_146295_m - 256) / 2;
      GlStateManager.func_179097_i();
      this.func_73729_b(x, y, 0, 0, 255, 255);
      GlStateManager.func_179126_j();
      List list = (List)this.recipeLists.get(shownRecipe);
      if (list == null || list.size() == 0) {
         list = (List)this.drilldownLists.get(shownRecipe);
      }

      if (list != null && list.size() > 0) {
         this.hasRecipePages = list.size() > 1;
         this.recipePageMax = list.size() - 1;
         if (this.recipePage > this.recipePageMax) {
            this.recipePage = this.recipePageMax;
         }

         Object recipe = list.get(this.recipePage % list.size());
         if (recipe != null) {
            if (recipe instanceof IArcaneRecipe) {
               this.drawArcaneCraftingPage(x + 128, y + 128, mx, my, (IArcaneRecipe)recipe);
            } else if (recipe instanceof IRecipe) {
               this.drawCraftingPage(x + 128, y + 128, mx, my, (IRecipe)recipe);
            } else if (recipe instanceof CrucibleRecipe) {
               this.drawCruciblePage(x + 128, y + 128, mx, my, (CrucibleRecipe)recipe);
            } else if (recipe instanceof InfusionRecipe) {
               this.drawInfusionPage(x + 128, y + 128, mx, my, (InfusionRecipe)recipe);
            } else if (recipe instanceof ThaumcraftApi.BluePrint) {
               this.drawCompoundCraftingPage(x + 128, y + 128, mx, my, (ThaumcraftApi.BluePrint)recipe);
               this.renderingCompound = true;
            }
         }

         if (this.hasRecipePages) {
            this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
            float bob = MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 3.0F) * 0.2F + 0.1F;
            if (this.recipePage > 0) {
               this.drawTexturedModalRectScaled(x + 40, y + 232, 0, 184, 12, 8, bob);
            }

            if (this.recipePage < this.recipePageMax) {
               this.drawTexturedModalRectScaled(x + 204, y + 232, 12, 184, 12, 8, bob);
            }
         }
      }

      this.allowWithPagePopup = false;
   }

   private void drawCompoundCraftingPage(int x, int y, int mx, int my, ThaumcraftApi.BluePrint recipe) {
      if (recipe.getParts() != null) {
         if (this.blockAccess == null) {
            this.blockAccess = new GuiResearchPage.BlueprintBlockAccess(recipe.getParts(), false);
         }

         int ySize = recipe.getParts().length;
         int xSize = recipe.getParts()[0].length;
         int zSize = recipe.getParts()[0][0].length;
         String text = I18n.func_74838_a("recipe.type.construct");
         int offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
         int s = Math.max(Math.max(xSize, zSize), ySize) * 2;
         float scale = (float)(38 - s);
         this.renderBluePrint(this.blockAccess, x, y, scale, recipe.getParts(), mx, my, recipe.getIngredientList());
         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, this.mouseInside(x + 80, y + 100, 8, 8, mx, my) ? 1.0F : 0.75F);
         this.func_73729_b(x + 80, y + 100, cycleMultiblockLines ? 168 : 160, 224, 8, 8);
      }
   }

   private void renderBluePrint(GuiResearchPage.BlueprintBlockAccess ba, int x, int y, float scale, Part[][][] blueprint, int mx, int my, ItemStack[] ingredients) {
      BlockRendererDispatcher blockRender = Minecraft.func_71410_x().func_175602_ab();
      int ySize = blueprint.length;
      int xSize = blueprint[0].length;
      int zSize = blueprint[0][0].length;
      this.transX = (float)(x - xSize / 2);
      this.transY = (float)y - (float)Math.sqrt((double)(ySize * ySize + xSize * xSize + zSize * zSize)) / 2.0F;
      GlStateManager.func_179091_B();
      GlStateManager.func_179094_E();
      RenderHelper.func_74518_a();
      GlStateManager.func_179109_b(this.transX, this.transY, (float)Math.max(ySize, Math.max(xSize, zSize)));
      GlStateManager.func_179152_a(scale, -scale, 1.0F);
      GlStateManager.func_179114_b(this.rotX, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(this.rotY, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179109_b((float)zSize / -2.0F, (float)ySize / -2.0F, (float)xSize / -2.0F);
      GlStateManager.func_179140_f();
      if (Minecraft.func_71379_u()) {
         GlStateManager.func_179103_j(7425);
      } else {
         GlStateManager.func_179103_j(7424);
      }

      this.field_146297_k.func_110434_K().func_110577_a(TextureMap.field_110575_b);
      new ArrayList();

      int a;
      for(a = 0; a < ySize; ++a) {
         for(int l = 0; l < xSize; ++l) {
            for(int w = 0; w < zSize; ++w) {
               BlockPos pos = new BlockPos(l, a, w);
               if (!ba.func_175623_d(pos)) {
                  GlStateManager.func_179109_b((float)l, (float)a, (float)w);
                  GlStateManager.func_179109_b((float)(-l), (float)(-a), (float)(-w));
                  IBlockState state = ba.func_180495_p(pos);
                  Tessellator tessellator = Tessellator.func_178181_a();
                  VertexBuffer buffer = tessellator.func_178180_c();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_176600_a);
                  boolean b = blockRender.func_175018_a(state, pos, ba, buffer);
                  tessellator.func_78381_a();

                  try {
                     if (!b && state.func_177230_c().hasTileEntity(state)) {
                        TileEntity te = state.func_177230_c().createTileEntity(this.field_146297_k.field_71441_e, state);
                        RenderHelper.func_74519_b();
                        int i = 250;
                        int j = i % 65536;
                        int k = i / 65536;
                        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
                        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                        TileEntityRendererDispatcher.field_147556_a.func_147549_a(te, (double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), this.field_146297_k.func_184121_ak());
                     }
                  } catch (Exception var26) {
                  }
               }
            }
         }
      }

      GlStateManager.func_179121_F();
      if (ingredients != null) {
         for(a = 0; a < ingredients.length; ++a) {
            if (ingredients[a] != null && ingredients[a].func_77973_b() != null) {
               RenderHelper.func_74520_c();
               GL11.glDisable(2896);
               GL11.glEnable(32826);
               GL11.glEnable(2903);
               GL11.glEnable(2896);
               this.drawStackAt(ingredients[a], x - 85 + a * 17, y + 90, mx, my, true);
               GL11.glDisable(2896);
               GL11.glDepthMask(true);
               GL11.glEnable(2929);
            }
         }
      }

      RenderHelper.func_74518_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179147_l();
      RenderHelper.func_74518_a();
   }

   private void drawAspects(int mx, int my) {
      this.allowWithPagePopup = true;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex4);
      int x = (this.field_146294_l - 256) / 2;
      int y = (this.field_146295_m - 256) / 2;
      GlStateManager.func_179097_i();
      this.func_73729_b(x, y, 0, 0, 255, 255);
      GlStateManager.func_179126_j();
      this.drawAspectPage(x + 60, y + 24, mx, my);
      this.allowWithPagePopup = false;
   }

   private void drawAspectPage(int x, int y, int mx, int my) {
      if (this.knownPlayerAspects != null && this.knownPlayerAspects.size() > 0) {
         GL11.glPushMatrix();
         int mposx = mx;
         int mposy = my;
         int count = -1;
         int start = aspectsPage * 5;
         Aspect[] var9 = this.knownPlayerAspects.getAspectsSortedByName();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Aspect aspect = var9[var11];
            ++count;
            if (count >= start) {
               if (count > start + 4) {
                  break;
               }

               if (aspect.getImage() != null) {
                  int ty = y + count % 5 * 40;
                  if (mposx >= x && mposy >= ty && mposx < x + 40 && mposy < ty + 40) {
                     this.field_146297_k.field_71446_o.func_110577_a(this.tex3);
                     GL11.glPushMatrix();
                     GlStateManager.func_179147_l();
                     GlStateManager.func_179112_b(770, 771);
                     GL11.glTranslated((double)(x - 2), (double)(y + count % 5 * 40 - 2), 0.0D);
                     GL11.glScaled(2.0D, 2.0D, 0.0D);
                     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.5F);
                     UtilsFX.drawTexturedQuadFull(0.0F, 0.0F, (double)this.field_73735_i);
                     GL11.glPopMatrix();
                  }

                  GL11.glPushMatrix();
                  GL11.glTranslated((double)(x + 2), (double)(y + 2 + count % 5 * 40), 0.0D);
                  GL11.glScalef(1.5F, 1.5F, 1.5F);
                  UtilsFX.drawTag(0, 0, aspect, 0.0F, 0, (double)this.field_73735_i);
                  GL11.glPopMatrix();
                  String text = aspect.getName();
                  int offset = this.field_146297_k.field_71466_p.func_78256_a(text) / 2;
                  this.field_146297_k.field_71466_p.func_78276_b(text, x + 16 - offset, y + 29 + count % 5 * 40, 5263440);
                  if (aspect.getComponents() != null) {
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(x + 60), (double)(y + 4 + count % 5 * 40), 0.0D);
                     GL11.glScalef(1.25F, 1.25F, 1.25F);
                     if (this.playerKnowledge.isResearchKnown("!" + aspect.getComponents()[0].getTag().toLowerCase())) {
                        UtilsFX.drawTag(0, 0, aspect.getComponents()[0], 0.0F, 0, (double)this.field_73735_i);
                     } else {
                        this.field_146297_k.field_71446_o.func_110577_a(this.dummyResearch);
                        GlStateManager.func_179131_c(0.8F, 0.8F, 0.8F, 1.0F);
                        UtilsFX.drawTexturedQuadFull(0.0F, 0.0F, (double)this.field_73735_i);
                     }

                     GL11.glPopMatrix();
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(x + 102), (double)(y + 4 + count % 5 * 40), 0.0D);
                     GL11.glScalef(1.25F, 1.25F, 1.25F);
                     if (this.playerKnowledge.isResearchKnown("!" + aspect.getComponents()[1].getTag().toLowerCase())) {
                        UtilsFX.drawTag(0, 0, aspect.getComponents()[1], 0.0F, 0, (double)this.field_73735_i);
                     } else {
                        this.field_146297_k.field_71446_o.func_110577_a(this.dummyResearch);
                        GlStateManager.func_179131_c(0.8F, 0.8F, 0.8F, 1.0F);
                        UtilsFX.drawTexturedQuadFull(0.0F, 0.0F, (double)this.field_73735_i);
                     }

                     GL11.glPopMatrix();
                     if (this.playerKnowledge.isResearchKnown("!" + aspect.getComponents()[0].getTag().toLowerCase())) {
                        text = aspect.getComponents()[0].getName();
                        offset = this.field_146297_k.field_71466_p.func_78256_a(text) / 2;
                        this.field_146297_k.field_71466_p.func_78276_b("§o" + text, x + 22 - offset + 50, y + 29 + count % 5 * 40, 5263440);
                     }

                     if (this.playerKnowledge.isResearchKnown("!" + aspect.getComponents()[1].getTag().toLowerCase())) {
                        text = aspect.getComponents()[1].getName();
                        offset = this.field_146297_k.field_71466_p.func_78256_a(text) / 2;
                        this.field_146297_k.field_71466_p.func_78276_b("§o" + text, x + 22 - offset + 92, y + 29 + count % 5 * 40, 5263440);
                     }

                     this.field_146297_k.field_71466_p.func_78276_b("=", x + 9 + 32, y + 12 + count % 5 * 40, 10066329);
                     this.field_146297_k.field_71466_p.func_78276_b("+", x + 10 + 79, y + 12 + count % 5 * 40, 10066329);
                  } else {
                     this.field_146297_k.field_71466_p.func_78276_b(I18n.func_74838_a("tc.aspect.primal"), x + 54, y + 12 + count % 5 * 40, 7829367);
                  }
               }
            }
         }

         this.field_146297_k.field_71446_o.func_110577_a(this.tex1);
         float bob = MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 3.0F) * 0.2F + 0.1F;
         if (aspectsPage > 0) {
            this.drawTexturedModalRectScaled(x - 20, y + 208, 0, 184, 12, 8, bob);
         }

         if (aspectsPage < this.maxAspectPages - 1) {
            this.drawTexturedModalRectScaled(x + 144, y + 208, 12, 184, 12, 8, bob);
         }

         GL11.glPopMatrix();
      }

   }

   private void drawArcaneCraftingPage(int x, int y, int mx, int my, IArcaneRecipe recipe) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glPushMatrix();
      this.field_146297_k.field_71446_o.func_110577_a(this.tex2);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glTranslatef((float)x, (float)y, 0.0F);
      GL11.glScalef(2.0F, 2.0F, 1.0F);
      this.func_73729_b(-26, -26, 112, 15, 52, 52);
      this.func_73729_b(-8, -46, 20, 3, 16, 16);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glTranslatef((float)x, (float)y, 0.0F);
      GL11.glScalef(2.0F, 2.0F, 1.0F);
      this.func_73729_b(-6, 40, 68, 76, 12, 12);
      GL11.glPopMatrix();
      String text = "" + recipe.getVis();
      int offset = this.field_146297_k.field_71466_p.func_78256_a(text);
      this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y + 90, 5263440);
      this.drawPopupAt(x - offset / 2 - 15, y + 75, 30, 30, mx, my, "wandtable.text1");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glTranslated(0.0D, 0.0D, 100.0D);
      this.drawStackAt(InventoryUtils.cycleItemStack(recipe.func_77571_b(), 0), x - 8, y - 84, mx, my, false);
      ItemStack[] crystals = recipe.getCrystals();
      int rw;
      int i;
      int i;
      int j;
      if (crystals != null) {
         rw = 0;
         i = crystals.length;
         ItemStack[] var11 = crystals;
         i = crystals.length;

         for(j = 0; j < i; ++j) {
            ItemStack crystal = var11[j];
            this.drawStackAt(InventoryUtils.cycleItemStack(crystal, rw), x + 4 - i * 10 + rw * 20, y + 59, mx, my, true);
            ++rw;
         }
      }

      if (recipe != null && recipe instanceof ShapedArcaneRecipe) {
         text = I18n.func_74838_a("recipe.type.arcane");
         offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
         rw = ((ShapedArcaneRecipe)recipe).width;
         i = ((ShapedArcaneRecipe)recipe).height;
         Object[] items = ((ShapedArcaneRecipe)recipe).getInput();

         for(i = 0; i < rw && i < 3; ++i) {
            for(j = 0; j < i && j < 3; ++j) {
               if (items[i + j * rw] != null) {
                  this.drawStackAt(InventoryUtils.cycleItemStack(items[i + j * rw], i + j * rw), x - 40 + i * 32, y - 40 + j * 32, mx, my, true);
               }
            }
         }
      }

      if (recipe != null && recipe instanceof ShapelessArcaneRecipe) {
         text = I18n.func_74838_a("recipe.type.arcane.shapeless");
         offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
         List<Object> items = ((ShapelessArcaneRecipe)recipe).getInput();

         for(i = 0; i < items.size() && i < 9; ++i) {
            if (items.get(i) != null) {
               this.drawStackAt(InventoryUtils.cycleItemStack(items.get(i), i), x - 40 + i % 3 * 32, y - 40 + i / 3 * 32, mx, my, true);
            }
         }
      }

      GL11.glPopMatrix();
   }

   private void drawCraftingPage(int x, int y, int mx, int my, IRecipe recipe) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (recipe != null) {
         GL11.glPushMatrix();
         this.field_146297_k.field_71446_o.func_110577_a(this.tex2);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GL11.glTranslatef((float)x, (float)y, 0.0F);
         GL11.glScalef(2.0F, 2.0F, 1.0F);
         this.func_73729_b(-26, -26, 60, 15, 51, 52);
         this.func_73729_b(-8, -46, 20, 3, 16, 16);
         GL11.glPopMatrix();
         this.drawStackAt(InventoryUtils.cycleItemStack(recipe.func_77571_b(), 0), x - 8, y - 84, mx, my, false);
         String text;
         int offset;
         int i;
         if (recipe != null && (recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe)) {
            text = I18n.func_74838_a("recipe.type.workbench");
            offset = this.field_146297_k.field_71466_p.func_78256_a(text);
            this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
            int rw = false;
            int rh = false;
            Object[] items = null;
            int rw;
            if (recipe instanceof ShapedRecipes) {
               rw = ((ShapedRecipes)recipe).field_77576_b;
               i = ((ShapedRecipes)recipe).field_77577_c;
               items = ((ShapedRecipes)recipe).field_77574_d;
            } else {
               rw = (Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, new String[]{"width"});
               i = (Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, new String[]{"height"});
               items = ((ShapedOreRecipe)recipe).getInput();
            }

            for(int i = 0; i < rw && i < 3; ++i) {
               for(int j = 0; j < i && j < 3; ++j) {
                  if (((Object[])items)[i + j * rw] != null) {
                     this.drawStackAt(InventoryUtils.cycleItemStack(((Object[])items)[i + j * rw], i + j * rw), x - 40 + i * 32, y - 40 + j * 32, mx, my, true);
                  }
               }
            }
         }

         if (recipe != null && (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe)) {
            text = I18n.func_74838_a("recipe.type.workbenchshapeless");
            offset = this.field_146297_k.field_71466_p.func_78256_a(text);
            this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
            List<Object> items = null;
            if (recipe instanceof ShapelessRecipes) {
               items = new ArrayList();
               items.addAll(((ShapelessRecipes)recipe).field_77579_b);
            } else {
               items = ((ShapelessOreRecipe)recipe).getInput();
            }

            for(i = 0; i < items.size() && i < 9; ++i) {
               if (items.get(i) != null) {
                  this.drawStackAt(InventoryUtils.cycleItemStack(items.get(i), i), x + -40 + i % 3 * 32, y - 40 + i / 3 * 32, mx, my, true);
               }
            }
         }

         GL11.glPopMatrix();
      }
   }

   private void drawCruciblePage(int x, int y, int mx, int my, CrucibleRecipe rc) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (rc != null) {
         GL11.glPushMatrix();
         String text = I18n.func_74838_a("recipe.type.crucible");
         int offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex2);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GL11.glTranslatef((float)x, (float)y, 0.0F);
         GL11.glScalef(2.0F, 2.0F, 1.0F);
         this.func_73729_b(-28, -29, 0, 3, 56, 17);
         GL11.glTranslatef(0.0F, 32.0F, 0.0F);
         this.func_73729_b(-28, -44, 0, 20, 56, 48);
         GL11.glTranslatef(0.0F, -8.0F, 0.0F);
         this.func_73729_b(-25, -50, 100, 84, 11, 13);
         GL11.glPopMatrix();
         int mposx = mx;
         int mposy = my;
         int total = 0;
         int rows = (rc.aspects.size() - 1) / 3;
         int shift = (3 - rc.aspects.size() % 3) * 10;
         int sx = x - 28;
         int sy = y + 8 - 10 * rows;
         Aspect[] var15 = rc.aspects.getAspectsSortedByName();
         int var16 = var15.length;

         int var17;
         Aspect tag;
         byte m;
         int vx;
         int vy;
         for(var17 = 0; var17 < var16; ++var17) {
            tag = var15[var17];
            m = 0;
            if (total / 3 >= rows && (rows > 1 || rc.aspects.size() < 3)) {
               m = 1;
            }

            vx = sx + total % 3 * 20 + shift * m;
            vy = sy + total / 3 * 20;
            UtilsFX.drawTag(vx, vy, tag, (float)rc.aspects.getAmount(tag), 0, (double)this.field_73735_i);
            ++total;
         }

         this.drawStackAt(rc.getRecipeOutput(), x - 8, y - 50, mx, my, false);
         this.drawStackAt(InventoryUtils.cycleItemStack(rc.catalyst, 0), x - 64, y - 56, mx, my, true);
         total = 0;
         var15 = rc.aspects.getAspectsSortedByName();
         var16 = var15.length;

         for(var17 = 0; var17 < var16; ++var17) {
            tag = var15[var17];
            m = 0;
            if (total / 3 >= rows && (rows > 1 || rc.aspects.size() < 3)) {
               m = 1;
            }

            vx = sx + total % 3 * 20 + shift * m;
            vy = sy + total / 3 * 20;
            if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
               this.tipText = Arrays.asList(tag.getName(), tag.getLocalizedDescription());
            }

            ++total;
         }

         GL11.glPopMatrix();
      }

   }

   private void drawInfusionPage(int x, int y, int mx, int my, InfusionRecipe ri) {
      if (ri != null) {
         Object[] components = ri.getComponents();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GL11.glPushMatrix();
         AspectList aspects = ri.getAspects();
         Object output = ri.getRecipeOutput();
         int mposy;
         int total;
         if (ri instanceof InfusionRunicAugmentRecipe) {
            ItemStack[] c = ((InfusionRunicAugmentRecipe)ri).getComponents((ItemStack)ri.getRecipeInput());
            components = c;
            ArrayList<ItemStack> com = new ArrayList();
            ItemStack[] var11 = c;
            mposy = c.length;

            for(total = 0; total < mposy; ++total) {
               ItemStack s = var11[total];
               com.add(s);
            }

            aspects = ri.getAspects(this.field_146297_k.field_71439_g, (ItemStack)ri.getRecipeInput(), com);
            output = ((InfusionRunicAugmentRecipe)ri).getRecipeOutput(this.field_146297_k.field_71439_g, (ItemStack)ri.getRecipeInput(), com);
         }

         int mposx;
         if (ri instanceof InfusionEnchantmentRecipe) {
            ArrayList<ItemStack> com = new ArrayList();
            Object var36 = components;
            mposx = ((Object[])components).length;

            for(mposy = 0; mposy < mposx; ++mposy) {
               Object s = ((Object[])var36)[mposy];
               if (s instanceof ItemStack) {
                  com.add((ItemStack)s);
               }
            }

            aspects = ri.getAspects(this.field_146297_k.field_71439_g, (ItemStack)ri.getRecipeInput(), com);
            output = ((InfusionEnchantmentRecipe)ri).getRecipeOutput((EntityPlayer)null, (ItemStack)ri.getRecipeInput(), com);
         }

         String text = I18n.func_74838_a("recipe.type.infusion");
         int offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y - 104, 5263440);
         this.field_146297_k.field_71446_o.func_110577_a(this.tex2);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GL11.glTranslatef((float)x, (float)(y + 20), 0.0F);
         GL11.glScalef(2.0F, 2.0F, 1.0F);
         this.func_73729_b(-28, -56, 0, 3, 56, 17);
         GL11.glTranslatef(0.0F, 19.0F, 0.0F);
         this.func_73729_b(-28, -55, 200, 77, 60, 44);
         GL11.glPopMatrix();
         mposx = mx;
         mposy = my;
         total = 0;
         int rows = (aspects.size() - 1) / 5;
         int shift = (5 - aspects.size() % 5) * 10;
         int sx = x - 48;
         int sy = y + 50 - 10 * rows;
         Aspect[] idisp = aspects.getAspectsSortedByName();
         int var19 = idisp.length;

         int le;
         int a;
         for(le = 0; le < var19; ++le) {
            Aspect tag = idisp[le];
            int m = 0;
            if (total / 5 >= rows && (rows > 1 || aspects.size() < 5)) {
               m = 1;
            }

            int vx = sx + total % 5 * 20 + shift * m;
            a = sy + total / 5 * 20;
            UtilsFX.drawTag(vx, a, tag, (float)aspects.getAmount(tag), 0, (double)this.field_73735_i);
            ++total;
         }

         idisp = null;
         ItemStack idisp;
         if (output instanceof ItemStack) {
            idisp = InventoryUtils.cycleItemStack((ItemStack)output);
         } else {
            idisp = InventoryUtils.cycleItemStack(ri.getRecipeInput()).func_77946_l();

            try {
               Object[] obj = (Object[])((Object[])output);
               NBTBase tag = (NBTBase)obj[1];
               idisp.func_77983_a((String)obj[0], tag);
            } catch (Exception var33) {
            }
         }

         this.drawStackAt(idisp, x - 8, y - 85, mx, my, false);
         ItemStack rinp = InventoryUtils.cycleItemStack(ri.getRecipeInput()).func_77946_l();
         this.drawStackAt(rinp, x - 8, y - 16, mx, my, true);
         GL11.glPushMatrix();
         GL11.glTranslated(0.0D, 0.0D, 100.0D);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         le = ((Object[])components).length;
         ArrayList<PosXY> coords = new ArrayList();
         float pieSlice = (float)(360 / le);
         float currentRot = -90.0F;

         int inst;
         int yy;
         for(a = 0; a < le; ++a) {
            inst = (int)(MathHelper.func_76134_b(currentRot / 180.0F * 3.1415927F) * 40.0F) - 8;
            yy = (int)(MathHelper.func_76126_a(currentRot / 180.0F * 3.1415927F) * 40.0F) - 8;
            currentRot += pieSlice;
            coords.add(new PosXY(inst, yy));
         }

         ArrayList<ItemStack> cmps = new ArrayList();
         total = 0;
         sx = x;
         sy = y - 8;
         Object var49 = components;
         yy = ((Object[])components).length;

         int var27;
         for(var27 = 0; var27 < yy; ++var27) {
            Object ingredient = ((Object[])var49)[var27];
            int vx = sx + ((PosXY)coords.get(total)).x;
            int vy = sy + ((PosXY)coords.get(total)).y;
            ItemStack is = InventoryUtils.cycleItemStack(ingredient);
            this.drawStackAt(is.func_77946_l().func_77979_a(1), vx, vy, mx, my, true);
            ++total;
            cmps.add(is.func_77946_l());
         }

         GL11.glPopMatrix();
         inst = Math.min(5, ri.getInstability(this.field_146297_k.field_71439_g, rinp, cmps) / 2);
         text = I18n.func_74838_a("tc.inst") + " " + I18n.func_74838_a("tc.inst." + inst);
         offset = this.field_146297_k.field_71466_p.func_78256_a(text);
         this.field_146297_k.field_71466_p.func_78276_b(text, x - offset / 2, y + 94, 5263440);
         total = 0;
         rows = (aspects.size() - 1) / 5;
         shift = (5 - aspects.size() % 5) * 10;
         sx = x - 48;
         sy = y + 50 - 10 * rows;
         Aspect[] var50 = aspects.getAspectsSortedByName();
         var27 = var50.length;

         for(int var51 = 0; var51 < var27; ++var51) {
            Aspect tag = var50[var51];
            int m = 0;
            if (total / 5 >= rows && (rows > 1 || aspects.size() < 5)) {
               m = 1;
            }

            int vx = sx + total % 5 * 20 + shift * m;
            int vy = sy + total / 5 * 20;
            if (mposx >= vx && mposy >= vy && mposx < vx + 16 && mposy < vy + 16) {
               this.tipText = Arrays.asList(tag.getName(), tag.getLocalizedDescription());
            }

            ++total;
         }

         GL11.glPopMatrix();
      }

   }

   protected void func_73869_a(char par1, int par2) throws IOException {
      if (par2 != this.field_146297_k.field_71474_y.field_151445_Q.func_151463_i() && par2 != 1) {
         if (par2 != 203 && par2 != 200 && par2 != 201) {
            if (par2 != 205 && par2 != 208 && par2 != 209) {
               if (par2 == 14) {
                  this.goBack();
               } else {
                  super.func_73869_a(par1, par2);
               }
            } else {
               this.nextPage();
            }
         } else {
            this.prevPage();
         }
      } else {
         history.clear();
         if (shownRecipe == null && !this.showingAspects) {
            this.field_146297_k.func_147108_a(new GuiResearchBrowser(this.guiMapX, this.guiMapY));
         } else {
            shownRecipe = null;
            this.blockAccess = null;
            this.showingAspects = false;
            Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.4F, 1.1F);
         }
      }

   }

   private void nextPage() {
      if (this.page < this.maxPages - 2) {
         this.page += 2;
         this.lastCycle = 0L;
         this.cycle = -1;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.66F, 1.0F);
      }

   }

   private void prevPage() {
      if (this.page >= 2) {
         this.page -= 2;
         this.lastCycle = 0L;
         this.cycle = -1;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.66F, 1.0F);
      }

   }

   private void goBack() {
      if (!history.isEmpty()) {
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.66F, 1.0F);
         shownRecipe = (String)history.pop();
         this.blockAccess = null;
         if (history.isEmpty()) {
            shownRecipe = null;
         }
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) {
      this.checkRequisites();
      int var4 = (this.field_146294_l - this.paneWidth) / 2;
      int var5 = (this.field_146295_m - this.paneHeight) / 2;
      int mx = par1 - this.hrx;
      int my = par2 - this.hry;
      if (shownRecipe == null && !this.hold && this.hasAllRequisites && mx >= 0 && my >= 0 && mx < 64 && my < 12) {
         PacketHandler.INSTANCE.sendToServer(new PacketSyncProgressToServer(this.research.getKey(), false, true, true));
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.write, 0.66F, 1.0F);
         this.lastCheck = 0L;
         this.lastStage = this.currentStage;
         this.hold = true;
         this.keyCache.clear();
         this.drilldownLists.clear();
      }

      if (this.knownPlayerAspects != null && this.playerKnowledge.isResearchComplete("FIRSTSTEPS")) {
         mx = par1 - (var4 - 48);
         my = par2 - (var5 + 8);
         if (mx >= 0 && my >= 0 && mx < 25 && my < 16) {
            shownRecipe = null;
            this.showingAspects = !this.showingAspects;
            this.blockAccess = null;
            history.clear();
            if (aspectsPage > this.maxAspectPages) {
               aspectsPage = 0;
            }

            Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
         }
      }

      mx = par1 - (var4 + 205);
      my = par2 - (var5 + 192);
      if (this.showingAspects && aspectsPage < this.maxAspectPages - 1 && mx >= 0 && my >= 0 && mx < 14 && my < 14) {
         ++aspectsPage;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
      }

      mx = par1 - (var4 + 38);
      my = par2 - (var5 + 192);
      if (this.showingAspects && aspectsPage > 0 && mx >= 0 && my >= 0 && mx < 14 && my < 14) {
         --aspectsPage;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
      }

      int aa = 0;

      Iterator var9;
      for(var9 = this.recipeLists.keySet().iterator(); var9.hasNext(); ++aa) {
         String rk = (String)var9.next();
         mx = par1 - (var4 + 280);
         my = par2 - (var5 + 3 + aa * 24);
         if (mx >= 0 && my >= 0 && mx < 30 && my < 16) {
            if (rk.equals(shownRecipe)) {
               shownRecipe = null;
            } else {
               shownRecipe = rk;
            }

            this.showingAspects = false;
            this.blockAccess = null;
            history.clear();
            Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
            break;
         }
      }

      mx = par1 - (var4 + 205);
      my = par2 - (var5 + 192);
      if (this.hasRecipePages && this.recipePage < this.recipePageMax && mx >= 0 && my >= 0 && mx < 14 && my < 14) {
         ++this.recipePage;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
      }

      mx = par1 - (var4 + 38);
      my = par2 - (var5 + 192);
      if (this.hasRecipePages && this.recipePage > 0 && mx >= 0 && my >= 0 && mx < 14 && my < 14) {
         --this.recipePage;
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.7F, 0.9F);
      }

      mx = par1 - (var4 + 261);
      my = par2 - (var5 + 189);
      if (shownRecipe == null && mx >= 0 && my >= 0 && mx < 14 && my < 10) {
         this.nextPage();
      }

      mx = par1 - (var4 - 17);
      my = par2 - (var5 + 189);
      if (shownRecipe == null && mx >= 0 && my >= 0 && mx < 14 && my < 10) {
         this.prevPage();
      }

      mx = par1 - (var4 + 118);
      my = par2 - (var5 + 190);
      if (mx >= 0 && my >= 0 && mx < 20 && my < 12) {
         this.goBack();
      }

      mx = par1 - (var4 + 210);
      my = par2 - (var5 + 190);
      if (this.renderingCompound && mx >= 0 && my >= 0 && mx < 10 && my < 10) {
         Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.clack, 0.66F, 1.0F);
         cycleMultiblockLines = !cycleMultiblockLines;
      }

      if (this.reference.size() > 0) {
         var9 = this.reference.iterator();

         while(var9.hasNext()) {
            List coords = (List)var9.next();
            if (par1 >= (Integer)coords.get(0) && par2 >= (Integer)coords.get(1) && par1 < (Integer)coords.get(0) + 16 && par2 < (Integer)coords.get(1) + 16) {
               Minecraft.func_71410_x().field_71439_g.func_184185_a(SoundsTC.page, 0.66F, 1.0F);
               history.push(shownRecipe);
               this.recipePage = Integer.parseInt((String)coords.get(2));
               shownRecipe = (String)coords.get(3);
               if (!this.drilldownLists.containsKey(shownRecipe)) {
                  this.addRecipesToList(shownRecipe, this.drilldownLists, (LinkedHashMap)null);
               }

               this.blockAccess = null;
            }
         }
      }

      try {
         super.func_73864_a(par1, par2, par3);
      } catch (IOException var11) {
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   void drawPopupAt(int x, int y, int mx, int my, String text) {
      if ((shownRecipe == null || this.allowWithPagePopup) && mx >= x && my >= y && mx < x + 16 && my < y + 16) {
         ArrayList<String> s = new ArrayList();
         s.add(I18n.func_74838_a(text));
         this.tipText = s;
      }

   }

   void drawPopupAt(int x, int y, int w, int h, int mx, int my, String text) {
      if ((shownRecipe == null || this.allowWithPagePopup) && mx >= x && my >= y && mx < x + w && my < y + h) {
         ArrayList<String> s = new ArrayList();
         s.add(I18n.func_74838_a(text));
         this.tipText = s;
      }

   }

   boolean mouseInside(int x, int y, int w, int h, int mx, int my) {
      return mx >= x && my >= y && mx < x + w && my < y + h;
   }

   void drawStackAt(ItemStack itemstack, int x, int y, int mx, int my, boolean clickthrough) {
      UtilsFX.renderItemStack(this.field_146297_k, itemstack, x, y, (String)null);
      if ((shownRecipe == null || this.allowWithPagePopup) && mx >= x && my >= y && mx < x + 16 && my < y + 16 && itemstack != null && itemstack.func_77973_b() != null) {
         if (clickthrough) {
            List addtext = itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
            String ref = this.getCraftingRecipeKey(this.field_146297_k.field_71439_g, itemstack);
            if (ref != null) {
               if (ref.equals("UNKNOWN")) {
                  addtext.add(TextFormatting.DARK_RED + "" + TextFormatting.ITALIC + I18n.func_74838_a("recipe.unknown"));
               } else {
                  addtext.add(TextFormatting.BLUE + "" + TextFormatting.ITALIC + I18n.func_74838_a("recipe.clickthrough"));
                  String[] q = ref.split(";", 2);
                  this.reference.add(Arrays.asList(mx, my, q[0], q[1]));
               }
            }

            this.tipText = addtext;
         } else {
            this.tipText = itemstack.func_82840_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71474_y.field_82882_x);
         }
      }

   }

   public void drawTexturedModalRectScaled(int par1, int par2, int par3, int par4, int par5, int par6, float scale) {
      GL11.glPushMatrix();
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.func_178181_a();
      GL11.glTranslatef((float)par1 + (float)par5 / 2.0F, (float)par2 + (float)par6 / 2.0F, 0.0F);
      GL11.glScalef(1.0F + scale, 1.0F + scale, 1.0F);
      var9.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      var9.func_178180_c().func_181662_b((double)((float)(-par5) / 2.0F), (double)((float)par6 / 2.0F), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * var7), (double)((float)(par4 + par6) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)((float)par5 / 2.0F), (double)((float)par6 / 2.0F), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * var7), (double)((float)(par4 + par6) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)((float)par5 / 2.0F), (double)((float)(-par6) / 2.0F), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * var7), (double)((float)(par4 + 0) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)((float)(-par5) / 2.0F), (double)((float)(-par6) / 2.0F), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * var7), (double)((float)(par4 + 0) * var8)).func_181675_d();
      var9.func_78381_a();
      GL11.glPopMatrix();
   }

   private void parsePages() {
      this.checkRequisites();
      this.pages.clear();
      if (this.research.getStages() != null) {
         boolean complete = false;

         for(this.currentStage = ThaumcraftCapabilities.getKnowledge(this.field_146297_k.field_71439_g).getResearchStage(this.research.getKey()) - 1; this.currentStage >= this.research.getStages().length; complete = true) {
            --this.currentStage;
         }

         if (this.currentStage < 0) {
            this.currentStage = 0;
         }

         ResearchStage stage = this.research.getStages()[this.currentStage];
         ResearchAddendum[] addenda = null;
         if (this.research.getAddenda() != null && complete) {
            addenda = this.research.getAddenda();
         }

         this.generateRecipesLists(stage, addenda);
         String rawText = stage.getTextLocalized();
         int var8;
         if (addenda != null) {
            int ac = 0;
            ResearchAddendum[] var6 = addenda;
            int var7 = addenda.length;

            for(var8 = 0; var8 < var7; ++var8) {
               ResearchAddendum addendum = var6[var8];
               if (ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, addendum.getResearch())) {
                  ++ac;
                  ITextComponent text = new TextComponentTranslation("tc.addendumtext", new Object[]{ac});
                  rawText = rawText + "<PAGE>" + text.func_150254_d() + "<BR>" + addendum.getTextLocalized();
               }
            }
         }

         rawText = rawText.replaceAll("<BR>", "ï¿½\n\n");
         rawText = rawText.replaceAll("<BR/>", "ï¿½\n\n");
         rawText = rawText.replaceAll("<LINE>", "ï¿½");
         rawText = rawText.replaceAll("<LINE/>", "ï¿½");
         rawText = rawText.replaceAll("<DIV>", "ï¿½");
         rawText = rawText.replaceAll("<DIV/>", "ï¿½");
         rawText = rawText.replaceAll("<PAGE>", "ï¿½");
         rawText = rawText.replaceAll("<PAGE/>", "ï¿½");
         ArrayList<GuiResearchPage.PageImage> images = new ArrayList();
         String[] imgSplit = rawText.split("<IMG>");
         String[] var23 = imgSplit;
         var8 = imgSplit.length;

         int heightRemaining;
         int a;
         String t;
         for(a = 0; a < var8; ++a) {
            t = var23[a];
            heightRemaining = t.indexOf("</IMG>");
            if (heightRemaining >= 0) {
               String clean = t.substring(0, heightRemaining);
               GuiResearchPage.PageImage pi = GuiResearchPage.PageImage.parse(clean);
               if (pi == null) {
                  rawText = rawText.replaceFirst(clean, "\n");
               } else {
                  images.add(pi);
                  rawText = rawText.replaceFirst(clean, "ï¿½");
               }
            }
         }

         rawText = rawText.replaceAll("<IMG>", "");
         rawText = rawText.replaceAll("</IMG>", "");
         List<String> firstPassText = new ArrayList();
         String[] temp = rawText.split("ï¿½");

         int b;
         String line;
         for(a = 0; a < temp.length; ++a) {
            t = temp[a];
            String[] temp1 = t.split("ï¿½");

            for(int x = 0; x < temp1.length; ++x) {
               String t1 = temp1[x];
               String[] temp2 = t1.split("ï¿½");

               for(b = 0; b < temp2.length; ++b) {
                  line = temp2[b];
                  String[] temp3 = line.split("ï¿½");

                  for(int c = 0; c < temp3.length; ++c) {
                     String t3 = temp3[c];
                     firstPassText.add(t3);
                     if (c != temp3.length - 1) {
                        firstPassText.add("ï¿½");
                     }
                  }

                  if (b != temp2.length - 1) {
                     firstPassText.add("ï¿½");
                  }
               }

               if (x != temp1.length - 1) {
                  firstPassText.add("ï¿½");
               }
            }

            if (a != temp.length - 1) {
               firstPassText.add("ï¿½");
            }
         }

         List<String> parsedText = new ArrayList();
         Iterator var29 = firstPassText.iterator();

         while(var29.hasNext()) {
            String s = (String)var29.next();
            List<String> pt1 = this.field_146297_k.field_71466_p.func_78271_c(s, 140);
            Iterator var37 = pt1.iterator();

            while(var37.hasNext()) {
               String ln = (String)var37.next();
               parsedText.add(ln);
            }
         }

         int lineHeight = this.field_146297_k.field_71466_p.field_78288_b;
         heightRemaining = 182;
         int dividerSpace = 0;
         if (this.research.getKey().equals("KNOWLEDGETYPES")) {
            int tc = 0;
            IPlayerKnowledge.EnumKnowledgeType[] var40 = IPlayerKnowledge.EnumKnowledgeType.values();
            b = var40.length;

            label185:
            for(int var44 = 0; var44 < b; ++var44) {
               IPlayerKnowledge.EnumKnowledgeType type = var40[var44];
               Iterator var46 = ResearchCategories.researchCategories.values().iterator();

               int amt;
               do {
                  ResearchCategory category;
                  do {
                     if (!var46.hasNext()) {
                        continue label185;
                     }

                     category = (ResearchCategory)var46.next();
                  } while(!type.hasFields() && category != null);

                  amt = this.playerKnowledge.getKnowledge(type, category);
               } while(amt <= 0);

               ++tc;
            }

            heightRemaining -= 20 * tc;
            dividerSpace = 12;
         }

         if (!this.isComplete) {
            if (stage.getCraft() != null) {
               heightRemaining -= 18;
               dividerSpace = 15;
            }

            if (stage.getObtain() != null) {
               heightRemaining -= 18;
               dividerSpace = 15;
            }

            if (stage.getKnow() != null) {
               heightRemaining -= 18;
               dividerSpace = 15;
            }

            if (stage.getResearch() != null) {
               heightRemaining -= 18;
               dividerSpace = 15;
            }
         }

         heightRemaining -= dividerSpace;
         GuiResearchPage.Page page1 = new GuiResearchPage.Page();
         ArrayList<GuiResearchPage.PageImage> tempImages = new ArrayList();
         Iterator var43 = parsedText.iterator();

         while(var43.hasNext()) {
            line = (String)var43.next();
            if (line.contains("ï¿½")) {
               if (!images.isEmpty()) {
                  tempImages.add(images.remove(0));
               }

               line = "";
            }

            if (line.contains("ï¿½")) {
               tempImages.add(PILINE);
               line = "";
            }

            if (line.contains("ï¿½")) {
               tempImages.add(PIDIV);
               line = "";
            }

            if (line.contains("ï¿½")) {
               heightRemaining = 210;
               this.pages.add(page1.copy());
               page1 = new GuiResearchPage.Page();
               line = "";
            }

            if (!line.isEmpty()) {
               line = line.trim();
               page1.contents.add(line);
               heightRemaining -= lineHeight;
               if (line.endsWith("ï¿½")) {
                  heightRemaining = (int)((double)heightRemaining - (double)lineHeight * 0.66D);
               }
            }

            while(!tempImages.isEmpty() && heightRemaining >= ((GuiResearchPage.PageImage)tempImages.get(0)).ah + 2) {
               heightRemaining -= ((GuiResearchPage.PageImage)tempImages.get(0)).ah + 2;
               page1.contents.add(tempImages.remove(0));
            }

            if (heightRemaining < lineHeight && !page1.contents.isEmpty()) {
               heightRemaining = 210;
               this.pages.add(page1.copy());
               page1 = new GuiResearchPage.Page();
            }
         }

         if (!page1.contents.isEmpty()) {
            this.pages.add(page1.copy());
         }

         page1 = new GuiResearchPage.Page();
         heightRemaining = 210;

         while(!tempImages.isEmpty()) {
            if (heightRemaining < ((GuiResearchPage.PageImage)tempImages.get(0)).ah + 2) {
               heightRemaining = 210;
               this.pages.add(page1.copy());
               page1 = new GuiResearchPage.Page();
            } else {
               heightRemaining -= ((GuiResearchPage.PageImage)tempImages.get(0)).ah + 2;
               page1.contents.add(tempImages.remove(0));
            }
         }

         if (!page1.contents.isEmpty()) {
            this.pages.add(page1.copy());
         }

         this.rhash = this.research.getKey().hashCode() + this.currentStage * 50;
      }
   }

   private void checkRequisites() {
      if (this.research.getStages() != null) {
         for(this.isComplete = this.playerKnowledge.isResearchComplete(this.research.getKey()); this.currentStage >= this.research.getStages().length; --this.currentStage) {
         }

         if (this.currentStage < 0) {
            return;
         }

         this.hasAllRequisites = true;
         this.hasItem = null;
         this.hasCraft = null;
         this.hasResearch = null;
         this.hasKnow = null;
         ResearchStage stage = this.research.getStages()[this.currentStage];
         ItemStack[] o = stage.getObtain();
         if (o != null) {
            this.hasItem = new boolean[o.length];

            for(int a = 0; a < o.length; ++a) {
               this.hasItem[a] = InventoryUtils.isPlayerCarryingAmount(this.field_146297_k.field_71439_g, o[a], false);
               if (!this.hasItem[a]) {
                  this.hasAllRequisites = false;
               }
            }
         }

         ItemStack[] c = stage.getCraft();
         if (c != null) {
            this.hasCraft = new boolean[c.length];

            for(int a = 0; a < c.length; ++a) {
               if (!this.playerKnowledge.isResearchKnown("[#]" + stage.getCraftReference()[a])) {
                  this.hasAllRequisites = false;
                  this.hasCraft[a] = false;
               } else {
                  this.hasCraft[a] = true;
               }
            }
         }

         String[] r = stage.getResearch();
         if (r != null) {
            this.hasResearch = new boolean[r.length];

            for(int a = 0; a < r.length; ++a) {
               if (!ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, r[a])) {
                  this.hasAllRequisites = false;
                  this.hasResearch[a] = false;
               } else {
                  this.hasResearch[a] = true;
               }
            }
         }

         ResearchStage.Knowledge[] k = stage.getKnow();
         if (k != null) {
            this.hasKnow = new boolean[k.length];

            for(int a = 0; a < k.length; ++a) {
               int pk = this.playerKnowledge.getKnowledge(k[a].type, k[a].category);
               if (pk < k[a].amount) {
                  this.hasAllRequisites = false;
                  this.hasKnow[a] = false;
               } else {
                  this.hasKnow[a] = true;
               }
            }
         }
      }

   }

   private int findRecipe(String rec, ItemStack stack) {
      Object recipe = CommonInternals.craftingRecipeCatalog.get(rec);
      int page1;
      int var6;
      int var7;
      if (recipe instanceof CrucibleRecipe[]) {
         page1 = 0;
         CrucibleRecipe[] var5 = (CrucibleRecipe[])((CrucibleRecipe[])recipe);
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            CrucibleRecipe r = var5[var7];
            if (r.getRecipeOutput().func_77969_a(stack)) {
               if (!ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, r.research)) {
                  return -99;
               }

               return page1;
            }

            ++page1;
         }
      }

      if (recipe instanceof InfusionRecipe[]) {
         page1 = 0;
         InfusionRecipe[] var9 = (InfusionRecipe[])((InfusionRecipe[])recipe);
         var6 = var9.length;

         for(var7 = 0; var7 < var6; ++var7) {
            InfusionRecipe r = var9[var7];
            if (r.getRecipeOutput() instanceof ItemStack && ((ItemStack)r.getRecipeOutput()).func_77969_a(stack)) {
               if (!ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, r.getResearch())) {
                  return -99;
               }

               return page1;
            }

            ++page1;
         }
      }

      if (recipe instanceof IRecipe[]) {
         page1 = 0;
         IRecipe[] var10 = (IRecipe[])((IRecipe[])recipe);
         var6 = var10.length;

         for(var7 = 0; var7 < var6; ++var7) {
            IRecipe r = var10[var7];
            if (r.func_77571_b().func_77969_a(stack)) {
               if (r instanceof IArcaneRecipe && !ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, ((IArcaneRecipe)r).getResearch())) {
                  return -99;
               }

               return page1;
            }

            ++page1;
         }
      }

      if (recipe instanceof RecipeMisc[]) {
         page1 = 0;
         RecipeMisc[] var11 = (RecipeMisc[])((RecipeMisc[])recipe);
         var6 = var11.length;

         for(var7 = 0; var7 < var6; ++var7) {
            RecipeMisc r = var11[var7];
            if (r.getOutput().func_77969_a(stack)) {
               return page1;
            }

            ++page1;
         }
      }

      return -1;
   }

   private String getCraftingRecipeKey(EntityPlayer player, ItemStack stack) {
      int key = stack.serializeNBT().toString().hashCode();
      if (this.keyCache.containsKey(key)) {
         return (String)this.keyCache.get(key);
      } else {
         Iterator var4 = ResearchCategories.researchCategories.values().iterator();

         label68:
         while(var4.hasNext()) {
            ResearchCategory rcl = (ResearchCategory)var4.next();
            Iterator var6 = rcl.research.values().iterator();

            while(true) {
               ResearchEntry ri;
               do {
                  if (!var6.hasNext()) {
                     continue label68;
                  }

                  ri = (ResearchEntry)var6.next();
               } while(ri.getStages() == null);

               for(int a = 0; a < ri.getStages().length; ++a) {
                  ResearchStage stage = ri.getStages()[a];
                  if (stage.getRecipes() != null) {
                     String[] var10 = stage.getRecipes();
                     int var11 = var10.length;

                     for(int var12 = 0; var12 < var11; ++var12) {
                        String rec = var10[var12];
                        int page1 = this.findRecipe(rec, stack);
                        if (page1 != -1) {
                           String s = page1 + ";" + rec;
                           if (page1 == -99) {
                              s = "UNKNOWN";
                           }

                           this.keyCache.put(key, s);
                           return s;
                        }
                     }
                  }
               }

               Iterator var16 = CommonInternals.craftingRecipesUnlinked.iterator();

               while(var16.hasNext()) {
                  String rec = (String)var16.next();
                  int page1 = this.findRecipe(rec, stack);
                  if (page1 != -1) {
                     this.keyCache.put(key, page1 + ";" + rec);
                     return page1 + ";" + rec;
                  }
               }
            }
         }

         this.keyCache.put(key, (Object)null);
         return null;
      }
   }

   public static class BlueprintBlockAccess implements IBlockAccess {
      private final Part[][][] data;
      private IBlockState[][][] structure;
      public int sliceLine = 0;

      public BlueprintBlockAccess(Part[][][] data, boolean target) {
         this.data = new Part[data.length][data[0].length][data[0][0].length];

         int y;
         int x;
         int z;
         for(y = 0; y < data.length; ++y) {
            for(x = 0; x < data[0].length; ++x) {
               for(z = 0; z < data[0][0].length; ++z) {
                  this.data[y][x][z] = data[y][x][z];
               }
            }
         }

         this.structure = new IBlockState[data.length][data[0].length][data[0][0].length];
         if (target) {
            for(y = 0; y < this.data.length; ++y) {
               Matrix matrix = new Matrix(this.data[y]);
               matrix.Rotate90DegRight(3);
               this.data[y] = matrix.getMatrix();
            }
         }

         for(y = 0; y < data.length; ++y) {
            for(x = 0; x < data[0].length; ++x) {
               for(z = 0; z < data[0][0].length; ++z) {
                  this.structure[data.length - y - 1][x][z] = target ? this.convertTarget(x, y, z) : this.convert(x, y, z);
               }
            }
         }

      }

      private IBlockState convert(int x, int y, int z) {
         if (this.data[y][x][z] != null && this.data[y][x][z].getSource() != null) {
            if (this.data[y][x][z].getSource() instanceof ItemStack && Block.func_149634_a(((ItemStack)this.data[y][x][z].getSource()).func_77973_b()) != null) {
               return Block.func_149634_a(((ItemStack)this.data[y][x][z].getSource()).func_77973_b()).func_176203_a(((ItemStack)this.data[y][x][z].getSource()).func_77952_i());
            } else if (this.data[y][x][z].getSource() instanceof Block) {
               return ((Block)this.data[y][x][z].getSource()).func_176223_P();
            } else if (this.data[y][x][z].getSource() instanceof IBlockState) {
               return (IBlockState)this.data[y][x][z].getSource();
            } else {
               if (this.data[y][x][z].getSource() instanceof Material) {
                  if ((Material)this.data[y][x][z].getSource() == Material.field_151587_i) {
                     return Blocks.field_150353_l.func_176223_P();
                  }

                  if ((Material)this.data[y][x][z].getSource() == Material.field_151586_h) {
                     return Blocks.field_150355_j.func_176223_P();
                  }
               }

               return Blocks.field_150350_a.func_176223_P();
            }
         } else {
            return Blocks.field_150350_a.func_176223_P();
         }
      }

      private IBlockState convertTarget(int x, int y, int z) {
         if (this.data[y][x][z] == null) {
            return Blocks.field_150350_a.func_176223_P();
         } else if (this.data[y][x][z].getTarget() == null) {
            return this.convert(x, y, z);
         } else if (this.data[y][x][z].getTarget() instanceof ItemStack && Block.func_149634_a(((ItemStack)this.data[y][x][z].getTarget()).func_77973_b()) != null) {
            return Block.func_149634_a(((ItemStack)this.data[y][x][z].getTarget()).func_77973_b()).func_176203_a(((ItemStack)this.data[y][x][z].getTarget()).func_77952_i());
         } else if (this.data[y][x][z].getTarget() instanceof Block) {
            return ((Block)this.data[y][x][z].getTarget()).func_176223_P();
         } else if (this.data[y][x][z].getTarget() instanceof IBlockState) {
            return (IBlockState)this.data[y][x][z].getTarget();
         } else {
            if (this.data[y][x][z].getTarget() instanceof Material) {
               if ((Material)this.data[y][x][z].getTarget() == Material.field_151587_i) {
                  return Blocks.field_150353_l.func_176223_P();
               }

               if ((Material)this.data[y][x][z].getTarget() == Material.field_151586_h) {
                  return Blocks.field_150355_j.func_176223_P();
               }
            }

            return Blocks.field_150350_a.func_176223_P();
         }
      }

      @Nullable
      public TileEntity func_175625_s(BlockPos pos) {
         return null;
      }

      public int func_175626_b(BlockPos pos, int lightValue) {
         return 15728880;
      }

      public IBlockState func_180495_p(BlockPos pos) {
         int x = pos.func_177958_n();
         int y = pos.func_177956_o();
         int z = pos.func_177952_p();
         if (this.sliceLine > this.structure.length) {
            this.sliceLine = 0;
         }

         return y >= 0 && y < this.structure.length - this.sliceLine && x >= 0 && x < this.structure[y].length && z >= 0 && z < this.structure[y][x].length ? this.structure[y][x][z] : Blocks.field_150350_a.func_176223_P();
      }

      public boolean func_175623_d(BlockPos pos) {
         return this.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a;
      }

      public int func_175627_a(BlockPos pos, EnumFacing direction) {
         return 0;
      }

      public WorldType func_175624_G() {
         return null;
      }

      public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
         return false;
      }

      public Biome func_180494_b(BlockPos pos) {
         return null;
      }
   }

   private static class PageImage {
      int x;
      int y;
      int w;
      int h;
      int aw;
      int ah;
      float scale;
      ResourceLocation loc;

      public static GuiResearchPage.PageImage parse(String text) {
         String[] s = text.split(":");
         if (s.length != 7) {
            return null;
         } else {
            try {
               GuiResearchPage.PageImage pi = new GuiResearchPage.PageImage();
               pi.loc = new ResourceLocation(s[0], s[1]);
               pi.x = Integer.parseInt(s[2]);
               pi.y = Integer.parseInt(s[3]);
               pi.w = Integer.parseInt(s[4]);
               pi.h = Integer.parseInt(s[5]);
               pi.scale = Float.parseFloat(s[6]);
               pi.aw = (int)((float)pi.w * pi.scale);
               pi.ah = (int)((float)pi.h * pi.scale);
               return pi.ah <= 208 && pi.aw <= 140 ? pi : null;
            } catch (Exception var3) {
               return null;
            }
         }
      }
   }

   private class Page {
      ArrayList contents;

      private Page() {
         this.contents = new ArrayList();
      }

      public GuiResearchPage.Page copy() {
         GuiResearchPage.Page p = GuiResearchPage.this.new Page();
         p.contents.addAll(this.contents);
         return p;
      }

      // $FF: synthetic method
      Page(Object x1) {
         this();
      }
   }
}
