package com.zhuravlev.vitaly.weatherapp.ui.activities

import android.os.Bundle
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.KodeinActivity

class MainActivity : KodeinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
