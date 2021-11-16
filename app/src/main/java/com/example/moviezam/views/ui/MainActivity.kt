package com.example.moviezam.views.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviezam.R
import com.example.moviezam.models.Store
import com.facebook.drawee.backends.pipeline.Fresco


class MainActivity : AppCompatActivity(), BaseFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)

        val f: Fragment = FilmFragment()
        Store.id = 1111

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, f, "SONG")
                .commitAllowingStateLoss()
        }
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
