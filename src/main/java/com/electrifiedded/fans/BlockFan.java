package com.electrifiedded.fans;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;

/** meta 0..5 = facing (0 south,1 east,2 north,3 west,4 up,5 down, as in 1.7), bit 8 = powered. */
public class BlockFan extends BlockContainer {
   public static final PropertyInteger FACING = PropertyInteger.create("facing", 0, 5);
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public final int power;

   public BlockFan(int power) {
      super(Material.IRON);
      this.power = power;
      setSoundType(SoundType.METAL);
      setCreativeTab(CreativeTabs.REDSTONE);
      setHardness(2.0F);
      setResistance(10.0F);
      setTranslationKey("fan" + power);
      setRegistryName(Fans.ID, "fan" + power);
      setDefaultState(blockState.getBaseState().withProperty(FACING, 0).withProperty(POWERED, false));
   }

   public static EnumFacing dir(int f) {
      switch (f) {
         case 0: return EnumFacing.SOUTH;
         case 1: return EnumFacing.EAST;
         case 2: return EnumFacing.NORTH;
         case 3: return EnumFacing.WEST;
         case 4: return EnumFacing.UP;
         default: return EnumFacing.DOWN;
      }
   }

   public static int meta(EnumFacing d) {
      switch (d) {
         case SOUTH: return 0;
         case EAST: return 1;
         case NORTH: return 2;
         case WEST: return 3;
         case UP: return 4;
         default: return 5;
      }
   }

   /** Fan blows away from the face that was clicked (like an end rod / dispenser placement). */
   @Override
   public IBlockState getStateForPlacement(World w, BlockPos pos, EnumFacing side, float hx, float hy, float hz, int m, EntityLivingBase placer) {
      EnumFacing d = placer != null && placer.isSneaking() ? EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite() : side;
      return getDefaultState().withProperty(FACING, meta(d)).withProperty(POWERED, w.isBlockPowered(pos));
   }

   @Override public IBlockState withRotation(IBlockState s, Rotation r) { return s.withProperty(FACING, meta(r.rotate(dir(s.getValue(FACING))))); }
   @Override public IBlockState withMirror(IBlockState s, Mirror m) { return s.withProperty(FACING, meta(m.mirror(dir(s.getValue(FACING))))); }

   @Override protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING, POWERED); }
   @Override public IBlockState getStateFromMeta(int m) {
      return getDefaultState().withProperty(FACING, Math.min(m & 7, 5)).withProperty(POWERED, (m & 8) != 0);
   }
   @Override public int getMetaFromState(IBlockState s) { return s.getValue(FACING) | (s.getValue(POWERED) ? 8 : 0); }
   @Override public EnumBlockRenderType getRenderType(IBlockState s) { return EnumBlockRenderType.MODEL; }
   @Override public TileEntity createNewTileEntity(World w, int m) { return new TileEntityFan(); }

   @Override
   public boolean onBlockActivated(World w, BlockPos pos, IBlockState s, EntityPlayer p, EnumHand h, EnumFacing f, float x, float y, float z) {
      if (p.isSneaking()) return false;
      if (!w.isRemote) {
         int c = s.getValue(FACING);
         w.setBlockState(pos, s.withProperty(FACING, c == 5 ? 0 : c + 1), 2);
      }
      return true;
   }

   private void updatePower(World w, BlockPos pos, IBlockState s) {
      if (w.isRemote) return;
      boolean on = w.isBlockPowered(pos);
      if (on != s.getValue(POWERED)) w.setBlockState(pos, s.withProperty(POWERED, on), 2);
   }

   @Override public void onBlockAdded(World w, BlockPos pos, IBlockState s) { updatePower(w, pos, s); }
   @Override public void neighborChanged(IBlockState s, World w, BlockPos pos, Block b, BlockPos from) { updatePower(w, pos, s); }

   public double getPower() { return power * 0.0225; }
   public double getRange() { return power * 4.0; }
}
