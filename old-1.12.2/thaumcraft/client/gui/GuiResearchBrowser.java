package thaumcraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigResearch;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncProgressToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearchFlagsToServer;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.InventoryUtils;

@SideOnly(Side.CLIENT)
public class GuiResearchBrowser extends GuiScreen {
   private static int guiBoundsLeft;
   private static int guiBoundsTop;
   private static int guiBoundsRight;
   private static int guiBoundsBottom;
   protected int mouseX = 0;
   protected int mouseY = 0;
   protected float screenZoom = 1.0F;
   protected double curMouseX;
   protected double curMouseY;
   protected double guiMapX;
   protected double guiMapY;
   protected double tempMapX;
   protected double tempMapY;
   private int isMouseButtonDown = 0;
   public static double lastX = -9999.0D;
   public static double lastY = -9999.0D;
   GuiResearchBrowser instance = null;
   private int screenX;
   private int screenY;
   private int startX = 16;
   private int startY = 16;
   long t = 0L;
   private LinkedList<ResearchEntry> research = new LinkedList();
   static String selectedCategory = null;
   private ResearchEntry currentHighlight = null;
   private EntityPlayer player = null;
   long popuptime = 0L;
   String popupmessage = "";
   private GuiTextField searchField;
   private static boolean searching = false;
   private ArrayList<String> categoriesTC = new ArrayList();
   private ArrayList<String> categoriesOther = new ArrayList();
   static int catScrollPos = 0;
   static int catScrollMax = 0;
   public int addonShift = 0;
   private ArrayList<String> invisible = new ArrayList();
   ArrayList<Pair<String, GuiResearchBrowser.SearchResult>> searchResults = new ArrayList();
   ResourceLocation tx1 = new ResourceLocation("thaumcraft", "textures/gui/gui_research_browser.png");

   public GuiResearchBrowser() {
      this.curMouseX = this.guiMapX = this.tempMapX = lastX;
      this.curMouseY = this.guiMapY = this.tempMapY = lastY;
      this.player = Minecraft.func_71410_x().field_71439_g;
      this.instance = this;
   }

   public GuiResearchBrowser(double x, double y) {
      this.curMouseX = this.guiMapX = this.tempMapX = x;
      this.curMouseY = this.guiMapY = this.tempMapY = y;
      this.player = Minecraft.func_71410_x().field_71439_g;
      this.instance = this;
   }

   public void func_73866_w_() {
      this.updateResearch();
   }

   public void updateResearch() {
      if (this.field_146297_k == null) {
         this.field_146297_k = Minecraft.func_71410_x();
      }

      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiResearchBrowser.GuiSearchButton(2, 1, this.field_146295_m - 17, 16, 16, I18n.func_74837_a("tc.search", new Object[0])));
      Keyboard.enableRepeatEvents(true);
      this.searchField = new GuiTextField(0, this.field_146289_q, 20, 20, 89, this.field_146289_q.field_78288_b);
      this.searchField.func_146203_f(15);
      this.searchField.func_146185_a(true);
      this.searchField.func_146189_e(false);
      this.searchField.func_146193_g(16777215);
      if (searching) {
         this.searchField.func_146189_e(true);
         this.searchField.func_146205_d(false);
         this.searchField.func_146195_b(true);
         this.searchField.func_146180_a("");
         this.updateSearch();
      }

      this.screenX = this.field_146294_l - 32;
      this.screenY = this.field_146295_m - 32;
      this.research.clear();
      if (selectedCategory == null) {
         Collection cats = ResearchCategories.researchCategories.keySet();
         selectedCategory = (String)cats.iterator().next();
      }

      int limit = (int)Math.floor((double)((float)(this.screenY - 28) / 24.0F));
      this.addonShift = 0;
      int count = 0;
      this.categoriesTC.clear();
      this.categoriesOther.clear();
      Iterator var3 = ResearchCategories.researchCategories.keySet().iterator();

