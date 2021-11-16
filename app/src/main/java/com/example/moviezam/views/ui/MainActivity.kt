package com.example.moviezam.views.ui


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moviezam.R
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class MainActivity : AppCompatActivity() {
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
        songJson.addProperty("album_name", "")
        songJson.addProperty(
            "external_art_url", convertedObject.getAsJsonObject("track")
                .getAsJsonObject("images")["background"].asString
        )
        songJson.addProperty("amazon", "")
        songJson.addProperty("apple_music", "")
        songJson.addProperty("itunes", "")
        songJson.addProperty("spotify", "")
        songJson.addProperty("youtube", "")
        songJson.addProperty("films", "[]")// надо сделать List<FilmCard> или не надо
        return songJson
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)


        val shazamFragment: Fragment = ShazamFragment()
        val bundle = Bundle()
        bundle.putString("output", externalCacheDir?.absolutePath + "/soundrecorder/recording.wav")
        bundle.putString("dir", externalCacheDir?.absolutePath + "/soundrecorder/")
        //bundle.putString("output", "/storage/emulated/0/Android/media/trigwg.wav")
        shazamFragment.arguments = bundle
        val tempjson = "{\"matches\":[{\"id\":\"539756316\",\"offset\":25.97609375,\"channel\":\"0\",\"timeskew\":-0.07637352,\"frequencyskew\":-0.081837}],\"timestamp\":1636488431741,\"timezone\":\"America/Chicago\",\"tagid\":\"04db5f19-e057-4d64-b5e2-8122387b3e2e\",\"track\":{\"layout\":\"5\",\"type\":\"MUSIC\",\"key\":\"539756316\",\"title\":\"Trigger Of Love\",\"subtitle\":\"JAWNY\",\"images\":{\"background\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"coverart\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"coverarthq\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"joecolor\":\"b:0c0a13p:f0f8fbs:f4e6d2t:c2c8ccq:c6baac\"},\"share\":{\"subject\":\"Trigger Of Love - JAWNY\",\"text\":\"I used Shazam to discover Trigger Of Love by JAWNY.\",\"href\":\"https://www.shazam.com/track/539756316/trigger-of-love\",\"image\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"twitter\":\"I used @Shazam to discover Trigger Of Love by JAWNY.\",\"html\":\"https://www.shazam.com/snippets/email-share/539756316?lang=en-US&amp;country=US\",\"snapchat\":\"https://www.shazam.com/partner/sc/track/539756316\"},\"hub\":{\"type\":\"APPLEMUSIC\",\"image\":\"https://images.shazam.com/static/icons/hub/ios/v5/applemusic_{scalefactor}.png\",\"actions\":[{\"name\":\"apple\",\"type\":\"applemusicplay\",\"id\":\"1536068355\"},{\"name\":\"apple\",\"type\":\"uri\",\"uri\":\"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/a4/79/9c/a4799ca3-c635-f13d-1847-67e6eb35498a/mzaf_12609007627404109670.plus.aac.ep.m4a\"}],\"options\":[{\"caption\":\"OPEN\",\"actions\":[{\"name\":\"hub:applemusic:deeplink\",\"type\":\"applemusicopen\",\"uri\":\"https://music.apple.com/us/album/trigger-of-love/1536068346?i=1536068355&amp;mttnagencyid=769459046716559743&amp;mttnsiteid=125115&amp;mttn3pid=a_custom_779816081798873874&amp;mttnsub1=Shazam_ios&amp;mttnsub2=5348615A-616D-3235-3830-44754D6D5973&amp;itscg=30201&amp;app=music&amp;itsct=Shazam_ios\"},{\"name\":\"hub:applemusic:deeplink\",\"type\":\"uri\",\"uri\":\"https://music.apple.com/us/album/trigger-of-love/1536068346?i=1536068355&amp;mttnagencyid=769459046716559743&amp;mttnsiteid=125115&amp;mttn3pid=a_custom_779816081798873874&amp;mttnsub1=Shazam_ios&amp;mttnsub2=5348615A-616D-3235-3830-44754D6D5973&amp;itscg=30201&amp;app=music&amp;itsct=Shazam_ios\"}],\"beacondata\":{\"type\":\"open\",\"providername\":\"applemusic\"},\"image\":\"https://images.shazam.com/static/icons/hub/ios/v5/overflow-open-option_{scalefactor}.png\",\"type\":\"open\",\"listcaption\":\"Open in Apple Music\",\"overflowimage\":\"https://images.shazam.com/static/icons/hub/ios/v5/applemusic-overflow_{scalefactor}.png\",\"colouroverflowimage\":false,\"providername\":\"applemusic\"},{\"caption\":\"BUY\",\"actions\":[{\"type\":\"uri\",\"uri\":\"https://itunes.apple.com/us/album/trigger-of-love/1536068346?i=1536068355&amp;mttnagencyid=769459046716559743&amp;mttnsiteid=125115&amp;mttn3pid=a_custom_779816081798873874&amp;mttnsub1=Shazam_ios&amp;mttnsub2=5348615A-616D-3235-3830-44754D6D5973&amp;itscg=30201&amp;app=itunes&amp;itsct=Shazam_ios\"}],\"beacondata\":{\"type\":\"buy\",\"providername\":\"itunes\"},\"image\":\"https://images.shazam.com/static/icons/hub/ios/v5/itunes-overflow-buy_{scalefactor}.png\",\"type\":\"buy\",\"listcaption\":\"Buy on iTunes\",\"overflowimage\":\"https://images.shazam.com/static/icons/hub/ios/v5/itunes-overflow-buy_{scalefactor}.png\",\"colouroverflowimage\":false,\"providername\":\"itunes\"}],\"providers\":[{\"caption\":\"Open in Spotify\",\"images\":{\"overflow\":\"https://images.shazam.com/static/icons/hub/ios/v5/spotify-overflow_{scalefactor}.png\",\"default\":\"https://images.shazam.com/static/icons/hub/ios/v5/spotify_{scalefactor}.png\"},\"actions\":[{\"name\":\"hub:spotify:searchdeeplink\",\"type\":\"uri\",\"uri\":\"spotify:search:Trigger%20Of%20Love%20JAWNY\"}],\"type\":\"SPOTIFY\"},{\"caption\":\"Open in Deezer\",\"images\":{\"overflow\":\"https://images.shazam.com/static/icons/hub/ios/v5/deezer-overflow_{scalefactor}.png\",\"default\":\"https://images.shazam.com/static/icons/hub/ios/v5/deezer_{scalefactor}.png\"},\"actions\":[{\"name\":\"hub:deezer:searchdeeplink\",\"type\":\"uri\",\"uri\":\"deezer-query://www.deezer.com/play?query=%7Btrack%3A%27Trigger+Of+Love%27%20artist%3A%27JAWNY%27%7D\"}],\"type\":\"DEEZER\"}],\"explicit\":true,\"displayname\":\"APPLE MUSIC\"},\"url\":\"https://www.shazam.com/track/539756316/trigger-of-love\",\"isrc\":\"USUG12003818\",\"genres\":{\"primary\":\"Alternative\"},\"urlparams\":{\"{tracktitle}\":\"Trigger+Of+Love\",\"{trackartist}\":\"JAWNY\"},\"myshazam\":{\"apple\":{\"actions\":[{\"name\":\"myshazam:apple\",\"type\":\"uri\",\"uri\":\"https://music.apple.com/us/album/trigger-of-love/1536068346?i=1536068355&amp;mttnagencyid=769459046716559743&amp;mttnsiteid=125115&amp;mttn3pid=a_custom_779816081798873874&amp;mttnsub1=Shazam_ios&amp;mttnsub2=5348615A-616D-3235-3830-44754D6D5973&amp;itscg=30201&amp;app=music&amp;itsct=Shazam_ios\"}]}},\"albumadamid\":\"1536068346\",\"sections\":[{\"type\":\"SONG\",\"metapages\":[{\"image\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"caption\":\"Trigger Of Love\"}],\"tabname\":\"Song\",\"metadata\":[{\"title\":\"Album\",\"text\":\"For Abby\"},{\"title\":\"Label\",\"text\":\"Johnny Utah PS\"},{\"title\":\"Released\",\"text\":\"2020\"}]},{\"type\":\"VIDEO\",\"tabname\":\"Video\",\"youtubeurl\":{\"caption\":\"JAWNY - Trigger Of Love (Audio)\",\"image\":{\"dimensions\":{\"width\":1280,\"height\":720},\"url\":\"https://i.ytimg.com/vi/URIsvm0vrqE/maxresdefault.jpg\"},\"actions\":[{\"name\":\"video:youtube\",\"type\":\"webview\",\"share\":{\"subject\":\"Trigger Of Love - JAWNY\",\"text\":\"I used Shazam to discover Trigger Of Love by JAWNY.\",\"href\":\"https://www.shazam.com/track/539756316/trigger-of-love\",\"image\":\"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/400x400cc.jpg\",\"twitter\":\"I used @Shazam to discover Trigger Of Love by JAWNY.\",\"html\":\"https://www.shazam.com/snippets/email-share/539756316?lang=-&amp;country=US\",\"snapchat\":\"https://www.shazam.com/partner/sc/track/539756316\"},\"uri\":\"https://youtu.be/URIsvm0vrqE?autoplay=1\"}]}},{\"type\":\"RELATED\",\"tabname\":\"Related\"}]}}"
        val songJson = JsonParser().parse(tempjson).asJsonObject

        Log.d(
            "MediaRecorderder", generateJson(songJson).toString()
        )

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, shazamFragment, "SHAZAM")
                .commitAllowingStateLoss()
        }
    }



}

