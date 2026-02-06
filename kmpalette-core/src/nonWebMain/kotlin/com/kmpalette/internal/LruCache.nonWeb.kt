package com.kmpalette.internal

internal actual open class LruCache<K : Any, V : Any> actual constructor(
    maxSize: Int,
) {
    private val delegate = androidx.collection.LruCache<K, V>(maxSize)

    actual operator fun get(key: K): V? = delegate[key]

    actual fun put(
        key: K,
        value: V,
    ): V? = delegate.put(key, value)

    actual fun evictAll() {
        delegate.evictAll()
    }
}
