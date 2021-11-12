package com.example.moviezam.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentFilmBinding
import com.example.moviezam.models.Film
import com.example.moviezam.viewmodels.FilmViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val viewModel = FilmViewModel()
    private var film: Film? = null

    private var artistAdapter: ArtistCardAdapter? = null
    private var songAdapter: SongCardAdapter? = null
    private var filmAdapter: FilmCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments


        if (bundle != null) {
            lifecycleScope.launch {
                film = async {viewModel.loadFilm(bundle.getInt("id"))}.await()
                setUpBasic()
            }

        } else {
            val gson = Gson()
            val json = bundle?.get("film").toString()
            film = gson.fromJson(json, Film::class.java)
            setUpBasic()
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val gson = Gson()
        val json = gson.toJson(film)
        savedInstanceState.putString("film", json)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        binding.artists.layoutManager = LinearLayoutManager(this.context)
        binding.songs.layoutManager = LinearLayoutManager(this.context)
        binding.similar.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun setUpBasic()  {
        binding.filmImg.setImageURI(film?.image)
        binding.filmTitle.text = film?.name
        binding.filmDesc.text = film?.releaseDate
        artistAdapter = ArtistCardAdapter(film!!.artists)
        songAdapter = SongCardAdapter(film!!.songs)
        filmAdapter = FilmCardAdapter(film!!.similar)
        binding.artists.adapter = artistAdapter
        binding.songs.adapter = songAdapter
        binding.similar.adapter = filmAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}