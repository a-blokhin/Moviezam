package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment.OnListFragmentInteractionListener
import com.example.moviezam.views.ui.SongFragment


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
        holder.bind(songs!![position])
    }

    override fun getItemCount() = if (songs != null) songs!!.size else 0

    class SongCardViewHolder(
        private val binding: ItemSongBinding,
        private var mListener: OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongCard) {
            binding.songName.text = song.name
            binding.artistName.text = song.artist.plus(" - ").plus(song.album)
            if (song.image == "" || song.image == null) {
                binding.avatarImage.setImageURI("https://image.shutterstock.com/image-vector/music-icon-symbol-simple-design-260nw-1934430458.jpg")
            } else {
                binding.avatarImage.setImageURI(song.image)
            }
            binding.itemSong.setOnClickListener{
                mListener.onListFragmentInteraction(song.id, SongFragment())
            }
        }
    }

}