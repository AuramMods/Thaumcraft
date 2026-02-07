package thaumcraft.common.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public interface IBlockTypes {
   String getTypeName(IBlockState var1);

   boolean hasTypes();

   IProperty[] getTypes();

   boolean isType(IBlockState var1, IStringSerializable var2);
}