      while(true) {
         label112:
         while(true) {
            String rcl;
            int v;
            ResearchCategory rc;
            do {
               if (!var3.hasNext()) {
                  if (count > limit || count < catScrollPos) {
                     this.addonShift = (this.screenY - 28) % 24 / 2;
                     this.field_146292_n.add(new GuiResearchBrowser.GuiScrollButton(false, 3, this.field_146294_l - 14, 20, 10, 11, ""));
                     this.field_146292_n.add(new GuiResearchBrowser.GuiScrollButton(true, 4, this.field_146294_l - 14, this.screenY + 1, 10, 11, ""));
                  }

                  catScrollMax = count - limit;
                  if (selectedCategory != null && !selectedCategory.equals("")) {
                     Collection col = ResearchCategories.getResearchCategory(selectedCategory).research.values();
                     Iterator var16 = col.iterator();

                     while(var16.hasNext()) {
                        Object res = var16.next();
                        this.research.add((ResearchEntry)res);
                     }

                     guiBoundsLeft = 99999;
                     guiBoundsTop = 99999;
                     guiBoundsRight = -99999;
                     guiBoundsBottom = -99999;
                     var16 = this.research.iterator();

                     while(var16.hasNext()) {
                        ResearchEntry res = (ResearchEntry)var16.next();
                        if (res != null && this.isVisible(res)) {
                           if (res.getDisplayColumn() * 24 - this.screenX + 48 < guiBoundsLeft) {
                              guiBoundsLeft = res.getDisplayColumn() * 24 - this.screenX + 48;
                           }

                           if (res.getDisplayColumn() * 24 - 24 > guiBoundsRight) {
                              guiBoundsRight = res.getDisplayColumn() * 24 - 24;
                           }

                           if (res.getDisplayRow() * 24 - this.screenY + 48 < guiBoundsTop) {
                              guiBoundsTop = res.getDisplayRow() * 24 - this.screenY + 48;
                           }

                           if (res.getDisplayRow() * 24 - 24 > guiBoundsBottom) {
                              guiBoundsBottom = res.getDisplayRow() * 24 - 24;
                           }
                        }
                     }

                     return;
                  }

                  return;
               }

               rcl = (String)var3.next();
               int rt = 0;
               int rco = 0;
               Collection col = ResearchCategories.getResearchCategory(rcl).research.values();
               Iterator var8 = col.iterator();

               while(var8.hasNext()) {
                  Object res = var8.next();
                  if (!((ResearchEntry)res).hasMeta(ResearchEntry.EnumResearchMeta.AUTOUNLOCK)) {
                     ++rt;
                     if (ThaumcraftCapabilities.knowsResearch(this.player, ((ResearchEntry)res).getKey())) {
                        ++rco;
                     }
                  }
               }

               v = (int)((float)rco / (float)rt * 100.0F);
               rc = ResearchCategories.getResearchCategory(rcl);
            } while(rc.researchKey != null && !ThaumcraftCapabilities.knowsResearchStrict(this.player, rc.researchKey));

            String[] var10 = ConfigResearch.TCCategories;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               String tcc = var10[var12];
               if (tcc.equals(rcl)) {
                  this.categoriesTC.add(rcl);
                  this.field_146292_n.add(new GuiResearchBrowser.GuiCategoryButton(rc, rcl, false, 20 + this.categoriesTC.size(), 1, 10 + this.categoriesTC.size() * 24, 16, 16, I18n.func_74837_a("tc.research_category." + rcl, new Object[0]), v));
                  continue label112;
               }
            }

            ++count;
            if (count <= limit + catScrollPos && count - 1 >= catScrollPos) {
               this.categoriesOther.add(rcl);
               this.field_146292_n.add(new GuiResearchBrowser.GuiCategoryButton(rc, rcl, true, 50 + this.categoriesOther.size(), this.field_146294_l - 17, 10 + this.categoriesOther.size() * 24, 16, 16, I18n.func_74837_a("tc.research_category." + rcl, new Object[0]), v));
            }
         }
      }
   }

   private boolean isVisible(ResearchEntry res) {
      if (ThaumcraftCapabilities.knowsResearch(this.player, res.getKey())) {
         return true;
      } else if (this.invisible.contains(res.getKey()) || res.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN) && !this.canUnlockResearch(res)) {
         return false;
      } else if (res.getParents() == null && res.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN)) {
         return false;
      } else {
         if (res.getParents() != null) {
            String[] var2 = res.getParents();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String r = var2[var4];
               ResearchEntry ri = ResearchCategories.getResearch(r);
               if (ri != null && !this.isVisible(ri)) {
                  this.invisible.add(r);
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean canUnlockResearch(ResearchEntry res) {
      return ResearchManager.doesPlayerHaveRequisites(this.player, res.getKey());
   }

   public void func_146281_b() {
      lastX = this.guiMapX;
      lastY = this.guiMapY;
      Keyboard.enableRepeatEvents(false);
      super.func_146281_b();
   }

   public void func_146280_a(Minecraft mc, int width, int height) {
      super.func_146280_a(mc, width, height);
      this.updateResearch();
      if (lastX == -9999.0D || this.guiMapX > (double)guiBoundsRight || this.guiMapX < (double)guiBoundsLeft) {
         this.guiMapX = this.tempMapX = (double)((guiBoundsLeft + guiBoundsRight) / 2);
      }

      if (lastY == -9999.0D || this.guiMapY > (double)guiBoundsBottom || this.guiMapY < (double)guiBoundsTop) {
         this.guiMapY = this.tempMapY = (double)((guiBoundsBottom + guiBoundsTop) / 2);
      }

   }

   protected void func_73869_a(char par1, int par2) throws IOException {
      if (searching && this.searchField.func_146201_a(par1, par2)) {
         this.updateSearch();
      } else if (par2 == this.field_146297_k.field_71474_y.field_151445_Q.func_151463_i()) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
         this.field_146297_k.func_71381_h();
      }

      super.func_73869_a(par1, par2);
   }

   private void updateSearch() {
      this.searchResults.clear();
      this.invisible.clear();
      String s1 = this.searchField.func_146179_b().toLowerCase();
      Iterator var2 = this.categoriesTC.iterator();

      String pre;
      while(var2.hasNext()) {
         pre = (String)var2.next();
         if (pre.toLowerCase().contains(s1)) {
            this.searchResults.add(Pair.of(I18n.func_74837_a("tc.research_category." + pre, new Object[0]), new GuiResearchBrowser.SearchResult(pre, (String)null, true)));
         }
      }

      var2 = this.categoriesOther.iterator();

      while(var2.hasNext()) {
         pre = (String)var2.next();
         if (pre.toLowerCase().contains(s1)) {
            this.searchResults.add(Pair.of(I18n.func_74837_a("tc.research_category." + pre, new Object[0]), new GuiResearchBrowser.SearchResult(pre, (String)null, true)));
         }
      }

      var2 = ThaumcraftCapabilities.getKnowledge(this.player).getResearchList().iterator();

      while(true) {
         ResearchStage page;
         do {
            do {
               ResearchEntry ri;
               int stage;
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           Collections.sort(this.searchResults);
                           return;
                        }

                        pre = (String)var2.next();
                        ri = ResearchCategories.getResearch(pre);
                     } while(ri == null);
                  } while(ri.getLocalizedName() == null);

                  if (ri.getLocalizedName().toLowerCase().contains(s1)) {
                     this.searchResults.add(Pair.of(ri.getLocalizedName(), new GuiResearchBrowser.SearchResult(pre, (String)null)));
                  }

                  stage = ThaumcraftCapabilities.getKnowledge(this.player).getResearchStage(pre);
               } while(ri.getStages() == null);

               int s = ri.getStages().length - 1 < stage + 1 ? ri.getStages().length - 1 : stage + 1;
               page = ri.getStages()[s];
            } while(page == null);
         } while(page.getRecipes() == null);

         String[] var8 = page.getRecipes();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String rec = var8[var10];
            Object recipeObject = CommonInternals.craftingRecipeCatalog.get(rec);
            ItemStack ro = null;
            Object[] recipeArray = (Object[])((Object[])recipeObject);
            Object[] var15 = recipeArray;
            int var16 = recipeArray.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               Object robj = var15[var17];
               if (robj instanceof IRecipe) {
                  ro = ((IRecipe)robj).func_77571_b();
               } else if (robj instanceof InfusionRecipe && ((InfusionRecipe)robj).getRecipeOutput() instanceof ItemStack) {
                  ro = (ItemStack)((InfusionRecipe)robj).getRecipeOutput();
               } else if (robj instanceof CrucibleRecipe) {
                  ro = ((CrucibleRecipe)robj).getRecipeOutput();
               }

               if (ro != null && ro.func_82833_r().toLowerCase().contains(s1)) {
                  this.searchResults.add(Pair.of(ro.func_82833_r(), new GuiResearchBrowser.SearchResult(pre, rec)));
               }
            }
         }
      }
   }

   public void func_73863_a(int mx, int my, float par3) {
      int locX;
      if (!searching) {
         if (!Mouse.isButtonDown(0)) {
            this.isMouseButtonDown = 0;
         } else {
            if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && mx >= this.startX && mx < this.startX + this.screenX && my >= this.startY && my < this.startY + this.screenY) {
               if (this.isMouseButtonDown == 0) {
                  this.isMouseButtonDown = 1;
               } else {
                  this.guiMapX -= (double)(mx - this.mouseX) * (double)this.screenZoom;
                  this.guiMapY -= (double)(my - this.mouseY) * (double)this.screenZoom;
                  this.tempMapX = this.curMouseX = this.guiMapX;
                  this.tempMapY = this.curMouseY = this.guiMapY;
               }

               this.mouseX = mx;
               this.mouseY = my;
            }

            if (this.tempMapX < (double)guiBoundsLeft * (double)this.screenZoom) {
               this.tempMapX = (double)guiBoundsLeft * (double)this.screenZoom;
            }

            if (this.tempMapY < (double)guiBoundsTop * (double)this.screenZoom) {
               this.tempMapY = (double)guiBoundsTop * (double)this.screenZoom;
            }

            if (this.tempMapX >= (double)guiBoundsRight * (double)this.screenZoom) {
               this.tempMapX = (double)((float)guiBoundsRight * this.screenZoom - 1.0F);
            }

            if (this.tempMapY >= (double)guiBoundsBottom * (double)this.screenZoom) {
               this.tempMapY = (double)((float)guiBoundsBottom * this.screenZoom - 1.0F);
            }
         }

         locX = Mouse.getDWheel();
         float f4 = this.screenZoom;
         if (locX < 0) {
            this.screenZoom += 0.25F;
         } else if (locX > 0) {
            this.screenZoom -= 0.25F;
         }

         this.screenZoom = MathHelper.func_76131_a(this.screenZoom, 1.0F, 2.0F);
      }

      this.func_146276_q_();
      this.t = System.nanoTime() / 50000000L;
      locX = MathHelper.func_76128_c(this.curMouseX + (this.guiMapX - this.curMouseX) * (double)par3);
      int locY = MathHelper.func_76128_c(this.curMouseY + (this.guiMapY - this.curMouseY) * (double)par3);
      if ((float)locX < (float)guiBoundsLeft * this.screenZoom) {
         locX = (int)((float)guiBoundsLeft * this.screenZoom);
      }

      if ((float)locY < (float)guiBoundsTop * this.screenZoom) {
         locY = (int)((float)guiBoundsTop * this.screenZoom);
      }

      if ((float)locX >= (float)guiBoundsRight * this.screenZoom) {
         locX = (int)((float)guiBoundsRight * this.screenZoom - 1.0F);
      }

      if ((float)locY >= (float)guiBoundsBottom * this.screenZoom) {
         locY = (int)((float)guiBoundsBottom * this.screenZoom - 1.0F);
      }

      this.genResearchBackgroundFixedPre(mx, my, par3, locX, locY);
      if (!searching) {
         GL11.glPushMatrix();
         GL11.glScalef(1.0F / this.screenZoom, 1.0F / this.screenZoom, 1.0F);
         this.genResearchBackgroundZoomable(mx, my, par3, locX, locY);
         GL11.glPopMatrix();
      } else {
         this.searchField.func_146194_f();
         int q = 0;
         Iterator var7 = this.searchResults.iterator();

         while(var7.hasNext()) {
            Pair p = (Pair)var7.next();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GuiResearchBrowser.SearchResult sr = (GuiResearchBrowser.SearchResult)p.getRight();
            int color = sr.cat ? 14527146 : (sr.recipe == null ? 14540253 : 11184861);
            if (sr.recipe != null) {
               this.field_146297_k.field_71446_o.func_110577_a(this.tx1);
               GL11.glPushMatrix();
               GL11.glScaled(0.5D, 0.5D, 0.5D);
               this.func_73729_b(44, (32 + q * 10) * 2, 224, 48, 16, 16);
               GL11.glPopMatrix();
            }

            if (mx > 22 && mx < 18 + this.screenX && my >= 32 + q * 10 && my < 40 + q * 10) {
               color = sr.recipe == null ? 16777215 : (sr.cat ? 16764108 : 13421823);
            }

            this.field_146289_q.func_78276_b((String)p.getLeft(), 32, 32 + q * 10, color);
            ++q;
            if (32 + (q + 1) * 10 > this.screenY) {
               this.field_146289_q.func_78276_b(I18n.func_74837_a("tc.search.more", new Object[0]), 22, 34 + q * 10, 11184810);
               break;
            }
         }
      }

      this.genResearchBackgroundFixedPost(mx, my, par3, locX, locY);
      if (this.popuptime > System.currentTimeMillis()) {
         ArrayList<String> text = new ArrayList();
         text.add(this.popupmessage);
         UtilsFX.drawCustomTooltip(this, this.field_146289_q, text, 10, 34, -99);
      }

   }

   public void func_73876_c() {
      this.curMouseX = this.guiMapX;
      this.curMouseY = this.guiMapY;
      double var1 = this.tempMapX - this.guiMapX;
      double var3 = this.tempMapY - this.guiMapY;
      if (var1 * var1 + var3 * var3 < 4.0D) {
         this.guiMapX += var1;
         this.guiMapY += var3;
      } else {
         this.guiMapX += var1 * 0.85D;
         this.guiMapY += var3 * 0.85D;
      }

   }

   private void genResearchBackgroundFixedPre(int par1, int par2, float par3, int locX, int locY) {
      this.field_73735_i = 0.0F;
      GL11.glDepthFunc(518);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      GlStateManager.func_179098_w();
      GlStateManager.func_179140_f();
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
   }

   protected void genResearchBackgroundZoomable(int mx, int my, float par3, int locX, int locY) {
      GL11.glPushMatrix();
      GlStateManager.func_179147_l();
      GL11.glEnable(3042);
      GlStateManager.func_179112_b(770, 771);
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ResearchCategories.getResearchCategory(selectedCategory).background);
      this.drawTexturedModalRectWithDoubles((float)(this.startX - 2) * this.screenZoom, (float)(this.startY - 2) * this.screenZoom, (double)locX / 2.0D, (double)locY / 2.0D, (double)((float)(this.screenX + 4) * this.screenZoom), (double)((float)(this.screenY + 4) * this.screenZoom));
      if (ResearchCategories.getResearchCategory(selectedCategory).background2 != null) {
         Minecraft.func_71410_x().field_71446_o.func_110577_a(ResearchCategories.getResearchCategory(selectedCategory).background2);
         this.drawTexturedModalRectWithDoubles((float)(this.startX - 2) * this.screenZoom, (float)(this.startY - 2) * this.screenZoom, (double)locX / 1.5D, (double)locY / 1.5D, (double)((float)(this.screenX + 4) * this.screenZoom), (double)((float)(this.screenY + 4) * this.screenZoom));
      }

      GlStateManager.func_179084_k();
      GlStateManager.func_179092_a(516, 0.1F);
      GL11.glPopMatrix();
      GL11.glEnable(2929);
      GL11.glDepthFunc(515);
      this.field_146297_k.field_71446_o.func_110577_a(this.tx1);
      int iconY;
      ResearchEntry iconResearch;
      boolean knowsSibling;
      if (ThaumcraftCapabilities.getKnowledge(this.player).getResearchList() != null) {
         for(int index = 0; index < this.research.size(); ++index) {
            ResearchEntry source = (ResearchEntry)this.research.get(index);
            if (source.getParents() != null && source.getParents().length > 0) {
               for(iconY = 0; iconY < source.getParents().length; ++iconY) {
                  if (source.getParents()[iconY] != null && ResearchCategories.getResearch(source.getParentsClean()[iconY]) != null && ResearchCategories.getResearch(source.getParentsClean()[iconY]).getCategory().equals(selectedCategory)) {
                     iconResearch = ResearchCategories.getResearch(source.getParentsClean()[iconY]);
                     if (iconResearch.getSiblings() == null || !Arrays.asList(iconResearch.getSiblings()).contains(source.getKey())) {
                        knowsSibling = ThaumcraftCapabilities.knowsResearchStrict(this.player, source.getParents()[iconY]);
                        boolean b = this.isVisible(source);
                        if (b) {
                           if (knowsSibling) {
                              this.drawLine(source.getDisplayColumn(), source.getDisplayRow(), iconResearch.getDisplayColumn(), iconResearch.getDisplayRow(), 0.6F, 0.6F, 0.6F, locX, locY, 3.0F, true, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                           } else if (this.isVisible(iconResearch)) {
                              this.drawLine(source.getDisplayColumn(), source.getDisplayRow(), iconResearch.getDisplayColumn(), iconResearch.getDisplayRow(), 0.2F, 0.2F, 0.2F, locX, locY, 2.0F, true, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                           }
                        }
                     }
                  }
               }
            }

            if (source.getSiblings() != null && source.getSiblings().length > 0) {
               for(iconY = 0; iconY < source.getSiblings().length; ++iconY) {
                  if (source.getSiblings()[iconY] != null && ResearchCategories.getResearch(source.getSiblings()[iconY]) != null && ResearchCategories.getResearch(source.getSiblings()[iconY]).getCategory().equals(selectedCategory)) {
                     iconResearch = ResearchCategories.getResearch(source.getSiblings()[iconY]);
                     knowsSibling = ThaumcraftCapabilities.knowsResearchStrict(this.player, iconResearch.getKey());
                     if (this.isVisible(source)) {
                        if (knowsSibling) {
                           this.drawLine(iconResearch.getDisplayColumn(), iconResearch.getDisplayRow(), source.getDisplayColumn(), source.getDisplayRow(), 0.3F, 0.3F, 0.4F, locX, locY, 1.0F, false, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                        } else if (this.isVisible(iconResearch)) {
                           this.drawLine(iconResearch.getDisplayColumn(), iconResearch.getDisplayRow(), source.getDisplayColumn(), source.getDisplayRow(), 0.1875F, 0.1875F, 0.25F, locX, locY, 0.0F, false, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                        }
                     }
                  }
               }
            }
         }
      }

      this.currentHighlight = null;
      GL11.glEnable(32826);
      GL11.glEnable(2903);

      for(int var24 = 0; var24 < this.research.size(); ++var24) {
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         iconResearch = (ResearchEntry)this.research.get(var24);
         knowsSibling = false;
         int ix;
         int idx;
         if (iconResearch.getStages() != null) {
            ResearchStage[] var25 = iconResearch.getStages();
            ix = var25.length;

            for(idx = 0; idx < ix; ++idx) {
               ResearchStage stage = var25[idx];
               if (stage.getWarp() > 0) {
                  knowsSibling = true;
                  break;
               }
            }
         }

         int var26 = iconResearch.getDisplayColumn() * 24 - locX;
         int var27 = iconResearch.getDisplayRow() * 24 - locY;
         if (var26 >= -24 && var27 >= -24 && (float)var26 <= (float)this.screenX * this.screenZoom && (float)var27 <= (float)this.screenY * this.screenZoom) {
            int iconX = this.startX + var26;
            iconY = this.startY + var27;
            if (this.isVisible(iconResearch)) {
               if (knowsSibling) {
                  drawForbidden((double)(iconX + 8), (double)(iconY + 8));
               }

               float var38;
               if (ThaumcraftCapabilities.getKnowledge(this.player).isResearchComplete(iconResearch.getKey())) {
                  var38 = 1.0F;
                  GL11.glColor4f(var38, var38, var38, 1.0F);
               } else if (this.canUnlockResearch(iconResearch)) {
                  var38 = (float)Math.sin((double)(Minecraft.func_71386_F() % 600L) / 600.0D * 3.141592653589793D * 2.0D) * 0.25F + 0.75F;
                  GL11.glColor4f(var38, var38, var38, 1.0F);
               } else {
                  var38 = 0.3F;
                  GL11.glColor4f(var38, var38, var38, 1.0F);
               }

               this.field_146297_k.field_71446_o.func_110577_a(this.tx1);
               GL11.glEnable(2884);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.ROUND)) {
                  this.func_73729_b(iconX - 8, iconY - 8, 144, 48 + (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN) ? 32 : 0), 32, 32);
               } else {
                  ix = 80;
                  idx = 48;
                  if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN)) {
                     idx += 32;
                  }

                  if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HEX)) {
                     ix += 32;
                  }

                  this.func_73729_b(iconX - 8, iconY - 8, ix, idx, 32, 32);
               }

               if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.SPIKY)) {
                  this.func_73729_b(iconX - 8, iconY - 8, 176, 48 + (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN) ? 32 : 0), 32, 32);
               }

               boolean bw = false;
               if (!this.canUnlockResearch(iconResearch)) {
                  float var40 = 0.1F;
                  GL11.glColor4f(var40, var40, var40, 1.0F);
                  this.field_146296_j.func_175039_a(false);
                  bw = true;
               }

               if (ThaumcraftCapabilities.getKnowledge(this.player).hasResearchFlag(iconResearch.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
                  GL11.glPushMatrix();
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  GL11.glTranslatef((float)(iconX - 9), (float)(iconY - 9), 0.0F);
                  GL11.glScaled(0.5D, 0.5D, 1.0D);
                  this.func_73729_b(0, 0, 176, 16, 32, 32);
                  GL11.glPopMatrix();
               }

               if (ThaumcraftCapabilities.getKnowledge(this.player).hasResearchFlag(iconResearch.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE)) {
                  GL11.glPushMatrix();
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  GL11.glTranslatef((float)(iconX - 9), (float)(iconY + 9), 0.0F);
                  GL11.glScaled(0.5D, 0.5D, 1.0D);
                  this.func_73729_b(0, 0, 208, 16, 32, 32);
                  GL11.glPopMatrix();
               }

               if (iconResearch.getIcons() != null && iconResearch.getIcons().length > 0) {
                  idx = (int)(System.currentTimeMillis() / 1000L % (long)iconResearch.getIcons().length);
                  GL11.glPushMatrix();
                  GL11.glEnable(3042);
                  GL11.glBlendFunc(770, 771);
                  if (iconResearch.getIcons()[idx] instanceof ResourceLocation) {
                     this.field_146297_k.field_71446_o.func_110577_a((ResourceLocation)iconResearch.getIcons()[idx]);
                     if (bw) {
                        GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
                     }

                     int w = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                     int h = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                     int m;
                     float q;
                     float idx1;
                     if (h > w && h % w == 0) {
                        m = h / w;
                        q = 16.0F / (float)m;
                        idx1 = (float)(System.currentTimeMillis() / 150L % (long)m) * q;
                        UtilsFX.drawTexturedQuadF((float)iconX, (float)iconY, 0.0F, idx1, 16.0F, q, (double)this.field_73735_i);
                     } else if (w > h && w % h == 0) {
                        m = w / h;
                        q = 16.0F / (float)m;
                        idx1 = (float)(System.currentTimeMillis() / 150L % (long)m) * q;
                        UtilsFX.drawTexturedQuadF((float)iconX, (float)iconY, idx1, 0.0F, q, 16.0F, (double)this.field_73735_i);
                     } else {
                        UtilsFX.drawTexturedQuadFull((float)iconX, (float)iconY, (double)this.field_73735_i);
                     }
                  } else if (iconResearch.getIcons()[idx] instanceof ItemStack) {
                     RenderHelper.func_74520_c();
                     GL11.glDisable(2896);
                     GL11.glEnable(32826);
                     GL11.glEnable(2903);
                     GL11.glEnable(2896);
                     this.field_146296_j.func_180450_b(InventoryUtils.cycleItemStack(iconResearch.getIcons()[idx]), iconX, iconY);
                     GL11.glDisable(2896);
                     GL11.glDepthMask(true);
                     GL11.glEnable(2929);
                  } else if (iconResearch.getIcons()[idx] instanceof String && ((String)iconResearch.getIcons()[idx]).startsWith("focus")) {
                     String k = ((String)iconResearch.getIcons()[idx]).replaceAll("focus:", "");
                     IFocusPart fp = FocusHelper.getFocusPart(k.trim());
                     if (fp != null) {
                        GuiFocalManipulator.drawPart(fp, iconX + 8, iconY + 8, 24.0F, bw ? 50 : 220);
                     }
                  }

                  GL11.glDisable(3042);
                  GL11.glPopMatrix();
               }

               if (!this.canUnlockResearch(iconResearch)) {
                  this.field_146296_j.func_175039_a(true);
                  bw = false;
               }

               if (mx >= this.startX && my >= this.startY && mx < this.startX + this.screenX && my < this.startY + this.screenY && (float)mx >= (float)(iconX - 2) / this.screenZoom && (float)mx <= (float)(iconX + 18) / this.screenZoom && (float)my >= (float)(iconY - 2) / this.screenZoom && (float)my <= (float)(iconY + 18) / this.screenZoom) {
                  this.currentHighlight = iconResearch;
               }

               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
         }
      }

      GL11.glDisable(2929);
   }

   private void genResearchBackgroundFixedPost(int mx, int my, float par3, int locX, int locY) {
      this.field_146297_k.field_71446_o.func_110577_a(this.tx1);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      int c;
      int p;
      for(c = 16; c < this.field_146294_l - 16; c += 64) {
         p = 64;
         if (c + p > this.field_146294_l - 16) {
            p = this.field_146294_l - 16 - c;
         }

         if (p > 0) {
            this.func_73729_b(c, -2, 48, 13, p, 22);
            this.func_73729_b(c, this.field_146295_m - 20, 48, 13, p, 22);
         }
      }

      for(c = 16; c < this.field_146295_m - 16; c += 64) {
         p = 64;
         if (c + p > this.field_146295_m - 16) {
            p = this.field_146295_m - 16 - c;
         }

         if (p > 0) {
            this.func_73729_b(-2, c, 13, 48, 22, p);
            this.func_73729_b(this.field_146294_l - 20, c, 13, 48, 22, p);
         }
      }

      this.func_73729_b(-2, -2, 13, 13, 22, 22);
      this.func_73729_b(-2, this.field_146295_m - 20, 13, 13, 22, 22);
      this.func_73729_b(this.field_146294_l - 20, -2, 13, 13, 22, 22);
      this.func_73729_b(this.field_146294_l - 20, this.field_146295_m - 20, 13, 13, 22, 22);
      GL11.glPopMatrix();
      this.field_73735_i = 0.0F;
      GL11.glDepthFunc(515);
      GL11.glDisable(2929);
      GL11.glEnable(3553);
      super.func_73863_a(mx, my, par3);
      if (this.currentHighlight != null) {
         ArrayList<String> text = new ArrayList();
         text.add("§6" + this.currentHighlight.getLocalizedName());
         if (this.canUnlockResearch(this.currentHighlight)) {
            if (!ThaumcraftCapabilities.getKnowledge(this.player).isResearchComplete(this.currentHighlight.getKey()) && this.currentHighlight.getStages() != null) {
               int stage = ThaumcraftCapabilities.getKnowledge(this.player).getResearchStage(this.currentHighlight.getKey());
               if (stage > 0) {
                  text.add("@@" + TextFormatting.AQUA + I18n.func_74838_a("tc.research.stage") + " " + stage + "/" + this.currentHighlight.getStages().length + TextFormatting.RESET);
               } else {
                  text.add("@@" + TextFormatting.GREEN + I18n.func_74838_a("tc.research.begin") + TextFormatting.RESET);
               }
            }
         } else {
            text.add("@@§c" + I18n.func_74838_a("tc.researchmissing"));
         }

         if (ThaumcraftCapabilities.getKnowledge(this.player).hasResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
            text.add("@@" + I18n.func_74838_a("tc.research.newresearch"));
         }

         if (ThaumcraftCapabilities.getKnowledge(this.player).hasResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE)) {
            text.add("@@" + I18n.func_74838_a("tc.research.newpage"));
         }

         UtilsFX.drawCustomTooltip(this, this.field_146289_q, text, mx + 3, my - 3, -99);
      }

      GlStateManager.func_179126_j();
      GlStateManager.func_179145_e();
      RenderHelper.func_74518_a();
   }

   protected void func_73864_a(int mx, int my, int par3) {
      this.popuptime = System.currentTimeMillis() - 1L;
      if (!searching && this.currentHighlight != null && !ThaumcraftCapabilities.knowsResearch(this.player, this.currentHighlight.getKey()) && this.canUnlockResearch(this.currentHighlight)) {
         this.updateResearch();
         PacketHandler.INSTANCE.sendToServer(new PacketSyncProgressToServer(this.currentHighlight.getKey(), true));
         this.field_146297_k.func_147108_a(new GuiResearchPage(this.currentHighlight, (String)null, this.guiMapX, this.guiMapY));
         this.popuptime = System.currentTimeMillis() + 3000L;
         this.popupmessage = (new TextComponentTranslation(I18n.func_74838_a("tc.research.popup"), new Object[]{"" + this.currentHighlight.getLocalizedName()})).func_150260_c();
      } else if (this.currentHighlight != null && ThaumcraftCapabilities.knowsResearch(this.player, this.currentHighlight.getKey())) {
         ThaumcraftCapabilities.getKnowledge(this.player).clearResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH);
         ThaumcraftCapabilities.getKnowledge(this.player).clearResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE);
         PacketHandler.INSTANCE.sendToServer(new PacketSyncResearchFlagsToServer(this.field_146297_k.field_71439_g, this.currentHighlight.getKey()));
         this.field_146297_k.func_147108_a(new GuiResearchPage(this.currentHighlight, (String)null, this.guiMapX, this.guiMapY));
      } else if (searching) {
         int q = 0;
         Iterator var5 = this.searchResults.iterator();

         while(var5.hasNext()) {
            Pair p = (Pair)var5.next();
            GuiResearchBrowser.SearchResult sr = (GuiResearchBrowser.SearchResult)p.getRight();
            if (mx > 22 && mx < 18 + this.screenX && my >= 32 + q * 10 && my < 40 + q * 10) {
               if (ThaumcraftCapabilities.knowsResearch(this.player, sr.key) && !sr.cat) {
                  this.field_146297_k.func_147108_a(new GuiResearchPage(ResearchCategories.getResearch(sr.key), sr.recipe, this.guiMapX, this.guiMapY));
                  break;
               }

               if (this.categoriesTC.contains(sr.key) || this.categoriesOther.contains(sr.key)) {
                  searching = false;
                  this.searchField.func_146189_e(false);
                  this.searchField.func_146205_d(true);
                  this.searchField.func_146195_b(false);
                  selectedCategory = sr.key;
                  this.updateResearch();
                  this.guiMapX = this.tempMapX = (double)((guiBoundsLeft + guiBoundsRight) / 2);
                  this.guiMapY = this.tempMapY = (double)((guiBoundsBottom + guiBoundsTop) / 2);
                  break;
               }
            }

            ++q;
            if (32 + (q + 1) * 10 > this.screenY) {
               break;
            }
         }
      }

      try {
         super.func_73864_a(mx, my, par3);
      } catch (IOException var8) {
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 2) {
         selectedCategory = "";
         searching = true;
         this.searchField.func_146189_e(true);
         this.searchField.func_146205_d(false);
         this.searchField.func_146195_b(true);
         this.searchField.func_146180_a("");
         this.updateSearch();
      }

      if (button.field_146127_k == 3 && catScrollPos > 0) {
         --catScrollPos;
         this.updateResearch();
      }

      if (button.field_146127_k == 4 && catScrollPos < catScrollMax) {
         ++catScrollPos;
         this.updateResearch();
      }

      if (button.field_146127_k >= 20 && button instanceof GuiResearchBrowser.GuiCategoryButton && ((GuiResearchBrowser.GuiCategoryButton)button).key != selectedCategory) {
         searching = false;
         this.searchField.func_146189_e(false);
         this.searchField.func_146205_d(true);
         this.searchField.func_146195_b(false);
         selectedCategory = ((GuiResearchBrowser.GuiCategoryButton)button).key;
         this.updateResearch();
         this.guiMapX = this.tempMapX = (double)((guiBoundsLeft + guiBoundsRight) / 2);
         this.guiMapY = this.tempMapY = (double)((guiBoundsBottom + guiBoundsTop) / 2);
      }

   }

   private void playButtonClick() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.clack, 0.4F, 1.0F);
   }

   public boolean func_73868_f() {
      return false;
   }

   private void drawLine(int x, int y, int x2, int y2, float r, float g, float b, int locX, int locY, float zMod, boolean arrow, boolean flipped) {
      float zt = this.field_73735_i;
      this.field_73735_i += zMod;
      boolean bigCorner = false;
      int xd;
      int yd;
      int xm;
      int ym;
      int xx;
      int yy;
      if (flipped) {
         xd = Math.abs(x2 - x);
         yd = Math.abs(y2 - y);
         xm = xd == 0 ? 0 : (x2 - x > 0 ? -1 : 1);
         ym = yd == 0 ? 0 : (y2 - y > 0 ? -1 : 1);
         if (xd > 1 && yd > 1) {
            bigCorner = true;
         }

         xx = x2 * 24 - 4 - locX + this.startX;
         yy = y2 * 24 - 4 - locY + this.startY;
      } else {
         xd = Math.abs(x - x2);
         yd = Math.abs(y - y2);
         xm = xd == 0 ? 0 : (x - x2 > 0 ? -1 : 1);
         ym = yd == 0 ? 0 : (y - y2 > 0 ? -1 : 1);
         if (xd > 1 && yd > 1) {
            bigCorner = true;
         }

         xx = x * 24 - 4 - locX + this.startX;
         yy = y * 24 - 4 - locY + this.startY;
      }

      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(r, g, b, 1.0F);
      int v;
      int h;
      if (arrow) {
         if (flipped) {
            v = x * 24 - 8 - locX + this.startX;
            h = y * 24 - 8 - locY + this.startY;
            if (xm < 0) {
               this.func_73729_b(v, h, 160, 112, 32, 32);
            } else if (xm > 0) {
               this.func_73729_b(v, h, 128, 112, 32, 32);
            } else if (ym > 0) {
               this.func_73729_b(v, h, 64, 112, 32, 32);
            } else if (ym < 0) {
               this.func_73729_b(v, h, 96, 112, 32, 32);
            }
         } else if (ym < 0) {
            this.func_73729_b(xx - 4, yy - 4, 64, 112, 32, 32);
         } else if (ym > 0) {
            this.func_73729_b(xx - 4, yy - 4, 96, 112, 32, 32);
         } else if (xm > 0) {
            this.func_73729_b(xx - 4, yy - 4, 160, 112, 32, 32);
         } else if (xm < 0) {
            this.func_73729_b(xx - 4, yy - 4, 128, 112, 32, 32);
         }
      }

      v = 1;

      byte h;
      for(h = 0; v < yd - (bigCorner ? 1 : 0); ++v) {
         this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 0, 228, 24, 24);
      }

      if (bigCorner) {
         if (xm < 0 && ym > 0) {
            this.func_73729_b(xx + xm * 24 * h - 24, yy + ym * 24 * v, 0, 180, 48, 48);
         }

         if (xm > 0 && ym > 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 48, 180, 48, 48);
         }

         if (xm < 0 && ym < 0) {
            this.func_73729_b(xx + xm * 24 * h - 24, yy + ym * 24 * v - 24, 96, 180, 48, 48);
         }

         if (xm > 0 && ym < 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v - 24, 144, 180, 48, 48);
         }
      } else {
         if (xm < 0 && ym > 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 48, 228, 24, 24);
         }

         if (xm > 0 && ym > 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 72, 228, 24, 24);
         }

         if (xm < 0 && ym < 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 96, 228, 24, 24);
         }

         if (xm > 0 && ym < 0) {
            this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 120, 228, 24, 24);
         }
      }

      v += bigCorner ? 1 : 0;

      for(h = h + (bigCorner ? 2 : 1); h < xd; ++h) {
         this.func_73729_b(xx + xm * 24 * h, yy + ym * 24 * v, 24, 228, 24, 24);
      }

      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
      this.field_73735_i = zt;
   }

   public static void drawForbidden(double x, double y) {
      int count = FMLClientHandler.instance().getClient().field_71439_g.field_70173_aa;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, 0.0D);
      UtilsFX.renderQuadCentered(UtilsFX.nodeTexture, 32, 32, 160 + count % 32, 90.0F, 0.33F, 0.0F, 0.44F, 220, 1, 0.9F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void drawTexturedModalRectWithDoubles(float xCoord, float yCoord, double minU, double minV, double maxU, double maxV) {
      float f2 = 0.00390625F;
      float f3 = 0.00390625F;
      Tessellator tessellator = Tessellator.func_178181_a();
      VertexBuffer VertexBuffer = tessellator.func_178180_c();
      VertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      VertexBuffer.func_181662_b((double)(xCoord + 0.0F), (double)yCoord + maxV, (double)this.field_73735_i).func_187315_a((minU + 0.0D) * (double)f2, (minV + maxV) * (double)f3).func_181675_d();
      VertexBuffer.func_181662_b((double)xCoord + maxU, (double)yCoord + maxV, (double)this.field_73735_i).func_187315_a((minU + maxU) * (double)f2, (minV + maxV) * (double)f3).func_181675_d();
      VertexBuffer.func_181662_b((double)xCoord + maxU, (double)(yCoord + 0.0F), (double)this.field_73735_i).func_187315_a((minU + maxU) * (double)f2, (minV + 0.0D) * (double)f3).func_181675_d();
      VertexBuffer.func_181662_b((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)this.field_73735_i).func_187315_a((minU + 0.0D) * (double)f2, (minV + 0.0D) * (double)f3).func_181675_d();
      tessellator.func_78381_a();
   }

   private class GuiSearchButton extends GuiButton {
      public GuiSearchButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
         super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
      }

      public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
         if (this.field_146125_m) {
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179112_b(770, 771);
            mc.field_71446_o.func_110577_a(GuiResearchBrowser.this.tx1);
            GL11.glPushMatrix();
            if (this.field_146123_n) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, 160, 16, 16, 16);
            GL11.glPopMatrix();
            if (this.field_146123_n) {
               this.func_73731_b(mc.field_71466_p, this.field_146126_j, this.field_146128_h + 19, this.field_146129_i + 4, 16777215);
            }

            this.func_146119_b(mc, mouseX, mouseY);
         }

      }
   }

   private class GuiScrollButton extends GuiButton {
      boolean flip;

      public GuiScrollButton(boolean flip, int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
         super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
         this.flip = flip;
      }

      public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
         if (this.field_146125_m) {
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179112_b(770, 771);
            mc.field_71446_o.func_110577_a(GuiResearchBrowser.this.tx1);
            GL11.glPushMatrix();
            if (this.field_146123_n) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               GL11.glColor4f(0.7F, 0.7F, 0.7F, 1.0F);
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, 51, this.flip ? 71 : 55, 10, 11);
            GL11.glPopMatrix();
            this.func_146119_b(mc, mouseX, mouseY);
         }

      }
   }

   private class GuiCategoryButton extends GuiButton {
      ResearchCategory rc;
      String key;
      boolean flip;
      int completion;

      public GuiCategoryButton(ResearchCategory rc, String key, boolean flip, int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_, int completion) {
         super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
         this.rc = rc;
         this.key = key;
         this.flip = flip;
         this.completion = completion;
      }

      public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
         return this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h && mouseY >= this.field_146129_i + GuiResearchBrowser.this.addonShift && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g + GuiResearchBrowser.this.addonShift;
      }

      public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
         if (this.field_146125_m) {
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i + GuiResearchBrowser.this.addonShift && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g + GuiResearchBrowser.this.addonShift;
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179112_b(770, 771);
            mc.field_71446_o.func_110577_a(GuiResearchBrowser.this.tx1);
            GL11.glPushMatrix();
            if (!GuiResearchBrowser.selectedCategory.equals(this.key)) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               GL11.glColor4f(0.6F, 1.0F, 1.0F, 1.0F);
            }

            this.func_73729_b(this.field_146128_h - 3, this.field_146129_i - 3 + GuiResearchBrowser.this.addonShift, 13, 13, 22, 22);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            mc.field_71446_o.func_110577_a(this.rc.icon);
            if (!GuiResearchBrowser.selectedCategory.equals(this.key) && !this.field_146123_n) {
               GL11.glColor4f(0.66F, 0.66F, 0.66F, 0.8F);
            } else {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            UtilsFX.drawTexturedQuadFull((float)this.field_146128_h, (float)(this.field_146129_i + GuiResearchBrowser.this.addonShift), -80.0D);
            GL11.glPopMatrix();
            mc.field_71446_o.func_110577_a(GuiResearchBrowser.this.tx1);
            boolean nr = false;
            boolean np = false;
            Iterator var6 = this.rc.research.keySet().iterator();

            while(var6.hasNext()) {
               String rk = (String)var6.next();
               if (ThaumcraftCapabilities.knowsResearch(GuiResearchBrowser.this.player, rk)) {
                  if (!nr && ThaumcraftCapabilities.getKnowledge(GuiResearchBrowser.this.player).hasResearchFlag(rk, IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
                     nr = true;
                  }

                  if (!np && ThaumcraftCapabilities.getKnowledge(GuiResearchBrowser.this.player).hasResearchFlag(rk, IPlayerKnowledge.EnumResearchFlag.PAGE)) {
                     np = true;
                  }

                  if (nr && np) {
                     break;
                  }
               }
            }

            if (nr) {
               GL11.glPushMatrix();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
               GL11.glTranslated((double)(this.field_146128_h - 2), (double)(this.field_146129_i + GuiResearchBrowser.this.addonShift - 2), 0.0D);
               GL11.glScaled(0.25D, 0.25D, 1.0D);
               this.func_73729_b(0, 0, 176, 16, 32, 32);
               GL11.glPopMatrix();
            }

            if (np) {
               GL11.glPushMatrix();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
               GL11.glTranslated((double)(this.field_146128_h - 2), (double)(this.field_146129_i + GuiResearchBrowser.this.addonShift + 9), 0.0D);
               GL11.glScaled(0.25D, 0.25D, 1.0D);
               this.func_73729_b(0, 0, 208, 16, 32, 32);
               GL11.glPopMatrix();
            }

            if (this.field_146123_n) {
               String dp = this.field_146126_j + " (" + this.completion + "%)";
               this.func_73731_b(mc.field_71466_p, dp, !this.flip ? this.field_146128_h + 22 : GuiResearchBrowser.this.screenX + 9 - mc.field_71466_p.func_78256_a(dp), this.field_146129_i + 4 + GuiResearchBrowser.this.addonShift, 16777215);
               int t = 9;
               if (nr) {
                  this.func_73731_b(mc.field_71466_p, I18n.func_74838_a("tc.research.newresearch"), !this.flip ? this.field_146128_h + 22 : GuiResearchBrowser.this.screenX + 9 - mc.field_71466_p.func_78256_a(I18n.func_74838_a("tc.research.newresearch")), this.field_146129_i + 4 + t + GuiResearchBrowser.this.addonShift, 16777215);
                  t += 9;
               }

               if (np) {
                  this.func_73731_b(mc.field_71466_p, I18n.func_74838_a("tc.research.newpage"), !this.flip ? this.field_146128_h + 22 : GuiResearchBrowser.this.screenX + 9 - mc.field_71466_p.func_78256_a(I18n.func_74838_a("tc.research.newpage")), this.field_146129_i + 4 + t + GuiResearchBrowser.this.addonShift, 16777215);
               }
            }

            this.func_146119_b(mc, mouseX, mouseY);
         }

      }
   }

   private class SearchResult implements Comparable {
      String key;
      String recipe;
      boolean cat;

      private SearchResult(String key, String recipe) {
         this.key = key;
         this.recipe = recipe;
         this.cat = false;
      }

      private SearchResult(String key, String recipe, boolean cat) {
         this.key = key;
         this.recipe = recipe;
         this.cat = cat;
      }

      public int compareTo(Object arg0) {
         GuiResearchBrowser.SearchResult arg = (GuiResearchBrowser.SearchResult)arg0;
         int k = this.key.compareTo(arg.key);
         return k == 0 && this.recipe != null && arg.recipe != null ? this.recipe.compareTo(arg.recipe) : k;
      }

      // $FF: synthetic method
      SearchResult(String x1, String x2, boolean x3, Object x4) {
         this(x1, x2, x3);
      }

      // $FF: synthetic method
      SearchResult(String x1, String x2, Object x3) {
         this(x1, x2);
      }
   }
}
