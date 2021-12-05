package com.example.moviezam.views.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviezam.databinding.FragmentFavoritesBinding
import com.example.moviezam.models.Favourite
import com.example.moviezam.models.FavouriteEntity
import com.example.moviezam.viewmodels.FavouriteViewModel
import com.example.moviezam.views.adapters.FavouriteCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter


class FavouriteFragment : BaseFragment() {
    private lateinit var mListener: OnListFragmentInteractionListener
    private var _binding: FragmentFavoritesBinding? = null
    private val viewModel = FavouriteViewModel()

    private var favouriteSaved: Favourite? = null

    private var adapter: FavouriteCardAdapter? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.favourites.layoutManager = LinearLayoutManager(this.context)
        adapter = FavouriteCardAdapter(mListener, listOf())
        binding.favourites.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        favouriteSaved?.let {
            setUpBasic(it)
            return
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loadFavourite().observe(this, {
            it?.let { resource ->
                binding.progressBar.visibility = View.GONE
                resource.data?.let { favourite ->
                    setUpBasic(favourite)
                    favouriteSaved = favourite}
            }
        })
    }

    private fun setUpBasic(favourite: Favourite) {

        adapter!!.setData(favourite.favourites)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}