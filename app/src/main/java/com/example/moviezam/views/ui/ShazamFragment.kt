package com.example.moviezam.views.ui

//import com.example.moviezam.models.SongCard
//import com.example.moviezam.repository.SongRepository
import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezam.databinding.FragmentShazamBinding
import com.example.moviezam.models.Store
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.viewmodels.ShazamViewModel
import com.example.moviezam.views.adapters.ArtistCardAdapter
import com.example.moviezam.views.adapters.FilmCardAdapter
import com.example.moviezam.views.adapters.SongCardAdapter
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.io.File
import java.nio.charset.Charset

class ShazamFragment : BaseFragment(){
    private var _binding: FragmentShazamBinding? = null
    private val viewModel = ShazamViewModel()
    private var song_name: String? = null
    var state = true
    private var songCardAdapter: SongCardAdapter? = null
    private lateinit var mListener: OnListFragmentInteractionListener
    private val binding get() = _binding!!
    private val repo = SongRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShazamBinding.inflate(inflater, container, false)
        setUpBasic()
        return binding.root
    }
    private fun setUpBasic()  {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Store.id = -2
                    Store.shazam = query
                    mListener.onListFragmentInteraction(-2, SearchFragment())
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    fun generateJson(convertedObject: JsonObject): JsonObject? {
        val songJson = JsonParser().parse("{}").asJsonObject
        songJson.addProperty("id", -1)
        songJson.addProperty(
            "name", convertedObject.getAsJsonObject("track")["title"].asString
        )
        songJson.addProperty(
            "name_stub", ""
        )
        songJson.addProperty(
            "artist", convertedObject.getAsJsonObject("track")["subtitle"].asString
        )
        var album_name = ""
        for (section in convertedObject.get("track").asJsonObject.get("sections").asJsonArray){
            if (section.asJsonObject.get("type").asString == "SONG"){
                for (data in section.asJsonObject.get("metadata").asJsonArray){
                    if (data.asJsonObject.get("title").asString == "Album"){
                        album_name = data.asJsonObject.get("text").asString
                    }
                }
            }
        }

        songJson.addProperty("album_name", album_name)
        songJson.addProperty(
            "external_art_url", convertedObject.getAsJsonObject("track")
                .getAsJsonObject("images")["background"].asString
        )
        songJson.addProperty("amazon", "")
        songJson.addProperty("apple_music", "")
        songJson.addProperty("itunes", "")
        songJson.addProperty("spotify", "")
        songJson.addProperty("youtube", "")
        songJson.addProperty("films", "")// надо сделать List<FilmCard> или не надо
        return songJson
    }
    fun drawprogresss(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val fileSize = 44
                binding.progressBar.max = fileSize
                var progressStatus = -1
                while (progressStatus < fileSize) {
                    progressStatus += 1
                    delay(100)
                    // This thread runs in the UI
                    binding.annotationRecord.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = progressStatus
                }
                binding.progressBar.progress = 0
                binding.annotationRecord.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.progressShaz.visibility = View.VISIBLE
                binding.annotationBase.visibility = View.VISIBLE
                progressStatus = 0
                binding.progressBar.isIndeterminate = false
            }
        }
    }
    fun search() {
        val recordAudioPermissionStatus = (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.RECORD_AUDIO))
        val writePermissionStatus = (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
        if (recordAudioPermissionStatus != PackageManager.PERMISSION_GRANTED &&
            writePermissionStatus != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
        } else {

            if (state) {
                drawprogresss()
                state = false
                val output = requireActivity().externalCacheDir?.absolutePath + "/soundrecorder/recording.wav"
                val dir = requireActivity().externalCacheDir?.absolutePath + "/soundrecorder/"
                lifecycleScope.launch {
                    //Toast.makeText(getActivity(), "Запись 4.5 секунды пошла", Toast.LENGTH_LONG).show()
                    song_name = withContext(Dispatchers.Default) {
                        return@withContext viewModel.findSong(output.toString(), dir.toString())
                    }
                    /*val f = File(dir+"shazam.txt")
                    if (!f.exists()) {
                        f.createNewFile()
                    }
                    f.writeText(song_name.toString(), Charset.defaultCharset())*/
                    val convertedObject = JsonParser().parse(song_name).asJsonObject
                    if (convertedObject.get("matches").toString().length > 2) {
                        /*Toast.makeText(
                            getActivity(),
                            convertedObject.getAsJsonObject("track")["title"].asString,
                            Toast.LENGTH_LONG
                        ).show()*/

                        Log.d(
                            "MediaRecorderder",
                            convertedObject.getAsJsonObject("track")["title"].asString
                        )

                        val req = convertedObject.getAsJsonObject("track")["title"].asString
                        //val req = convertedObject.getAsJsonObject("track").getAsJsonObject("urlparams")["{tracktitle}"].asString.lowercase()

                        Log.d(
                            "MediaRecorderder", req
                        )
                        var songsPerPage = repo.getSongsPageByName(req, 1)
                        binding.annotationBase.visibility = View.GONE
                        binding.progressShaz.visibility = View.GONE
                        if (songsPerPage.size > 0){
                            Store.id = songsPerPage[0].id
                            mListener.onListFragmentInteraction(songsPerPage[0].id, SongFragment())
                        } else{
                            binding.annotationNotInBase.visibility = View.VISIBLE
                            delay(2000)
                            Store.id = -1
                            var songJson = generateJson(convertedObject).toString().dropLast(3)+"[]}"
                            Store.shazam = songJson
                            //Toast.makeText(getActivity(), "песни нету в беке", Toast.LENGTH_SHORT).show()
                            Log.d(
                                "MediaRecorderder", "песни нету в беке"
                            )
                            Log.d(
                                "MediaRecorderder", songJson.toString()
                            )
                            binding.annotationNotInBase.visibility = View.GONE
                            mListener.onListFragmentInteraction(-1, SongFragment())
                        }



                    } else {
                        binding.annotationBase.visibility = View.GONE
                        binding.progressShaz.visibility = View.GONE
                        binding.annotationNotRecognized.visibility = View.VISIBLE
                        delay(1500)
                        binding.annotationNotRecognized.visibility = View.GONE
                    }
                    state = true
                }

            } else {
                Log.d("MediaRecorderder", "MediaRecorderder is working")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //раскоментировать это и нажимать на значок лупы второй сверху строки поиска
        binding.favouriteFab.setOnClickListener {
            mListener.onListFragmentInteraction(0, FavouriteFragment())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.fab.setOnClickListener {
                search()
            }
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                "$context must implement OnListFragmentInteractionListener"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}