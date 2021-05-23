package com.example.adviceapp

import android.util.LruCache


lateinit var memoryCache: LruCache<String, String>


 class Lrucache<T> (val maxSize: Int) {

    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8

    private val memoryCache: LruCache<String, String>? = null
    fun sizeOf(key: String?, value: String?) : Int {
        return value!!.toInt()

    }


    private val internalCache: MutableMap<String, T> = object : LinkedHashMap<String, T>(0, 0.75f, true) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, T>?): Boolean {
        return size > maxSize
    }
}

    fun put(key: String, value: T) {
    internalCache.put(key, value)
    }

    fun get(key: String ): T? {
        return internalCache.get(key)
    }

    fun delete(key: String ): Boolean {
        return internalCache.remove(key) != null
    }

    fun reset() {
        internalCache.clear()
    }

    fun size(): Long {
        return synchronized(this) {
            val snapshot = LinkedHashMap(internalCache)
            snapshot.size.toLong()
        }
    }
    fun dump() {
        print("!!!"+internalCache)
    }

}






