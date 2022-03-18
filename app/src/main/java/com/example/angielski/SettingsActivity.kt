package com.example.angielski

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
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
        binding.editTextWord.hint =  sharedPref.getInt("wordsCount",0).toString()
        binding.buttonSave.setOnClickListener {
            val wordsCount = binding.editTextWord.text

            if(wordsCount.isEmpty()){
                Toast.makeText(applicationContext, "Brak wartości", Toast.LENGTH_SHORT).show()
            }
            else{
                // ----- Add data and commit -----
                editor.putInt("wordsCount",wordsCount.toString().toInt() )

                // ----- Update daily words limit -----
                editor.putInt("WordsToLearnPlToEng",wordsCount.toString().toInt() )
                editor.putInt("WordsToLearnEngToPl",wordsCount.toString().toInt() )

                editor.apply()

                // ----- Print communique "Saved' -----
                Toast.makeText(applicationContext, "Zapisano", Toast.LENGTH_SHORT).show()
            }


        }


    }


    // ----- Maybe for a future update. Replay time setting. Now const. ------
//    fun setOnCheckedChangeListener1(){
//        val numRepeat = 3 // liczba powtórek
//        val selectedButtonId = arrayOf(binding.radioGroup1.checkedRadioButtonId,binding.radioGroup2.checkedRadioButtonId,binding.radioGroup3.checkedRadioButtonId)
//
//        var tabString= arrayOfNulls<String>(numRepeat)
//        for(i in 0 until numRepeat){
//            val radioButtonText = findViewById<RadioButton>(selectedButtonId[i]).toString()
//            Log.d("TAG", radioButtonText)
//
//            val wynik: String
//
//            wynik = binding.editText1.text.toString()
//            tabString[i] = ""
//            when (radioButtonText){
//                "m" -> "minute"
//
//            }
//        }
//
//    }
}