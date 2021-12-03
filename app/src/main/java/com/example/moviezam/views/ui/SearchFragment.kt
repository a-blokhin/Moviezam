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
import com.example.moviezam.databinding.FragmentSearchBinding
import com.example.moviezam.viewmodels.SearchViewModel
import com.example.moviezam.views.adapters.*
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

    private var mListener: OnListFragmentInteractionListener? = null

    private var searchJob: Job? = null
    private var currentSearchQuery: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpBasic()
        initSearch()
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

        binding.list.adapter = songSearchAdapter

        val lm = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.list.layoutManager = lm

        for (adapter in listOf(songSearchAdapter, artistSearchAdapter, filmSearchAdapter)) {
            (adapter as PagingDataAdapter<*, *>).addLoadStateListener { loadState ->
                binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
                binding.progressBarRepoSearch.isVisible = loadState.source.refresh is LoadState.Loading

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
                updateList()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                updateList()
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
            currentSearchQuery = it.toString()
            search(it.toString())
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            when(binding.buttonView.checkedRadioButtonId) {
                binding.songButton.id -> {
                    searchViewModel.searchSongs(query).collectLatest {
                        binding.list.adapter = songSearchAdapter
                        songSearchAdapter?.submitData(it)
                    }
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