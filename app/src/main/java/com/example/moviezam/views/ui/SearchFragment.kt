package com.example.moviezam.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.FragmentSearchBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.viewmodels.SongsViewModel
import com.example.moviezam.views.adapters.SongCardAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.example.moviezam.R

class SearchFragment: Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val viewModel = SongsViewModel()
    private var songList: List<SongCard>? = null

    private var adapter: SongCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this.context)
        val bundle = this.arguments

        if (bundle != null) {
            lifecycleScope.launch {
                songList = withContext(Dispatchers.Default) {
                    bundle.getIntegerArrayList("ids")?.let { viewModel.loadSongs(it) }
                }
                setUpBasic()
            }
        } else {
            songList = setDefaultSongs()
            setUpBasic()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.songList.layoutManager = LinearLayoutManager(this.context)
        binding.songList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    adapter?.filter?.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val gson = Gson()
        val json = gson.toJson(songList)
        savedInstanceState.putString("songs", json)
    }

    private fun setUpBasic()  {
        adapter = SongCardAdapter(songList ?: emptyList())
        binding.songList.adapter = adapter
    }

    private fun setDefaultSongs() : List<SongCard> {
        var defaultSongs = emptyList<SongCard>()
        runBlocking {
            withContext(Dispatchers.IO) {
                defaultSongs = listOf(
                    SongRepository().getSongById(227934),
                    SongRepository().getSongById(227935),
                    SongRepository().getSongById(227936),
                    SongRepository().getSongById(227937),
                    SongRepository().getSongById(227938),
                    SongRepository().getSongById(227939),
                    SongRepository().getSongById(227940),
                    SongRepository().getSongById(227941),
                    SongRepository().getSongById(227942),
                    SongRepository().getSongById(227943))
            }
        }
        return defaultSongs
    }
}