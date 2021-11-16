package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.Artist
import com.example.moviezam.models.SongCard
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment.OnListFragmentInteractionListener


class SongCardAdapter(private var mListener: OnListFragmentInteractionListener, private var songs: List<SongCard>) : RecyclerView.Adapter<SongCardAdapter.SongCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongCardViewHolder(binding, mListener)
    }

    fun setData(songs: List<SongCard>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SongCardViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount() = songs.size

    class SongCardViewHolder(
        private val binding: ItemSongBinding,
        private var mListener: OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongCard) {
            binding.songName.text = song.name
            binding.artistName.text = song.artist

            if (song.image == "")
                binding.avatarImage
                    .setImageURI("https://i.pinimg.com/originals/dc/5d/ab/dc5dabf254765cc40a460496aeba681a.jpg")
            else
                binding.avatarImage.setImageURI(song.image)

            binding.itemSong.setOnClickListener{
                mListener.onListFragmentInteraction(7130, ArtistFragment())
            }
        }
    }
}