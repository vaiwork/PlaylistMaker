package com.vaiwork.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    companion object {
        const val DARK_MODE = "DARK_MODE"
        const val SWITCH_STATE = "SWITCH_STATE"
    }

    private var isCheckedSwitch: Int = 0
    private var isNightModeOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // set dark mode or disable dark mode on switch click
        val switchDarkMode = findViewById<SwitchMaterial>(R.id.id_switch_dark_mode)
        switchDarkMode.setOnClickListener {
            isCheckedSwitch += 1
            if (isNightModeOn) {
                isNightModeOn = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                isNightModeOn = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            switchDarkMode.isChecked = isCheckedSwitch % 2 == 1
        }

        // action for sent app image view
        val sendImageView = findViewById<ImageView>(R.id.id_sent_app)
        sendImageView.setOnClickListener {
            val sendImageViewIntent = Intent(Intent.ACTION_SEND)
            sendImageViewIntent.type = "text/plain"
            sendImageViewIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.url_android_developer))
            val shareSendImageViewIntent = Intent.createChooser(sendImageViewIntent, null)
            startActivity(shareSendImageViewIntent)
        }

        // action for write support image view
        val writeSupportImageView = findViewById<ImageView>(R.id.id_write_support)
        writeSupportImageView.setOnClickListener{
            val writeSupportImageViewIntent = Intent(Intent.ACTION_SENDTO, android.net.Uri.parse("mailto:"))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
            startActivity(writeSupportImageViewIntent)
        }

        // action for user agreement image view
        val userAgreementImageView = findViewById<ImageView>(R.id.id_user_agreement)
        userAgreementImageView.setOnClickListener{
            val userAgreementImageViewIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(getString(R.string.url_user_agreement)))
            startActivity(userAgreementImageViewIntent)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        isNightModeOn = savedInstanceState.getBoolean(DARK_MODE)
        isCheckedSwitch = savedInstanceState.getInt(SWITCH_STATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SWITCH_STATE, isCheckedSwitch)
        outState.putBoolean(DARK_MODE, isNightModeOn)
    }
}