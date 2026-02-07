package thaumcraft.common.lib.crafting;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.events.ServerEvents;
import thaumcraft.common.lib.events.ToolEvents;
import thaumcraft.common.lib.utils.BlockUtils;

public class DustTriggerMultiblock implements IDustTrigger {
   Part[][][] blueprint;
   String research;
   int ySize;
   int xSize;
   int zSize;

   public DustTriggerMultiblock(String research, Part[][][] blueprint) {
      this.blueprint = blueprint;
      this.research = research;
      this.ySize = this.blueprint.length;
      this.xSize = this.blueprint[0].length;
      this.zSize = this.blueprint[0][0].length;
   }

   public IDustTrigger.Placement getValidFace(World world, EntityPlayer player, BlockPos pos, EnumFacing face) {
      if (this.research != null && !ThaumcraftCapabilities.getKnowledge(player).isResearchKnown(this.research)) {
         return null;
      } else {
         for(int yy = -this.ySize; yy <= 0; ++yy) {
            for(int xx = -this.xSize; xx <= 0; ++xx) {
               for(int zz = -this.zSize; zz <= 0; ++zz) {
                  BlockPos p2 = pos.func_177982_a(xx, yy, zz);
                  EnumFacing f = this.fitMultiblock(world, p2);
                  if (f != null) {
                     return new IDustTrigger.Placement(xx, yy, zz, f);
                  }
               }
            }
         }

         return null;
      }
   }

   private EnumFacing fitMultiblock(World world, BlockPos pos) {
      EnumFacing[] var3 = EnumFacing.field_176754_o;
      int var4 = var3.length;

      label67:
      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing face = var3[var5];

         for(int y = 0; y < this.ySize; ++y) {
            Matrix matrix = new Matrix(this.blueprint[y]);
            matrix.Rotate90DegRight(3 - face.func_176736_b());

            for(int x = 0; x < matrix.rows; ++x) {
               for(int z = 0; z < matrix.cols; ++z) {
                  if (matrix.matrix[x][z] != null) {
                     IBlockState bsWo = world.func_180495_p(pos.func_177982_a(x, -y + (this.ySize - 1), z));
                     if (matrix.matrix[x][z].getSource() instanceof Block && bsWo.func_177230_c() != (Block)matrix.matrix[x][z].getSource() || matrix.matrix[x][z].getSource() instanceof Material && bsWo.func_185904_a() != (Material)matrix.matrix[x][z].getSource() || matrix.matrix[x][z].getSource() instanceof ItemStack && (bsWo.func_177230_c() != Block.func_149634_a(((ItemStack)matrix.matrix[x][z].getSource()).func_77973_b()) || bsWo.func_177230_c().func_176201_c(bsWo) != ((ItemStack)matrix.matrix[x][z].getSource()).func_77952_i()) || matrix.matrix[x][z].getSource() instanceof IBlockState && bsWo != matrix.matrix[x][z].getSource()) {
                        continue label67;
                     }
                  }
               }
            }
         }

         return face;
      }

      return null;
   }

   public List<BlockPos> sparkle(World world, EntityPlayer player, BlockPos pos, IDustTrigger.Placement placement) {
      BlockPos p2 = pos.func_177982_a(placement.xOffset, placement.yOffset, placement.zOffset);
      ArrayList<BlockPos> list = new ArrayList();

      for(int y = 0; y < this.ySize; ++y) {
         Matrix matrix = new Matrix(this.blueprint[y]);
         matrix.Rotate90DegRight(3 - placement.facing.func_176736_b());

         for(int x = 0; x < matrix.rows; ++x) {
            for(int z = 0; z < matrix.cols; ++z) {
               if (matrix.matrix[x][z] != null) {
                  BlockPos p3 = p2.func_177982_a(x, -y + (this.ySize - 1), z);
                  if (matrix.matrix[x][z].getSource() != null && BlockUtils.isBlockExposed(world, p3)) {
                     list.add(p3);
                  }
               }
            }
         }
      }

      return list;
   }

   public void execute(final World world, final EntityPlayer player, BlockPos pos, IDustTrigger.Placement placement, EnumFacing side) {
      if (!world.field_72995_K) {
         FMLCommonHandler.instance().firePlayerCraftingEvent(player, new ItemStack(BlocksTC.infernalFurnace), new InventoryFake(new ItemStack[]{null}));
         BlockPos p2 = pos.func_177982_a(placement.xOffset, placement.yOffset, placement.zOffset);

         for(int y = 0; y < this.ySize; ++y) {
            Matrix matrix = new Matrix(this.blueprint[y]);
            matrix.Rotate90DegRight(3 - placement.facing.func_176736_b());

            for(int x = 0; x < matrix.rows; ++x) {
               for(int z = 0; z < matrix.cols; ++z) {
                  if (matrix.matrix[x][z] != null && matrix.matrix[x][z].getTarget() != null) {
                     final ItemStack targetObject;
                     if (matrix.matrix[x][z].getTarget() instanceof Block) {
                        int meta = 0;
                        if ((Block)matrix.matrix[x][z].getTarget() instanceof IBlockFacingHorizontal) {
                           IBlockState state = ((Block)matrix.matrix[x][z].getTarget()).func_176223_P().func_177226_a(IBlockFacingHorizontal.FACING, matrix.matrix[x][z].getApplyPlayerFacing() ? side : (matrix.matrix[x][z].isOpp() ? placement.facing.func_176734_d() : placement.facing));
                           meta = ((Block)matrix.matrix[x][z].getTarget()).func_176201_c(state);
                        }

                        targetObject = new ItemStack((Block)matrix.matrix[x][z].getTarget(), 1, meta);
                     } else if (matrix.matrix[x][z].getTarget() instanceof ItemStack) {
                        targetObject = ((ItemStack)matrix.matrix[x][z].getTarget()).func_77946_l();
                     } else {
                        targetObject = null;
                     }

                     final BlockPos p3 = p2.func_177982_a(x, -y + (this.ySize - 1), z);
                     final Object sourceObject;
                     if (matrix.matrix[x][z].getSource() instanceof Block) {
                        sourceObject = world.func_180495_p(p3);
                     } else if (matrix.matrix[x][z].getSource() instanceof Material) {
                        sourceObject = (Material)matrix.matrix[x][z].getSource();
                     } else if (matrix.matrix[x][z].getSource() instanceof IBlockState) {
                        sourceObject = (IBlockState)matrix.matrix[x][z].getSource();
                     } else {
                        sourceObject = null;
                     }

                     ToolEvents.addBlockedBlock(world, p3);
                     ServerEvents.addRunnableServer(world, new Runnable() {
                        public void run() {
                           ServerEvents.addSwapper(world, p3, sourceObject, targetObject, false, 0, player, true, false, -9999, false, false, 0, ServerEvents.DEFAULT_PREDICATE, 0.0F);
                           ToolEvents.clearBlockedBlock(world, p3);
                        }
                     }, matrix.matrix[x][z].getPriority());
                  }
               }
            }
         }
      }

   }
}
