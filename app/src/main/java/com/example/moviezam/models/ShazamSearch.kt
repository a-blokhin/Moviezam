package com.example.moviezam.models

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class ShazamSearch {
    interface APIService {
        @POST("/songs/detect/")
        @Headers(
            "content-type: text/plain",
            "x-rapidapi-host: shazam.p.rapidapi.com",
            "x-rapidapi-key: 59a8429e11msh93251c2d858a96ep114b40jsnfb035a647df7"
        )
        suspend fun ShazamRequest(@Body requestBody: RequestBody): Response<ResponseBody>
    }

    private var mediaRecorder: WavAudioRecorder? = null

    suspend fun record(output: String) {

        mediaRecorder = WavAudioRecorder.getInstanse()
        mediaRecorder?.setOutputFile(output)
        Log.d("MediaRecorder", mediaRecorder!!.getState().toString())
        try {
            Log.d("MediaRecorder", "Recording starting!")
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            delay(4500)
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun search(output: String): String {

        record(output)

        val encoded = Files.readAllBytes(Paths.get(output))


        @RequiresApi(Build.VERSION_CODES.O)
        fun ByteArray.toBase64(): String = String(Base64.getEncoder().encode(this))
        val b64 = encoded.toBase64()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://shazam.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create Service
        val service = retrofit.create(APIService::class.java)
        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody: RequestBody = b64.toRequestBody("text/plain".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            return@withContext service.ShazamRequest(requestBody).body()?.string().toString()
        }


    }


}