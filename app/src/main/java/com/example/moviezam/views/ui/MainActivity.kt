package com.example.moviezam.views.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviezam.R
import com.example.moviezam.models.AppDatabase
import com.example.moviezam.models.Store
import com.facebook.drawee.backends.pipeline.Fresco


class MainActivity : AppCompatActivity(), BaseFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        AppDatabase.getInstance(this)

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

