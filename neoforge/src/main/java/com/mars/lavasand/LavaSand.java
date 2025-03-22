package com.mars.lavasand;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class LavaSand {
    public LavaSand(IEventBus eventBus) {
        CommonClass.init();
    }
}
