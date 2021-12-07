package com.example.moviezam.views.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.App
import com.example.moviezam.databinding.ItemArtistBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ArtistSearchAdapter(
    private var mListener: BaseFragment.OnListFragmentInteractionListener
) : PagingDataAdapter<ArtistCard, ArtistSearchAdapter.ArtistSearchViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ArtistCard>() {
            override fun areItemsTheSame(oldItem: ArtistCard, newItem: ArtistCard): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ArtistCard, newItem: ArtistCard): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistSearchViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistSearchViewHolder(binding, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ArtistSearchViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ArtistSearchViewHolder(
        private val binding: ItemArtistBinding,
        private var mListener: BaseFragment.OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(artist: ArtistCard) {
            binding.artistName.text = artist.name
            binding.artistName.isSelected = true
            if (artist.image == "" || artist.image == null) {
                binding.avatarImage.setImageURI("https://contractdynamics.com/wp-content/uploads/music-bckg.jpg")
            } else {
                binding.avatarImage.setImageURI(artist.image)
            }
            binding.itemArtist.setOnClickListener {
                runBlocking {
                    launch {
                        artist.time = System.currentTimeMillis().toString()
                        App().searchRepo?.insertArtist(artist)
                    }
                }
                mListener.onListFragmentInteraction(artist.id, ArtistFragment())
            }
        }
    }
}