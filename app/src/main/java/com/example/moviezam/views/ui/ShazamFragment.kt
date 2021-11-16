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


import com.example.moviezam.databinding.ActivityMainBinding
import com.example.moviezam.viewmodels.ShazamViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import com.google.gson.JsonObject

import com.google.gson.Gson
import com.google.gson.JsonParser
import android.R
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository

import com.example.moviezam.viewmodels.SongsViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import kotlinx.coroutines.*


class ShazamFragment : Fragment()  {
    private var _binding: ActivityMainBinding? = null
    private val viewModel = ShazamViewModel()
    private var song_name: String? = null
    var state = true
    private var currJob: Job? = null
    private var songList: MutableList<SongCard> = mutableListOf<SongCard>()
    private var artistList: MutableList<ArtistCard> = mutableListOf()
    private val binding get() = _binding!!
    private val songsViewModel = SongsViewModel()
    private var songCardAdapter = SongCardAdapter(songList)
    private var artistCardAdapter = ArtistCardAdapter(artistList)
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

    fun uploadSongList(text: String) {
        currJob?.cancel()
        songList?.clear()

        currJob = CoroutineScope(Dispatchers.IO).launch {
            songsViewModel.loadSongsByPrefix(text, songCardAdapter!!)
            songList.addAll(songsViewModel.songList)
        }
    }
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
                        Toast.makeText(getActivity(), "Запись 4.5 секунды пошла", Toast.LENGTH_LONG)
                            .show()
                        song_name = withContext(Dispatchers.Default) {
                            return@withContext viewModel.findSong(output.toString(), dir.toString())
                        }
                        song_name
                        val convertedObject = JsonParser().parse(song_name).asJsonObject
                        if (convertedObject.get("matches").toString().length > 2) {
                            Toast.makeText(
                                getActivity(),convertedObject.getAsJsonObject("track")["title"].asString, Toast.LENGTH_LONG
                            ).show()

                            val songJson = generateJson(convertedObject)

                            Log.d(
                                "MediaRecorderder", convertedObject.getAsJsonObject("track")["title"].asString
                            )

                            //val req = convertedObject.getAsJsonObject("track").getAsJsonObject("urlparams")["{tracktitle}"].asString
                            val req =convertedObject.getAsJsonObject("track").getAsJsonObject("urlparams")["{tracktitle}"].asString.lowercase()

                            Log.d(
                                "MediaRecorderder", req
                            )
                            uploadSongList(req)
                            Log.d("MediaRecorderder", songList.toString())
                            val size = songList?.size.toString()?: "не нашлось в беке"
                            Toast.makeText(getActivity(),
                                size, Toast.LENGTH_LONG
                            ).show()



                            /*val fragment2 = SongFragment()
                            val bundle = Bundle()
                            bundle.putString("output", )
                            bundle.putString("dir", )
                            fragment2.arguments = bundle
                            val fragmentManager: FragmentManager = requireActivity().fragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            fragmentTransaction.replace(com.example.moviezam.R.id.content_main, fragment2, "tag")
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()*/
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