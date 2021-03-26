
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
import kotlin.collections.ArrayList


class HomePage : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var adviceList = mutableListOf<PostData>()
    private var showAdviceList = mutableListOf<PostData>() // Empty list with no data
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
                val advice =  document.toObject(PostData::class.java)
                if (advice != null)
                    adviceList.add(advice)
                if (advice != null) {
                    showAdviceList.add(advice)
                }
            }
            adapter.notifyDataSetChanged()
        }
        //println("!!! GOT DATA ${adviceList}")


        /*

        fun bubbleSortAdvices(showAdviceList: ArrayList<String>): ArrayList<String> {

            var swap = true

            var letters = arrayOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","å","ä","ö")

            var changes = 0

            while (swap) {

                swap = false

                for (i in 0 until showAdviceList.size -1 ) {

                    changes ++

                    val wordToCheck = showAdviceList[i]
                    val nextWord = showAdviceList[i+1]
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

            return showAdviceList

        }

        bubbleSortAdvices(showAdviceList)
         */


        val fab = findViewById<View>(R.id.add)
        fab.setOnClickListener{
            //Toast.makeText(this, "Clicked on Add item", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PostItem::class.java)
            startActivity(intent)
        }

        val fab2 = findViewById<View>(R.id.favorite)
        fab2.setOnClickListener {

            //addedFavorites()
            //Toast.makeText(this, "Clicked on Favorites ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Favorites::class.java)
            startActivity(intent)

             /* Video till hur man gör en cool expandable floating action button
            https://www.youtube.com/watch?v=umCX1-Tq25k&ab_channel=Stevdza-San
            https://www.youtube.com/watch?v=0AlquC1rScQ&ab_channel=AndroidWorldClub  */
        }

        /*add_favorite.setOnCheckedChangeListener { checkBox, isChecked ->

            if (isChecked) {

                Toast.makeText(this, "ADDED", Toast.LENGTH_LONG).show()

            } else {

                Toast.makeText(this, "DELETED", Toast.LENGTH_LONG).show()
            }

        }
         */

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
































