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
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment
import com.example.moviezam.views.ui.FilmFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ArtistCardAdapter(
    private var mListener: BaseFragment.OnListFragmentInteractionListener,
    private var artists: List<ArtistCard>
) : RecyclerView.Adapter<ArtistCardAdapter.ArtistCardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistCardViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistCardAdapter.ArtistCardViewHolder(binding, mListener)
    }

    fun setData(artists: List<ArtistCard>) {
        this.artists = artists
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ArtistCardViewHolder, position: Int) {
        holder.bind(artists!![position])
    }

    override fun getItemCount() = if (artists != null) artists!!.size else 0

    class ArtistCardViewHolder(
        private val binding: ItemArtistBinding,
        private var mListener: BaseFragment.OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(artist: ArtistCard) {
            binding.artistName.text = artist.name
            if (artist.image == "" || artist.image == null) {
                binding.avatarImage.setImageURI("https://contractdynamics.com/wp-content/uploads/music-bckg.jpg")
            } else {
                binding.avatarImage.setImageURI(artist.image)
            }
            binding.itemArtist.setOnClickListener {
                mListener.onListFragmentInteraction(artist.id, ArtistFragment())
            }
        }
    }

}