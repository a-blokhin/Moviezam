package com.example.moviezam.views.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentArtistBinding
import com.example.moviezam.models.Artist
import com.example.moviezam.models.Resource
import com.example.moviezam.models.Store
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val viewModel = ArtistViewModel()
    private var bundle: Bundle? = null

    private var adapter: SongCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = this.arguments

    }

    private fun setupObservers() {
        viewModel.loadArtist(Store.artistId).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { artist -> setUpBasic(artist) }
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
        if (Store.artistId > 0) {
            setupObservers()
        } else {
            Toast.makeText(activity, "Artist does not exist =(", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        binding.songs.layoutManager = LinearLayoutManager(this.context)
        adapter = SongCardAdapter()
        binding.songs.adapter = adapter

        return binding.root
    }


    private fun setUpBasic(artist: Artist) {
        binding.image.setImageURI(artist.image)
        binding.artistName.text = artist.name
        adapter!!.setData(artist.songs)

        if (artist.urlOfficial != "") {
            binding.official.setOnClickListener {
                goToUrl(artist.urlOfficial)
            }
        } else {
            binding.official.visibility = View.GONE
        }

        if (artist.urlAppleMusic != "") {
            binding.appleMusic.setOnClickListener {
                goToUrl(artist.urlAppleMusic)
            }
        } else {
            binding.appleMusic.visibility = View.GONE
        }

        if (artist.urlSpotify != "") {
            binding.spotify.setOnClickListener {
                goToUrl(artist.urlSpotify)
            }
        } else {
            binding.spotify.visibility = View.GONE
        }

        if (artist.urlItunes != "") {
            binding.itunes.setOnClickListener {
                goToUrl(artist.urlItunes)
            }
        } else {
            binding.itunes.visibility = View.GONE
        }

        if (artist.urlAmazon != "") {
            binding.amazon.setOnClickListener {
                goToUrl(artist.urlAmazon)
            }
        } else {
            binding.amazon.visibility = View.GONE
        }

        if (artist.urlWikipedia != "") {
            binding.wikipedia.setOnClickListener {
                goToUrl(artist.urlWikipedia)
            }
        } else {
            binding.wikipedia.visibility = View.GONE
        }
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

