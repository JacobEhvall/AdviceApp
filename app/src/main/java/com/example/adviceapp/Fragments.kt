package com.example.adviceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.adviceapp.fragments.AddFragment
import com.example.adviceapp.fragments.HomeFragment
import com.example.adviceapp.fragments.PersonInfoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class Fragments : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var addFragment: AddFragment
    lateinit var personInfoFragment: PersonInfoFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.wrap_view,homeFragment )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {



                R.id.home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.wrap_view,homeFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.add -> {
                    addFragment = AddFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.wrap_view,addFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.person -> {
                    personInfoFragment = PersonInfoFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.wrap_view,personInfoFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit()
                }

            }

            true
        }

    }

}
