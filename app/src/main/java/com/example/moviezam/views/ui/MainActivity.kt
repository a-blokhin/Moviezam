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
    fun generateJson(convertedObject: JsonObject): JsonObject? {
        val songJson = JsonParser().parse("{}").asJsonObject
        songJson.addProperty("id", -1)
        songJson.addProperty(
            "name", convertedObject.getAsJsonObject("track")["title"].asString
        )
        songJson.addProperty(
            "name_stub", ""
        )
        songJson.addProperty(
            "artist", convertedObject.getAsJsonObject("track")["subtitle"].asString
        )
        songJson.addProperty("album_name", "")
        songJson.addProperty(
            "external_art_url", convertedObject.getAsJsonObject("track")
                .getAsJsonObject("images")["background"].asString
        )
        songJson.addProperty("amazon", "")
        songJson.addProperty("apple_music", "")
        songJson.addProperty("itunes", "")
        songJson.addProperty("spotify", "")
        songJson.addProperty("youtube", "")
        songJson.addProperty("films", "[]")// надо сделать List<FilmCard> или не надо
        return songJson
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)


        val shazamFragment: Fragment = ShazamFragment()
        val bundle = Bundle()
        bundle.putString("output", externalCacheDir?.absolutePath + "/soundrecorder/recording.wav")
        bundle.putString("dir", externalCacheDir?.absolutePath + "/soundrecorder/")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, shazamFragment, "SHAZAM")
                .commitAllowingStateLoss()
        }
    }



}

