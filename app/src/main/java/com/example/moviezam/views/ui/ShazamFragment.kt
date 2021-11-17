package com.example.moviezam.views.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.R
import com.example.moviezam.databinding.ActivityMainBinding
import com.example.moviezam.databinding.FragmentShazamBinding
//import com.example.moviezam.models.SongCard
import com.example.moviezam.models.Store
import com.example.moviezam.repository.SongRepository
//import com.example.moviezam.repository.SongRepository
import com.example.moviezam.viewmodels.ShazamViewModel
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.lang.RuntimeException

class ShazamFragment : BaseFragment(){
    private var _binding: FragmentShazamBinding? = null
    private val viewModel = ShazamViewModel()
    private var song_name: String? = null
    var state = true
    private var songCardAdapter: SongCardAdapter? = null
    private lateinit var mListener: OnListFragmentInteractionListener
    private val binding get() = _binding!!
    private val repo = SongRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShazamBinding.inflate(inflater, container, false)
        return binding.root
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

    fun search() {
        val recordAudioPermissionStatus = (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.RECORD_AUDIO))
        val writePermissionStatus = (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
        if (recordAudioPermissionStatus != PackageManager.PERMISSION_GRANTED &&
            writePermissionStatus != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
        } else {

            if (state) {
                state = false
                val output = requireActivity().externalCacheDir?.absolutePath + "/soundrecorder/recording.wav"
                val dir = requireActivity().externalCacheDir?.absolutePath + "/soundrecorder/"
                lifecycleScope.launch {
                    Toast.makeText(getActivity(), "Запись 4.5 секунды пошла", Toast.LENGTH_LONG)
                        .show()
                    song_name = withContext(Dispatchers.Default) {
                        return@withContext viewModel.findSong(output.toString(), dir.toString())
                    }
                    val convertedObject = JsonParser().parse(song_name).asJsonObject
                    if (convertedObject.get("matches").toString().length > 2) {
                        Toast.makeText(
                            getActivity(),
                            convertedObject.getAsJsonObject("track")["title"].asString,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.d(
                            "MediaRecorderder",
                            convertedObject.getAsJsonObject("track")["title"].asString
                        )

                        val req = convertedObject.getAsJsonObject("track")["title"].asString
                        //val req = convertedObject.getAsJsonObject("track").getAsJsonObject("urlparams")["{tracktitle}"].asString.lowercase()

                        Log.d(
                            "MediaRecorderder", req
                        )

                        var songsPerPage = repo.getSongsPageByName(req, 1)
                        if (songsPerPage.size > 0){
                            Store.id = songsPerPage[0].id
                            mListener.onListFragmentInteraction(songsPerPage[0].id, SongFragment())
                        } else{
                            Store.id = -1
                            var songJson = generateJson(convertedObject)
                            Store.shazam = songJson
                            Toast.makeText(getActivity(), "песни нету в беке", Toast.LENGTH_SHORT).show()
                            Log.d(
                            "MediaRecorderder", "песни нету в беке"
                            )
                            Log.d(
                                "MediaRecorderder", songJson.toString()
                            )
                            mListener.onListFragmentInteraction(-1, SongFragment())
                        }



                    } else {
                        Toast.makeText(getActivity(), "Песня не нашлась", Toast.LENGTH_LONG)
                            .show()
                    }
                    state = true
                }

            } else {
                Log.d("MediaRecorderder", "MediaRecorderder is working")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //раскоментировать это и нажимать на значок лупы второй сверху строки поиска

        binding.searchView.setOnClickListener {
            mListener.onListFragmentInteraction(0, SearchFragment())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.fab.setOnClickListener {
                search()
            }
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                "$context must implement OnListFragmentInteractionListener"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}