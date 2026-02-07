package thaumcraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftManager;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.gui.plugins.GuiImageButton;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerResearchTable;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketStartTheoryToServer;
import thaumcraft.common.tiles.crafting.TileResearchTable;

@SideOnly(Side.CLIENT)
public class GuiResearchTable extends GuiContainer {
   private float xSize_lo;
   private float ySize_lo;
   private TileResearchTable table;
   private FontRenderer galFontRenderer;
   private String username;
   EntityPlayer player;
   ResourceLocation txBackground = new ResourceLocation("thaumcraft", "textures/gui/gui_research_table.png");
   ResourceLocation txBase = new ResourceLocation("thaumcraft", "textures/gui/gui_base.png");
   ResourceLocation txPaper = new ResourceLocation("thaumcraft", "textures/gui/paper.png");
   ResourceLocation txPaperGilded = new ResourceLocation("thaumcraft", "textures/gui/papergilded.png");
   ResourceLocation txQuestion = new ResourceLocation("thaumcraft", "textures/aspects/_unknown.png");
   ResearchTableData.CardChoice lastDraw;
   float[] cardHover = new float[]{0.0F, 0.0F, 0.0F};
   float[] cardZoomOut = new float[]{0.0F, 0.0F, 0.0F};
   float[] cardZoomIn = new float[]{0.0F, 0.0F, 0.0F};
   boolean[] cardActive = new boolean[]{true, true, true};
   boolean cardSelected = false;
   public HashMap<String, Integer> tempCatTotals = new HashMap();
   long nexCatCheck = 0L;
   long nextCheck = 0L;
   int dummyInspirationStart = 0;
   Set<String> currentAids = new HashSet();
   Set<String> selectedAids = new HashSet();
   GuiImageButton buttonCreate;
   GuiImageButton buttonComplete;
   public ArrayList<ResearchTableData.CardChoice> cardChoices;

   public GuiResearchTable(EntityPlayer player, TileResearchTable e) {
      super(new ContainerResearchTable(player.field_71071_by, e));
      this.buttonCreate = new GuiImageButton(this, 1, this.field_147003_i + 128, this.field_147009_r + 22, 49, 11, "button.create.theory", (String)null, this.txBase, 37, 66, 51, 13, 8978346);
      this.buttonComplete = new GuiImageButton(this, 7, this.field_147003_i + 191, this.field_147009_r + 96, 49, 11, "button.complete.theory", (String)null, this.txBase, 37, 66, 51, 13, 8978346);
      this.cardChoices = new ArrayList();
      this.table = e;
      this.field_146999_f = 255;
      this.field_147000_g = 255;
      this.galFontRenderer = FMLClientHandler.instance().getClient().field_71464_q;
      this.username = player.func_70005_c_();
      this.player = player;
      if (this.table.data != null) {
         Iterator var3 = this.table.data.categoryTotals.keySet().iterator();

         while(var3.hasNext()) {
            String cat = (String)var3.next();
            this.tempCatTotals.put(cat, this.table.data.categoryTotals.get(cat));
         }

         this.syncFromTableChoices();
         this.lastDraw = this.table.data.lastDraw;
      }

   }

   private void syncFromTableChoices() {
      this.cardChoices.clear();
      Iterator var1 = this.table.data.cardChoices.iterator();

      while(var1.hasNext()) {
         ResearchTableData.CardChoice cc = (ResearchTableData.CardChoice)var1.next();
         this.cardChoices.add(cc);
      }

   }

   protected void func_146979_b(int mx, int my) {
   }

