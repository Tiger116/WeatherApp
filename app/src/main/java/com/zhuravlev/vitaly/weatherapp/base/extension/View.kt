package com.zhuravlev.vitaly.weatherapp.base.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.postDelayed
import com.zhuravlev.vitaly.weatherapp.base.Constants

fun View.onDelayedClick(delay: Long = Constants.ON_CLICK_DELAY, listener: (View) -> Unit) =
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            listener(this)
            handler.postDelayed(delay) {
                isEnabled = true
            }
        }
    }

internal fun View.hideKeyboard() {
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also { manager ->
        if (manager.isActive) {
            manager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}

internal fun View.showKeyboard() {
    this.requestFocus()
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also { manager ->
        manager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }
}