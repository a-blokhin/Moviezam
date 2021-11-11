package com.example.moviezam.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ItemSongBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

class SongCardAdapter(private var songs: List<SongCard> = emptyList()) : RecyclerView.Adapter<SongCardAdapter.SongCardViewHolder>(), Filterable {
    private var songCardFilterList = emptyList<SongCard>()

    init {
        songCardFilterList = songs
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongCardViewHolder, position: Int) {
        holder.bind(songCardFilterList[position])
    }

    override fun getItemCount() = songCardFilterList.size

    class SongCardViewHolder(
        private val binding: ItemSongBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongCard) {
            binding.songName.text = song.name
            binding.avatarImage.setImageURI(song.image)
            binding.artistName.text = song.artist
            if (song.image == "")
                binding.avatarImage
                    .setImageURI("https://i.pinimg.com/originals/dc/5d/ab/dc5dabf254765cc40a460496aeba681a.jpg")
            else
                binding.avatarImage.setImageURI(song.image)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                songCardFilterList = if (charSearch.isEmpty()) {
                    songs
                } else {
                    try {
                        val resultList = emptyList<SongCard>().toMutableList()
                        runBlocking {
                            withContext(Dispatchers.IO) {
                                //TODO: fix problem with loading all pages
                                var pageNum = 1
                                val maxPageNum = 2
                                var pageResults = SongRepository().getSongsByName(charSearch, pageNum)
                                while (pageResults.isNotEmpty() && pageNum <= maxPageNum) {
                                    resultList += pageResults
                                    pageResults = SongRepository().getSongsByName(charSearch, pageNum)
                                    pageNum++
                                }
                            }
                            resultList
                        }
                    }
                    catch (e : Exception) {
                        emptyList<SongCard>().toMutableList()
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = songCardFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                songCardFilterList = results?.values as List<SongCard>? ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

}