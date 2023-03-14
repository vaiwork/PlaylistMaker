package com.vaiwork.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AudioPleerActivity : AppCompatActivity() {

    private lateinit var albumImageView: ImageView
    private lateinit var timeTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var styleTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pleer)

        toolbar = findViewById(R.id.activity_pleer_back_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        var tracks: ArrayList<Track> = SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs()!!
        var clickedTrack = tracks[0]

        albumImageView = findViewById(R.id.activity_pleer_album_image)
        Glide.with(this).load(clickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_album_image_light_mode)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.activity_pleer_album_image_corner_radius))))
            .into(albumImageView)

        trackName = findViewById(R.id.activity_pleer_track_name)
        trackName.text = clickedTrack.trackName

        artistName = findViewById(R.id.activity_pleer_artist_name)
        artistName.text = clickedTrack.artistName

        timeTextView = findViewById(R.id.activity_pleer_time)
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(clickedTrack.trackTime)

        albumTextView = findViewById(R.id.activity_pleer_album)
        albumTextView.text = clickedTrack.collectionName

        yearTextView = findViewById(R.id.activity_pleer_year)
        yearTextView.text = clickedTrack.releaseDate.subSequence(0,4)

        styleTextView = findViewById(R.id.activity_pleer_style)
        styleTextView.text = clickedTrack.primaryGenreName

        countryTextView = findViewById(R.id.activity_pleer_country)
        countryTextView.text = clickedTrack.country
    }
}