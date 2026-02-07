package thaumcraft.common.items.curios;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.SoundsTC;

public class ItemPechWand extends ItemTCBase {
   public ItemPechWand() {
      super("pech_wand");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      list.add(I18n.func_74838_a("item.curio.text"));
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(playerIn);
      if (!knowledge.isResearchKnown("BASETHAUMATURGY")) {
         if (!worldIn.field_72995_K) {
            playerIn.func_145747_a(new TextComponentString(TextFormatting.RED + I18n.func_74838_a("not.pechwand")));
         }

         return super.func_77659_a(itemStackIn, worldIn, playerIn, hand);
      } else {
         if (!playerIn.field_71075_bZ.field_75098_d) {
            --itemStackIn.field_77994_a;
         }

         worldIn.func_184148_a((EntityPlayer)null, playerIn.field_70165_t, playerIn.field_70163_u, playerIn.field_70161_v, SoundsTC.learn, SoundCategory.NEUTRAL, 0.5F, 0.4F / (field_77697_d.nextFloat() * 0.4F + 0.8F));
         if (!worldIn.field_72995_K) {
            if (!knowledge.isResearchKnown("FOCUSPECH")) {
               ThaumcraftApi.internalMethods.progressResearch(playerIn, "FOCUSPECH");
               playerIn.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.pechwand")));
            }

            ThaumcraftApi.internalMethods.addKnowledge(playerIn, IPlayerKnowledge.EnumKnowledgeType.EPIPHANY, (ResearchCategory)null, 1);
            int oProg = IPlayerKnowledge.EnumKnowledgeType.OBSERVATION.getProgression();
            ResearchCategory[] rc = (ResearchCategory[])ResearchCategories.researchCategories.values().toArray(new ResearchCategory[0]);
            ThaumcraftApi.internalMethods.addKnowledge(playerIn, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, rc[playerIn.func_70681_au().nextInt(rc.length)], MathHelper.func_76136_a(playerIn.func_70681_au(), oProg / 3, oProg / 2));
            int tProg = IPlayerKnowledge.EnumKnowledgeType.THEORY.getProgression();
            ThaumcraftApi.internalMethods.addKnowledge(playerIn, IPlayerKnowledge.EnumKnowledgeType.THEORY, rc[playerIn.func_70681_au().nextInt(rc.length)], MathHelper.func_76136_a(playerIn.func_70681_au(), tProg / 5, tProg / 4));
         }

         playerIn.func_71029_a(StatList.func_188057_b(this));
         return super.func_77659_a(itemStackIn, worldIn, playerIn, hand);
      }
   }
}
