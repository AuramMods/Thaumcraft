package thaumcraft.common.entities.construct.golem;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.ISealDisplayer;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.golems.parts.GolemMaterial;
import thaumcraft.common.items.ItemTCBase;

public class ItemGolemPlacer extends ItemTCBase implements ISealDisplayer {
   public ItemGolemPlacer() {
      super("golem");
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      ItemStack is = new ItemStack(this, 1, 0);
      is.func_77983_a("props", new NBTTagLong(0L));
      par3List.add(is.func_77946_l());
      IGolemProperties props = new GolemProperties();
      props.setHead(GolemHead.getHeads()[1]);
      props.setArms(GolemArm.getArms()[1]);
      is.func_77983_a("props", new NBTTagLong(props.toLong()));
      par3List.add(is.func_77946_l());
      props = new GolemProperties();
      props.setMaterial(GolemMaterial.getMaterials()[1]);
      props.setHead(GolemHead.getHeads()[1]);
      props.setArms(GolemArm.getArms()[2]);
      is.func_77983_a("props", new NBTTagLong(props.toLong()));
      par3List.add(is.func_77946_l());
      props = new GolemProperties();
      props.setMaterial(GolemMaterial.getMaterials()[4]);
      props.setHead(GolemHead.getHeads()[1]);
      props.setArms(GolemArm.getArms()[3]);
      is.func_77983_a("props", new NBTTagLong(props.toLong()));
      par3List.add(is.func_77946_l());
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("props")) {
         IGolemProperties props = GolemProperties.fromLong(stack.func_77978_p().func_74763_f("props"));
         if (props.hasTrait(EnumGolemTrait.SMART)) {
            if (props.getRank() >= 10) {
               list.add("§6" + I18n.func_74838_a("golem.rank") + " " + props.getRank());
            } else {
               int rx = stack.func_77978_p().func_74762_e("xp");
               int xn = (props.getRank() + 1) * (props.getRank() + 1) * 1000;
               list.add("§6" + I18n.func_74838_a("golem.rank") + " " + props.getRank() + " §2(" + rx + "/" + xn + ")");
            }
         }

         list.add("§a" + props.getMaterial().getLocalizedName());
         Iterator var8 = props.getTraits().iterator();

         while(var8.hasNext()) {
            EnumGolemTrait tag = (EnumGolemTrait)var8.next();
            list.add("§9-" + tag.getLocalizedName());
         }
      }

      super.func_77624_a(stack, player, list, par4);
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      IBlockState bs = world.func_180495_p(pos);
      if (!bs.func_185904_a().func_76220_a()) {
         return EnumActionResult.FAIL;
      } else if (world.field_72995_K) {
         return EnumActionResult.PASS;
      } else {
         pos = pos.func_177972_a(side);
         world.func_180495_p(pos);
         if (!player.func_175151_a(pos, side, stack)) {
            return EnumActionResult.FAIL;
         } else {
            EntityThaumcraftGolem golem = new EntityThaumcraftGolem(world);
            golem.func_70080_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5D, 0.0F, 0.0F);
            if (golem != null && world.func_72838_d(golem)) {
               golem.setOwned(true);
               golem.setValidSpawn();
               golem.setOwnerId(player.func_110124_au());
               if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("props")) {
                  golem.setProperties(GolemProperties.fromLong(stack.func_77978_p().func_74763_f("props")));
               }

               if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("xp")) {
                  golem.rankXp = stack.func_77978_p().func_74762_e("xp");
               }

               golem.func_180482_a(world.func_175649_E(pos), (IEntityLivingData)null);
               if (!player.field_71075_bZ.field_75098_d) {
                  --stack.field_77994_a;
               }
            }

            return EnumActionResult.SUCCESS;
         }
      }
   }
}
