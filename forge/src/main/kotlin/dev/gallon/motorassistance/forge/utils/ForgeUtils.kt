package dev.gallon.motorassistance.forge.utils

import net.minecraftforge.fml.ModList

fun <T> whenModLoaded(id: String, fn: () -> T?): T? =
    if (ModList.get().isLoaded(id)) {
        fn()
    } else {
        null
    }
