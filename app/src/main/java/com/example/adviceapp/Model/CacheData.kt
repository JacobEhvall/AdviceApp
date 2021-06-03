package com.example.adviceapp.model


import android.util.LruCache
import com.example.adviceapp.Controller.PostData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.firestore.FirebaseFirestore


class CacheData {

    lateinit var db : FirebaseFirestore
    private var jsonStr = ""
    private val cacheKeys = mutableListOf<String>()

    private var cacheList = mutableListOf<PostData>()
    private var cachedAdvicesJson = mutableListOf<String>()
    //private var cacheAdviceList = mutableListOf<PostData>()
    private var cachedAdviceList = mutableListOf<PostData>()
    private var cacheKey = 1
    private val cache = LruCache<String, String>(8)


 fun cacheData()  {

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
                    println("!!! CACHEVÄRDE = " + jsonStr)
                    var hej = cache.get(cacheKey.toString())
                    println("!!! HÄMTA CACHEVÄRDE = " + hej)
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

        var thecachevalue = cache.get("0")

        println("!!! FUNKTIONEN HAR STARTAT" + thecachevalue)

        var position = 0
            for (i in cachedAdvicesJson) {
                var cacheAdvice = mapper.readValue<PostData>(cachedAdvicesJson[position])
                println("!!! DATA" + cachedAdvicesJson)
                GlobalAdviceList.globalAdviceList.add(cacheAdvice)
                position++
        }
        return GlobalAdviceList.globalAdviceList
    }


}



