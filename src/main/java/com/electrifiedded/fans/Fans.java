package com.electrifiedded.fans;

import com.electrifiedded.archaiclib.original.OriginalModAssets;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Fans.ID, name = "Fans", version = "1.0.0", dependencies = "required-after:archaiclib")
public class Fans {
   public static final String ID = "fans";
   public static final String ORIGINAL_NAME = "fans_mod_1.7.10.jar - Fans 1.4 for 1.7.10";
   public static final String ORIGINAL_URL = "https://www.curseforge.com/minecraft/mc-mods/fans";

   public Fans() {
      // lang is shipped by the port; textures come from the player's own original jar
      OriginalModAssets.hide(ID, "lang/en_us.lang");
   }

   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e) {
      OriginalModAssets.require(ID, "fans", ORIGINAL_NAME, ORIGINAL_URL, ID);
   }
}
