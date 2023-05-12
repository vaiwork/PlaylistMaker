package com.vaiwork.playlistmaker.presentation

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.util.Creator

class SettingsController(
    private val activity: Activity
) {
    private lateinit var toolbar: Toolbar
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var sendImageView: ImageView
    private lateinit var writeSupportImageView: ImageView
    private lateinit var userAgreementImageView: ImageView

    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(activity)

    fun onCreate() {
        toolbar = activity.findViewById(R.id.settings_toolbar)
        switchDarkMode = activity.findViewById(R.id.id_switch_dark_mode)
        sendImageView = activity.findViewById(R.id.id_sent_app)
        writeSupportImageView = activity.findViewById(R.id.id_write_support)
        userAgreementImageView = activity.findViewById(R.id.id_user_agreement)

        toolbar.setNavigationOnClickListener { activity.onBackPressed() }

        switchDarkMode.isChecked = sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)
        switchDarkMode.setOnCheckedChangeListener { switcher, checked ->
            sharedPreferenceInteractor.switchTheme(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, checked)
            AppCompatDelegate.setDefaultNightMode(
                if (checked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        sendImageView.setOnClickListener {
            val sendImageViewIntent = Intent(Intent.ACTION_SEND)
            sendImageViewIntent.type = "text/plain"
            sendImageViewIntent.putExtra(Intent.EXTRA_TEXT,activity.getString(R.string.url_android_developer))
            val shareSendImageViewIntent = Intent.createChooser(sendImageViewIntent, null)
            activity.startActivity(shareSendImageViewIntent)
        }

        writeSupportImageView.setOnClickListener{
            val writeSupportImageViewIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(activity.getString(R.string.support_email)))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_subject))
            writeSupportImageViewIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.support_body))
            activity.startActivity(writeSupportImageViewIntent)
        }

        userAgreementImageView.setOnClickListener{
            val userAgreementImageViewIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(activity.getString(
                R.string.url_user_agreement
            )))
            activity.startActivity(userAgreementImageViewIntent)
        }
    }
}