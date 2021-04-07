
package com.example.adviceapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.android.synthetic.main.advice_card.*
import java.util.*


class HomePage : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var adviceList = mutableListOf<PostData>()
    private var showAdviceList = mutableListOf<PostData>() // Add all data to this empty list no data
   // private var favoriteList = mutableListOf<PostData>()

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
                val advice = document.toObject(PostData::class.java)
                if (advice != null)
                    adviceList.add(advice)
                if (advice != null) {
                    //advice.documentId = document.id  save to later if you got time
                    //println("!!!!" + document) //get documents printed from Firebase
                    showAdviceList.add(advice)

                }
            }



            // Här skriver vi ut listan innan den sorteras
            println("!!! ________SORTERAR___________ ")
            for(t in adviceList) { println("!!! "+t) }




            // HÄR GÖR VI SORTERINGEN
            showAdviceList = bubbleSortAdvices(showAdviceList)


            // Här skriver vi ut resultatet av den sorterade listan
            println("!!!___________ ")
            for(t in showAdviceList) { println("!!! "+t) }

           adapter.notifyDataSetChanged()

        }
        //println("!!! GOT DATA ${adviceList}")




        val fab = findViewById<View>(R.id.add)
        fab.setOnClickListener{
            val intent = Intent(this, PostItem::class.java)
            startActivity(intent)
        }


             /* Video till hur man gör en cool expandable floating action button
            https://www.youtube.com/watch?v=umCX1-Tq25k&ab_channel=Stevdza-San
            https://www.youtube.com/watch?v=0AlquC1rScQ&ab_channel=AndroidWorldClub  */



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

    private fun bubbleSortAdvices(showAdviceList: MutableList<PostData> ): MutableList<PostData> {

        var swap = true

        var letters = arrayOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","å","ä","ö")

        var changes = 0

        while (swap) {

            swap = false

            for (i in 0 until showAdviceList.size -1 ) {

                changes ++

                val wordToCheck = showAdviceList[i].title.toString()
                val nextWord = showAdviceList[i+1].title.toString()
                val first = wordToCheck.substring(0,1)
                val second = nextWord.substring(0,1)
                val firstWord = letters.indexOf(first.toLowerCase())
                val secondWord = letters.indexOf(second.toLowerCase())

                if (firstWord > secondWord ) {
                    val temp = showAdviceList[i]
                    showAdviceList[i] = showAdviceList[i + 1]
                    showAdviceList[i + 1 ] = temp
                    swap = true
                }

            }


        }

        print("!!! Sorterad");

        return showAdviceList

    }

}
































