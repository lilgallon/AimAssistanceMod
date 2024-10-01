package dev.gallon.motorassistance.fabric.utils

import net.fabricmc.loader.api.FabricLoader

fun <T> whenModLoaded(id: String, fn: () -> T?): T? =
    if (FabricLoader.getInstance().isModLoaded(id)) {
        fn()
    } else {
        null
    }
