package thaumcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.gui.plugins.GuiHoverButton;
import thaumcraft.client.gui.plugins.GuiImageButton;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketFocusPlaceToServer;
import thaumcraft.common.lib.utils.HexUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;

@SideOnly(Side.CLIENT)
public class GuiFocalManipulator extends GuiContainer {
   private TileFocalManipulator table;
   private float xSize_lo;
   private float ySize_lo;
   private int isMouseButtonDown = 0;
   private String draggedPart = "";
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_wandtable.png");
   GuiImageButton buttonConfirm;
   GuiImageButton buttonScrollUp;
   GuiImageButton buttonScrollDown;
   long time;
   long nextSparkle;
   int lastSize;
   DecimalFormat myFormatter;
   ArrayList<String> shownParts;
   int partsStart;
   FocusCore tempCore;
   ItemStack[] components;
   boolean valid;
   private final int HEX_SIZE;
   final int tableCenterX;
   final int tableCenterY;
   ResourceLocation texh1;
   ResourceLocation texh2;
   static ResourceLocation iMedium = new ResourceLocation("thaumcraft", "textures/foci/_medium.png");
   static ResourceLocation iEffect = new ResourceLocation("thaumcraft", "textures/foci/_effect.png");
   static ResourceLocation iModifier = new ResourceLocation("thaumcraft", "textures/foci/_modifier.png");

