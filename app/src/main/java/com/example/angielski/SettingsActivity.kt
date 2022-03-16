package com.example.angielski

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.angielski.databinding.ActivityLearningBinding
import com.example.angielski.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    // ----- How to get to id layout -----
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // ----- How to get to id layout -----
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----- Creare and update local file (variable) "Settings" -----
        val sharedPref: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        binding.editTextWord.hint =  sharedPref.getString("wordsCount","?")
        binding.buttonSave.setOnClickListener {
            val wordsCount = binding.editTextWord.text

            // ----- Add data and commit -----
            editor.putString("wordsCount",wordsCount.toString() )
            editor.apply()

            // ----- Print communique "Saved' -----
            Toast.makeText(applicationContext, "Zapisano", Toast.LENGTH_SHORT).show()
        }


    }
}