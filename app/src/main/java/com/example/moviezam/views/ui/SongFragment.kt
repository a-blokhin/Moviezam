package com.example.moviezam.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentFilmBinding
import com.example.moviezam.databinding.FragmentSongBinding
import com.example.moviezam.models.Film
import com.example.moviezam.models.Song
import com.example.moviezam.viewmodels.SongViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SongFragment : Fragment() {
    private var _binding: FragmentSongBinding? = null
    private val viewModel = SongViewModel()
    private var song: Song? = null

    private var filmAdapter: FilmCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments


        if (bundle != null) {
            lifecycleScope.launch {

//                val gson = Gson()
//                val json = bundle?.get("song").toString()
//                song = gson.fromJson(json, Song::class.java)

                song = async {viewModel.loadSong(bundle.getInt("id"))}.await()
                setUpBasic()
            }

        } else {
            val gson = Gson()
            val json = bundle?.get("song").toString()
            song = gson.fromJson(json, Song::class.java)
            setUpBasic()
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val gson = Gson()
        val json = gson.toJson(song)
        savedInstanceState.putString("song", json)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        binding.films.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun setUpBasic()  {
        binding.songImg.setImageURI(song?.externalArtUrl)
        binding.songTitle.text = song?.name
        binding.songDesc.text = song?.artist.plus(" - ").plus(song?.albumName)

        if (song!!.films != null) {
            filmAdapter = FilmCardAdapter(song!!.films)
            binding.films.adapter = filmAdapter
        } else {
            binding.films.visibility = View.GONE
            binding.filmsSection.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}