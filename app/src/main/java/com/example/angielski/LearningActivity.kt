package com.example.angielski

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import com.example.angielski.databinding.ActivityLearningBinding
import com.example.angielski.databinding.ActivityMainBinding
import java.util.*

class LearningActivity : AppCompatActivity() {

    // ----- How to get to id layout -----
    private lateinit var binding: ActivityLearningBinding


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        // ----- Tag for the tests -----
//        val TAG = "TAG"

        // ----- How to get to id layout -----
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----- Take data from the main activity, REPEAT OR LEARNING -----
        val isLearning = intent.getBooleanExtra("is_learning",false)
//        val isLearning = getIntent().getBooleanExtra("is_learning",false)

        // ----- Get local file (variable) "Settings", to read an edit "editor"-----
        val sharedPref: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        // ----- Open database -----
        val dbHelper = DataBaseHelper(applicationContext)
        val db = dbHelper.readableDatabase
        val query1: String
        val query2: String
        val query: Array<String> = arrayOf("","")

        // ----- Create a few query to db, to repeating or learning  -----
        if(isLearning){ //learning
            query1 = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_LEARNED_PL_TO_ANG} = 'f' "
            query2 = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_LEARNED_ANG_TO_PL} = 'f' "
            query[0] = query1
            query[1] = query2
        }
        else{ //repeating
            query1 = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_LEARNED_PL_TO_ANG} = 't' "+
                    " AND DATETIME('now', 'localtime') >= DATETIME(${TableInfo.TABLE_COLUMN_DATE},${TableInfo.TABLE_COLUMN_REPLAY_PL_TO_ANG}); "
            query2 = "SELECT * FROM " + TableInfo.TABLE_NAME +
                    " WHERE ${TableInfo.TABLE_COLUMN_LEARNED_ANG_TO_PL} = 't' "+
                    " AND DATETIME('now', 'localtime') >= DATETIME(${TableInfo.TABLE_COLUMN_DATE},${TableInfo.TABLE_COLUMN_REPLAY_ANG_TO_PL}); "

            query[0] = query1
            query[1] = query2

        }


        // ----- get two tables from db for the ang->pl and pl->ang -----
        val result = Array(2){i -> db.rawQuery(query[i],null)}


        // ----- create a few variables for proper operation -----
        var englishWord = ""
        var polishWord = ""
        var answere = ""
        var wordsCounter = arrayOf(0,0) // Counter of correct  words
        val wordsToLearnName = arrayOf("WordsToLearnPlToEng","WordsToLearnEngToPl")
        val wordsToLearn  = arrayOf(sharedPref.getInt("WordsToLearnPlToEng",9),sharedPref.getInt("WordsToLearnEngToPl",9)) // number of words to learn, for index 0 - pl->ang and 1 ang->pl
        var ii =0 // Actual words (Column) - Words
        var jj =0 // ang-> pl or pl-> ang


        // ----- Create and fill a queue. Needed to repeat words until they are learned -----
        var myQueue: Queue<Array<Int>> = LinkedList<Array<Int>>()
