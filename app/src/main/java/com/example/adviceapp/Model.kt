package com.example.adviceapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class Model : ViewModel () {

    //lateinit var db : FirebaseFirestore
    private var jsonStr = ""
    private val cacheKeys = mutableListOf<String>()
    private var cacheAdviceList = mutableListOf<PostData>()
    private var cachedAdvicesJson = mutableListOf<String>()
    private val cache = Lrucache<String>(8)


    lateinit var db: FirebaseFirestore


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
            //GlobalAdviceList.globalAdviceList.clear()
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val advice = document.toObject(PostData::class.java)
                    if (advice != null) {
                        adviceList.value = advice
                        GlobalAdviceList.globalAdviceList.add(advice)
                        println("!!! vi fick datan!" + advice.toString())
                    }



                }

            }


        }


    }

}
