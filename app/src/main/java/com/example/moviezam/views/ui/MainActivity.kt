package com.example.moviezam.views.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviezam.R
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)


        val f: Fragment = SongFragment()
        val bundle = Bundle()
        bundle.putInt("id", 1111)
        f.arguments = bundle

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, f, "SONG")
                .commitAllowingStateLoss()
        }
    }



}