package com.example.moviezam.views.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.FragmentSearchBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.LoadingInfo
import com.example.moviezam.models.SongCard
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.viewmodels.FilmViewModel
import com.example.moviezam.viewmodels.SongsViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import kotlinx.coroutines.*
import java.lang.RuntimeException

class SearchFragment: BaseFragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val songsViewModel = SongsViewModel()
    private val artistsViewModel = ArtistViewModel()
    private val filmViewModel = FilmViewModel()

    private var songList: MutableList<SongCard> = mutableListOf()
    private var songLoadingInfo = LoadingInfo()
    private var artistList: MutableList<ArtistCard> = mutableListOf()
    private var artistLoadingInfo = LoadingInfo()

    private var filmList: MutableList<FilmCard> = mutableListOf()
    private var filmLoadingInfo = LoadingInfo()

    private var songCardAdapter: SongCardAdapter? = null
    private var artistCardAdapter: ArtistCardAdapter? = null
    private var filmCardAdapter: FilmCardAdapter? = null

    private var songJob: Job? = null
    private var artistJob: Job? = null
    private var filmJob: Job? = null

    private var mListener: OnListFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpBasic()
        if (Store.id == -2) {
            Store.id = 0
            binding.searchView.setQuery(Store.shazam,false)
            Store.shazam = ""
        }
        return binding.root
    }

    private fun uploadSongList(text: String) {
        if (!songLoadingInfo.hasPagesToLoad) return

        if (text != songLoadingInfo.currSearchText) {
            songList.clear()
            songCardAdapter?.notifyDataSetChanged()
            songLoadingInfo = LoadingInfo(1, text, true)
        }
        songJob?.cancel()

        songJob = CoroutineScope(Dispatchers.IO).launch {
            val loadedList = songsViewModel.loadSongsByPrefix(text, songLoadingInfo.currPageNumber)

            if (loadedList.isEmpty()) {
                songLoadingInfo.hasPagesToLoad = false
            } else {
                songList.addAll(loadedList)
                val update = CoroutineScope(Dispatchers.Main).launch {
                    songCardAdapter?.notifyDataSetChanged()
                }
                update.join()
            }
            songLoadingInfo.currPageNumber++
        }
    }

    private fun uploadFilmList(text: String) {
        if (!filmLoadingInfo.hasPagesToLoad) return

        if (text != filmLoadingInfo.currSearchText) {
            filmList.clear()
            filmCardAdapter?.notifyDataSetChanged()
            filmLoadingInfo = LoadingInfo(1, text, true)
        }
        filmJob?.cancel()

        filmJob = CoroutineScope(Dispatchers.IO).launch {
            val loadedList = filmViewModel.loadFilmsByPrefix(text, filmLoadingInfo.currPageNumber)

            if (loadedList.isEmpty()) {
                filmLoadingInfo.hasPagesToLoad = false
            } else {
                filmList.addAll(loadedList)
                val update = CoroutineScope(Dispatchers.Main).launch {
                    filmCardAdapter?.notifyDataSetChanged()
                }
                update.join()
            }
            filmLoadingInfo.currPageNumber++
        }
    }

    private fun uploadArtistList(text: String) {
        if (!artistLoadingInfo.hasPagesToLoad) return

        if (text != artistLoadingInfo.currSearchText) {
            artistList.clear()
            artistCardAdapter?.notifyDataSetChanged()
            artistLoadingInfo = LoadingInfo(1, text, true)
        }
        artistJob?.cancel()

        artistJob = CoroutineScope(Dispatchers.IO).launch {
            val loadedList =
                artistsViewModel.loadArtistsByPrefix(text, artistLoadingInfo.currPageNumber)

            if (loadedList.isEmpty()) {
                artistLoadingInfo.hasPagesToLoad = false
            } else {
                artistList.addAll(loadedList)
                val update = CoroutineScope(Dispatchers.Main).launch {
                    artistCardAdapter?.notifyDataSetChanged()
                }
                update.join()
            }
            artistLoadingInfo.currPageNumber++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBasic()  {
        songCardAdapter = SongCardAdapter(mListener!!, songList)
        artistCardAdapter = ArtistCardAdapter(mListener!!, artistList)
        filmCardAdapter = FilmCardAdapter(mListener!!, filmList)

        binding.list.adapter = songCardAdapter

        val lm = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.list.layoutManager = lm
        binding.list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val totalItemCount = lm.itemCount
                    val lastVisibleItem = lm.findLastVisibleItemPosition()
                    val posDiffToLoad = 3

                    if (lastVisibleItem + posDiffToLoad < totalItemCount) return

                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(songLoadingInfo.currSearchText)
                        binding.artistButton.id -> uploadArtistList(artistLoadingInfo.currSearchText)
                        binding.filmButton.id -> uploadFilmList(filmLoadingInfo.currSearchText)
                    }
                }
            }
        })

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
                binding.filmButton.id -> {
                    binding.list.adapter = filmCardAdapter
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(query)
                        binding.artistButton.id -> uploadArtistList(query)
                        binding.filmButton.id -> uploadFilmList(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    when(binding.buttonView.checkedRadioButtonId) {
                        binding.songButton.id -> uploadSongList(newText)
                        binding.artistButton.id -> uploadArtistList(newText)
                        binding.filmButton.id -> uploadFilmList(newText)
                    }
                }
                return false
            }
        })
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
}