package com.example.moviezam.views.ui


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moviezam.R
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)


        val shazamFragment: Fragment = ShazamFragment()
        val bundle = Bundle()
        bundle.putString("output", externalCacheDir?.absolutePath + "/soundrecorder/recording.wav")
        bundle.putString("dir", externalCacheDir?.absolutePath + "/soundrecorder/")
        shazamFragment.arguments = bundle
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, shazamFragment, "SHAZAM")
                .commitAllowingStateLoss()
        }
    }



}