//        var exampleTab: Array<Int> = arrayOf(0,1)
        if(isLearning){
            for(i in 0..1)
                for(j in 0 until wordsToLearn[i])
                    myQueue.add(arrayOf(i,j))
        }
        // -----
        else{
            for(i in 0..1)
                for(j in 0 until result[i].count)
                    myQueue.add(arrayOf(i,j))
        }



        // ----- if queue is not empty fill the initial layout elements -----
        if(!myQueue.isEmpty()){

            val myOneTab = myQueue.poll()
            ii = myOneTab[1]
            jj = myOneTab[0]

            // ----- Fill data the first time -----
            if(result[0].moveToFirst() ){
                polishWord =  result[0].getString(result[0].getColumnIndex(TableInfo.TABLE_COLUMN_POLISH))
                binding.textViewWords.text = polishWord
                englishWord = result[0].getString(result[0].getColumnIndex(TableInfo.TABLE_COLUMN_ENGLISH))
                val id = resources.getIdentifier(result[0].getString(result[0].getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "drawable", packageName )
                binding.imageViewWord.setImageResource(id)
            }
            else // If data pl->ang does not exist
            {
                if(result[1].moveToFirst()){
                    polishWord =  result[1].getString(result[1].getColumnIndex(TableInfo.TABLE_COLUMN_POLISH))
                    binding.textViewWords.text = polishWord
                    englishWord = result[1].getString(result[1].getColumnIndex(TableInfo.TABLE_COLUMN_ENGLISH))
                    val id = resources.getIdentifier(result[1].getString(result[1].getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "drawable", packageName )
                    binding.imageViewWord.setImageResource(id)
                }
                else // ----- Finish lesson -----
                    setViewFinishLesson()
            }
        }
        else{ // ----- Finish lesson -----
            setViewFinishLesson()
        }





        // ----- Support for the "Check" button -----
        binding.buttonCheckAnswere.setOnClickListener {

            // ----- Get data from user -----
            answere = binding.editTextAnswere.text.toString()

            if(jj ==  0)
                binding.textViewWords.text = "$polishWord - $englishWord"
            else
                binding.textViewWords.text = "$englishWord - $polishWord"

            // ----- If correct data -----
            if(checkAnswere.check(englishWord,answere)){

                // ----- Increment  wordsCounter and update  LOCAL variable words to learn (ang->pl and pl->ang) -----
                wordsCounter[jj]++
                editor.putInt(wordsToLearnName[jj], wordsToLearn[jj] - wordsCounter[jj])

                // ----- Increment total number words  by 0.5 in LOCAL variable -----
                editor.putFloat("TotalLearnedWords", 0.5f + (sharedPref.getFloat("TotalLearnedWords",0f)))
                editor.apply()

                // ----- Positive sound effect -----
                MediaPlayer.create(this,R.raw.duolingo_1).start()
                binding.imageViewAnswere.setImageResource(R.drawable.answere_1)


                // ----- If the words from eng->pl or pl->ang learned, update the database -----
                val replay = arrayOf(result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_REPLAY_PL_TO_ANG)),
                    result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_REPLAY_PL_TO_ANG)))
                val tableColumnIsLearned = arrayOf("learned_pl_to_ang","learned_ang_to_pl")
                val tableColumnReplay = arrayOf("replay_time_pl_to_ang","replay_time_ang_to_pl")
                val newReplay: String
                var newIsLearned = "'t'"

                // ----- Change the time for the next replay. If all replays are done set column (in db) is learned "ct" -----
                when (replay[jj]){
                    "+1 second" -> newReplay = "'+2 second'"
                    "+2 second" -> newReplay = "'+3 second'"
                    "+3 second" -> newReplay = "'+4 second'"
                    "+4 second" ->{
                        newIsLearned = "'ct'"
                        newReplay = "'+0 second'"
                    }
                    else -> {
                        newReplay = "'+1 second'"
                    }


                }


                // ----- Update db -----
                val queryUpdate = "UPDATE ${TableInfo.TABLE_NAME} "+
                        "SET ${tableColumnIsLearned[jj]} = ${newIsLearned}, " +
                        "${TableInfo.TABLE_COLUMN_DATE} = DATETIME('now', 'localtime'), " +
                        "${tableColumnReplay[jj]} =  $newReplay "+
                        " WHERE ${BaseColumns._ID} = ${result[jj].getString(result[jj].getColumnIndex(BaseColumns._ID))};" //_id  ${result.getString(result.getColumnIndex(BaseColumns._ID))}

                db.execSQL(queryUpdate)


            }
            // ----- If answer is wrong -----
            else
            {
                myQueue.add(arrayOf(jj,ii))
                MediaPlayer.create(this,R.raw.duolingo_2).start()
                binding.imageViewAnswere.setImageResource(R.drawable.answere_2)
            }
            binding.imageViewAnswere.visibility = View.VISIBLE
            binding.buttonCheckAnswere.visibility = View.INVISIBLE
            binding.buttonContinue.visibility = View.VISIBLE

            hideKeyboard()

        }

        // ----- Support for the second "CONTINUE" button -----
        binding.buttonContinue.setOnClickListener {
            // ----- Clear editTextAnswere -----
            binding.editTextAnswere.setText("")

            // ----- If queue is empty then Finish lesson -----
            if(myQueue.isEmpty()){
                setViewFinishLesson()
            }
            else{ // Otherwise, load the data
                val myTab = myQueue.poll()
                ii = myTab[1]
                jj = myTab[0]

                binding.buttonContinue.visibility = View.INVISIBLE
                binding.buttonCheckAnswere.visibility = View.VISIBLE
                binding.imageViewAnswere.visibility = View.INVISIBLE
                if(result[jj].moveToPosition(ii)){
                    polishWord = result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_POLISH))
                    englishWord = result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_ENGLISH))

                    // ----- swap values of variables if the translation eng->pl -----
                    if (jj == 1){
                        var buf = polishWord
                        polishWord = englishWord
                        englishWord = buf
                    }

                    // ----- Set the textView  with polish or english word -----
                    binding.textViewWords.text = polishWord

                    val id = resources.getIdentifier(result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "drawable", packageName )
                    binding.imageViewWord.setImageResource(id)

                }
            }

        }

        // ----- The sound of word -----
        binding.imageButtonSound.setOnClickListener{
            val id = resources.getIdentifier(result[jj].getString(result[jj].getColumnIndex(TableInfo.TABLE_COLUMN_ID_PICTURE)), "raw", packageName )
            MediaPlayer.create(this,id).start()
        }

        // ----- Support for the second "IDontKnow" button
        binding.buttonIDontKnow.setOnClickListener {
            MediaPlayer.create(this,R.raw.duolingo_3).start()
            binding.textViewWords.text = "$polishWord - $englishWord"
            binding.imageViewAnswere.setImageResource(R.drawable.answere_2)
            binding.imageViewAnswere.visibility = View.VISIBLE
            binding.buttonCheckAnswere.visibility = View.INVISIBLE
            binding.buttonContinue.visibility = View.VISIBLE

        }

        // ----- Support for the "End lessons" button that is displayed after the end of the lesson -----
        binding.buttonFinishLesson.setOnClickListener {
            finish() // The end of this activity
            result[0].close() // Close the cursor
            result[1].close() // Close the cursor
            db.close() // Close DataBase
        }



//        db.close()








//--------------------------------------------------------------------------------------------------------
    }


    fun setViewFinishLesson(){
        binding.buttonCheckAnswere.visibility = View.INVISIBLE
        binding.editTextAnswere.visibility = View.INVISIBLE
        binding.imageButtonSound.visibility = View.INVISIBLE
        binding.imageViewAnswere.visibility = View.INVISIBLE
        binding.imageViewWord.setImageResource(R.drawable.finish_lesson)
        binding.textViewWords.text = "KONIEC LEKCJI"
        binding.textViewWords.textSize = 40f
        binding.textViewWords.setTextColor(Color.parseColor("#C1B443"))
        binding.buttonIDontKnow.visibility = View.INVISIBLE
        binding.buttonFinishLesson.visibility = View.VISIBLE
        binding.buttonContinue.visibility = View.INVISIBLE
        MediaPlayer.create(this, R.raw.finish_lesson).start()
    }
}