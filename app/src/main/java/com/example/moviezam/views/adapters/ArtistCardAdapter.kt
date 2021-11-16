package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemArtistBinding
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment

class ArtistCardAdapter(private var mListener: BaseFragment.OnListFragmentInteractionListener, private val artists: List<ArtistCard>) : RecyclerView.Adapter<ArtistCardAdapter.ArtistCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistCardViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistCardViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ArtistCardViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun getItemCount() = artists.size

    class ArtistCardViewHolder(
        private val binding: ItemArtistBinding,
        private val mListener: BaseFragment.OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistCard) {
            binding.artistName.text = artist.name
            if (artist.image == "")
                binding.avatarImage
                    .setImageURI("https://contractdynamics.com/wp-content/uploads/music-bckg.jpg")
            else
                binding.avatarImage.setImageURI(artist.image)

            binding.itemArtist.setOnClickListener{
                mListener.onListFragmentInteraction(artist.id, ArtistFragment())
            }
        }
    }
}