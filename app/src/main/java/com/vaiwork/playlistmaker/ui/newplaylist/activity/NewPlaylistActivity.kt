package com.vaiwork.playlistmaker.ui.newplaylist.activity

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private lateinit var playlistTitleEditText: EditText
    private lateinit var playlistDescriptionEditText: EditText
    private lateinit var createPlaylistAppCompatButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_playlist)

        backToolbar = findViewById(R.id.activity_new_playlist_back_toolbar)
        backToolbar.setNavigationOnClickListener {
            if (playlistImageView.drawable != null
                && (!playlistTitleEditText.text.isNullOrEmpty()
                        || !playlistDescriptionEditText.text.isNullOrEmpty())) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Завершить создание плейлиста?")
                    .setMessage("Все несохраненные данные будут потеряны")
                    .setNegativeButton("Отмена") { dialog, which ->

                    }
                    .setPositiveButton("Завершить") { dialog, which ->
                        finish()
                    }
                    .show()
            } else {
                finish()
            }
        }

        playlistImageView = findViewById(R.id.activity_new_playlist_image)
        val playlistImageViewMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistImageView.setImageURI(uri)
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
            }
        }
        playlistTitleEditTextWatcher.let { playlistTitleEditText.addTextChangedListener(it) }

        playlistDescriptionEditText = findViewById(R.id.activity_new_playlist_track_description_edit_text)

        createPlaylistAppCompatButton = findViewById(R.id.activity_new_playlist_create_image_button)
        createPlaylistAppCompatButton.isEnabled = false
        createPlaylistAppCompatButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setMessage("Плейлист " + playlistTitleEditText.text.toString() + " создан")
                .setNeutralButton("ОК",  object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        newPlaylistViewModel.addPlaylist(playlistTitleEditText.text.toString(), playlistDescriptionEditText.text.toString())
                        finish()
                    }
                })
                .show()
        }
    }

    private fun setCreatePlaylistAppCompatButtonActive(isActive: Boolean) {
        createPlaylistAppCompatButton.isEnabled = isActive
    }

    private fun saveImageToPrivateStorage(uri: Uri): String {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyPlaylistPictures")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, uri.path!!.substringAfterLast("/").replace(":", "_") + ".jpeg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toURI().toString()
    }

}