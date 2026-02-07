package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;

public class PacketFXFocusPartImpact implements IMessage, IMessageHandler<PacketFXFocusPartImpact, IMessage> {
   private double x;
   private double y;
   private double z;
   private String parts;

   public PacketFXFocusPartImpact() {
   }

   public PacketFXFocusPartImpact(double x, double y, double z, String[] parts) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.parts = "";

      for(int a = 0; a < parts.length; ++a) {
         if (a > 0) {
            this.parts = this.parts + "%";
         }

         this.parts = this.parts + parts[a];
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeFloat((float)this.x);
      buffer.writeFloat((float)this.y);
      buffer.writeFloat((float)this.z);
      ByteBufUtils.writeUTF8String(buffer, this.parts);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = (double)buffer.readFloat();
      this.y = (double)buffer.readFloat();
      this.z = (double)buffer.readFloat();
      this.parts = ByteBufUtils.readUTF8String(buffer);
   }

   public IMessage onMessage(final PacketFXFocusPartImpact message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketFXFocusPartImpact.this.processMessage(message);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   void processMessage(PacketFXFocusPartImpact message) {
      String[] partKeys = message.parts.split("%");
      int amt = 20 / partKeys.length;
      Random r = Minecraft.func_71410_x().field_71441_e.field_73012_v;
      String[] var5 = partKeys;
      int var6 = partKeys.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String k = var5[var7];
         IFocusPart part = FocusHelper.getFocusPart(k);
         if (part != null) {
            for(int a = 0; a < amt; ++a) {
               part.drawCustomParticle(Minecraft.func_71410_x().field_71441_e, message.x, message.y, message.z, r.nextGaussian() * 0.075D, r.nextGaussian() * 0.075D, r.nextGaussian() * 0.075D);
            }
         }
      }

   }
}
