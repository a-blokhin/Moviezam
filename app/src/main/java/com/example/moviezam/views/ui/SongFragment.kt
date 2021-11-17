package com.example.moviezam.views.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentArtistBinding
import com.example.moviezam.databinding.FragmentFilmBinding
import com.example.moviezam.databinding.FragmentSongBinding
import com.example.moviezam.models.*
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.viewmodels.FilmViewModel
import com.example.moviezam.viewmodels.SongViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import java.lang.RuntimeException


class SongFragment : BaseFragment() {
    private var _binding: FragmentSongBinding? = null
    private val viewModel = SongViewModel()
    private var songSaved: Song? = null
    private var mListener: OnListFragmentInteractionListener? = null

    private var filmsAdapter: FilmCardAdapter? = null

    private val binding get() = _binding!!


    private fun setupObservers() {
        viewModel.loadSong(Store.id).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { song -> setUpBasic(song) }
                    }
                    Resource.Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
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

        binding.films.layoutManager = LinearLayoutManager(this.context)

        filmsAdapter = mListener?.let { FilmCardAdapter(it, listOf<FilmCard>() ) }

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
        songSaved = song
        binding.songImg.setImageURI(song.externalArtUrl)
        binding.songTitle.text = song.name
        binding.songTitle.isSelected = true
        binding.songDesc.text = if (song.albumName != "") song.artist.plus(" - ").plus(song.albumName) else song.artist
        binding.songDesc.isSelected = true
        filmsAdapter!!.setData(song.films)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}