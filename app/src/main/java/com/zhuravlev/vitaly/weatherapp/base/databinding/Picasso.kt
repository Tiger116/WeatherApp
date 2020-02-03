package com.zhuravlev.vitaly.weatherapp.base.databinding

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zhuravlev.vitaly.weatherapp.base.Constants

@BindingAdapter(
    "icon",
    "error",
    requireAll = false
)
fun bindingLoadIcon(
    imageView: ImageView,
    icon: String?,
    error: Drawable?
) {
    when {
        icon != null -> {
            Picasso.get()
                .load("${Constants.OPEN_WEATHER_ICON_URL}$icon@2x.png")
                .fit()
                .also {
                    error?.let { placeholder -> it.error(placeholder) }
                }
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        Log.d("BindingAdapter", "onSuccess")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("BindingAdapter", "onError: ")
                        e?.printStackTrace()
                    }
                })
        }
        else -> imageView.setImageDrawable(error)
    }
}