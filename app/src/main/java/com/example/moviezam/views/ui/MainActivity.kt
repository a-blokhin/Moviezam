package com.example.moviezam.views.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.moviezam.R
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)


        val shazamFragment: Fragment = ShazamFragment()
        val bundle = Bundle()
        bundle.putString("output", externalCacheDir?.absolutePath + "/soundrecorder/recording.wav")
        bundle.putString("dir", externalCacheDir?.absolutePath + "/soundrecorder/")
        //bundle.putString("output", "/storage/emulated/0/Android/media/trigwg.wav")
        shazamFragment.arguments = bundle


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, shazamFragment, "ARTIST")
                .commitAllowingStateLoss()
        }
    }



}