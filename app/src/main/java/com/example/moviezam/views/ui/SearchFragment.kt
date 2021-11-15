package com.example.moviezam.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

class SearchFragment: Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val songsViewModel = SongsViewModel()
    private val artistsViewModel = ArtistViewModel()

    private var songList: MutableList<SongCard> = setDefaultSongs()
    private var artistList: MutableList<ArtistCard> = mutableListOf()

    private var songCardAdapter: SongCardAdapter? = null
    private var artistCardAdapter: ArtistCardAdapter? = null

    private var currJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Fresco.initialize(this.context)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpBasic()

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
                binding.filmButton.id -> TODO()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(query)
                        binding.artistButton.id -> uploadArtistList(query)
                        binding.filmButton.id -> TODO()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val minSizeToSearch = 2
                if (newText != null && newText.length > minSizeToSearch) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(newText)
                        binding.artistButton.id -> uploadArtistList(newText)
                        binding.filmButton.id -> TODO()
                    }
                }
                return false
            }
        })

        return binding.root
    }

    fun uploadSongList(text: String) {
        currJob?.cancel()
        songList.clear()

        currJob = CoroutineScope(Dispatchers.IO).launch {
            songsViewModel.loadSongsByPrefix(text, songCardAdapter!!)
            songList.addAll(songsViewModel.songList)
        }
    }

    fun uploadArtistList(text: String) {
        currJob?.cancel()
        artistList.clear()

        currJob = CoroutineScope(Dispatchers.IO).launch {
            artistsViewModel.loadArtistsByPrefix(text, artistCardAdapter!!)
            artistList.addAll(artistsViewModel.artistList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBasic()  {
        songCardAdapter = SongCardAdapter(songList)
        artistCardAdapter = ArtistCardAdapter(artistList)

        binding.list.adapter = songCardAdapter
    }

    private fun setDefaultSongs() : MutableList<SongCard> {
        val defaultSongs = mutableListOf<SongCard>()
        runBlocking {
            withContext(Dispatchers.IO) {
                val repo = SongRepository()
                val popularSongsIds = listOf(13153, 176895, 104405, 105478, 28627, 160111, 77456)

                for (id in popularSongsIds) {
                    defaultSongs.add(repo.getSongById(id))
                }
            }
        }
        return defaultSongs
    }
}