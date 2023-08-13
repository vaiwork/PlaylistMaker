package com.vaiwork.playlistmaker.ui.audioplayer.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.ActivatePlayState
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.SpendTimeState
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.TracksMediaPlayerViewModel
import com.vaiwork.playlistmaker.ui.newplaylist.activity.NewPlaylistActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {
    private val tracksMediaPlayerViewModel: TracksMediaPlayerViewModel by viewModel()

    private lateinit var albumImageView: ImageView
    private lateinit var timeTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var styleTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var playImageView: ImageView
    private lateinit var spendTimeTextView: TextView
    private lateinit var likeImageView: ImageView

    private lateinit var addToPlaylistImageView: ImageView
    private lateinit var bottomSheetDialogFragment: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var overlay: View
    private lateinit var newPlaylistButton: AppCompatButton

    private var playlistsAdapter: AudioPlayerPlaylistsAdapter = AudioPlayerPlaylistsAdapter()

    companion object {
        private const val ARGS_TRACK_ID = "track_id"
        fun createArgs(trackId: Int): Bundle = bundleOf(ARGS_TRACK_ID to trackId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pleer)

        toolbar = findViewById(R.id.activity_pleer_back_toolbar)
        playImageView = findViewById(R.id.activity_pleer_play_image_view)
        albumImageView = findViewById(R.id.activity_pleer_album_image)
        trackName = findViewById(R.id.activity_pleer_track_name)
        artistName = findViewById(R.id.activity_pleer_artist_name)
        timeTextView = findViewById(R.id.activity_pleer_time)
        albumTextView = findViewById(R.id.activity_pleer_album)
        yearTextView = findViewById(R.id.activity_pleer_year)
        styleTextView = findViewById(R.id.activity_pleer_style)
        countryTextView = findViewById(R.id.activity_pleer_country)
        spendTimeTextView = findViewById(R.id.activity_pleer_spend_time_text_view)
        likeImageView = findViewById(R.id.activity_pleer_like_image_view)
        addToPlaylistImageView = findViewById(R.id.activity_pleer_add_to_playlist_image_view)

        overlay = findViewById(R.id.overlay)

        newPlaylistButton = findViewById(R.id.activity_audio_pleer_new_playlist_buttom)
        newPlaylistButton.setOnClickListener {
            startActivity(Intent(this, NewPlaylistActivity::class.java))
        }

        bottomSheetDialogFragment = findViewById(R.id.activity_audio_pleer_bottom_sheet)
        bottomSheetDialogFragment.visibility = View.GONE
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDialogFragment).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

        recyclerView = findViewById(R.id.activity_audio_pleer_recycler_view)

        addToPlaylistImageView.setOnClickListener {
            bottomSheetDialogFragment.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        tracksMediaPlayerViewModel.observePlaylistsState().observe(this) { it ->
            when(it) {
                is PlaylistsState.Default -> { showPlaylists(false) }
                is PlaylistsState.Content -> { viewPlaylists(it.playlists) }
            }
        }
        tracksMediaPlayerViewModel.isPlaylistsEmpty()
        playlistsAdapter.setItemClickListener {
            tracksMediaPlayerViewModel.addTrackToPlaylist(it)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        tracksMediaPlayerViewModel.setDefaultPlayerState()
        val trackId = intent.getIntExtra(ARGS_TRACK_ID,-1)
        tracksMediaPlayerViewModel.preparePlayerTrack(trackId)

        playImageView.setOnClickListener {
            tracksMediaPlayerViewModel.playbackControl()
        }

        likeImageView.setOnClickListener {
            tracksMediaPlayerViewModel.likeControl()
        }

        tracksMediaPlayerViewModel.observeFavouriteTrack().observe(this) {
            tracksMediaPlayerViewModel.changeTrack(it)
            tracksMediaPlayerViewModel.setAlbumImage(
                R.drawable.placeholder_album_image_light_mode,
                R.dimen.activity_pleer_album_image_corner_radius,
                albumImageView
            )
            trackName.text = tracksMediaPlayerViewModel.getTrackName()
            artistName.text = tracksMediaPlayerViewModel.getTrackArtistName()
            timeTextView.text = tracksMediaPlayerViewModel.getTrackTime()
            albumTextView.text = tracksMediaPlayerViewModel.getTrackCollection()
            yearTextView.text = tracksMediaPlayerViewModel.getTrackYear()
            styleTextView.text = tracksMediaPlayerViewModel.getTrackPrimaryGenre()
            countryTextView.text = tracksMediaPlayerViewModel.getTrackCountry()
        }

        tracksMediaPlayerViewModel.observeIsTrackAdded().observe(this) {
            renderIsTrackAdded(it)
        }

        tracksMediaPlayerViewModel.observeIsTrackFromFavourites().observe(this) {
            renderIsTrackFromFavourite(it)
        }

        tracksMediaPlayerViewModel.observeAudioPlayerState().observe(this) {
            render(it)
        }

        tracksMediaPlayerViewModel.observeLikeButtonState().observe(this) { likeButtonState ->
            when (likeButtonState) {
                is LikeButtonState.ClickedAdd -> {
                    setClickedAddLikeButtonImage()
                    tracksMediaPlayerViewModel.likeButtonClicked()
                }
                is LikeButtonState.ClickedRemove -> {
                    setClickedRemoveLikeButtonImage()
                    tracksMediaPlayerViewModel.likeButtonClicked()
                }
                else -> {
                    tracksMediaPlayerViewModel.isTrackFromFavouritesControl(trackId)
                }
            }
        }

        tracksMediaPlayerViewModel.observeSetSpendTime().observe(this) { spendTimeState ->
            if (spendTimeState is SpendTimeState.Changed) {
                setSpendTime(spendTimeState.timeString)
                tracksMediaPlayerViewModel.spendTimeWasChanged()
            }
        }

        tracksMediaPlayerViewModel.observeActivatePlayImage().observe(this) { activatePlayImage ->
            if (activatePlayImage is ActivatePlayState.Changed) {
                activatePlayImage(activatePlayImage.isEnabled)
                tracksMediaPlayerViewModel.activatePlayImageChanged()
            }
        }
    }

    override fun onResume() {
        tracksMediaPlayerViewModel.isPlaylistsEmpty()
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        tracksMediaPlayerViewModel.onSaveInstanceState()
        super.onSaveInstanceState(outState)
    }

    private fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.PreparedPaused -> setLightPlayImage()
            is AudioPlayerState.Started -> setPauseImage(state.isDarkTheme)
        }
    }

    private fun renderIsTrackFromFavourite(isTrackInFavourites: Boolean) {
        if (isTrackInFavourites) {
            setClickedAddLikeButtonImage()
        } else {
            setClickedRemoveLikeButtonImage()
        }
    }

    private fun setClickedRemoveLikeButtonImage() {
        likeImageView.setImageResource(R.drawable.hearth_light_mode_button)
    }

    private fun setClickedAddLikeButtonImage() {
        likeImageView.setImageResource(R.drawable.heart_clicked_mode_button)
    }

    private fun setSpendTime(text: String) {
        spendTimeTextView.text = text
    }

    private fun setPauseImage(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            setNightPauseImage()
        } else {
            setLightPauseImage()
        }
    }

    private fun setNightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_night_mode_button)
    }

    private fun setLightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_light_mode_button)
    }


    private fun setLightPlayImage() {
        playImageView.setImageResource(R.drawable.play_light_mode_button)
    }

    private fun activatePlayImage(value: Boolean) {
        playImageView.isEnabled = value
    }

    private fun viewPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.setPlaylists(playlists)
        recyclerView.adapter = playlistsAdapter
        showPlaylists(true)
    }

    private fun showPlaylists(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun renderIsTrackAdded(value: Pair<Int, String>) {
        when (value.first) {
            0 -> {
                showTrackAddedToPlaylist("Добавлено в плейлист " + value.second)
            }
            else -> {
                showTrackAlreadyAddedToPlaylist("Трек уже добавлен в плейлист " + value.second)
            }
        }
    }

    private fun showTrackAddedToPlaylist(value: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(value)
            .show()
        bottomSheetDialogFragment.visibility = View.GONE
        overlay.visibility = View.GONE
    }

    private fun showTrackAlreadyAddedToPlaylist(value: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(value)
            .show()
    }
}