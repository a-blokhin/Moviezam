package com.example.moviezam.views.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.R
import com.example.moviezam.databinding.FragmentSongBinding
import com.example.moviezam.models.*
import com.example.moviezam.viewmodels.SongViewModel
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.RuntimeException


class SongFragment : BaseFragment() {
    private var _binding: FragmentSongBinding? = null
    private val viewModel = SongViewModel()
    private var songSaved: Song? = null
    private lateinit var mListener: OnListFragmentInteractionListener

    private var filmsAdapter: FilmCardAdapter? = null

    private val binding get() = _binding!!
    private var isFav = false


    private fun setupObservers() {
        viewModel.loadSong(Store.id).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { song ->
                            setUpBasic(song)
                            songSaved = song}
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        songSaved?.let {
            setUpBasic(it)
            return
        }
        if (Store.id > 0) {
            setupObservers()
        } else if (Store.id == -1) {
            val gson = Gson()
            setUpBasic(gson.fromJson(Store.shazam.toString(), Song::class.java))
        } else {
            Toast.makeText(activity, "Song does not exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)

        var filmItemShimmer = binding.filmItemShimmer
        filmItemShimmer.startShimmer();

        binding.films.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        filmsAdapter = FilmCardAdapter(mListener, listOf<FilmCard>() )

        binding.films.adapter = filmsAdapter

        return binding.root
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

    private fun setUpBasic(song: Song) {

        var filmItemShimmer = binding.filmItemShimmer

        filmItemShimmer.stopShimmer();
        filmItemShimmer.setVisibility(View.GONE);

        binding.songImg.setImageURI(song.externalArtUrl)
        binding.songTitle.text = song.name
        binding.songTitle.isSelected = true
        binding.songDesc.text = if (song.albumName != "") song.artist.plus(" - ").plus(song.albumName) else song.artist
        binding.songDesc.isSelected = true
        filmsAdapter!!.setData(song.films)

        CoroutineScope(Dispatchers.IO).launch {
            val favourite =
                context?.let {
                    AppDatabase.getInstance(context)?.favDao?.getByType(
                        song.id.toLong(),
                        getType(Type.SONG)
                    )
                }
            if (favourite != null) {
                binding.like.setImageResource(R.drawable.love_black)
                isFav = true
            }
        }

        binding.like.setOnClickListener {
            val fav = FavouriteEntity(
                song.id.toLong(),
                song.name,
                song.externalArtUrl,
                getType(Type.SONG)
            )
            CoroutineScope(Dispatchers.IO).launch {
                if (isFav) {
                    AppDatabase.getInstance(context)?.favDao?.delete(
                        song.id.toLong(),
                        getType(Type.SONG)
                    )
                    binding.like.setImageResource(R.drawable.love)
                    isFav = false
                } else {
                    AppDatabase.getInstance(context)?.favDao?.insert(fav)
                    binding.like.setImageResource(R.drawable.love_black)
                    isFav = true
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}