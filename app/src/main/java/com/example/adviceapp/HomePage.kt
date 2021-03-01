package com.example.adviceapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class HomePage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    private var adviceList = mutableListOf<PostData>()
    private var showAdviceList = mutableListOf<PostData>() // Empty list with no data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* Connect and get data from Firebase  */
         db = FirebaseFirestore.getInstance()
        // add everything from adviceList to our new empty list showAdviceList



        /* This is the adapter that binds the data together */
        val adapter = ItemsAdapter(this, showAdviceList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        /* Listen to the Firebase collection and display data inside recyclerview */
        db = FirebaseFirestore.getInstance()
        val adviceRef = db.collection("advice")
        adviceRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot.documents) {
                val advice =  document.toObject(PostData::class.java)
                if (advice != null)
                    adviceList.add(advice)
                     showAdviceList.addAll(adviceList)
                    //showAdviceList.addAll(adviceList)
            }
            adapter.notifyDataSetChanged()
        }
        println("!!! GOT DATA ${adviceList}")



        val fab = findViewById<View>(R.id.floatingActionButton)
        fab.setOnClickListener{
            val intent = Intent(this, PostItem::class.java)
            startActivity(intent)
        }


        /* Listen and update the list as things will be added */
        val docRef = db.collection("advice")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                adviceList.clear()
                for (document in snapshot.documents) {
                    val advicePost = document.toObject(PostData::class.java)
                    if (advicePost != null) {
                        adviceList.add(advicePost)
                    }
                    adapter.notifyDataSetChanged()
                }

                println("!!! ADDED DATA TO ADVICELIST  ${adviceList}")

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if ( menuItem!= null ) {

            val searchResult = menuItem.actionView as SearchView
            searchResult.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }


                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {

                        showAdviceList.clear()
                        val searchfield = newText.toLowerCase(Locale.getDefault())
                        adviceList.forEach {

                            if (it.title?.toLowerCase(Locale.getDefault())!!.contains(searchfield)) {
                                showAdviceList.add(it)
                            }
                        }

                        recyclerview.adapter!!.notifyDataSetChanged()

                    } else {

                        showAdviceList.clear()
                        showAdviceList.addAll(adviceList)
                        recyclerview.adapter?.notifyDataSetChanged()

                    }

                    return true

                }
             })

    }

    return super.onCreateOptionsMenu(menu)

}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}


























