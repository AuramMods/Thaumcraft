package thaumcraft.proxies;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.client.gui.GuiArcaneBore;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.client.gui.GuiFocalManipulator;
import thaumcraft.client.gui.GuiFocusPouch;
import thaumcraft.client.gui.GuiGolemBuilder;
import thaumcraft.client.gui.GuiHandMirror;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchTable;
import thaumcraft.client.gui.GuiSmelter;
import thaumcraft.client.gui.GuiSpa;
import thaumcraft.client.gui.GuiThaumatorium;
import thaumcraft.client.gui.GuiTurretAdvanced;
import thaumcraft.client.gui.GuiTurretBasic;
import thaumcraft.common.container.ContainerArcaneBore;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.container.ContainerFocusPouch;
import thaumcraft.common.container.ContainerGolemBuilder;
import thaumcraft.common.container.ContainerHandMirror;
import thaumcraft.common.container.ContainerResearchTable;
import thaumcraft.common.container.ContainerSmelter;
import thaumcraft.common.container.ContainerSpa;
import thaumcraft.common.container.ContainerThaumatorium;
import thaumcraft.common.container.ContainerTurretAdvanced;
import thaumcraft.common.container.ContainerTurretBasic;
import thaumcraft.common.entities.construct.EntityArcaneBore;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;
import thaumcraft.common.entities.construct.EntityTurretCrossbowAdvanced;
import thaumcraft.common.entities.construct.golem.ItemGolemBell;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;
import thaumcraft.common.tiles.crafting.TileResearchTable;
import thaumcraft.common.tiles.crafting.TileThaumatorium;
import thaumcraft.common.tiles.devices.TileSpa;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class ProxyGUI {
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (world instanceof WorldClient) {
         switch(ID) {
         case 3:
            return new GuiThaumatorium(player.field_71071_by, (TileThaumatorium)world.func_175625_s(new BlockPos(x, y, z)));
         case 4:
            return new GuiHandMirror(player.field_71071_by, world, x, y, z);
         case 5:
            return new GuiFocusPouch(player.field_71071_by, world, x, y, z);
         case 6:
            return new GuiSpa(player.field_71071_by, (TileSpa)world.func_175625_s(new BlockPos(x, y, z)));
         case 7:
            return new GuiFocalManipulator(player.field_71071_by, (TileFocalManipulator)world.func_175625_s(new BlockPos(x, y, z)));
         case 8:
         case 11:
         case 15:
         default:
            break;
         case 9:
            return new GuiSmelter(player.field_71071_by, (TileSmelter)world.func_175625_s(new BlockPos(x, y, z)));
         case 10:
            return new GuiResearchTable(player, (TileResearchTable)world.func_175625_s(new BlockPos(x, y, z)));
         case 12:
            return new GuiResearchBrowser();
         case 13:
            return new GuiArcaneWorkbench(player.field_71071_by, (TileArcaneWorkbench)world.func_175625_s(new BlockPos(x, y, z)));
         case 14:
            return new GuiArcaneBore(player.field_71071_by, world, (EntityArcaneBore)((WorldClient)world).func_73045_a(x));
         case 16:
            return new GuiTurretBasic(player.field_71071_by, world, (EntityTurretCrossbow)((WorldClient)world).func_73045_a(x));
         case 17:
            return new GuiTurretAdvanced(player.field_71071_by, world, (EntityTurretCrossbowAdvanced)((WorldClient)world).func_73045_a(x));
         case 18:
            ISealEntity se = ItemGolemBell.getSeal(player);
            if (se != null) {
               return se.getSeal().returnGui(world, player, new BlockPos(x, y, z), se.getSealPos().face, se);
            }
            break;
         case 19:
            return new GuiGolemBuilder(player.field_71071_by, (TileGolemBuilder)world.func_175625_s(new BlockPos(x, y, z)));
         }
      }

      return null;
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch(ID) {
      case 3:
         return new ContainerThaumatorium(player.field_71071_by, (TileThaumatorium)world.func_175625_s(new BlockPos(x, y, z)));
      case 4:
         return new ContainerHandMirror(player.field_71071_by, world, x, y, z);
      case 5:
         return new ContainerFocusPouch(player.field_71071_by, world, x, y, z);
      case 6:
         return new ContainerSpa(player.field_71071_by, (TileSpa)world.func_175625_s(new BlockPos(x, y, z)));
      case 7:
         return new ContainerFocalManipulator(player.field_71071_by, (TileFocalManipulator)world.func_175625_s(new BlockPos(x, y, z)));
      case 9:
         return new ContainerSmelter(player.field_71071_by, (TileSmelter)world.func_175625_s(new BlockPos(x, y, z)));
      case 10:
         return new ContainerResearchTable(player.field_71071_by, (TileResearchTable)world.func_175625_s(new BlockPos(x, y, z)));
      case 13:
         return new ContainerArcaneWorkbench(player.field_71071_by, (TileArcaneWorkbench)world.func_175625_s(new BlockPos(x, y, z)));
      case 14:
         return new ContainerArcaneBore(player.field_71071_by, world, (EntityArcaneBore)((WorldServer)world).func_73045_a(x));
      case 16:
         return new ContainerTurretBasic(player.field_71071_by, world, (EntityTurretCrossbow)((WorldServer)world).func_73045_a(x));
      case 17:
         return new ContainerTurretAdvanced(player.field_71071_by, world, (EntityTurretCrossbowAdvanced)((WorldServer)world).func_73045_a(x));
      case 18:
         ISealEntity se = ItemGolemBell.getSeal(player);
         if (se != null) {
            return se.getSeal().returnContainer(world, player, new BlockPos(x, y, z), se.getSealPos().face, se);
         }
      case 8:
      case 11:
      case 12:
      case 15:
      default:
         return null;
      case 19:
         return new ContainerGolemBuilder(player.field_71071_by, (TileGolemBuilder)world.func_175625_s(new BlockPos(x, y, z)));
      }
   }
}
