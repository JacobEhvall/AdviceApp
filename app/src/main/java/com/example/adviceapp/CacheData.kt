package com.example.adviceapp

import androidx.collection.LruCache
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.firestore.FirebaseFirestore
/*
class CacheData {

    lateinit var db : FirebaseFirestore
    private var jsonStr = ""
    private val cacheKeys = mutableListOf<String>()
    private var cacheAdviceList = mutableListOf<PostData>()
    private var cachedAdvicesJson = mutableListOf<String>()
    private val cache = LruCache<String>(8) // might not work cause we haven´t implemented the LruCache file right now :)


 fun cacheData() {

    db = FirebaseFirestore.getInstance()
    val mapper = jacksonObjectMapper()
    var cacheKey = 1
    val docRef = db.collection("advice")
    docRef.addSnapshotListener { snapshot, e ->
        if (snapshot != null) {
            for (document in snapshot.documents) {
                val advice = document.toObject(PostData::class.java)
                if (advice != null) {
                    jsonStr = mapper.writeValueAsString(advice)
                    cache.put(cacheKey.toString(), jsonStr)
                    cacheKeys.add(cacheKey.toString())
                    cachedAdvicesJson.add(jsonStr)
                }
                cacheKey++
            }
        }
    }
}
    fun getCachedData() : MutableList<PostData> {
        val mapper = jacksonObjectMapper()
        var position = 0
        for (i in cachedAdvicesJson) {
            var cacheAdvice = mapper.readValue<PostData>(cachedAdvicesJson[position])
            cacheAdviceList.add(cacheAdvice)
            position++
        }
        return cacheAdviceList
    }

}
 */

