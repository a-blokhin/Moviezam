package com.example.moviezam.views.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.ActivityMainBinding
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.views.adapters.SongCardAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = findViewById<RecyclerView>(com.example.moviezam.R.id.userList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        var user : List<SongCard> = emptyList()

        runBlocking {
            withContext(Dispatchers.IO) {
                user = listOf(
                    SongRepository().getSongById(227934),
                    SongRepository().getSongById(227935),
                    SongRepository().getSongById(227936),
                    SongRepository().getSongById(227937),
                    SongRepository().getSongById(227938),
                    SongRepository().getSongById(227939),
                    SongRepository().getSongById(227940),
                    SongRepository().getSongById(227941),
                    SongRepository().getSongById(227942),
                    SongRepository().getSongById(227943))
            }
        }

        val userAdapter = SongCardAdapter(user)

        binding.userList.adapter = userAdapter;

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    userAdapter.filter.filter(newText)
                }
                return false
            }


        })

    }
}