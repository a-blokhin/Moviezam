package com.example.moviezam.views.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.R
import com.example.moviezam.databinding.FragmentSearchBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.viewmodels.SongsViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.RuntimeException

class SearchFragment: Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val songsViewModel = SongsViewModel()
    private val artistsViewModel = ArtistViewModel()

    private var songList: MutableList<SongCard> = setDefaultSongs()
    private var artistList: MutableList<ArtistCard> = mutableListOf()
    //TODO: заглушка, потом поменять на карточки фильмов
    private var filmList: MutableList<SongCard> = mutableListOf()

    private var songCardAdapter: SongCardAdapter? = null
    private var artistCardAdapter: ArtistCardAdapter? = null
    //TODO: заглушка, поменять на адаптер фильмов
    private var filmCardAdapter: SongCardAdapter?= null

    private var mListener: BaseFragment.OnListFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpBasic()
        return binding.root
    }

    private fun uploadSongList(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            songsViewModel.loadSongsByPrefix(text, songCardAdapter!!, songList)
        }
    }

    private fun uploadArtistList(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            artistsViewModel.loadArtistsByPrefix(text, artistCardAdapter!!, artistList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBasic()  {
        songCardAdapter = SongCardAdapter(mListener!!, songList)
        artistCardAdapter = ArtistCardAdapter(mListener!!, artistList)

        binding.list.adapter = songCardAdapter

        binding.list.layoutManager = LinearLayoutManager(this.context)
        binding.list.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.buttonView.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.songButton.id -> {
                    binding.list.adapter = songCardAdapter
                }
                binding.artistButton.id -> {
                    binding.list.adapter = artistCardAdapter
                }
                binding.filmButton.id -> {}
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(query)
                        binding.artistButton.id -> uploadArtistList(query)
                        binding.filmButton.id -> {}
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(newText)
                        binding.artistButton.id -> uploadArtistList(newText)
                        binding.filmButton.id -> {}
                    }
                }
                return false
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is BaseFragment.OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                "$context must implement OnListFragmentInteractionListener"
            )
        }
    }

    private fun setDefaultSongs() : MutableList<SongCard> {
        val defaultSongs = mutableListOf<SongCard>()
        runBlocking {
            withContext(Dispatchers.IO) {
                val repo = SongRepository()
                val popularSongsIds = listOf(13153, 176895, 104405, 105478, 28627, 160111, 77456)

                for (id in popularSongsIds) {
                    defaultSongs.add(repo.getSongCardById(id))
                }
            }
        }
        return defaultSongs
    }
}