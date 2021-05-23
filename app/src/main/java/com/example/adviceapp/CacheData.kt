package com.example.adviceapp


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.firestore.FirebaseFirestore


class CacheData {

    lateinit var db : FirebaseFirestore
    private var jsonStr = ""
    private val cacheKeys = mutableListOf<String>()
    private var cacheAdviceList = mutableListOf<PostData>()

    private var cacheList = mutableListOf<PostData>()

    private var cachedAdvicesJson = mutableListOf<String>()
    private val cache = Lrucache<String>(8)


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
                    cacheList.add(advice)
                    val adviceTitle = advice.title.toString()
                    cacheKey++
                    mapper.writeValueAsString(adviceTitle)
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



        println("!!! FUNKTIONEN HAR STARTAT" + cache) // KOMMER HIT

        val theCachedData = cache.get("1")

        println("!!! cachad data: "+cachedAdvicesJson)


        var position = 0
     for (i in cachedAdvicesJson) {
            var cacheAdvice = mapper.readValue<PostData>(cachedAdvicesJson[position])
            cacheAdviceList.add(cacheAdvice)
            GlobalAdviceList.globalAdviceList = cacheAdviceList
            println("!!! cachad data: "+cacheAdviceList)
            position++
        }
        return cacheAdviceList
    }

}



