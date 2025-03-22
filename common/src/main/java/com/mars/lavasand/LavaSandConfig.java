package com.mars.lavasand;

import com.mars.deimos.config.DeimosConfig;
import com.google.common.collect.Lists;

import java.util.List;

public class LavaSandConfig extends DeimosConfig {
    @Entry public static List<String> blocks_to_glass = Lists.newArrayList(
        "minecraft:sand", "minecraft:red_sand"
    );
}
