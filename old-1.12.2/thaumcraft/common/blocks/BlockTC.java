package thaumcraft.common.blocks;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.ConfigItems;

public class BlockTC<E extends Enum<E> & IStringSerializable> extends Block implements IBlockTypes {
   protected static IProperty[] tempVariants;
   public final PropertyEnum<E> TYPE;
   public final E[] variantValues;

   public BlockTC(Material material, Class types) {
      super(createProperties(material, types));
      if (types != Object.class) {
         this.TYPE = PropertyEnum.func_177709_a("type", types);
         this.variantValues = (Enum[])this.TYPE.func_177699_b().getEnumConstants();
      } else {
         this.TYPE = null;
         this.variantValues = null;
      }

      this.func_149647_a(ConfigItems.TABTC);
      this.func_149752_b(2.0F);
      this.setInitDefaultState();
   }

   public BlockTC(Material material) {
      this(material, Object.class);
   }

   public BlockTC(Material material, SoundType st) {
      this(material, Object.class);
      this.func_149672_a(st);
   }

   public BlockTC(Material material, Class types, SoundType st) {
      this(material, types);
      this.func_149672_a(st);
   }

   protected BlockStateContainer func_180661_e() {
      if (this.TYPE == null) {
         return tempVariants == null ? super.func_180661_e() : new BlockStateContainer(this, tempVariants);
      } else {
         return new BlockStateContainer(this, new IProperty[]{this.TYPE});
      }
   }

   protected static Material createProperties(Material material, Class types) {
      tempVariants = null;
      if (types != Object.class) {
         tempVariants = new IProperty[]{PropertyEnum.func_177709_a("type", types)};
      }

      return material;
   }

   public void setInitDefaultState() {
      if (this.TYPE != null) {
         this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(this.TYPE, this.variantValues[0]));
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item item, CreativeTabs tab, List list) {
      if (this.hasTypes()) {
         BlockStateContainer bsc = this.func_176194_O();
         IBlockState state = bsc.func_177621_b();
         IProperty[] var6 = this.getTypes();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            IProperty prop = var6[var8];

            for(Iterator var10 = prop.func_177700_c().iterator(); var10.hasNext(); state = state.func_177231_a(prop)) {
               Object value = var10.next();
               list.add(new ItemStack(item, 1, this.func_180651_a(state)));
            }
         }
      } else {
         list.add(new ItemStack(item, 1, 0));
      }

   }

   public int func_180651_a(IBlockState state) {
      return this.func_176201_c(state);
   }

   public IBlockState func_176203_a(int meta) {
      if (this.TYPE == null) {
         return super.func_176203_a(meta);
      } else {
         return meta < this.variantValues.length ? this.func_176223_P().func_177226_a(this.TYPE, this.variantValues[meta]) : this.func_176223_P();
      }
   }

   public int func_176201_c(IBlockState state) {
      if (this.TYPE == null) {
         return super.func_176201_c(state);
      } else {
         int meta = ((Enum)state.func_177229_b(this.TYPE)).ordinal();
         return meta;
      }
   }

   public IProperty[] getTypes() {
      return this.TYPE == null ? null : new IProperty[]{this.TYPE};
   }

   public boolean hasTypes() {
      return this.getTypes() != null;
   }

   public String getTypeName(IBlockState state) {
      if (this.TYPE == null) {
         String unlocalizedName = state.func_177230_c().func_149739_a();
         return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
      } else {
         IStringSerializable type = (IStringSerializable)state.func_177229_b(this.TYPE);
         return type.func_176610_l();
      }
   }

   public boolean isType(IBlockState state, IStringSerializable type) {
      if (this.TYPE == null) {
         return false;
      } else {
         return state.func_177229_b(this.TYPE) == type;
      }
   }
}
