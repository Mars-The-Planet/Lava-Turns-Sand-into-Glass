package com.mars.lavasand;

import com.mars.deimos.config.DeimosConfig;

import static com.mars.lavasand.Constants.MOD_ID;

public class CommonClass {
    public static void init() {
        DeimosConfig.init(MOD_ID, LavaSandConfig.class);
    }
}
