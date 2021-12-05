package com.example.moviezam.views.adapters

import android.app.PendingIntent.getActivity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.R
import com.example.moviezam.databinding.ItemFavouriteBinding
import com.example.moviezam.models.FavouriteEntity
import com.example.moviezam.models.Type
import com.example.moviezam.models.getType
import com.example.moviezam.views.ui.*


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
                    "film"   -> binding.avatarImage.setImageURI("https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/c4/3c/66/c43c66c5-df9c-7021-7df0-04c1678c0ed3/794043915260.jpg/88x88bb.jpeg")
                    "song"   -> binding.avatarImage.setImageURI("https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/c4/3c/66/c43c66c5-df9c-7021-7df0-04c1678c0ed3/794043915260.jpg/88x88bb.jpeg")
                    "artist" -> binding.avatarImage.setImageURI("https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/c4/3c/66/c43c66c5-df9c-7021-7df0-04c1678c0ed3/794043915260.jpg/88x88bb.jpeg")
                    else     -> binding.avatarImage.setImageURI("https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/c4/3c/66/c43c66c5-df9c-7021-7df0-04c1678c0ed3/794043915260.jpg/88x88bb.jpeg")
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