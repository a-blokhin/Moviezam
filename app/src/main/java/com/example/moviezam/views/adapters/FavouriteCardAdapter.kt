package com.example.moviezam.views.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemFavouriteBinding
import com.example.moviezam.models.FavouriteEntity
import com.example.moviezam.views.ui.ArtistFragment
import com.example.moviezam.views.ui.BaseFragment
import com.example.moviezam.views.ui.FilmFragment
import com.example.moviezam.views.ui.SongFragment


class FavouriteCardAdapter(
    private var mListener: BaseFragment.OnListFragmentInteractionListener,
    private var favourites: List<FavouriteEntity>
) : RecyclerView.Adapter<FavouriteCardAdapter.FavouriteCardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCardViewHolder {
        val binding = ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteCardViewHolder(binding, mListener)
    }

    fun setData(favourites: List<FavouriteEntity>) {
        this.favourites = favourites
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FavouriteCardViewHolder, position: Int) {
        holder.bind(favourites[position])
    }

    override fun getItemCount() = favourites.size

    class FavouriteCardViewHolder(
        private val binding: ItemFavouriteBinding,
        private var mListener: BaseFragment.OnListFragmentInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(favourite: FavouriteEntity) {
            binding.favouriteName.text = favourite.name
            binding.favouriteName.isSelected = true
            if (favourite.img == "" || favourite.img == null) {
                when (favourite.type) {
                    "film" -> binding.avatarImage.setImageURI("https://images.unsplash.com/photo-1535016120720-40c646be5580?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2070&q=80")
                    "song" -> binding.avatarImage.setImageURI("https://image.shutterstock.com/image-vector/music-icon-symbol-simple-design-260nw-1934430458.jpg")
                    "artist" -> binding.avatarImage.setImageURI("https://contractdynamics.com/wp-content/uploads/music-bckg.jpg")
                }
            } else {
                binding.avatarImage.setImageURI(favourite.img)
            }

            binding.itemArtist.setOnClickListener {
                when (favourite.type) {
                    "film" -> mListener.onListFragmentInteraction(favourite.id.toInt(), FilmFragment())
                    "song" -> mListener.onListFragmentInteraction(favourite.id.toInt(), SongFragment())
                    "artist" -> mListener.onListFragmentInteraction(favourite.id.toInt(), ArtistFragment())
                }
            }
        }
    }
}