package com.vaiwork.playlistmaker.ui.newplaylist.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.newplaylist.view_model.NewPlaylistTitleState
import com.vaiwork.playlistmaker.ui.newplaylist.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistActivity : AppCompatActivity() {

    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    private lateinit var backToolbar: Toolbar
    private lateinit var playlistImageView: ImageView
    private lateinit var playlistTitleEditText: TextView
    private lateinit var playlistDescriptionEditText: TextView
    private lateinit var createPlaylistAppCompatButton: AppCompatButton

    private lateinit var playlistDescirptionInputTextLayout: TextInputLayout
    private lateinit var playlistTitleInputTextLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_playlist)

        playlistDescirptionInputTextLayout = findViewById(R.id.activity_new_playlist_track_description)
        playlistTitleInputTextLayout = findViewById(R.id.activity_new_playlist_playlist_name)

        backToolbar = findViewById(R.id.activity_new_playlist_back_toolbar)
        backToolbar.setNavigationOnClickListener {
            if (playlistImageView.drawable != null
                && (!playlistTitleEditText.text.isNullOrEmpty()
                        || !playlistDescriptionEditText.text.isNullOrEmpty())) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Завершить создание плейлиста?")
                    .setMessage("Все несохраненные данные будут потеряны")
                    .setNegativeButton("Отмена") { _, _ ->

                    }
                    .setPositiveButton("Завершить") { _, _ ->
                        finish()
                    }
                    .show()
            } else {
                finish()
            }
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (playlistImageView.drawable != null
                    && (!playlistTitleEditText.text.isNullOrEmpty()
                            || !playlistDescriptionEditText.text.isNullOrEmpty())) {
                    MaterialAlertDialogBuilder(this@NewPlaylistActivity)
                        .setTitle("Завершить создание плейлиста?")
                        .setMessage("Все несохраненные данные будут потеряны")
                        .setNegativeButton("Отмена") { _, _ ->

                        }
                        .setPositiveButton("Завершить") { _, _ ->
                            finish()
                        }
                        .show()
                } else {
                    finish()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)


        playlistImageView = findViewById(R.id.activity_new_playlist_image)
        val playlistImageViewMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(playlistImageView)
                    .load(uri)
                    .placeholder(R.drawable.playlist_placeholder_image_mini)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            playlistImageView.resources.getDimensionPixelSize(
                                R.dimen.activity_pleer_album_image_corner_radius
                            )
                        )
                    ).into(playlistImageView)
                newPlaylistViewModel.setPlaylistCoverLocalUri(saveImageToPrivateStorage(uri))
            }
        }
        playlistImageView.setOnClickListener {
            playlistImageViewMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playlistTitleEditText = findViewById(R.id.activity_new_playlist_playlist_name_edit_text)
        newPlaylistViewModel.observeNewPlaylistTitle().observe(this) {
            when (it) {
                is NewPlaylistTitleState.Changed -> setCreatePlaylistAppCompatButtonActive(true)
                is NewPlaylistTitleState.Default -> setCreatePlaylistAppCompatButtonActive(false)
            }
        }
        val playlistTitleEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newPlaylistViewModel.newPlaylistTitleController(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                playlistTitleInputTextLayout.isHovered = s.toString().isNotEmpty()
            }
        }
        playlistTitleEditTextWatcher.let { playlistTitleEditText.addTextChangedListener(it) }

        playlistDescriptionEditText = findViewById(R.id.activity_new_playlist_track_description_edit_text)
        val playlistDescriptionEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                playlistDescirptionInputTextLayout.isHovered = s.toString().isNotEmpty()
            }
        }
        playlistDescriptionEditTextWatcher.let { playlistDescriptionEditText.addTextChangedListener(it) }

        createPlaylistAppCompatButton = findViewById(R.id.activity_new_playlist_create_image_button)
        createPlaylistAppCompatButton.isEnabled = false
        createPlaylistAppCompatButton.setOnClickListener {
            Toast.makeText(this, "Плейлист " + playlistTitleEditText.text.toString() + " создан", Toast.LENGTH_SHORT).show()
            newPlaylistViewModel.addPlaylist(
                playlistTitleEditText.text.toString(),
                playlistDescriptionEditText.text.toString()
            )
            finish()
        }
    }

    private fun setCreatePlaylistAppCompatButtonActive(isActive: Boolean) {
        createPlaylistAppCompatButton.isEnabled = isActive
    }

    private fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyPlaylistPictures")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, uri.path!!.substringAfterLast("/").replace(":", "_") + ".jpeg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toURI().toString()
    }

}