   public void func_73863_a(int mx, int my, float par3) {
      super.func_73863_a(mx, my, par3);
      this.xSize_lo = (float)mx;
      this.ySize_lo = (float)my;
      int xx = this.field_147003_i;
      int yy = this.field_147009_r;
      RenderHelper.func_74518_a();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int side;
      int r;
      if (this.table.data == null) {
         if (!this.currentAids.isEmpty()) {
            side = Math.min(this.currentAids.size(), 6);
            int c = 0;
            r = 0;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
            this.field_146297_k.field_71446_o.func_110577_a(this.txBase);
            Iterator var9 = this.currentAids.iterator();

            while(var9.hasNext()) {
               String key = (String)var9.next();
               ITheorycraftAid mutator = (ITheorycraftAid)TheorycraftManager.aids.get(key);
               if (mutator != null) {
                  int x = xx + 128 + 20 * c - side * 10;
                  int y = yy + 85 + 35 * r;
                  if (this.func_146978_c(x - xx, y - yy, 16, 16, mx, my) && !this.selectedAids.contains(key)) {
                     this.func_73729_b(x, y, 0, 96, 16, 16);
                  }

                  ++c;
                  if (c >= side) {
                     ++r;
                     c = 0;
                  }
               }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      } else {
         int sx = 128;
         int cw = 110;
         r = this.cardChoices.size();
         int a = 0;
         if (!this.cardSelected) {
            Iterator var18 = this.cardChoices.iterator();

            while(var18.hasNext()) {
               ResearchTableData.CardChoice cardChoice = (ResearchTableData.CardChoice)var18.next();
               if (this.cardZoomOut[a] >= 1.0F) {
                  float dx = (float)(55 + sx - 55 * r + cw * a - 65);
                  float fx = 65.0F + dx * this.cardZoomOut[a];
                  float qx = 191.0F - fx;
                  if (this.cardActive[a]) {
                     fx += qx * this.cardZoomIn[a];
                  }

                  this.drawSheetOverlay((double)fx, 100.0D, cardChoice, mx, my);
                  ++a;
               }
            }
         }

         int qq = 0;
         if (this.table.func_70301_a(0) == null || this.table.func_70301_a(0).func_77952_i() == this.table.func_70301_a(0).func_77958_k()) {
            side = Math.max(this.field_146289_q.func_78256_a(I18n.func_74838_a("tile.researchtable.noink.0")), this.field_146289_q.func_78256_a(I18n.func_74838_a("tile.researchtable.noink.1"))) / 2;
            UtilsFX.drawCustomTooltip(this, this.field_146289_q, Arrays.asList(I18n.func_74838_a("tile.researchtable.noink.0"), I18n.func_74838_a("tile.researchtable.noink.1")), xx - side + 116, yy + 60 + qq, 11, true);
            qq += 40;
         }

         if (this.table.func_70301_a(1) == null) {
            side = this.field_146289_q.func_78256_a(I18n.func_74838_a("tile.researchtable.nopaper.0")) / 2;
            UtilsFX.drawCustomTooltip(this, this.field_146289_q, Arrays.asList(I18n.func_74838_a("tile.researchtable.nopaper.0")), xx - side + 116, yy + 60 + qq, 11, true);
         }
      }

   }

   protected void func_146976_a(float partialTicks, int mx, int my) {
      this.checkButtons();
      int xx = this.field_147003_i;
      int yy = this.field_147009_r;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.field_146297_k.field_71446_o.func_110577_a(this.txBackground);
      this.func_73729_b(xx, yy, 0, 0, 255, 255);
      this.field_146289_q.func_78276_b(" ", 0, 0, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int side;
      int r;
      Iterator var9;
      String cat;
      int t1;
      if (this.table.data == null) {
         if (this.nextCheck < (long)this.player.field_70173_aa) {
            this.currentAids = this.table.checkSurroundingAids();
            this.dummyInspirationStart = ResearchTableData.getAvailableInspiration(this.player);
            this.nextCheck = (long)(this.player.field_70173_aa + 100);
         }

         this.field_146297_k.field_71446_o.func_110577_a(this.txBase);
         GL11.glPushMatrix();
         GL11.glTranslated((double)(xx + 128 - this.dummyInspirationStart * 5), (double)(yy + 55), 0.0D);
         GL11.glScaled(0.5D, 0.5D, 0.0D);

         for(side = 0; side < this.dummyInspirationStart; ++side) {
            this.func_73729_b(20 * side, 0, this.dummyInspirationStart - this.selectedAids.size() <= side ? 48 : 32, 96, 16, 16);
         }

         GL11.glPopMatrix();
         if (!this.currentAids.isEmpty()) {
            side = Math.min(this.currentAids.size(), 6);
            int c = 0;
            r = 0;
            var9 = this.currentAids.iterator();

            while(var9.hasNext()) {
               cat = (String)var9.next();
               ITheorycraftAid mutator = (ITheorycraftAid)TheorycraftManager.aids.get(cat);
               if (mutator != null) {
                  t1 = xx + 128 + 20 * c - side * 10;
                  int y = yy + 85 + 35 * r;
                  if (this.selectedAids.contains(cat)) {
                     this.field_146297_k.field_71446_o.func_110577_a(this.txBase);
                     this.func_73729_b(t1, y, 0, 96, 16, 16);
                  }

                  GL11.glPushMatrix();
                  RenderHelper.func_74520_c();
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179091_B();
                  GlStateManager.func_179142_g();
                  GlStateManager.func_179145_e();
                  ItemStack s = mutator.getAidObject() instanceof ItemStack ? (ItemStack)mutator.getAidObject() : new ItemStack((Block)mutator.getAidObject());
                  this.field_146296_j.func_180450_b(s, t1, y);
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179132_a(true);
                  GlStateManager.func_179126_j();
                  GL11.glPopMatrix();
                  ++c;
                  if (c >= side) {
                     ++r;
                     c = 0;
                  }
               }
            }
         }
      } else {
         this.checkCards();
         this.field_146297_k.field_71446_o.func_110577_a(this.txBase);
         GL11.glPushMatrix();
         GL11.glTranslated((double)(xx + 15), (double)(yy + 150), 0.0D);
         if (this.table.data != null) {
            for(side = 0; side < this.table.data.bonusDraws; ++side) {
               this.func_73729_b(side * 2, side, 64, 96, 16, 16);
            }
         }

         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslated((double)(xx + 128 - this.table.data.inspirationStart * 5), (double)(yy + 16), 0.0D);
         GL11.glScaled(0.5D, 0.5D, 0.0D);

         for(side = 0; side < this.table.data.inspirationStart; ++side) {
            this.func_73729_b(20 * side, 0, this.table.data.inspiration <= side ? 48 : 32, 96, 16, 16);
         }

         GL11.glPopMatrix();
         side = 0;
         if (this.table.func_70301_a(1) != null) {
            side = 1 + this.table.func_70301_a(1).field_77994_a / 4;
         }

         Random r = new Random(55L);
         if (side > 0 && !this.table.data.isComplete()) {
            for(r = 0; r < side; ++r) {
               this.drawSheet((double)(xx + 65), (double)(yy + 100), 6.0D, r, 1.0F, 1.0F, (ResearchTableData.CardChoice)null);
            }

            boolean highlight = false;
            int var7 = mx - (25 + xx);
            int var8 = my - (55 + yy);
            if (this.cardChoices.isEmpty() && var7 >= 0 && var8 >= 0 && var7 < 75 && var8 < 90) {
               highlight = true;
            }

            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, highlight ? 1.0F : 0.5F);
            GlStateManager.func_179147_l();
            this.field_146297_k.field_71446_o.func_110577_a(this.txQuestion);
            GL11.glTranslated((double)(xx + 65), (double)(yy + 100), 0.0D);
            GL11.glScaled(highlight ? 1.75D : 1.5D, highlight ? 1.75D : 1.5D, 0.0D);
            UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }

         Iterator var31 = this.table.data.savedCards.iterator();

         while(var31.hasNext()) {
            Long seed = (Long)var31.next();
            r = new Random(seed);
            this.drawSheet((double)(xx + 191), (double)(yy + 100), 6.0D, r, 1.0F, 1.0F, (ResearchTableData.CardChoice)null);
         }

         if (this.lastDraw != null) {
            r = new Random(this.lastDraw.card.getSeed());
            this.drawSheet((double)(xx + 191), (double)(yy + 100), 6.0D, r, 1.0F, 1.0F, this.lastDraw);
         }

         ArrayList<String> sparkle = new ArrayList();
         int t0;
         if (this.nexCatCheck < (long)this.player.field_70173_aa) {
            var9 = ResearchCategories.researchCategories.keySet().iterator();

            while(true) {
               while(var9.hasNext()) {
                  cat = (String)var9.next();
                  t0 = 0;
                  if (this.table.data.categoryTotals.containsKey(cat)) {
                     t0 = (Integer)this.table.data.categoryTotals.get(cat);
                  }

                  t1 = 0;
                  if (this.tempCatTotals.containsKey(cat)) {
                     t1 = (Integer)this.tempCatTotals.get(cat);
                  }

                  if (t0 == 0 && t1 == 0) {
                     this.tempCatTotals.remove(cat);
                  } else {
                     if (t1 > t0) {
                        --t1;
                     }

                     if (t1 < t0) {
                        ++t1;
                        sparkle.add(cat);
                     }

                     this.tempCatTotals.put(cat, t1);
                  }
               }

               this.nexCatCheck = (long)(this.player.field_70173_aa + 1);
               break;
            }
         }

         HashMap<String, Integer> unsortedMap = new HashMap();
         cat = null;
         t0 = 0;
         Iterator var39 = this.tempCatTotals.keySet().iterator();

         int i;
         while(var39.hasNext()) {
            String cat = (String)var39.next();
            i = (Integer)this.tempCatTotals.get(cat);
            if (i != 0) {
               if (i > t0) {
                  t0 = i;
                  cat = cat;
               }

               unsortedMap.put(cat, i);
            }
         }

         if (cat != null) {
            unsortedMap.put(cat, unsortedMap.get(cat));
         }

         Comparator<Entry<String, Integer>> valueComparator = (e1, e2) -> {
            return ((Integer)e2.getValue()).compareTo((Integer)e1.getValue());
         };
         Map<String, Integer> sortedMap = (Map)unsortedMap.entrySet().stream().sorted(valueComparator).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> {
            return e1;
         }, LinkedHashMap::new));
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         i = 0;

         int a;
         for(Iterator var15 = sortedMap.keySet().iterator(); var15.hasNext(); ++i) {
            String field = (String)var15.next();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)(xx + 253), (float)(yy + 16 + i * 18 + (i > 0 ? 4 : 0)), 0.0F);
            GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
            this.field_146297_k.field_71446_o.func_110577_a(ResearchCategories.getResearchCategory(field).icon);
            this.func_73729_b(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 5.0F);
            String s = sortedMap.get(field) + "%";
            if (i == 0) {
               a = (Integer)sortedMap.get(field) / 4;
               s = s + " (+" + a + ")";
            }

            this.field_146297_k.field_71466_p.func_175063_a(s, (float)(xx + 276), (float)(yy + 20 + i * 18 + (i > 0 ? 4 : 0)), this.table.data.categoriesBlocked.contains(field) ? 6316128 : (i == 0 ? '\ue0c0' : 16777215));
            if (sparkle.contains(field)) {
               for(a = 0; a < 2; ++a) {
                  float rr = (float)MathHelper.func_76136_a(this.player.func_70681_au(), 255, 255) / 255.0F;
                  float gg = (float)MathHelper.func_76136_a(this.player.func_70681_au(), 189, 255) / 255.0F;
                  float bb = (float)MathHelper.func_76136_a(this.player.func_70681_au(), 64, 255) / 255.0F;
                  FXDispatcher.INSTANCE.drawSimpleSparkleGui(this.player.func_70681_au(), (double)((float)(xx + 276) + this.player.func_70681_au().nextFloat() * (float)this.field_146289_q.func_78256_a(s)), (double)((float)(yy + 20) + this.player.func_70681_au().nextFloat() * 8.0F + (float)(i * 18) + (float)(i > 0 ? 4 : 0)), this.player.field_70170_p.field_73012_v.nextGaussian() * 0.5D, this.player.field_70170_p.field_73012_v.nextGaussian() * 0.5D, 24.0F, rr, gg, bb, 0, 0.9F, -1.0F);
               }
            }

            a = mx - (xx + 256);
            int var8 = my - (yy + 16 + i * 18 + (i > 0 ? 4 : 0));
            if (a >= 0 && var8 >= 0 && a < 16 && var8 < 16) {
               GL11.glPushMatrix();
               UtilsFX.drawCustomTooltip(this, this.field_146289_q, Arrays.asList(ResearchCategories.getCategoryName(field)), mx + 8, my + 8, 11);
               GL11.glPopMatrix();
               RenderHelper.func_74518_a();
            }
         }

