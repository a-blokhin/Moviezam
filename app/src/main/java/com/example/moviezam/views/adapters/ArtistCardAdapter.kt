package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemArtistBinding
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.SongCard

class ArtistCardAdapter(private val artists: List<ArtistCard>) : RecyclerView.Adapter<ArtistCardAdapter.ArtistCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistCardViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistCardViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun getItemCount() = artists.size

    class ArtistCardViewHolder(
        private val binding: ItemArtistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistCard) {
            binding.title.text = artist.name
            binding.image.setImageURI(artist.image)
        }
    }

}