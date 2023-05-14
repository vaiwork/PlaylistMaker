package com.vaiwork.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.presentation.settings.SettingsPresenter
import com.vaiwork.playlistmaker.presentation.settings.SettingsView
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class SettingsActivity : AppCompatActivity(), SettingsView {

    private var settingsPresenter: SettingsPresenter? = null //Creator.provideSettingsController(this, this)

    private lateinit var toolbar: Toolbar
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var sendImageView: ImageView
    private lateinit var writeSupportImageView: ImageView
    private lateinit var userAgreementImageView: ImageView

    override fun onStart() {
        super.onStart()
        settingsPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        settingsPresenter?.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsPresenter = (this.applicationContext as? App)?.settingsPresenter
        if (settingsPresenter == null) {
            settingsPresenter = Creator.provideSettingsController(this)
            (this.applicationContext as? App)?.settingsPresenter = settingsPresenter
        }
        settingsPresenter?.attachView(this)

        toolbar = findViewById(R.id.settings_toolbar)
        switchDarkMode = findViewById(R.id.id_switch_dark_mode)
        sendImageView = findViewById(R.id.id_sent_app)
        writeSupportImageView = findViewById(R.id.id_write_support)
        userAgreementImageView = findViewById(R.id.id_user_agreement)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        switchDarkMode.isChecked = settingsPresenter?.getDarkModeValue()!!
        switchDarkMode.setOnCheckedChangeListener { switcher, checked ->
            settingsPresenter?.switchAppTheme(checked)
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

    override fun onPause() {
        super.onPause()
        settingsPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        settingsPresenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        settingsPresenter?.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter?.detachView()

        if (isFinishing()) {
            // Очищаем ссылку на Presenter в Application
            (this.application as? App)?.settingsPresenter = null
        }
    }
}