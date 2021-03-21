package com.cookandroid.gachon_study_room.adapter.binding

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cookandroid.gachon_study_room.singleton.TimeRequest

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: Int) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

}