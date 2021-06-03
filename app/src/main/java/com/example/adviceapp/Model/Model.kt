package com.example.adviceapp.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adviceapp.Controller.PostData
import com.example.adviceapp.Controller.UserData
import com.example.adviceapp.model.GlobalAdviceList
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.firestore.FirebaseFirestore

class Model : ViewModel () {

    //lateinit var db : FirebaseFirestore
    private var jsonStr = ""
    private var cacheAdviceList = mutableListOf<PostData>()
    private val cache = Lrucache<String, String>(8)


    lateinit var db: FirebaseFirestore
    val mapper = jacksonObjectMapper()



    private val _userEmail = MutableLiveData<String>()

    fun userEmail(): LiveData<String> {
        return _userEmail
    }

    private val adviceList = MutableLiveData<PostData>()

    fun getAdviceList(): MutableLiveData<PostData> {
        return adviceList
    }


    fun getFirebaseData(uid: String) {

        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    val userEmail = document.toObject(UserData::class.java)
                    _userEmail.value = userEmail!!.email
                }
            }
        }

    }

    fun getAdviceListData() {

        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("advice")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                var cacheKey = 1
                for (document in snapshot.documents) {
                    val advice = document.toObject(PostData::class.java)
                    if (advice != null) {
                        adviceList.value = advice
                        GlobalAdviceList.globalAdviceList.add(advice)
                        val cacheAdvice = advice.title.toString()
                        cacheKey ++

                        mapper.writeValueAsString(cacheAdvice)
                        cache.put("1", cacheAdvice)
                        println("+++"+cacheAdvice)

                        jsonStr = mapper.writeValueAsString(cacheAdvice)
                        cache.put(cacheKey.toString(), jsonStr)
                        //println("!!! vi fick datan!" + advice.toString())
                    }
                    cacheKey++
                }


            }


        }


    }

}
