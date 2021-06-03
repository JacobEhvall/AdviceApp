package com.example.adviceapp.Model

import android.graphics.Bitmap
import java.util.*

/**
 * An LRU cache, based on `LinkedHashMap`.
 *
 *
 *
 * This cache has a fixed maximum number of elements (`cacheSize`).
 * If the cache is full and another entry is added, the LRU (least recently
 * used) entry is dropped.
 *
 *
 *
 * This class is thread-safe. All methods of this class are synchronized.
 *
 *
 *
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br></br>
 * Multi-licensed: EPL / LGPL / GPL / AL / BSD.
 */
class Lrucache<K, V>(private val cacheSize: Int) {
    private val map: LinkedHashMap<K, V>

    //val lru : Lrucache<Any,Any> = Lrucache(1024)

    /**
     * Retrieves an entry from the cache.<br></br>
     * The retrieved entry becomes the MRU (most recently used) entry.
     *
     * @param key
     * the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this
     * key exists in the cache.
     */

    @Synchronized
    operator fun get(key: K): V? {
        return map[key]
    }

    /**
     * Adds an entry to this cache.
     * The new entry becomes the MRU (most recently used) entry.
     * If an entry with the specified key already exists in the cache, it is
     * replaced by the new entry.
     * If the cache is full, the LRU (least recently used) entry is removed from
     * the cache.
     *
     * @param key
     * the key with which the specified value is to be associated.
     * @param value
     * a value to be associated with the specified key.
     */
    @Synchronized
     fun put(key: K, value: V) {
        map[key] = value
    }

    /**
     * Clears the cache.
     */
    @Synchronized
    fun clear() {
        map.clear()
    }

    /**
     * Returns the number of used entries in the cache.
     *
     * @return the number of entries currently in the cache.
     */
    @Synchronized
    fun usedEntries(): Int {
        return map.size
    }

    /**
     * Returns a `Collection` that contains a copy of all cache
     * entries.
     *
     * @return a `Collection` with a copy of the cache content.
     */
    @get:Synchronized
    val all: Collection<Map.Entry<K, V>>
        get() = ArrayList<Map.Entry<K, V>>(map.entries)

    companion object {
        private const val hashTableLoadFactor = 0.75f
    }

    /**
     * Creates a new LRU cache.
     *
     * @param cacheSize
     * the maximum number of entries that will be kept in this cache.
     */
    init {
        val hashTableCapacity = Math.ceil((cacheSize / hashTableLoadFactor).toDouble()).toInt() + 1
        map = object : LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor, true) {
            // (an anonymous inner class)
            val serialVersionUID: Long = 1
            override fun removeEldestEntry(eldest: Map.Entry<K, V>): Boolean {
                return size > cacheSize
            }
        }
    }
} // end c
