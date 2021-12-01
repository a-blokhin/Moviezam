package com.example.moviezam.views.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentArtistBinding
import com.example.moviezam.models.Artist
import com.example.moviezam.models.Resource
import com.example.moviezam.models.Store
import com.example.moviezam.viewmodels.ArtistViewModel
import com.example.moviezam.views.adapters.SongCardAdapter
import java.lang.RuntimeException
import androidx.recyclerview.widget.RecyclerView






class ArtistFragment : BaseFragment() {
    private lateinit var mListener: OnListFragmentInteractionListener
    private var _binding: FragmentArtistBinding? = null
    private val viewModel = ArtistViewModel()

    private var artistSaved: Artist? = null

    private var adapter: SongCardAdapter? = null
    private val binding get() = _binding!!



    private fun setupObservers() {
        viewModel.loadArtist(Store.id).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        //binding.progressBar.visibility = View.GONE
                        resource.data?.let { artist ->
                            setUpBasic(artist)
                            artistSaved = artist }
                    }
                    Resource.Status.ERROR -> {
                        //binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                      //  binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        artistSaved?.let {
            setUpBasic(it)
            return
        }
        if (Store.id > 0) {
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
        binding.songs.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        adapter = SongCardAdapter(mListener, listOf())
        binding.songs.adapter = adapter

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                "$context must implement OnListFragmentInteractionListener"
            )
        }
    }

    private fun setUpBasic(artist: Artist) {

        binding.image.setImageURI(artist.image)
        binding.artistName.text = artist.name
        adapter!!.setData(artist.songs)
        binding.songs.addOnScrollListener(recyclerViewOnScrollListener)


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
        setArrows()
    }


    private fun setArrows(){
        binding.rightArrow.setOnClickListener {
            val next = (binding.songs.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
            if (next < adapter!!.itemCount){
                binding.songs.smoothScrollToPosition(next)
            }
        }

        binding.leftArrow.setOnClickListener {
            val prev = (binding.songs.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() - 1
            if (prev > 0){
                binding.songs.smoothScrollToPosition(prev)
            }
        }



    }

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastNum: Int = (binding.songs.layoutManager as LinearLayoutManager).itemCount-1
                val firstItem: Int =
                    (binding.songs.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastItem = (binding.songs.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if ((binding.rightArrow.visibility == View.GONE) && (lastItem != lastNum)){
                    binding.rightArrow.visibility = View.VISIBLE
                }
                if ((binding.leftArrow.visibility == View.GONE) && (firstItem != 0)){
                    binding.leftArrow.visibility = View.VISIBLE
                }

                if (lastItem == lastNum) {
                    binding.rightArrow.visibility = View.GONE
                } else if (firstItem == 0) {
                    binding.leftArrow.visibility = View.GONE
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