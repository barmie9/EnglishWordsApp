package com.example.angielski

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.media.MediaParser
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import com.example.angielski.databinding.ActivityLearningBinding
import com.example.angielski.databinding.ActivityMainBinding
import java.lang.reflect.Array

class LearningActivity : AppCompatActivity() {

    // ----- How to get to id layout -----
    private lateinit var binding: ActivityLearningBinding


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        //2 linijki ktore trzeba dodac https://youtu.be/qbq5PR-l5ZU?t=634
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Przejecie danych z głownej aktywności , mowiace o tym czy to powtórki czy nauka
        val isLearning = getIntent().getBooleanExtra("is_learning",false)

        val dbHelper = DataBaseHelper(applicationContext)
        val db = dbHelper.readableDatabase
        val query: String
        if(isLearning){
            query = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_IS_LEARNED} = 'f' "
        }
        else{
            query = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_IS_LEARNED} = 't' "+
                    " AND DATETIME('now', 'localtime') >= DATETIME(${TableInfo.TABLE_COLUMN_DATE},${TableInfo.TABLE_COLUMN_REPLAY}); "
        }

        val result = db.rawQuery(query,null)
        var englishWord: String = ""
        var polishWord: String = ""
        var answere: String = ""

        // Załadowanie za piperwszym razem danych
        if(result.moveToFirst()){
            polishWord =  result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_POLISH))
            binding.textViewWords.text = polishWord
            englishWord = result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ENGLISH))
            val id = resources.getIdentifier(result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "drawable", packageName )
            binding.imageViewWord.setImageResource(id)
        }


        var str: String = ""
        var tab = Array(2) {Array(10) {0} }

        // Pętla przechodząca wszystkie słówka z bazy dopóki nie zostaną wszystkie nauczone,
        // zarówno z ang->pl jak i pl->ang
        for(i in tab[0].indices){

            // ----- Obsługa przycisku "Sprawdz"
            binding.buttonCheckAnswere.setOnClickListener {

                answere = binding.editTextAnswere.text.toString()

                binding.textViewWords.text = "$polishWord - $englishWord"

                //Jeśli poprawna odpowiedz
                if(checkAnswere.check(englishWord,answere)){
                    MediaPlayer.create(this,R.raw.duolingo_1).start()
                    binding.imageViewAnswere.setImageResource(R.drawable.answere_1)

                    val replay = result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_REPLAY))
                    var newReplay: String = ""
                    var newIsLearned = "'t'"

                    when (replay){
                        "+1 second" -> newReplay = "'+2 second'"
                        "+2 second" -> newReplay = "'+3 second'"
                        "+3 second" -> newReplay = "'+4 second'"
                        "+4 second" ->{
                            newIsLearned = "'ct'"
                            newReplay = "'+0 second'"
//                        Toast.makeText(applicationContext, "8 minute", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
//                        Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show()
                            newReplay = "'+1 second'"
                        }


                    }


                    val queryUpdate = "UPDATE ${TableInfo.TABLE_NAME} "+
                            "SET ${TableInfo.TABLE_COLUMN_IS_LEARNED} = ${newIsLearned}, " +
                            "${TableInfo.TABLE_COLUMN_DATE} = DATETIME('now', 'localtime'), " +
                            "${TableInfo.TABLE_COLUMN_REPLAY} =  $newReplay "+
                            " WHERE ${BaseColumns._ID} = ${result.getString(result.getColumnIndex(BaseColumns._ID))};" //_id  ${result.getString(result.getColumnIndex(BaseColumns._ID))}

//                db.rawQuery(query_update, null)
                    db.execSQL(queryUpdate)

                }
                //Jesli bledna odpowiedz
                else
                {
                    MediaPlayer.create(this,R.raw.duolingo_2).start()
                    binding.imageViewAnswere.setImageResource(R.drawable.answere_2)
                }
                binding.imageViewAnswere.visibility = View.VISIBLE
                binding.buttonCheckAnswere.visibility = View.INVISIBLE
                binding.buttonContinue.visibility = View.VISIBLE

                hideKeyboard()
                binding.editTextAnswere.setText("")



            }

            // Obsługa drugiego przycisku "KONTYNUJ"
            binding.buttonContinue.setOnClickListener {
                binding.buttonContinue.visibility = View.INVISIBLE
                binding.buttonCheckAnswere.visibility = View.VISIBLE
                binding.imageViewAnswere.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, (i).toString(), Toast.LENGTH_SHORT).show()
                if(result.moveToPosition(0)){
                    polishWord = result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_POLISH))
                    binding.textViewWords.text = polishWord
                    englishWord = result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ENGLISH))

                    val id = resources.getIdentifier(result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "drawable", packageName )
                    binding.imageViewWord.setImageResource(id)

                    //                MediaPlayer.create(this,result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ID_SOUND)).toInt()).start()

                }
                else{
                    binding.buttonCheckAnswere.visibility = View.INVISIBLE
                    binding.editTextAnswere.visibility = View.INVISIBLE
                    binding.imageButtonSound.visibility = View.INVISIBLE
                    binding.imageViewWord.setImageResource(R.drawable.finish_lesson)
                    binding.textViewWords.text = "KONIEC LEKCJI"
                    binding.textViewWords.textSize = 40f
                    binding.textViewWords.setTextColor(Color.parseColor("#C1B443"))
                    binding.buttonIDontKnow.visibility = View.INVISIBLE
                    binding.buttonFinishLesson.visibility = View.VISIBLE
                    MediaPlayer.create(this, R.raw.finish_lesson).start()
                }
            }

            binding.imageButtonSound.setOnClickListener{
                val id = resources.getIdentifier(result.getString(result.getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "raw", packageName )
                MediaPlayer.create(this,id).start()
            }

            // Obsługa przycisku "NIE WIEM"
            binding.buttonIDontKnow.setOnClickListener {
                MediaPlayer.create(this,R.raw.duolingo_3).start()
                binding.textViewWords.text = "$polishWord - $englishWord"
                binding.imageViewAnswere.setImageResource(R.drawable.answere_2)
                binding.imageViewAnswere.visibility = View.VISIBLE
                binding.buttonCheckAnswere.visibility = View.INVISIBLE
                binding.buttonContinue.visibility = View.VISIBLE

            }

            // Obsługa przycisku "Zakończ lekcje", któy wyświetla się po zakończeniu lekcji
            binding.buttonFinishLesson.setOnClickListener {
                finish() // Kończy działanie bierzącej aktywności i powraca do głownego MENU
            }

//        result.close()
//        db.close()

        }

//        for (x_ in m){
//            for(x in x_){
//                str += x
//            }
//        }
//
//        Toast.makeText(applicationContext, m[0][0].toString(), Toast.LENGTH_SHORT).show()
//        while(true){
//
//            result.move(1)
//            result.moveToFirst()
//            // Jesli Skończą sie wszystkie słówka
//            if(true){
//                break
//            }
//        }


//        db.close()








//--------------------------------------------------------------------------------------------------------
    }
}