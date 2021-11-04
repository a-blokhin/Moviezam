package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard

class SongCardAdapter(private val songs: List<SongCard>) : RecyclerView.Adapter<SongCardAdapter.SongCardViewHolder>() {

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
            binding.text.text = song.name
            binding.image.setImageURI(song.image)
        }
    }

}