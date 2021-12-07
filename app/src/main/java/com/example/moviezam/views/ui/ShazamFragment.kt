package com.example.moviezam.views.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviezam.databinding.FragmentShazamBinding
import com.example.moviezam.models.Store
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.viewmodels.ShazamViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*

class ShazamFragment : BaseFragment(){
    private var _binding: FragmentShazamBinding? = null
    private val viewModel = ShazamViewModel()
    private var song_name: String? = null
    var state = true
    private lateinit var mListener: OnListFragmentInteractionListener
    private val binding get() = _binding!!
    private val repo = SongRepository()

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
                if ((query != null) && state) {
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
    /*    private fun handleVolume(volume: Int) {
            val scale = min(8.0, volume / MAX_RECORD_AMPLITUDE + 1.0).toFloat()
            //Log.d("Scale", "Scale = $scale")

            binding.volumeButton.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setInterpolator(interpolator)
                .duration = VOLUME_UPDATE_DURATION
        }
        private companion object {
            private const val MAX_RECORD_AMPLITUDE = 32768.0
            private const val VOLUME_UPDATE_DURATION = 100L
            private val interpolator = OvershootInterpolator()
        }

        fun drawVolume(output: String){
            val handler = Handler(Looper.getMainLooper())
            val fileSize = 88
            var progressStatus = -1
            //var mediaRecorder: MediaRecorder? = null
            var mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(output.dropLast(4)+"2.wav")}
            val runnable = java.lang.Runnable {
                binding.volumeButton.visibility = View.VISIBLE
                while (progressStatus < fileSize) {

                    //val volume =
                    //Log.d("volume", "Volume = $volume")

                    val scale = min(8.0, (mediaRecorder?.maxAmplitude?.div(MAX_RECORD_AMPLITUDE) ?: 0.0) + 1.0).toFloat()
                    handler.post {binding.volumeButton.animate()
                        .scaleX(scale)

                        //.setInterpolator(interpolator)
                        //.duration = VOLUME_UPDATE_DURATION
                    }
                    progressStatus += 1
                    Thread.sleep(170)
                }
                handler.post {
                    binding.volumeButton.visibility = View.GONE}
                mediaRecorder.stop()
                mediaRecorder.reset()
                mediaRecorder.release()
            }
            mediaRecorder.prepare()
            mediaRecorder.start()
            Thread(runnable).start()


        }*/
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
                val output = requireActivity().externalCacheDir?.absolutePath + "/recording.wav"
                lifecycleScope.launch {
                    //Toast.makeText(getActivity(), "Запись 4.5 секунды пошла", Toast.LENGTH_LONG).show()
                    song_name = withContext(Dispatchers.Default) {
                        return@withContext viewModel.findSong(output)
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