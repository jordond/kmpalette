package com.kmpalette.internal

internal actual open class LruCache<K : Any, V : Any> actual constructor(maxSize: Int) {

    private val delegate = androidx.collection.LruCache<K, V>(maxSize)

    actual fun size(): Int {
        return delegate.size()
    }

    actual operator fun get(key: K): V? {
        return delegate[key]
    }

    actual fun put(key: K, value: V): V? {
        return delegate.put(key, value)
    }

    actual fun evictAll() {
        delegate.evictAll()
    }
}