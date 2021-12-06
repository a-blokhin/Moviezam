package com.example.moviezam.views.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.R
import com.example.moviezam.databinding.FragmentFilmBinding
import com.example.moviezam.models.*
import com.example.moviezam.viewmodels.FilmViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.*


class FilmFragment : BaseFragment() {
    private var _binding: FragmentFilmBinding? = null
    private val viewModel = FilmViewModel()
    private var filmSaved: Film? = null
    private lateinit var mListener: OnListFragmentInteractionListener

    private var songsAdapter: SongCardAdapter? = null
    private var artistsAdapter: ArtistCardAdapter? = null
    private var filmsAdapter: FilmCardAdapter? = null

    private val binding get() = _binding!!
    private var isFav = false


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {
        viewModel.loadFilm(Store.id).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        //binding.progressBar.visibility = View.GONE
                        resource.data?.let { film ->
                            setUpBasic(film)
                            filmSaved = film}
                    }
                    Resource.Status.ERROR -> {
                        //binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        //binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        filmSaved?.let {
            setUpBasic(it)
            return
        }
        if (Store.id > 0) {
            setupObservers()
        } else {
            Toast.makeText(activity, "Film does not exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)

        songsAdapter = SongCardAdapter(mListener, listOf<SongCard>())
        artistsAdapter = ArtistCardAdapter(mListener, listOf<ArtistCard>())
        filmsAdapter = FilmCardAdapter(mListener, listOf<FilmCard>())

        binding.list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.list.adapter = songsAdapter

        binding.buttonView.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.songButton.id -> {
                    binding.list.adapter = songsAdapter
                }
                binding.artistButton.id -> {
                    binding.list.adapter = artistsAdapter
                }
                binding.filmButton.id -> {
                    binding.list.adapter = filmsAdapter
                }
            }
        }

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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpBasic(film: Film) {
        binding.filmImg.setImageURI(film.image)
        binding.filmTitle.text = film.name
        binding.filmTitle.isSelected = true
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        binding.filmDesc.text =
            LocalDate.parse(film?.releaseDate?.substringBefore(' ')).format(formatter)
        binding.filmDesc.isSelected = true
        songsAdapter!!.setData(film.songs)
        artistsAdapter!!.setData(film.artists)


        if (film.similar != null) {
            filmsAdapter!!.setData(film.similar)
        } else {
            binding.filmButton.visibility = View.GONE
        }
        CoroutineScope(Dispatchers.IO).launch {
            val favourite =
                context?.let {
                    AppDatabase.getInstance(context)?.favDao?.getByType(
                        film.id.toLong(),
                        getType(Type.FILM)
                    )
                }
            if (favourite != null) {
                binding.like.setImageResource(R.drawable.love_black)
                isFav = true
            }
        }

        binding.like.setOnClickListener {
            val fav = FavouriteEntity(film.id.toLong(), film.name, film.image, getType(Type.FILM))
            CoroutineScope(Dispatchers.IO).launch {
                if (isFav) {
                    AppDatabase.getInstance(context)?.favDao?.delete(
                        film.id.toLong(),
                        getType(Type.FILM)
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