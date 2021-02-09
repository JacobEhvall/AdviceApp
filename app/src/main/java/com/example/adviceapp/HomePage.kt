package com.example.adviceapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class HomePage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    private var adviceModelList = mutableListOf<PostData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* Connect and get data from Firebase  */
         db = FirebaseFirestore.getInstance()


        /* This is the adapter that binds the data together */
        val adapter = ItemsAdapter(this, adviceModelList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        /* Listen to the Firebase collection and display data inside recyclerview */

        /*
        adviceRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot.documents) {
                val advice =  document.toObject(PostData::class.java)
                if (advice != null)
                    adviceModelList.add(advice)
            }
            adapter.notifyDataSetChanged()
        }
        println("!!! GOT DATA ${adviceModelList}")
         */


        val fab = findViewById<View>(R.id.floatingActionButton)
        fab.setOnClickListener{ view ->
            val intent = Intent(this, PostItem::class.java)
            startActivity(intent)
        }


        db.collection("advice")
        .addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                adviceModelList.clear()
                for (document in snapshot.documents) {
                    val advicePost = document.toObject(PostData::class.java)
                    if (advicePost != null) {
                        adviceModelList.add(advicePost)
                    }
                    adapter.notifyDataSetChanged()
                }

                println("!!! ADDED DATA TO ADVICELIST  ${adviceModelList}")

            }

        }

    }
}


