         int sx = 128;
         int cw = 110;
         int sz = this.cardChoices.size();
         a = 0;

         for(Iterator var45 = this.cardChoices.iterator(); var45.hasNext(); ++a) {
            ResearchTableData.CardChoice cardChoice = (ResearchTableData.CardChoice)var45.next();
            r = new Random(cardChoice.card.getSeed());
            int var7 = mx - (5 + sx - 55 * sz + xx + cw * a);
            int var8 = my - (100 + yy - 60);
            float[] var10000;
            if ((double)this.cardZoomOut[a] >= 0.95D && !this.cardSelected) {
               if (var7 >= 0 && var8 >= 0 && var7 < 100 && var8 < 120) {
                  var10000 = this.cardHover;
                  var10000[a] += Math.max((0.25F - this.cardHover[a]) / 3.0F * partialTicks, 0.0025F);
               } else {
                  var10000 = this.cardHover;
                  var10000[a] -= 0.1F * partialTicks;
               }
            }

            float prevZoomIn;
            if (a == sz - 1 || (double)this.cardZoomOut[a + 1] > 0.6D) {
               prevZoomIn = this.cardZoomOut[a];
               var10000 = this.cardZoomOut;
               var10000[a] += Math.max((1.0F - this.cardZoomOut[a]) / 5.0F * partialTicks, 0.0025F);
               if (this.cardZoomOut[a] > 0.0F && prevZoomIn == 0.0F) {
                  this.playButtonPageFlip();
               }
            }

            prevZoomIn = this.cardZoomIn[a];
            if (this.cardSelected) {
               var10000 = this.cardZoomIn;
               var10000[a] = (float)((double)var10000[a] + (this.cardActive[a] ? Math.max((double)((1.0F - this.cardZoomIn[a]) / 3.0F * partialTicks), 0.0025D) : (double)(0.3F * partialTicks)));
               this.cardHover[a] = 1.0F - this.cardZoomIn[a];
            }

            this.cardZoomIn[a] = MathHelper.func_76131_a(this.cardZoomIn[a], 0.0F, 1.0F);
            this.cardHover[a] = MathHelper.func_76131_a(this.cardHover[a], 0.0F, 0.25F);
            this.cardZoomOut[a] = MathHelper.func_76131_a(this.cardZoomOut[a], 0.0F, 1.0F);
            float dx = (float)(55 + sx - 55 * sz + xx + cw * a - (xx + 65));
            float fx = (float)(xx + 65) + dx * this.cardZoomOut[a];
            float qx = (float)(xx + 191) - fx;
            if (this.cardActive[a]) {
               fx += qx * this.cardZoomIn[a];
            }

            this.drawSheet((double)fx, (double)(yy + 100), (double)(6.0F + this.cardZoomOut[a] * 2.0F - this.cardZoomIn[a] * 2.0F + this.cardHover[a]), r, this.cardActive[a] ? 1.0F : 1.0F - this.cardZoomIn[a], Math.max(1.0F - this.cardZoomOut[a], this.cardZoomIn[a]), cardChoice);
            if (this.cardSelected && this.cardActive[a] && this.cardZoomIn[a] >= 1.0F && prevZoomIn < 1.0F) {
               this.playButtonWrite();
               this.cardChoices.clear();
               this.cardSelected = false;
               this.lastDraw = this.table.data.lastDraw;
               break;
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.func_74518_a();
   }

   private void drawSheet(double x, double y, double scale, Random r, float alpha, float tilt, ResearchTableData.CardChoice cardChoice) {
      GL11.glPushMatrix();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
      GL11.glTranslated(x + r.nextGaussian(), y + r.nextGaussian(), 0.0D);
      GL11.glScaled(scale, scale, 0.0D);
      GL11.glRotated(r.nextGaussian() * (double)tilt, 0.0D, 0.0D, 1.0D);
      GL11.glPushMatrix();
      if (cardChoice != null && cardChoice.fromAid) {
         this.field_146297_k.field_71446_o.func_110577_a(this.txPaperGilded);
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(this.txPaper);
      }

      if (r.nextBoolean()) {
         GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
      }

      if (r.nextBoolean()) {
         GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
      }

      GL11.glDisable(2884);
      UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
      GL11.glEnable(2884);
      GL11.glPopMatrix();
      if (cardChoice != null && alpha == 1.0F) {
         if (cardChoice.card.getResearchCategory() != null) {
            ResearchCategory rc = ResearchCategories.getResearchCategory(cardChoice.card.getResearchCategory());
            if (rc != null) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha / 6.0F);
               GL11.glPushMatrix();
               GL11.glScaled(0.5D, 0.5D, 0.0D);
               this.field_146297_k.field_71446_o.func_110577_a(rc.icon);
               UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
               GL11.glPopMatrix();
            }
         }

         GL11.glPushMatrix();
         GL11.glScaled(0.0625D, 0.0625D, 0.0D);
         GL11.glColor4f(0.0F, 0.0F, 0.0F, alpha);
         String name = TextFormatting.BOLD + cardChoice.card.getLocalizedName() + TextFormatting.RESET;
         int sz = this.field_146289_q.func_78256_a(name);
         this.field_146289_q.func_78276_b(name, -sz / 2, -65, 0);
         this.field_146289_q.func_78279_b(cardChoice.card.getLocalizedText(), -70, -48, 140, 0);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.field_146297_k.field_71446_o.func_110577_a(this.txBase);
         GL11.glScaled(0.0625D, 0.0625D, 0.0D);
         int cc = cardChoice.card.getInspirationCost();
         boolean add = false;
         if (cc < 0) {
            add = true;
            cc = Math.abs(cc) + 1;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

         for(int a = 0; a < cc; ++a) {
            if (a == 0 && add) {
               this.func_73729_b(-10 * cc + 20 * a, -95, 48, 0, 16, 16);
            } else {
               this.func_73729_b(-10 * cc + 20 * a, -95, 32, 96, 16, 16);
            }
         }

         GL11.glPopMatrix();
         if (cardChoice.card.getRequiredItems() != null) {
            ItemStack[] items = cardChoice.card.getRequiredItems();
            GL11.glPushMatrix();

            for(int a = 0; a < items.length; ++a) {
               if (items[a] == null) {
                  GL11.glPushMatrix();
                  this.field_146297_k.field_71446_o.func_110577_a(this.txQuestion);
                  GL11.glScaled(0.125D, 0.125D, 0.0D);
                  GL11.glColor4f(0.75F, 0.75F, 0.75F, alpha);
                  GL11.glTranslated((double)(-9 * items.length + 18 * a), 35.0D, 0.0D);
                  UtilsFX.drawTexturedQuadFull(0.0F, 0.0F, 0.0D);
                  GL11.glPopMatrix();
               } else {
                  GL11.glPushMatrix();
                  GL11.glScaled(0.125D, 0.125D, 0.0D);
                  RenderHelper.func_74520_c();
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179091_B();
                  GlStateManager.func_179142_g();
                  GlStateManager.func_179145_e();
                  this.field_146296_j.func_180450_b(items[a], -9 * items.length + 18 * a, 35);
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179132_a(true);
                  GlStateManager.func_179126_j();
                  GL11.glPopMatrix();
               }
            }

            GL11.glPopMatrix();
         }
      }

      GlStateManager.func_179084_k();
      GL11.glPopMatrix();
   }

