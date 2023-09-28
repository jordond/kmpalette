package com.kmpalette.internal

 internal expect open class LruCache<K : Any, V : Any>(maxSize: Int) {

     operator fun get(key: K): V?
    fun put(key: K, value: V): V?
    fun evictAll()
}