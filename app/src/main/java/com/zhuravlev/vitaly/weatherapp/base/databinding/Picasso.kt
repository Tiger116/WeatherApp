package com.zhuravlev.vitaly.weatherapp.base.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.zhuravlev.vitaly.weatherapp.base.Constants

@BindingAdapter(
    "icon",
    "placeholder",
    requireAll = false
)
fun bindingLoadIcon(
    imageView: ImageView,
    icon: String?,
    placeholder: Drawable?
) {
    when {
        icon != null -> {
            Picasso.get()
                .load("${Constants.OPEN_WEATHER_ICON_URL}$icon@2x.png")
                .fit()
                .also {
                    placeholder?.let { placeholder ->
                        it.placeholder(placeholder)
                        it.error(placeholder)
                    }
                }
                .into(imageView)
        }
        else -> imageView.setImageDrawable(placeholder)
    }
}