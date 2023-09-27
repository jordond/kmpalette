package com.kmpalette.internal

import kotlinx.atomicfu.locks.synchronized

internal typealias LockObject = Any

/**
 * Modified from [this](https://github.com/qdsfdhvh/compose-imageloader/blob/7a12a122be95043fe2a9cbf2f560b60228e5cf79/image-loader/src/jsMain/kotlin/com/seiko/imageloader/util/LruCache.kt).
 */
internal actual open class LruCache<K : Any, V : Any> actual constructor(maxSize: Int) {

    private var _maxSize = 0
    private var _size = 0
    private var _putCount = 0
    private var _evictionCount = 0
    private var _hitCount = 0
    private var _missCount = 0

    private val map: LinkedHashMap<K, V>

    private val syncObject = LockObject()

    init {
        require(maxSize > 0) { "maxSize <= 0" }
        this._maxSize = maxSize
        this.map = LinkedHashMap(0, 0.75f)
    }

    actual operator fun get(key: K): V? {
        var mapValue: V?
        synchronized(syncObject) {
            mapValue = map[key]
            if (mapValue != null) {
                _hitCount++

            }
            _missCount++
            return mapValue
        }
    }

    actual fun put(key: K, value: V): V? {
        var previous: V? = null
        synchronized(syncObject) {
            _putCount++
            _size += 1
            previous = map.put(key, value)
            previous?.let {
                _size -= 1
            }
        }
        trimToSize(_maxSize)
        return previous
    }

    open fun trimToSize(maxSize: Int) {
        while (true) {
            lateinit var key: K
            synchronized(syncObject) {
                check(!(_size < 0 || map.isEmpty() && _size != 0)) {
                    this::class.simpleName + ".sizeOf() is reporting inconsistent results!"
                }
                if (_size <= maxSize || map.isEmpty()) return

                val (key1, _) = map.entries.iterator().next()
                key = key1
                map.remove(key)
                _size -= 1
                _evictionCount++
            }
        }
    }

    actual fun evictAll() {
        trimToSize(-1) // -1 will evict 0-sized elements
    }
}