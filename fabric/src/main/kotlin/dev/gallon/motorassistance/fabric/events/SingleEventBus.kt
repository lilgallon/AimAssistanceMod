package dev.gallon.motorassistance.fabric.events

import kotlin.reflect.KClass

// It's probably not production-ready, but it works for the example
object SingleEventBus {

    val callbacks = mutableMapOf<KClass<out Event>, List<(event: Event) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Event> register(noinline callback: (event: T) -> Unit) {
        if (T::class in callbacks) {
            callbacks[T::class] = callbacks[T::class]!!.plus(callback as (event: Event) -> Unit)
        } else {
            callbacks[T::class] = listOf(callback as (event: Event) -> Unit)
        }
    }

    fun <T : Event> send(event: T) {
        callbacks[event::class]?.forEach { it.invoke(event) }
    }
}
