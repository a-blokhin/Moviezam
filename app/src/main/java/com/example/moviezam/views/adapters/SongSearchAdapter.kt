package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.App
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.views.ui.BaseFragment
import com.example.moviezam.views.ui.SongFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SongSearchAdapter(
    private var mListener: BaseFragment.OnListFragmentInteractionListener
) : PagingDataAdapter<SongCard, SongSearchAdapter.SongSearchViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SongCard>() {
            override fun areItemsTheSame(oldItem: SongCard, newItem: SongCard): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: SongCard, newItem: SongCard): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSearchViewHolder =
        SongSearchViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)

    override fun onBindViewHolder(holder: SongSearchViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class SongSearchViewHolder(
        private val binding: ItemSongBinding,
        private var mListener: BaseFragment.OnListFragmentInteractionListener
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongCard) {
            binding.songName.text = song.name
            binding.songName.isSelected = true
            binding.artistName.text = if (song.album != "") song.artist.plus(" - ").plus(song.album) else song.artist
            binding.artistName.isSelected = true
            if (song.image == "" || song.image == null) {
                binding.avatarImage.setImageURI("https://image.shutterstock.com/image-vector/music-icon-symbol-simple-design-260nw-1934430458.jpg")
            } else {
                binding.avatarImage.setImageURI(song.image)
            }
            binding.itemSong.setOnClickListener{
                runBlocking {
                    launch {
                        song.time = System.currentTimeMillis().toString()
                        App().searchRepo?.insertSong(song)
                    }
                }
                mListener.onListFragmentInteraction(song.id, SongFragment())
            }
        }
    }
}