   public GuiFocalManipulator(InventoryPlayer par1InventoryPlayer, TileFocalManipulator table) {
      super(new ContainerFocalManipulator(par1InventoryPlayer, table));
      this.buttonConfirm = new GuiImageButton(this, 0, this.field_147003_i + 60, this.field_147009_r + 112, 24, 16, I18n.func_74838_a("wandtable.text3"), (String)null, this.tex, 219, 27, 34, 26);
      this.buttonScrollUp = new GuiImageButton(this, 1, this.field_147003_i + 4, this.field_147009_r + 72 - 48, 10, 10, I18n.func_74838_a("button.up"), (String)null, this.tex, 224, 72, 16, 16);
      this.buttonScrollDown = new GuiImageButton(this, 2, this.field_147003_i + 4, this.field_147009_r + 72 + 48, 10, 10, I18n.func_74838_a("button.down"), (String)null, this.tex, 224, 88, 16, 16);
      this.nextSparkle = 0L;
      this.lastSize = 0;
      this.myFormatter = new DecimalFormat("#######.##");
      this.shownParts = new ArrayList();
      this.partsStart = 0;
      this.tempCore = null;
      this.components = null;
      this.valid = false;
      this.HEX_SIZE = 17;
      this.tableCenterX = 96;
      this.tableCenterY = 72;
      this.texh1 = new ResourceLocation("thaumcraft", "textures/gui/hex1.png");
      this.texh2 = new ResourceLocation("thaumcraft", "textures/gui/hex2.png");
      this.table = table;
      this.field_146999_f = 192;
      this.field_147000_g = 233;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.gatherInfo();
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 0) {
         this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, 0);
      } else if (button.field_146127_k == 1 && this.partsStart > 0) {
         --this.partsStart;
      } else if (button.field_146127_k == 2 && this.partsStart < this.shownParts.size() - 4) {
         ++this.partsStart;
      } else {
         super.func_146284_a(button);
      }

   }

   public void func_73863_a(int mx, int my, float par3) {
      super.func_73863_a(mx, my, par3);
      GL11.glBlendFunc(770, 771);
      this.xSize_lo = (float)mx;
      this.ySize_lo = (float)my;
      int baseX = this.field_147003_i;
      int baseY = this.field_147009_r;
      int gx = (this.field_146294_l - this.field_146999_f) / 2;
      int gy = (this.field_146295_m - this.field_147000_g) / 2;
      int mposx = false;
      int mposy = false;
      int count = 0;
      int index = 0;
      if (this.shownParts.isEmpty()) {
         this.buttonScrollDown.field_146125_m = false;
         this.buttonScrollUp.field_146125_m = false;
      } else {
         this.buttonScrollDown.field_146125_m = true;
         this.buttonScrollUp.field_146125_m = true;
      }

      Iterator var12 = this.shownParts.iterator();

      String sk;
      while(var12.hasNext()) {
         sk = (String)var12.next();
         ++count;
         if (count - 1 >= this.partsStart) {
            GL11.glTranslated(0.0D, 0.0D, 5.0D);
            drawPart(FocusHelper.getFocusPart(sk), gx + 8, 28 + gy + 30 * index, 32.0F, 220);
            GL11.glTranslated(0.0D, 0.0D, -5.0D);
            ++index;
            if (index > 3) {
               break;
            }
         }
      }

      count = 0;
      index = 0;
      var12 = this.shownParts.iterator();

      List list;
      while(var12.hasNext()) {
         sk = (String)var12.next();
         ++count;
         if (count - 1 >= this.partsStart) {
            int mposx = mx - (baseX - 4);
            int mposy = my - (baseY + 16 + 30 * index);
            if (mposx >= 0 && mposy >= 0 && mposx < 24 && mposy < 24) {
               list = this.genPartText(FocusHelper.getFocusPart(sk), false);
               this.drawHoveringTextFixed(list, gx + 8, 28 + gy + 30 * index, this.field_146289_q, this.field_146294_l - (baseX + this.field_146999_f - 16));
            }

            ++index;
            if (index > 3) {
               break;
            }
         }
      }

      int var10002;
      HexUtils.Hex h;
      if (this.draggedPart.isEmpty()) {
         h = this.checkMouseOverHex(mx, my, gx, gy);
         if (this.table.data != null && h != null && !((String)this.table.data.hexes.get(h.toString())).isEmpty()) {
            HexUtils.Pixel pix = h.toPixel(17);
            list = this.genPartText(FocusHelper.getFocusPart((String)this.table.data.hexes.get(h.toString())), true);
            this.getClass();
            var10002 = gx + 96 + (int)pix.x;
            this.getClass();
            this.drawHoveringTextFixed(list, var10002, gy + 72 + (int)pix.y, this.field_146289_q, this.field_146294_l - (baseX + this.field_146999_f - 16));
         }
      }

      RenderHelper.func_74518_a();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.buttonConfirm.active = this.table.vis <= 0.0F && this.valid;
      if (this.table.vis <= 0.0F) {
         int mod;
         int a;
         HexUtils.Hex target;
         if (Mouse.isButtonDown(0)) {
            if (this.isMouseButtonDown == 0 && !this.shownParts.isEmpty()) {
               for(mod = 0; mod < 4; ++mod) {
                  a = gx - 8;
                  int sy = gy + 12 + 30 * mod;
                  if (mx >= a && mx < a + 30 && my >= sy && my < sy + 30) {
                     this.playSocketSound(0.9F);
                     this.isMouseButtonDown = 1;
                     this.draggedPart = (String)this.shownParts.get(mod + this.partsStart);
                  }
               }
            } else if (this.isMouseButtonDown == 1 && !this.draggedPart.isEmpty()) {
               h = this.checkMouseOverHex(mx, my, gx, gy);
               if (this.table.data != null && h != null && ((String)this.table.data.hexes.get(h.toString())).isEmpty()) {
                  this.getClass();
                  var10002 = gx + 96;
                  this.getClass();
                  this.drawHexHighlight(h, var10002, gy + 72);
               }

               GL11.glTranslated(0.0D, 0.0D, 5.0D);
               drawPart(FocusHelper.getFocusPart(this.draggedPart), mx, my, 32.0F, 220);
               GL11.glTranslated(0.0D, 0.0D, -5.0D);
               if (this.table.data != null && h != null && ((String)this.table.data.hexes.get(h.toString())).isEmpty()) {
                  for(a = 0; a < 6; ++a) {
                     target = h.getNeighbour(a);
                     String key = (String)this.table.data.hexes.get(target.toString());
                     if (key != null && !key.isEmpty()) {
                        IFocusPart p1 = FocusHelper.getFocusPart(key);
                        IFocusPart p2 = FocusHelper.getFocusPart(this.draggedPart);
                        if (FocusHelper.canPartsConnect(p1, p2)) {
                           this.drawHexLink(gx, gy, h, target, 0.5F);
                        }
                     }
                  }
               }
            }
         } else {
            if (this.isMouseButtonDown == 1 && !this.draggedPart.isEmpty() && this.table.data != null) {
               mod = mx - (gx + 96);
               a = my - (gy + 72);
               target = (new HexUtils.Pixel((double)mod, (double)a)).toHex(17);
               if (this.table.data.hexes.containsKey(target.toString()) && ((String)this.table.data.hexes.get(target.toString())).isEmpty() && this.validHex(target, this.draggedPart)) {
                  this.playSocketSound(1.0F);
                  this.table.data.hexes.put(target.toString(), this.draggedPart);
                  PacketHandler.INSTANCE.sendToServer(new PacketFocusPlaceToServer((byte)target.q, (byte)target.r, this.table.func_174877_v(), this.draggedPart));
                  this.draggedPart = "";
                  this.gatherInfo();
               }
            }

            this.isMouseButtonDown = 0;
            this.draggedPart = "";
         }
      }

      GlStateManager.func_179084_k();
   }

   private List genPartText(IFocusPart fp, boolean placed) {
      List list = new ArrayList();
      if (fp != null) {
         list.add(fp.getName());
         list.add(TextFormatting.DARK_PURPLE + fp.getText());
         if (fp.getEffectMultiplier() != 1.0F) {
            list.add(TextFormatting.GOLD + I18n.func_74838_a("focuspart.eff") + (fp.getEffectMultiplier() < 1.0F ? TextFormatting.RED : TextFormatting.GREEN) + " x" + this.myFormatter.format((double)fp.getEffectMultiplier()));
         }

         if (fp instanceof IFocusPartEffect && ((IFocusPartEffect)fp).getBaseCost() > 0.0F) {
            list.add(TextFormatting.AQUA + I18n.func_74838_a("focuspart.base") + " " + this.myFormatter.format((double)((IFocusPartEffect)fp).getBaseCost()));
         }

         if (fp.getCostMultiplier() != 1.0F) {
            list.add(TextFormatting.AQUA + I18n.func_74838_a("focuspart.mult") + (fp.getCostMultiplier() > 1.0F ? TextFormatting.RED : TextFormatting.GREEN) + " x" + this.myFormatter.format((double)fp.getCostMultiplier()));
         }

         if (placed && this.tempCore != null && fp instanceof IFocusPartEffect) {
            FocusCore.FocusEffect[] var4 = this.tempCore.effects;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               FocusCore.FocusEffect fe = var4[var6];
               if (fe.effect == fp) {
                  float cost = fe.effect.getBaseCost() * fe.costMultipler;
                  float eff = fe.effectMultipler;
                  list.add("");
                  list.add(TextFormatting.GOLD + I18n.func_74838_a("focuspart.finalCost") + " " + (cost > 1.0F ? TextFormatting.RED : TextFormatting.GREEN) + this.myFormatter.format((double)cost));
                  list.add(TextFormatting.GOLD + I18n.func_74838_a("focuspart.finalEffect") + (eff < 1.0F ? TextFormatting.RED : TextFormatting.GREEN) + " x" + this.myFormatter.format((double)eff));
                  break;
               }
            }
         }
      }

      return list;
   }

   protected void func_146976_a(float par1, int par2, int par3) {
      this.time = System.currentTimeMillis();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      if (this.table.func_70301_a(0) == null || this.table.doGuiReset) {
         this.table.doGuiReset = false;
         this.gatherInfo();
      }

      if (this.table.castCost > 0.0F) {
         this.field_146289_q.func_175063_a(TextFormatting.AQUA + I18n.func_74838_a("item.Focus.cost1") + ": " + this.table.castCost, (float)(k + 168), (float)(l + 16), 10092429);
      }

      if (this.components != null && this.components.length > 0) {
         this.field_146289_q.func_175063_a(TextFormatting.GOLD + I18n.func_74838_a("wandtable.text4"), (float)(k + 168), (float)(l + 35), 10092429);
      }

      int dispVis = (int)(this.table.vis > 0.0F ? this.table.vis : this.table.visCost);
      this.field_146289_q.func_175063_a(TextFormatting.AQUA + "" + dispVis, (float)(k + 124), (float)(l + 27), 10092429);
      int dispXp = this.table.vis > 0.0F ? 0 : this.table.xpCost;
      this.field_146289_q.func_175063_a("" + dispXp, (float)(k + 60), (float)(l + 27), dispXp > this.field_146297_k.field_71439_g.field_71068_ca ? 16151160 : 10092429);
      GL11.glDisable(3042);
      this.drawStuff(this.field_147003_i, this.field_147009_r, par2, par3);
   }

   private void drawStuff(int x, int y, int mx, int my) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GlStateManager.func_179140_f();
      ArrayList<String> temp = new ArrayList();
      if (this.table.data != null) {
         Iterator var6 = this.table.data.hexes.keySet().iterator();

         label52:
         while(true) {
            HexUtils.Hex hex;
            IFocusPart p;
            do {
               String key;
               do {
                  do {
                     do {
                        if (!var6.hasNext()) {
                           break label52;
                        }

                        String hl = (String)var6.next();
                        hex = HexUtils.Hex.fromString(hl);
                        key = (String)this.table.data.hexes.get(hl);
                     } while(hex == null);
                  } while(key == null);
               } while(key.isEmpty());

               p = FocusHelper.getFocusPart(key);
            } while(p == null);

            HexUtils.Pixel pix = hex.toPixel(17);
            drawPart(p, x + 96 + (int)pix.x, y + 72 + (int)pix.y, 32.0F, 220);

            for(int a = 0; a < 6; ++a) {
               HexUtils.Hex target = hex.getNeighbour(a);
               String key2 = (String)this.table.data.hexes.get(target.toString());
               if (key2 != null && !key2.isEmpty() && !temp.contains(hex.toString() + target.toString())) {
                  IFocusPart p1 = FocusHelper.getFocusPart(key2);
                  if (FocusHelper.canPartsConnect(p1, p)) {
                     this.drawHexLink(x, y, hex, target, 0.66F);
                     temp.add(hex.toString() + target.toString());
                     temp.add(target.toString() + hex.toString());
                  }
               }
            }
         }
      }

      GlStateManager.func_179145_e();
      RenderHelper.func_74518_a();
      GL11.glPopMatrix();
   }

   private void gatherInfo() {
      this.tempCore = null;
      this.field_146292_n.clear();
      this.field_146292_n.add(this.buttonConfirm);
      this.buttonConfirm.field_146128_h = this.field_147003_i + 61;
      this.buttonConfirm.field_146129_i = this.field_147009_r + 114;
      this.field_146292_n.add(this.buttonScrollUp);
      this.buttonScrollUp.field_146128_h = this.field_147003_i + 8;
      this.buttonScrollUp.field_146129_i = this.field_147009_r + 72 - 64;
      this.field_146292_n.add(this.buttonScrollDown);
      this.buttonScrollDown.field_146128_h = this.field_147003_i + 8;
      this.buttonScrollDown.field_146129_i = this.field_147009_r + 72 + 66;
      this.field_146292_n.add(new GuiHoverButton(this, 3, this.field_147003_i + 60, this.field_147009_r + 30, 32, 16, I18n.func_74838_a("wandtable.text2"), (String)null, new ResourceLocation("thaumcraft", "textures/gui/costxp.png")));
      this.field_146292_n.add(new GuiHoverButton(this, 4, this.field_147003_i + 125, this.field_147009_r + 30, 32, 16, I18n.func_74838_a("wandtable.text1"), (String)null, new ResourceLocation("thaumcraft", "textures/gui/costvis.png")));
      this.shownParts.clear();
      AspectList crystals = new AspectList();
      this.components = null;
      if (this.table.func_70301_a(0) != null) {
         ArrayList<String> pMed = new ArrayList();
         ArrayList<String> pEff = new ArrayList();
         ArrayList<String> pMod = new ArrayList();
         Iterator var5 = FocusHelper.focusParts.values().iterator();

         while(true) {
            IFocusPart part;
            label152:
            while(true) {
               do {
                  if (!var5.hasNext()) {
                     Collections.sort(pMed);
                     Collections.sort(pEff);
                     Collections.sort(pMod);
                     this.shownParts.addAll(pMed);
                     this.shownParts.addAll(pEff);
                     this.shownParts.addAll(pMod);
                     boolean m = false;
                     boolean e = false;
                     boolean o = true;
                     if (this.table.data != null) {
                        Iterator var20 = this.table.data.hexes.keySet().iterator();

                        label130:
                        while(true) {
                           String hexKey;
                           IFocusPart part;
                           do {
                              if (!var20.hasNext()) {
                                 break label130;
                              }

                              hexKey = (String)var20.next();
                              part = FocusHelper.getFocusPart((String)this.table.data.hexes.get(hexKey));
                           } while(part == null);

                           crystals.add(part.getAspect(), 1);
                           if (part.getType() == IFocusPart.EnumFocusPartType.MEDIUM) {
                              m = true;
                           }

                           if (part.getType() == IFocusPart.EnumFocusPartType.EFFECT) {
                              e = true;
                           }

                           HexUtils.Hex h = HexUtils.Hex.fromString(hexKey);
                           boolean con = false;

                           for(int a = 0; a < 6; ++a) {
                              HexUtils.Hex target = h.getNeighbour(a);
                              String key = (String)this.table.data.hexes.get(target.toString());
                              if (key != null && !key.isEmpty()) {
                                 IFocusPart p1 = FocusHelper.getFocusPart(key);
                                 if (FocusHelper.canPartsConnect(p1, part)) {
                                    con = true;
                                    break;
                                 }
                              }
                           }

                           if (!con) {
                              o = false;
                              break;
                           }
                        }

                        this.tempCore = this.table.calcCosts();
                     }

                     boolean c = false;
                     if (crystals.getAspects().length > 0) {
                        c = true;
                        this.components = new ItemStack[crystals.getAspects().length];
                        int r = 0;
                        Aspect[] var23 = crystals.getAspects();
                        int q = var23.length;

                        int z;
                        for(z = 0; z < q; ++z) {
                           Aspect as = var23[z];
                           this.components[r] = ThaumcraftApiHelper.makeCrystal(as, crystals.getAmount(as));
                           ++r;
                        }

                        if (this.components.length >= 0) {
                           boolean[] owns = new boolean[this.components.length];

                           for(q = 0; q < this.components.length; ++q) {
                              owns[q] = InventoryUtils.isPlayerCarryingAmount(this.field_146297_k.field_71439_g, this.components[q], false);
                              if (!owns[q]) {
                                 c = false;
                              }
                           }
                        }

                        if (this.components != null && this.components.length > 0) {
                           int i = 0;
                           q = 0;
                           z = 0;
                           ItemStack[] var29 = this.components;
                           int var30 = var29.length;

                           for(int var31 = 0; var31 < var30; ++var31) {
                              ItemStack stack = var29[var31];
                              this.field_146292_n.add(new GuiHoverButton(this, 11 + z, this.field_147003_i + 174 + i * 16, this.field_147009_r + 54 + 16 * q, 16, 16, stack.func_82833_r(), (String)null, stack));
                              ++i;
                              if (i > 4) {
                                 i = 0;
                                 ++q;
                              }

                              ++z;
                           }
                        }
                     }

                     this.valid = c && m && e && o && this.table.xpCost <= this.field_146297_k.field_71439_g.field_71068_ca;
                     return;
                  }

                  part = (IFocusPart)var5.next();
               } while(!ThaumcraftCapabilities.knowsResearchStrict(this.field_146297_k.field_71439_g, part.getResearch()));

               if (this.table.data == null) {
                  break;
               }

               if (part.getType() != IFocusPart.EnumFocusPartType.MEDIUM || ((String)this.table.data.hexes.get("0:0")).isEmpty()) {
                  Iterator var7 = this.table.data.hexes.values().iterator();

                  while(true) {
                     if (!var7.hasNext()) {
                        break label152;
                     }

                     String key = (String)var7.next();
                     if (part.getKey().equals(key)) {
                        break;
                     }
                  }
               }
            }

            switch(part.getType()) {
            case EFFECT:
               pEff.add(part.getKey());
               break;
            case MEDIUM:
               pMed.add(part.getKey());
               break;
            case MODIFIER:
               pMod.add(part.getKey());
            }
         }
      }
   }

   protected void func_146979_b(int mouseX, int mouseY) {
      RenderHelper.func_74518_a();
      Iterator iterator = this.field_146292_n.iterator();

      while(iterator.hasNext()) {
         GuiButton guibutton = (GuiButton)iterator.next();
         if (guibutton.func_146115_a()) {
            guibutton.func_146111_b(mouseX - this.field_147003_i, mouseY - this.field_147009_r);
            break;
         }
      }

      RenderHelper.func_74520_c();
   }

   protected void func_184098_a(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      super.func_184098_a(slotIn, slotId, mouseButton, type);
      if (this.draggedPart == null || this.draggedPart.isEmpty()) {
         this.gatherInfo();
      }

   }

   protected void func_73864_a(int mx, int my, int par3) {
      try {
         super.func_73864_a(mx, my, par3);
      } catch (IOException var7) {
      }

      int gx = (this.field_146294_l - this.field_146999_f) / 2;
      int gy = (this.field_146295_m - this.field_147000_g) / 2;
      HexUtils.Hex h = this.checkMouseOverHex(mx, my, gx, gy);
      if (this.table.vis <= 0.0F && this.table.data != null && h != null && !((String)this.table.data.hexes.get(h.toString())).isEmpty()) {
         if (par3 == 0) {
            this.draggedPart = "" + (String)this.table.data.hexes.get(h.toString());
            this.isMouseButtonDown = 1;
         }

         this.playSocketSound(0.8F);
         this.table.data.hexes.put(h.toString(), "");
         PacketHandler.INSTANCE.sendToServer(new PacketFocusPlaceToServer((byte)h.q, (byte)h.r, this.table.func_174877_v(), ""));
         this.gatherInfo();
      }

   }

   private void playButtonClick() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.clack, 0.4F, 1.0F);
   }

   private void playSocketSound(float pitch) {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.crystal, 0.4F, pitch);
   }

   protected void drawHoveringTextFixed(List listin, int x, int y, FontRenderer font, int width) {
      if (!listin.isEmpty()) {
         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
         List list = new ArrayList();
         Iterator var7 = listin.iterator();

         String s;
         while(var7.hasNext()) {
            Object o = var7.next();
            s = (String)o;
            s = this.trimStringNewline(s);
            List list2 = font.func_78271_c(s, width);
            Iterator iterator = list2.iterator();

            while(iterator.hasNext()) {
               String s1 = (String)iterator.next();
               list.add(s1);
            }
         }

         int k = 0;
         Iterator iterator = list.iterator();

         int k2;
         while(iterator.hasNext()) {
            s = (String)iterator.next();
            k2 = font.func_78256_a(s);
            if (k2 > k) {
               k = k2;
            }
         }

         int j2 = x + 12;
         k2 = y - 12;
         int i1 = 8;
         if (list.size() > 1) {
            i1 += 2 + (list.size() - 1) * 10;
         }

         this.field_73735_i = 300.0F;
         this.field_146296_j.field_77023_b = 300.0F;
         int j1 = -267386864;
         this.func_73733_a(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
         this.func_73733_a(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
         this.func_73733_a(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
         this.func_73733_a(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
         this.func_73733_a(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
         int k1 = 1347420415;
         int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
         this.func_73733_a(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
         this.func_73733_a(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
         this.func_73733_a(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
         this.func_73733_a(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

         for(int i2 = 0; i2 < list.size(); ++i2) {
            String s1 = (String)list.get(i2);
            font.func_175063_a(s1, (float)j2, (float)k2, -1);
            if (i2 == 0) {
               k2 += 2;
            }

            k2 += 10;
         }

         this.field_73735_i = 0.0F;
         this.field_146296_j.field_77023_b = 0.0F;
         GlStateManager.func_179145_e();
         GlStateManager.func_179126_j();
         RenderHelper.func_74519_b();
         GlStateManager.func_179091_B();
      }

   }

   private String trimStringNewline(String text) {
      while(text != null && text.endsWith("\n")) {
         text = text.substring(0, text.length() - 1);
      }

      return text;
   }

   private HexUtils.Hex checkMouseOverHex(int mx, int my, int gx, int gy) {
      int mouseX = mx - (gx + 96);
      int mouseY = my - (gy + 72);
      HexUtils.Hex hp = (new HexUtils.Pixel((double)mouseX, (double)mouseY)).toHex(17);
      if (this.table.data != null && this.table.data.hexes.containsKey(hp.toString())) {
         return this.draggedPart != null && !this.validHex(hp, this.draggedPart) ? null : hp;
      } else {
         return null;
      }
   }

   private boolean validHex(HexUtils.Hex hp, String part) {
      if (!part.isEmpty()) {
         if (FocusHelper.getFocusPart(part).getType() == IFocusPart.EnumFocusPartType.MEDIUM) {
            if (hp.q != 0 || hp.r != 0) {
               return false;
            }
         } else if (hp.q == 0 && hp.r == 0) {
            return false;
         }
      }

      return true;
   }

   public static void drawPart(IFocusPart part, int x, int y, float scale, int bright) {
      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glTranslated((double)x, (double)y, 0.0D);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
      float bob = MathHelper.func_76126_a((float)(Minecraft.func_71410_x().field_71439_g.field_70173_aa + x * y * 10) / 6.0F) * 0.1F;
      Color color = new Color(part.getGemColor());
      float r = (float)color.getRed() / 255.0F * ((float)bright / 220.0F);
      float g = (float)color.getGreen() / 255.0F * ((float)bright / 220.0F);
      float b = (float)color.getBlue() / 255.0F * ((float)bright / 220.0F);
      switch(part.getType()) {
      case EFFECT:
         UtilsFX.renderQuadCentered(iEffect, scale, r, g, b, 220, 771, 1.0F);
         break;
      case MEDIUM:
         UtilsFX.renderQuadCentered(iMedium, scale, r, g, b, 220, 771, 1.0F);
         break;
      case MODIFIER:
         UtilsFX.renderQuadCentered(iModifier, scale, r, g, b, 220, 771, 1.0F);
      }

      color = new Color(part.getIconColor());
      r = (float)color.getRed() / 255.0F * ((float)bright / 220.0F);
      g = (float)color.getGreen() / 255.0F * ((float)bright / 220.0F);
      b = (float)color.getBlue() / 255.0F * ((float)bright / 220.0F);
      GL11.glTranslated(0.0D, 0.0D, 1.0D);
      UtilsFX.renderQuadCentered(part.getIcon(), 16.0F, MathHelper.func_76131_a(r + bob, 0.0F, 1.0F), MathHelper.func_76131_a(g + bob, 0.0F, 1.0F), MathHelper.func_76131_a(b + bob, 0.0F, 1.0F), bright, 1, 0.75F);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }

   private void drawHexLink(int x, int y, HexUtils.Hex hex1, HexUtils.Hex hex2, float opacity) {
      HexUtils.Pixel pix1 = hex1.toPixel(17);
      HexUtils.Pixel pix2 = hex2.toPixel(17);
      double xx = (pix1.x + pix2.x) / 2.0D;
      double yy = (pix1.y + pix2.y) / 2.0D;
      double theta = Math.atan2(pix1.y - pix2.y, pix1.x - pix2.x);
      double angle = Math.toDegrees(theta);
      if (angle < 0.0D) {
         angle += 360.0D;
      }

      GL11.glPushMatrix();
      GL11.glTranslated((double)(x + 96) + xx, (double)(y + 72) + yy, 8.0D);
      GL11.glRotated(angle, 0.0D, 0.0D, 1.0D);
      GL11.glScaled(1.5D, 1.0D, 1.0D);
      this.field_146297_k.field_71446_o.func_110577_a(ParticleEngine.particleTexture);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.renderQuadCentered(64, 64, 576 + (int)(128.0D + ((double)this.field_146297_k.func_175606_aa().field_70173_aa + xx + yy * 10.0D) % 12.0D), 7.0F, 0.75F, 0.125F, 1.0F, 220, 1, opacity);
      GL11.glPopMatrix();
   }

   private void drawHexHighlight(HexUtils.Hex hex, int x, int y) {
      GL11.glPushMatrix();
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      this.field_146297_k.field_71446_o.func_110577_a(this.texh2);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      HexUtils.Pixel pix = hex.toPixel(17);
      GL11.glTranslated((double)x + pix.x, (double)y + pix.y, 0.0D);
      float bob = MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 3.0F);
      Tessellator tessellator = Tessellator.func_178181_a();
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181711_k);
      int i = 200;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      tessellator.func_178180_c().func_181662_b((double)(-15.0F - bob), (double)(15.0F + bob), (double)this.field_73735_i).func_187315_a(0.0D, 1.0D).func_187314_a(j, k).func_181666_a(1.0F, 1.0F, 1.0F, 0.5F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(15.0F + bob), (double)(15.0F + bob), (double)this.field_73735_i).func_187315_a(1.0D, 1.0D).func_187314_a(j, k).func_181666_a(1.0F, 1.0F, 1.0F, 0.5F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(15.0F + bob), (double)(-15.0F - bob), (double)this.field_73735_i).func_187315_a(1.0D, 0.0D).func_187314_a(j, k).func_181666_a(1.0F, 1.0F, 1.0F, 0.5F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(-15.0F - bob), (double)(-15.0F - bob), (double)this.field_73735_i).func_187315_a(0.0D, 0.0D).func_187314_a(j, k).func_181666_a(1.0F, 1.0F, 1.0F, 0.5F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glBlendFunc(770, 771);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }
}
