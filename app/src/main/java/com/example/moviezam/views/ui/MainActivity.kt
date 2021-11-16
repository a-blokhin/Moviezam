package com.example.moviezam.views.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviezam.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

