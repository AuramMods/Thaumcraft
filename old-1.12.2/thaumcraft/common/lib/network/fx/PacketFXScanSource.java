package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.utils.Utils;

public class PacketFXScanSource implements IMessage, IMessageHandler<PacketFXScanSource, IMessage> {
   private long loc;
   private int size;
   final int C_QUARTZ = 15064789;
   final int C_IRON = 14200723;
   final int C_LAPIS = 1328572;
   final int C_GOLD = 16576075;
   final int C_DIAMOND = 6155509;
   final int C_EMERALD = 1564002;
   final int C_REDSTONE = 16711680;
   final int C_COAL = 1052688;
   final int C_SILVER = 14342653;
   final int C_TIN = 15724539;
   final int C_COPPER = 16620629;
   final int C_AMBER = 16626469;
   final int C_CINNABAR = 10159368;

   public PacketFXScanSource() {
   }

   public PacketFXScanSource(BlockPos pos, int size) {
      this.loc = pos.func_177986_g();
      this.size = size;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.loc);
      buffer.writeByte(this.size);
   }

   public void fromBytes(ByteBuf buffer) {
      this.loc = buffer.readLong();
      this.size = buffer.readByte();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketFXScanSource message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketFXScanSource.this.startScan(Minecraft.func_71410_x().field_71439_g.field_70170_p, BlockPos.func_177969_a(message.loc), message.size);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   public void startScan(World world, BlockPos pos, int r) {
      int range = 4 + r * 4;
      ArrayList<BlockPos> positions = new ArrayList();

      int c;
      for(int xx = -range; xx <= range; ++xx) {
         for(int yy = -range; yy <= range; ++yy) {
            for(c = -range; c <= range; ++c) {
               BlockPos p = pos.func_177982_a(xx, yy, c);
               if (Utils.isOreBlock(world, p)) {
                  positions.add(p);
               }
            }
         }
      }

      while(true) {
         BlockPos start;
         ArrayList coll;
         do {
            if (positions.isEmpty()) {
               return;
            }

            start = (BlockPos)positions.get(0);
            coll = new ArrayList();
            coll.add(start);
            positions.remove(0);
            this.calcGroup(world, start, coll, positions);
         } while(coll.isEmpty());

         c = this.getOreColor(world, start);
         double x = 0.0D;
         double y = 0.0D;
         double z = 0.0D;

         BlockPos p;
         for(Iterator var15 = coll.iterator(); var15.hasNext(); z += (double)p.func_177952_p() + 0.5D) {
            p = (BlockPos)var15.next();
            x += (double)p.func_177958_n() + 0.5D;
            y += (double)p.func_177956_o() + 0.5D;
         }

         x /= (double)coll.size();
         y /= (double)coll.size();
         z /= (double)coll.size();
         double dis = Math.sqrt(pos.func_177957_d(x, y, z));
         FXGeneric fb = new FXGeneric(world, x, y, z, 0.0D, 0.0D, 0.0D);
         fb.func_187114_a(44);
         Color cc = new Color(c);
         fb.func_70538_b((float)cc.getRed() / 255.0F, (float)cc.getGreen() / 255.0F, (float)cc.getBlue() / 255.0F);
         float q = ((float)cc.getRed() / 255.0F + (float)cc.getGreen() / 255.0F + (float)cc.getBlue() / 255.0F) / 3.0F;
         fb.setAlphaF(0.0F, 1.0F, 0.8F, 0.0F);
         fb.setParticles(240, 15, 1);
         fb.setGridSize(16);
         fb.setLoop(true);
         fb.setScale(9.0F);
         fb.setLayer(q < 0.25F ? 3 : 2);
         fb.setRotationSpeed(0.0F);
         ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, (int)(dis * 3.0D));
      }
   }

   private void calcGroup(World world, BlockPos start, ArrayList<BlockPos> coll, ArrayList<BlockPos> positions) {
      IBlockState bs = world.func_180495_p(start);

      for(int x = -1; x <= 1; ++x) {
         for(int y = -1; y <= 1; ++y) {
            for(int z = -1; z <= 1; ++z) {
               BlockPos t = (new BlockPos(start)).func_177982_a(x, y, z);
               IBlockState ts = world.func_180495_p(t);
               if (ts.equals(bs) && positions.contains(t)) {
                  positions.remove(t);
                  coll.add(t);
                  if (positions.isEmpty()) {
                     return;
                  }

                  this.calcGroup(world, t, coll, positions);
               }
            }
         }
      }

   }

   private int getOreColor(World world, BlockPos pos) {
      IBlockState bi = world.func_180495_p(pos);
      if (bi.func_177230_c() != Blocks.field_150350_a && bi.func_177230_c() != Blocks.field_150357_h) {
         int md = bi.func_177230_c().func_180651_a(bi);
         ItemStack is = new ItemStack(bi.func_177230_c(), 1, md);
         if (is == null || is.func_77973_b() == null) {
            return 12632256;
         }

         int[] od = OreDictionary.getOreIDs(is);
         if (od != null && od.length > 0) {
            int[] var7 = od;
            int var8 = od.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               int id = var7[var9];
               if (OreDictionary.getOreName(id) != null) {
                  if (OreDictionary.getOreName(id).toUpperCase().contains("IRON")) {
                     return 14200723;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("COAL")) {
                     return 1052688;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("REDSTONE")) {
                     return 16711680;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("GOLD")) {
                     return 16576075;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("LAPIS")) {
                     return 1328572;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("DIAMOND")) {
                     return 6155509;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("EMERALD")) {
                     return 1564002;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("QUARTZ")) {
                     return 15064789;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("SILVER")) {
                     return 14342653;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("TIN")) {
                     return 15724539;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("COPPER")) {
                     return 16620629;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("AMBER")) {
                     return 16626469;
                  }

                  if (OreDictionary.getOreName(id).toUpperCase().contains("CINNABAR")) {
                     return 10159368;
                  }
               }
            }
         }
      }

      return 12632256;
   }
}
