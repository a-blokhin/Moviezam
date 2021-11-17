package com.example.moviezam.views.ui


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviezam.R
import com.example.moviezam.models.SongCard
import com.example.moviezam.models.Store
import com.google.gson.Gson
import java.nio.file.Paths.get

class MainActivity : AppCompatActivity(), BaseFragment.OnListFragmentInteractionListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shazamFragment: Fragment = ShazamFragment()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, shazamFragment, "SHAZAM")
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

