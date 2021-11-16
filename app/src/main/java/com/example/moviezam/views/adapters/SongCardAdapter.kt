package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment.OnListFragmentInteractionListener


class SongCardAdapter(private var mListener: OnListFragmentInteractionListener) : RecyclerView.Adapter<SongCardAdapter.SongCardViewHolder>() {
    private var songs: List<SongCard>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongCardViewHolder(binding, mListener)
    }

    fun setData(songs: List<SongCard>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SongCardViewHolder, position: Int) {
        holder.bind(songs!![position])
    }

    override fun getItemCount() = if (songs != null) songs!!.size else 0

    class SongCardViewHolder(
        private val binding: ItemSongBinding,
        private var mListener: OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongCard) {
            binding.text.text = song.name
            binding.image.setImageURI(song.image)
            binding.itemSong.setOnClickListener{
                mListener.onListFragmentInteraction(667, ArtistFragment())
            }
        }
    }

}