package com.zhuravlev.vitaly.weatherapp.base.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(
    "text",
    "capitalize"
)
fun bindingCapitalizeText(
    textView: TextView,
    text: String?,
    capitalize: Boolean?
) {
    if (capitalize == true) {
        textView.text = text?.capitalize()
    }
}