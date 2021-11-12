package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemArtistBinding
import com.example.moviezam.databinding.ItemFilmBinding
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.SongCard

class FilmCardAdapter(private val films: List<FilmCard>) : RecyclerView.Adapter<FilmCardAdapter.FilmCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmCardViewHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmCardViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    class FilmCardViewHolder(
        private val binding: ItemFilmBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: FilmCard) {
            binding.title.text = film.name
            binding.desc.text = film.releaseDate
            binding.image.setImageURI(film.image)
        }
    }

}