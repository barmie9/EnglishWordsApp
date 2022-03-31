package com.example.angielski

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.angielski.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            val numWords = fillDataBase.fill(applicationContext)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean("isCreatedDataBase",true)
            editor.putFloat("AllWordsNumber", numWords.toFloat())
            editor.putFloat("TotalLearnedWords",0f)
            //------------------ DO POPRAWY--------------------
            editor.putFloat("dailyLimit",5f)
            editor.apply()
            editor.putString("prevDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            editor.putFloat("learnedWordsPlToEng",0f)
            editor.putFloat("learnedWordsEngToPl",0f)
            //--------------------------------------
            editor.apply()
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
            var newActivity: Intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(newActivity)
        }

        // ----- Open a new activity "AboutAppActivity" -----
        binding.buttonAboutApp.setOnClickListener {
            var newActivity: Intent = Intent(applicationContext, AboutAppActivity::class.java)
            startActivity(newActivity)
        }


    }


    override fun onStart() {
        super.onStart()
        // ----- Local file (variable) "Settings" -----
        val sharedPref: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()


        // ----- Set info about number of learned words -----
        val plToEng = sharedPref.getFloat("learnedWordsPlToEng",51f)
        val engToPl = sharedPref.getFloat("learnedWordsEngToPl",51f)
        val dailyLimit= sharedPref.getFloat("dailyLimit",52f)
        Log.d("TAG", "daily update: plToEng: $plToEng , engToPl: $engToPl , dailyLimit: $dailyLimit")
        binding.textViewInfo.text = "Nauczone dzisiaj:  ${(plToEng + engToPl)/2f }/${dailyLimit}"

        // ----- Set info about total number of learned words -----
        val totalWords = sharedPref.getFloat("AllWordsNumber",0f)
        val totalLearnedWords = sharedPref.getFloat("TotalLearnedWords",0f)
        binding.textViewInfo2.text = "Cały kurs:  $totalLearnedWords/$totalWords "

        editor.putFloat("TotalWordsToLearn", totalWords - totalLearnedWords)
        editor.apply()



        // ----- Daily update of vocabulary words for learning -----
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val lastDate = sharedPref.getString("prevDate","")
        if(currentDate != lastDate){
            Toast.makeText(applicationContext, "Nowe Słówka", Toast.LENGTH_SHORT).show()
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putFloat("learnedWordsPlToEng",0f)
            editor.putFloat("learnedWordsEngToPl",0f)
            editor.putString("prevDate",currentDate)
            editor.apply()
        }
    }

}