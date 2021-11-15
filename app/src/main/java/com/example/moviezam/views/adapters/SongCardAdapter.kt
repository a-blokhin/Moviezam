package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard

class SongCardAdapter(private var songs: List<SongCard> = emptyList()) : RecyclerView.Adapter<SongCardAdapter.SongCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongCardViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount() = songs.size

    class SongCardViewHolder(
        private val binding: ItemSongBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongCard) {
            binding.songName.text = song.name
            binding.avatarImage.setImageURI(song.image)
            binding.artistName.text = song.artist
            if (song.image == "")
                binding.avatarImage
                    .setImageURI("https://i.pinimg.com/originals/dc/5d/ab/dc5dabf254765cc40a460496aeba681a.jpg")
            else
                binding.avatarImage.setImageURI(song.image)
        }
    }
}