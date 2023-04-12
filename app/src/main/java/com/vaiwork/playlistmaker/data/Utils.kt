package com.vaiwork.playlistmaker.data

import android.app.Activity
import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class Utils {
    fun setImageWithPlaceholder(activity: Activity, imageView: ImageView, url: String, placeholder: Int, requestOptions: RequestOptions) {
        Glide.with(activity).load(url)
            .placeholder(placeholder)
            .apply(requestOptions)
            .into(imageView)
    }

    fun getBooleanKeySharedPref(context: Context,
                                sharedPreferenceName: String,
                                sharedPreferenceMode: Int,
                                sharedPreferenceKey: String,
                                defaultValue: Boolean): Boolean {
        return context
            .getSharedPreferences(sharedPreferenceName, sharedPreferenceMode)
            .getBoolean(sharedPreferenceKey, defaultValue)
    }
}