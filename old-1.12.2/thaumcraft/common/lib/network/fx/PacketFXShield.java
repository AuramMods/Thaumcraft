package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.client.fx.other.FXShieldRunes;

public class PacketFXShield implements IMessage, IMessageHandler<PacketFXShield, IMessage> {
   private int source;
   private int target;

   public PacketFXShield() {
   }

   public PacketFXShield(int source, int target) {
      this.source = source;
      this.target = target;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.source);
      buffer.writeInt(this.target);
   }

   public void fromBytes(ByteBuf buffer) {
      this.source = buffer.readInt();
      this.target = buffer.readInt();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXShield message, MessageContext ctx) {
      Entity p = Thaumcraft.proxy.getClientWorld().func_73045_a(message.source);
      if (p == null) {
         return null;
      } else {
         float pitch = 0.0F;
         float yaw = 0.0F;
         if (message.target >= 0) {
            Entity t = Thaumcraft.proxy.getClientWorld().func_73045_a(message.target);
            if (t != null) {
               double d0 = p.field_70165_t - t.field_70165_t;
               double d1 = (p.func_174813_aQ().field_72338_b + p.func_174813_aQ().field_72337_e) / 2.0D - (t.func_174813_aQ().field_72338_b + t.func_174813_aQ().field_72337_e) / 2.0D;
               double d2 = p.field_70161_v - t.field_70161_v;
               double d3 = (double)MathHelper.func_76133_a(d0 * d0 + d2 * d2);
               float f = (float)(Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
               float f1 = (float)(-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D));
               pitch = f1;
               yaw = f;
            } else {
               pitch = 90.0F;
               yaw = 0.0F;
            }

            FXShieldRunes fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 8, yaw, pitch);
            FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
         } else {
            FXShieldRunes fb;
            if (message.target == -1) {
               fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 8, 0.0F, 90.0F);
               FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
               fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 8, 0.0F, 270.0F);
               FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
            } else if (message.target == -2) {
               fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 8, 0.0F, 270.0F);
               FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
            } else if (message.target == -3) {
               fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 8, 0.0F, 90.0F);
               FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
            }
         }

         return null;
      }
   }
}