   private void drawSheetOverlay(double x, double y, ResearchTableData.CardChoice cardChoice, int mx, int my) {
      GL11.glPushMatrix();
      if (cardChoice != null && cardChoice.card.getRequiredItems() != null) {
         ItemStack[] items = cardChoice.card.getRequiredItems();

         for(int a = 0; a < items.length; ++a) {
            if (this.func_146978_c((int)(x - (double)(9 * items.length) + (double)(18 * a)), (int)(y + 36.0D), 15, 15, mx, my)) {
               if (items[a] == null) {
                  this.func_146283_a(Arrays.asList(I18n.func_74838_a("tc.card.unknown")), mx, my);
               } else {
                  this.func_146285_a(items[a], mx, my);
               }
            }
         }
      }

      GL11.glPopMatrix();
   }

   private void drawCards() {
      this.cardSelected = false;
      this.cardHover = new float[]{0.0F, 0.0F, 0.0F};
      this.cardZoomOut = new float[]{0.0F, 0.0F, 0.0F};
      this.cardZoomIn = new float[]{0.0F, 0.0F, 0.0F};
      this.cardActive = new boolean[]{true, true, true};
      int draw = 2;
      if (this.table.data.bonusDraws > 0) {
         ++draw;
         --this.table.data.bonusDraws;
      }

      this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, draw);
      this.cardChoices.clear();
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(this.buttonCreate);
      this.buttonCreate.field_146128_h = this.field_147003_i + 128;
      this.buttonCreate.field_146129_i = this.field_147009_r + 22;
      this.field_146292_n.add(this.buttonComplete);
      this.buttonComplete.field_146128_h = this.field_147003_i + 191;
      this.buttonComplete.field_146129_i = this.field_147009_r + 96;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 1) {
         this.playButtonClick();
         PacketHandler.INSTANCE.sendToServer(new PacketStartTheoryToServer(this.table.func_174877_v(), this.selectedAids));
      } else if (button.field_146127_k == 7) {
         this.playButtonClick();
         this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, 7);
         this.tempCatTotals.clear();
         this.lastDraw = null;
      } else {
         super.func_146284_a(button);
      }

   }

   private void checkButtons() {
      this.buttonComplete.active = false;
      this.buttonComplete.field_146125_m = false;
      if (this.table.data != null) {
         this.buttonCreate.active = false;
         this.buttonCreate.field_146125_m = false;
         if (this.table.data.isComplete()) {
            this.buttonComplete.active = true;
            this.buttonComplete.field_146125_m = true;
         }
      } else {
         this.buttonCreate.field_146125_m = true;
         if (this.table.func_70301_a(1) != null && this.table.func_70301_a(0) != null && this.table.func_70301_a(0).func_77952_i() != this.table.func_70301_a(0).func_77958_k()) {
            this.buttonCreate.active = true;
         } else {
            this.buttonCreate.active = false;
         }
      }

   }

   protected void func_73864_a(int mx, int my, int par3) throws IOException {
      super.func_73864_a(mx, my, par3);
      int xx = (this.field_146294_l - this.field_146999_f) / 2;
      int yy = (this.field_146295_m - this.field_147000_g) / 2;
      int pressed;
      if (this.table.data == null) {
         if (!this.currentAids.isEmpty()) {
            int side = Math.min(this.currentAids.size(), 6);
            int c = 0;
            pressed = 0;
            Iterator var9 = this.currentAids.iterator();

            while(var9.hasNext()) {
               String key = (String)var9.next();
               ITheorycraftAid mutator = (ITheorycraftAid)TheorycraftManager.aids.get(key);
               if (mutator != null) {
                  int x = 128 + 20 * c - side * 10;
                  int y = 85 + 35 * pressed;
                  if (this.func_146978_c(x, y, 16, 16, mx, my)) {
                     if (this.selectedAids.contains(key)) {
                        this.selectedAids.remove(key);
                     } else if (this.selectedAids.size() + 1 < this.dummyInspirationStart) {
                        this.selectedAids.add(key);
                     }
                  }

                  ++c;
                  if (c >= side) {
                     ++pressed;
                     c = 0;
                  }
               }
            }
         }
      } else {
         int sx = 128;
         int cw = 110;
         int a;
         if (this.cardChoices.size() > 0) {
            pressed = -1;

            for(a = 0; a < this.cardChoices.size(); ++a) {
               int var7 = mx - (5 + sx - 55 * this.cardChoices.size() + xx + cw * a);
               int var8 = my - (100 + yy - 60);
               if ((double)this.cardZoomOut[a] >= 0.95D && !this.cardSelected && var7 >= 0 && var8 >= 0 && var7 < 100 && var8 < 120) {
                  pressed = a;
                  break;
               }
            }

            if (pressed >= 0 && this.table.func_70301_a(0) != null && this.table.func_70301_a(0).func_77952_i() != this.table.func_70301_a(0).func_77958_k()) {
               this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, 4 + pressed);
            }
         } else {
            pressed = mx - (25 + xx);
            a = my - (55 + yy);
            if (pressed >= 0 && a >= 0 && pressed < 75 && a < 90 && this.table.func_70301_a(1) != null) {
               this.drawCards();
            }
         }
      }

   }

   void checkCards() {
      if (this.table.data.cardChoices.size() > 0 && this.cardChoices.isEmpty()) {
         this.syncFromTableChoices();
      }

      if (!this.cardSelected) {
         for(int a = 0; a < this.cardChoices.size(); ++a) {
            if (this.table.data != null && this.table.data.cardChoices.size() > a && ((ResearchTableData.CardChoice)this.table.data.cardChoices.get(a)).selected) {
               for(int q = 0; q < this.cardChoices.size(); ++q) {
                  this.cardActive[q] = ((ResearchTableData.CardChoice)this.table.data.cardChoices.get(q)).selected;
               }

               this.cardSelected = true;
               this.playButtonPageSelect();
               this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, 1);
               break;
            }
         }
      }

   }

   private void playButtonPageFlip() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.page, 1.0F, 1.0F);
   }

   private void playButtonPageSelect() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.pageturn, 1.0F, 1.0F);
   }

   private void playButtonClick() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.clack, 0.4F, 1.0F);
   }

   private void playButtonWrite() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.write, 0.3F, 1.0F);
   }
}
