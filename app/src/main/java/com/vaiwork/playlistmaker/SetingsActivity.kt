package com.vaiwork.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // action for sent app image view
        val sendImageView = findViewById<ImageView>(R.id.id_sent_app)
        sendImageView.setOnClickListener {
            val sendImageViewIntent = Intent(Intent.ACTION_SEND)
            sendImageViewIntent.type = "text/plain"
            sendImageViewIntent.putExtra(Intent.EXTRA_TEXT, android.net.Uri.parse(getString(R.string.url_android_developer)))
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
}