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

class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val viewModel = ArtistViewModel()
    private var artist: Artist? = null

    private var adapter: SongCardAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments


        if (bundle != null) {
            lifecycleScope.launch {
                artist = async {viewModel.loadArtist(bundle.getInt("id"))}.await()
                setUpBasic()
            }

        } else {
            val gson = Gson()
            val json = bundle?.get("artist").toString()
            artist = gson.fromJson(json, Artist::class.java)
            setUpBasic()
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val gson = Gson()
        val json = gson.toJson(artist)
        savedInstanceState.putString("artist", json)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        binding.songs.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun setUpBasic()  {
        binding.image.setImageURI(artist?.image)
        binding.artistName.text = artist?.name
        adapter = SongCardAdapter(artist!!.songs)
        binding.songs.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}