package com.vaiwork.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.databinding.FragmentSettingsBinding
import com.vaiwork.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var sendImageView: ImageView
    private lateinit var writeSupportImageView: ImageView
    private lateinit var userAgreementImageView: ImageView

    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchDarkMode = binding.idSwitchDarkMode
        sendImageView = binding.idSentApp
        writeSupportImageView = binding.idWriteSupport
        userAgreementImageView = binding.idUserAgreement

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