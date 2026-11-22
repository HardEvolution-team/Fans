package com.electrifiedded.fans;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Fans.ID)
public class Registry {
   public static final Item PROPELLER = new Item().setTranslationKey("propeller").setCreativeTab(CreativeTabs.MATERIALS)
         .setRegistryName(Fans.ID, "propeller");
   public static final BlockFan[] FANS = {new BlockFan(1), new BlockFan(2), new BlockFan(3), new BlockFan(4)};

   @SubscribeEvent
   public static void blocks(RegistryEvent.Register<Block> e) {
      for (Block b : FANS) e.getRegistry().register(b);
      GameRegistry.registerTileEntity(TileEntityFan.class, new ResourceLocation(Fans.ID, "fan"));
   }

   @SubscribeEvent
   public static void items(RegistryEvent.Register<Item> e) {
      e.getRegistry().register(PROPELLER);
      for (Block b : FANS) e.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
   }

   @SubscribeEvent
   public static void recipes(RegistryEvent.Register<IRecipe> e) {
      GameRegistry.addShapedRecipe(new ResourceLocation(Fans.ID, "propeller"), null, new ItemStack(PROPELLER, 4),
            " I ", "ICI", " I ", 'I', Items.IRON_INGOT, 'C', Blocks.COBBLESTONE);
      Object[] mats = {Items.IRON_INGOT, Items.GOLD_INGOT, new ItemStack(Items.DYE, 1, 4), Items.DIAMOND};
      int[] counts = {4, 6, 2, 6};
      for (int i = 0; i < 4; i++) {
         GameRegistry.addShapedRecipe(new ResourceLocation(Fans.ID, "fan" + (i + 1)), null, new ItemStack(FANS[i], counts[i]),
               "BWB", "WPW", "BWB", 'B', mats[i], 'W', Blocks.PLANKS, 'P', PROPELLER);
      }
   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void models(ModelRegistryEvent e) {
      ModelLoader.setCustomModelResourceLocation(PROPELLER, 0, new ModelResourceLocation(PROPELLER.getRegistryName(), "inventory"));
      for (Block b : FANS) {
         ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
      }
   }
}
