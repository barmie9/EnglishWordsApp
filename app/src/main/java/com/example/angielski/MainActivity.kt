package com.example.angielski

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.angielski.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ----- How to get to id layout -----
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // ----- How to get to id layout -----
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----- Local file (variable) "Settings" -----
        val sharedPref: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)

        // ----- Create and fill DataBase for the first time -----
        if( !(sharedPref.getBoolean("isCreatedDataBase", false)) ){
            fillDataBase.fill(applicationContext)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean("isCreatedDataBase",true)
            editor.commit()
        }


        // ----- Open a new activity "LearningActivity" if Learning  -----
        binding.buttonLearning.setOnClickListener()
        {
            var newActivity: Intent = Intent(applicationContext, LearningActivity::class.java)
            newActivity.putExtra("is_learning", true)
            startActivity(newActivity)
        }

        // ----- Open a new activity "LearningActivity" if Repeating-----
        binding.buttonRepeating.setOnClickListener()
        {
            var newActivity: Intent = Intent(applicationContext, LearningActivity::class.java)
            newActivity.putExtra("is_learning", false)
            startActivity(newActivity)
        }
        // ----- Open a new activity "SettingsActivity" -----
        binding.buttonSettings.setOnClickListener {
            //Nowa aktywność odpalajaca SettingsdActivity
            var newActivity: Intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(newActivity)
        }

        // ----- Open a new activity "AboutAppActivity" -----
        binding.buttonAboutApp.setOnClickListener {
            //TODO
        }


    }



}