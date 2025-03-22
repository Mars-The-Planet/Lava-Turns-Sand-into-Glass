package com.mars.lavasand;

import com.mars.deimos.config.DeimosConfig;
import com.mars.lavasand.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import static com.mars.lavasand.Constants.MOD_ID;

public class CommonClass {
    public static void init() {
        DeimosConfig.init(MOD_ID, LavaSandConfig.class);
    }
}
