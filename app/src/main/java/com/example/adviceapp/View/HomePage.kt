
package com.example.adviceapp.View

import com.example.adviceapp.Model.Lrucache
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adviceapp.Model.Model
import com.example.adviceapp.Controller.PostData
import com.example.adviceapp.Model.FirebaseData
import com.example.adviceapp.R
import com.example.adviceapp.model.GlobalAdviceList
import com.example.adviceapp.model.NetworkHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*



class HomePage() : AppCompatActivity(), java.util.Observer {

    private lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
   // private var adviceList = mutableListOf<PostData>()
    private var filteredAdviceList = mutableListOf<PostData>() // This one I use for filtering
    private var adviceListAll = mutableListOf<PostData>() // Contains ALL loaded advice in the list

    private var cachedDataList = mutableListOf<PostData>()
    private var whichList = mutableListOf<PostData>()
    private val cache = Lrucache<String,String>(8)


    private var uid = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        // Reference to the textview that display the useremail with livedata
        val userEmail = findViewById<TextView>(R.id.display_name)

        // viewmodel gets the data from the Model
        val viewModel = ViewModelProvider(this).get(Model::class.java)

        /* This is the adapter that binds the data together */
        var adapter = ItemsAdapter(this, filteredAdviceList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)



        /* Check what kind of data we will get and display the list if internet or cache */

        if (NetworkHandler.isOnline(this)) {

            println("!!! hämtar från nätet!")
            viewModel.getAdviceListData()
            //CacheData().cacheData()
            whichList = GlobalAdviceList.globalAdviceList // Internet data, data ska cache:as
            Toast.makeText(this, "FIREBASE DATA", Toast.LENGTH_SHORT).show()

        }
        else {

            println("!!! hämtar från cache!")


            println("---"+ GlobalAdviceList.globalAdviceList)

            Toast.makeText(this, "CACHE DATA", Toast.LENGTH_SHORT).show()
        }

        // Check the user and set the user
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()


        // Set the useremail to the textview from the Model class
        viewModel.getFirebaseData(uid)


        // Clear the list so nothing gets populated
        GlobalAdviceList.globalAdviceList.clear()

        // Get the list and from the viewModel and observe the changes 
        viewModel.getAdviceList().observe(this, Observer {


            // We set the list that we add data to and do a Bubblesort the list from a Bubblesort algoritm
            GlobalAdviceList.globalAdviceList = bubbleSortAdvices(GlobalAdviceList.globalAdviceList)

            // We clear the list results from both lists
            filteredAdviceList.clear() // Filtered data
            adviceListAll.clear() // Firebase data

            // We add the all data to an filtered list and the list to display all the content
            // the its possible to show every advice and filtering on them
            filteredAdviceList.addAll(GlobalAdviceList.globalAdviceList)
            adviceListAll.addAll(GlobalAdviceList.globalAdviceList)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter.notifyDataSetChanged()

        })

        // We observe the data that we get from viewmodel that gets the data from Model class
        viewModel.userEmail().observe(this, Observer {

            // We set the text from the textview reference
            userEmail.text = it.toString()

        })


        // This is where we observe the textfield value ones it gets changed
        FirebaseData().userEmail.observe(this, Observer {
            userEmail.text = it.toString()
            println("!!! USER ${it}")

        })

            // Här skriver vi ut listan innan den sorteras
            //println("!!! ________SORTERAR___________ ")
            for(t in GlobalAdviceList.globalAdviceList) { println("!!! "+t) }



            // Här skriver vi ut resultatet av den sorterade listan
            //println("!!!___________ ")
            for(t in filteredAdviceList) { println("!!! "+t) }

            adapter.notifyDataSetChanged()


        // The fab plus button brings us to the next page
        val fab = findViewById<View>(R.id.add)
        fab.setOnClickListener{
            val intent = Intent(this, PostItem::class.java)
            startActivity(intent)

        }

    }


    // The is where the filtering of the data gets started
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if ( menuItem!= null) {

            val searchResult = menuItem.actionView as SearchView
            searchResult.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                // Ones the user starts to enter a value the results will show
                override fun onQueryTextChange(newText: String?): Boolean {

                    println("!!! filtrerar på:"+newText)


                    if (newText!!.isNotEmpty()) {

                        filteredAdviceList.clear()
                        val searchfield = newText.toLowerCase(Locale.getDefault())
                        GlobalAdviceList.globalAdviceList.forEach {

                            println("the value:"+it)

                            if (it.title?.toLowerCase(Locale.getDefault())!!.contains(searchfield)) {
                                filteredAdviceList.add(it)
                            }
                        }
                        GlobalAdviceList.globalAdviceList.clear()
                        GlobalAdviceList.globalAdviceList.addAll(filteredAdviceList)

                        recyclerview.adapter!!.notifyDataSetChanged()

                    } else {

                        filteredAdviceList.clear()

                        GlobalAdviceList.globalAdviceList.clear()
                        GlobalAdviceList.globalAdviceList.addAll(adviceListAll)
                        recyclerview.adapter?.notifyDataSetChanged()

                    }

                    return true

                }
            })

        }

        return super.onCreateOptionsMenu(menu)

    }

    //  When the user signout button gets pressed the signout function will be started
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out) {
            logOut()
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT ).show()

        }
        return super.onOptionsItemSelected(item)
    }

    // Log out the current logged in user
    private fun logOut() {
        auth.signOut()
        val intent = Intent(this, LogInScreen::class.java )
        startActivity(intent)
    }


    /* The Bubblesort algoritm that sorts the posted data to be displayed in alphabetical order.
     It goes through every index in the posted word that gets posted to Firebase. */
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

        return showAdviceList

    }

    override fun update(arg0: Observable?, arg: Any?) {
    }

}







