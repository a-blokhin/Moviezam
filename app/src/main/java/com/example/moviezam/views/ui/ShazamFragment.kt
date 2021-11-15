package com.example.moviezam.views.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.R

import com.example.moviezam.databinding.ActivityMainBinding
import com.example.moviezam.viewmodels.ShazamViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.google.gson.JsonObject

import com.google.gson.Gson
import com.google.gson.JsonParser







class ShazamFragment : Fragment()  {
    private var _binding: ActivityMainBinding? = null
    private val viewModel = ShazamViewModel()
    private var song_name: String? = null
    var state = true

    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this.context)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        LinearLayoutManager(this.context)
        _binding = ActivityMainBinding.inflate(inflater, container, false)


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        binding.searchView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                ActivityCompat.requestPermissions(this.context as Activity, permissions, 0)
            } else {

                if (state) {
                    state = false
                    val bundle = this.arguments
                    val output = bundle?.getString("output")
                    val dir = bundle?.getString("dir")
                    lifecycleScope.launch {
                        Toast.makeText(getActivity(), "Запись 3.5 секунды пошла", Toast.LENGTH_LONG)
                            .show()
                        song_name = withContext(Dispatchers.Default) {
                            return@withContext viewModel.findSong(output.toString(), dir.toString())
                        }
                        val parser = JsonParser()
                        val convertedObject = parser.parse(song_name).asJsonObject
                        if (convertedObject.get("matches").toString().length > 2) {
                            Toast.makeText(
                                getActivity(),convertedObject.getAsJsonObject("track")["title"].asString, Toast.LENGTH_LONG
                            ).show()

                            val songJson = parser.parse("{}").asJsonObject
                            songJson.addProperty("id", -1)
                            songJson.addProperty(
                                "name", convertedObject.getAsJsonObject("track")["title"].asString
                            )
                            songJson.addProperty(
                                "name_stub", convertedObject.getAsJsonObject("track")["title"].asString
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
                            songJson.addProperty("films", "")// надо сделать List<FilmCard>
                            Log.d(
                                "MediaRecorderder", convertedObject.getAsJsonObject("track")["title"].asString
                            )
                        } else {
                            Toast.makeText(getActivity(), "Песня не нашлась", Toast.LENGTH_LONG)
                                .show()
                        }

                        Log.d("MediaRecorderder", song_name.toString())
                        state = true
                    }

                } else {
                    Log.d("MediaRecorderder", "MediaRecorderder is working")
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}