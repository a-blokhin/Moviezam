package com.example.moviezam.views.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
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

    override fun onBackPressed() {
        super.onBackPressed()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val prevFragment = supportFragmentManager.fragments.lastOrNull()

        when (prevFragment) {
            is ShazamFragment -> {
                navView.menu.getItem(0).isChecked = true
            }
            is FavouriteFragment -> {
                navView.menu.getItem(2).isChecked = true
            }
            else -> {
                navView.menu.getItem(1).isChecked = true
            }
        }


//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        navView.menu.getItem(0).isChecked = true
//        navView.setSelectedItemId(R.id.shazamFragmentMenu);

//        super.onBackPressed()


    }
}

