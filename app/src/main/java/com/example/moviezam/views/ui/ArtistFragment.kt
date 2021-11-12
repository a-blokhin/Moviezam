package com.example.moviezam.views.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentArtistBinding
import com.example.moviezam.models.Artist
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
import android.net.Uri


class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val viewModel = ArtistViewModel()
    private var artist: Artist? = null
    private var bundle: Bundle? = null

    private var adapter: SongCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = this.arguments

    }

    override fun onStart() {
        super.onStart()
        //binding.progressBar.visibility = View.VISIBLE
        if ((bundle != null) && (bundle!!.getInt("id") != 0)) {

            lifecycleScope.launch {
                artist = async { viewModel.loadArtist(bundle!!.getInt("id")) }.await()
                setUpBasic()
            }

        } else {
            val gson = Gson()
            val json = bundle?.get("artist").toString()
            artist = gson.fromJson(json, Artist::class.java)
            setUpBasic()
        }
        //binding.progressBar.visibility = View.GONE
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val gson = Gson()
        val json = gson.toJson(artist)
        savedInstanceState.putString("artist", json)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        binding.songs.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }



    fun setUpBasic() {
        binding.image.setImageURI(artist?.image)
        if (artist != null) {

            binding.artistName.text = artist?.name
            adapter = SongCardAdapter(artist!!.songs)
            binding.songs.adapter = adapter

            if (artist!!.urlOfficial != "") {
                binding.official.setOnClickListener {
                    goToUrl(artist!!.urlOfficial)
                }
            } else {
                binding.official.visibility = View.GONE
            }

            if (artist!!.urlAppleMusic != "") {
                binding.appleMusic.setOnClickListener {
                    goToUrl(artist!!.urlAppleMusic)
                }
            }else {
                binding.appleMusic.visibility = View.GONE
            }

            if (artist!!.urlSpotify != "") {
                binding.spotify.setOnClickListener {
                    goToUrl(artist!!.urlSpotify)
                }
            }else {
                binding.spotify.visibility = View.GONE
            }

            if (artist!!.urlItunes != "") {
                binding.itunes.setOnClickListener {
                    goToUrl(artist!!.urlItunes)
                }
            } else {
                binding.itunes.visibility = View.GONE
            }

            if (artist!!.urlAmazon != "") {
                binding.amazon.setOnClickListener {
                    goToUrl(artist!!.urlAmazon)
                }
            } else {
                binding.amazon.visibility = View.GONE
            }

            if (artist!!.urlWikipedia != "") {
                binding.wikipedia.setOnClickListener {
                    goToUrl(artist!!.urlWikipedia)
                }
            } else {
                binding.wikipedia.visibility = View.GONE
            }
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