package com.example.moviezam.views.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemArtistBinding
import com.example.moviezam.databinding.ItemFilmBinding
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.views.ui.BaseFragment
import com.example.moviezam.views.ui.FilmFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FilmCardAdapter(private var mListener: BaseFragment.OnListFragmentInteractionListener, private var films: List<FilmCard>) : RecyclerView.Adapter<FilmCardAdapter.FilmCardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmCardViewHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmCardAdapter.FilmCardViewHolder(binding, mListener)
    }

    fun setData(films: List<FilmCard>) {
        this.films = films
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FilmCardViewHolder, position: Int) {
        holder.bind(films!![position])
    }

    override fun getItemCount() = if (films != null) films!!.size else 0

    class FilmCardViewHolder(
        private val binding: ItemFilmBinding,
        private var mListener: BaseFragment.OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(film: FilmCard) {
            binding.filmName.text = film.name
            binding.filmName.isSelected = true
            if (film.image == "" || film.image == null) {
                binding.avatarImage.setImageURI("https://images.unsplash.com/photo-1535016120720-40c646be5580?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2070&q=80")
            } else {
                binding.avatarImage.setImageURI(film.image)
            }
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            binding.releaseDate.text = LocalDate.parse(film?.releaseDate?.substringBefore(' ')).format(formatter)
            binding.releaseDate.isSelected = true
            binding.itemFilm.setOnClickListener{
                mListener.onListFragmentInteraction(film.id, FilmFragment())
            }
        }
    }

}