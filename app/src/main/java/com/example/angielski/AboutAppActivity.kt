package com.example.angielski

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.angielski.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {

    // ----- How to get to id layout -----
    private lateinit var binding: ActivityAboutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
    }
}