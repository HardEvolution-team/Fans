package com.electrifiedded.fans;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityFan extends TileEntity implements ITickable {
   @Override
   public void update() {
      IBlockState s = world.getBlockState(pos);
      if (!(s.getBlock() instanceof BlockFan) || !s.getValue(BlockFan.POWERED)) return;
      BlockFan fan = (BlockFan) s.getBlock();
      EnumFacing d = BlockFan.dir(s.getValue(BlockFan.FACING));
      double r = fan.getRange(), p = fan.getPower();
      AxisAlignedBB box = new AxisAlignedBB(pos).expand(d.getDirectionVec().getX() * r, d.getDirectionVec().getY() * r, d.getDirectionVec().getZ() * r);
      for (Entity e : world.getEntitiesWithinAABB(Entity.class, box)) {
         if (e.isSneaking()) continue;
         e.motionX += d.getDirectionVec().getX() * p;
         e.motionY += d.getDirectionVec().getY() * p;
         e.motionZ += d.getDirectionVec().getZ() * p;
      }
   }
}
