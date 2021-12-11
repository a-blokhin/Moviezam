package com.example.moviezam.views.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moviezam.R
import com.example.moviezam.models.*
import com.example.moviezam.repository.SearchRepository
import com.facebook.drawee.backends.pipeline.Fresco
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), BaseFragment.OnListFragmentInteractionListener {

    private var mListener: BaseFragment.OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)
        setContentView(R.layout.activity_main)

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.shazamFragment,
//            R.id.searchFragment,
//            R.id.favouriteFragment))
////        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        AppDatabase.getInstance(this)
        SearchHistoryDatabase.getInstance(this)

        val f = ShazamFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, f)
            .commit()

    }

    override fun onListFragmentInteraction(id: Int, f: BaseFragment) {
        Store.id = id
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, f)
            .addToBackStack(null)
            .commit()
    }
}

