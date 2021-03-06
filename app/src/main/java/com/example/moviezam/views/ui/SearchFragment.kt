package com.example.moviezam.views.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.App
import com.example.moviezam.R
import com.example.moviezam.databinding.FragmentSearchBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.models.Store
import com.example.moviezam.viewmodels.SearchViewModel
import com.example.moviezam.views.adapters.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchFragment: BaseFragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()

    private var songSearchAdapter: SongSearchAdapter? = null
    private var artistSearchAdapter: ArtistSearchAdapter? = null
    private var filmSearchAdapter: FilmSearchAdapter? = null

    private var songHistoryAdapter: SongCardAdapter? = null
    private var artistHistoryAdapter: ArtistCardAdapter? = null
    private var filmHistoryAdapter: FilmCardAdapter? = null

    private var mListener: OnListFragmentInteractionListener? = null

    private var searchJob: Job? = null
    private var currentSearchQuery: String? = null


    private var searchRepo = App().searchRepo

    private var songsHistory: List<SongCard> = searchRepo!!.songsHistory
    private var artistsHistory: List<ArtistCard> = searchRepo!!.artistsHistory
    private var filmsHistory: List<FilmCard> = searchRepo!!.filmsHistory

    fun insert(songCard: SongCard) = lifecycleScope.launch {
        searchRepo?.insertSong(songCard)
    }

    fun insert(filmCard: FilmCard) = lifecycleScope.launch {
        searchRepo?.insertFilm(filmCard)
    }

    fun insert(artistCard: ArtistCard) = lifecycleScope.launch {
        searchRepo?.insertArtist(artistCard)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val navView: BottomNavigationView = (activity as MainActivity).findViewById(R.id.nav_view)
        navView.menu.getItem(1).isChecked = true

        setUpBasic()
        initSearch()

        if (Store.id == -2) {
            Store.id = 0
            binding.searchView.setQuery(Store.shazam,false)
            Store.shazam = ""
        }

        if (Store.id == -3) {
            Store.id = 0
            binding.searchView.setQuery(Store.savedQuery ?: "",false)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBasic()  {

        songSearchAdapter = SongSearchAdapter(mListener!!)
        artistSearchAdapter = ArtistSearchAdapter(mListener!!)
        filmSearchAdapter = FilmSearchAdapter(mListener!!)
        songHistoryAdapter = SongCardAdapter(mListener!!, listOf())
        filmHistoryAdapter = FilmCardAdapter(mListener!!, listOf())
        artistHistoryAdapter = ArtistCardAdapter(mListener!!, listOf())

        binding.list.adapter = songSearchAdapter

        val lm = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.list.layoutManager = lm

        for (adapter in listOf(songSearchAdapter, artistSearchAdapter, filmSearchAdapter)) {
            (adapter as PagingDataAdapter<*, *>).addLoadStateListener { loadState ->

                val songItemShimmer = binding.songItemShimmer
                songItemShimmer.setVisibility(View.GONE)
                val artistItemShimmer = binding.artistItemShimmer
                artistItemShimmer.setVisibility(View.GONE)
                val filmItemShimmer = binding.filmItemShimmer
                filmItemShimmer.setVisibility(View.GONE)

                when (adapter) {
                    is SongSearchAdapter -> {
                        songItemShimmer.setVisibility(View.VISIBLE)
                        songItemShimmer.startShimmer()
                    }
                    artistSearchAdapter -> {
                        artistItemShimmer.setVisibility(View.VISIBLE)
                        artistItemShimmer.startShimmer()
                    }
                    filmSearchAdapter -> {
                        filmItemShimmer.setVisibility(View.VISIBLE)
                        filmItemShimmer.startShimmer()
                    }
                }

                binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
//                binding.progressBarRepoSearch.isVisible = loadState.source.refresh is LoadState.Loading
                binding.progressBarRepoSearch.isVisible = false

                if (loadState.source.refresh !is LoadState.Loading) {
                    songItemShimmer.stopShimmer()
                    songItemShimmer.setVisibility(View.GONE)
                    artistItemShimmer.stopShimmer()
                    artistItemShimmer.setVisibility(View.GONE)
                    filmItemShimmer.stopShimmer()
                    filmItemShimmer.setVisibility(View.GONE)
                }

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showStatusMessage(it.error.localizedMessage ?: "Error")
                }
            }
        }

        binding.list.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.buttonView.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.songButton.id -> {
                    binding.list.adapter = songSearchAdapter
                    updateList()
                }
                binding.artistButton.id -> {
                    binding.list.adapter = artistSearchAdapter
                    updateList()
                }
                binding.filmButton.id -> {
                    binding.list.adapter = filmSearchAdapter
                    updateList()
                }
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

    private fun showStatusMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Store.savedQuery = query
                when (!query.isNullOrEmpty()) {
                    true -> updateList()
                    else -> updateHistory()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Store.savedQuery = p0
                when (!p0.isNullOrEmpty()) {
                    true -> updateList()
                    else -> updateHistory()
                }
                return false
            }
        })


        lifecycleScope.launch {
            songSearchAdapter?.loadStateFlow?.distinctUntilChangedBy { it.refresh }
                ?.filter { it.refresh is LoadState.NotLoading }
        }
    }

    private fun updateList() {
        binding.searchView.query.let {
            when(it.toString().isEmpty()) {
                true -> updateHistory()
                else -> {
                    currentSearchQuery = it.toString()
                    search(it.toString())
                }
            }
        }
    }

    private fun updateHistory() {
        when(binding.buttonView.checkedRadioButtonId) {
            binding.songButton.id -> {
                binding.list.adapter = songHistoryAdapter
                songsHistory = App().searchRepo!!.songsHistory.sortedByDescending { it.time }
                songHistoryAdapter?.setData(songsHistory)
                songHistoryAdapter?.notifyDataSetChanged()
            }
            binding.filmButton.id -> {
                binding.list.adapter = filmHistoryAdapter
                filmsHistory = App().searchRepo!!.filmsHistory.sortedByDescending { it.time }
                filmHistoryAdapter?.setData(filmsHistory)
                filmHistoryAdapter?.notifyDataSetChanged()
            }
            binding.artistButton.id -> {
                binding.list.adapter = artistHistoryAdapter
                artistsHistory = App().searchRepo!!.artistsHistory.sortedByDescending { it.time }
                artistHistoryAdapter?.setData(artistsHistory)
                artistHistoryAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            when(binding.buttonView.checkedRadioButtonId) {
                binding.songButton.id -> {

//                    var songItemShimmer = binding.songItemShimmer
//                    songItemShimmer.visibility = View.VISIBLE
//                    songItemShimmer.startShimmer();

                    searchViewModel.searchSongs(query).collectLatest {
                        binding.list.adapter = songSearchAdapter
                        songSearchAdapter?.submitData(it)
                    }

//                    withContext(Dispatchers.Main) {
//                        songItemShimmer.stopShimmer();
//                        songItemShimmer.setVisibility(View.GONE);
//                    }
                }
                binding.filmButton.id -> {
                    searchViewModel.searchFilms(query).collectLatest {
                        binding.list.adapter = filmSearchAdapter
                        filmSearchAdapter?.submitData(it)
                    }
                }
                binding.artistButton.id -> {
                    searchViewModel.searchArtists(query).collectLatest {
                        binding.list.adapter = artistSearchAdapter
                        artistSearchAdapter?.submitData(it)
                    }
                }
            }
        }
    }
}