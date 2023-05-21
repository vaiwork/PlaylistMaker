package com.vaiwork.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel //Creator.provideSettingsController(this, this)

    private lateinit var toolbar: Toolbar
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var sendImageView: ImageView
    private lateinit var writeSupportImageView: ImageView
    private lateinit var userAgreementImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsViewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        toolbar = findViewById(R.id.settings_toolbar)
        switchDarkMode = findViewById(R.id.id_switch_dark_mode)
        sendImageView = findViewById(R.id.id_sent_app)
        writeSupportImageView = findViewById(R.id.id_write_support)
        userAgreementImageView = findViewById(R.id.id_user_agreement)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        switchDarkMode.isChecked = settingsViewModel.getDarkModeValue()
        switchDarkMode.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.switchAppTheme(checked)
        }

        sendImageView.setOnClickListener {
            val sendImageViewIntent = Intent(Intent.ACTION_SEND)
            sendImageViewIntent.type = "text/plain"
            sendImageViewIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.url_android_developer))
            val shareSendImageViewIntent = Intent.createChooser(sendImageViewIntent, null)
            startActivity(shareSendImageViewIntent)
        }

        writeSupportImageView.setOnClickListener{
            val writeSupportImageViewIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
            startActivity(writeSupportImageViewIntent)
        }

        userAgreementImageView.setOnClickListener{
            val userAgreementImageViewIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(
                    R.string.url_user_agreement
                )))
            startActivity(userAgreementImageViewIntent)
        }
    }
}