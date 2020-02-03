package com.zhuravlev.vitaly.weatherapp.base.snackbar

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.zhuravlev.vitaly.weatherapp.R

class Snackbar {
    fun showMessage(context: Context, message: String) {
        val snackbar = Snackbar.make(
            getRootView(context),
            message,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(context, R.color.snackbar_background)
        )
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            setTextColor(ContextCompat.getColor(context, R.color.white))
            maxLines = 5
        }
        snackbar.setAction(R.string.ok) {
            snackbar.dismiss() }
        snackbar.show()
    }

    private fun getRootView(context: Context): View {
        return (context as Activity).window.decorView.findViewById(android.R.id.content)
    }
}