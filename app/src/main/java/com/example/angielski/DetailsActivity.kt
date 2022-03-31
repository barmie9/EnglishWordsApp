package com.example.angielski

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

// --------------   OPIS TABELI ------------
object TableInfo: BaseColumns{
    const val TABLE_NAME = "My_table"
    const val TABLE_COLUMN_POLISH= "polish_word"
    const val TABLE_COLUMN_ENGLISH = "english_word"
    const val TABLE_COLUMN_ID_PICTURE_SOUND = "id_picture_sound"
    const val TABLE_COLUMN_LEARNED_PL_TO_ANG = "learned_pl_to_ang"
    const val TABLE_COLUMN_LEARNED_ANG_TO_PL = "learned_ang_to_pl"
    const val TABLE_COLUMN_DATE = "date_1"
    const val TABLE_COLUMN_REPLAY_PL_TO_ANG = "replay_time_pl_to_ang"
    const val TABLE_COLUMN_REPLAY_ANG_TO_PL = "replay_time_ang_to_pl"
}

//  ---------   PODSTAWOWE KOMENDY SQL  ---------------
object BasicCommand{
    const val SQL_CREATE_TABLE: String =
        "CREATE TABLE ${TableInfo.TABLE_NAME} ("+
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${TableInfo.TABLE_COLUMN_POLISH} TEXT NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_ENGLISH} TEXT NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_ID_PICTURE_SOUND} INTEGER NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_LEARNED_PL_TO_ANG} TEXT NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_LEARNED_ANG_TO_PL} TEXT NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_DATE} TEXT ,"+
                "${TableInfo.TABLE_COLUMN_REPLAY_PL_TO_ANG} TEXT ,"+
                "${TableInfo.TABLE_COLUMN_REPLAY_ANG_TO_PL} TEXT );"


    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
}

//  ----------------   WYPELNIENIE DANYCH BAZY  ------------------------
object fillDataBase{
    val value_1 = ContentValues()


    fun fill(context: Context):Int { // Return: Number of words in db
        val dbHelper = DataBaseHelper(context)
        val db = dbHelper.writableDatabase

        //     -----------------    USUWA I TWORZY TA SAMA TABELE -----------------------
        db.execSQL(BasicCommand.SQL_DELETE_TABLE)
        db.execSQL(BasicCommand.SQL_CREATE_TABLE)
        //  -------------------------------------------------------------------------------

        val saveToastInfo = Toast.makeText(context, "Baza Słówek Utworzona", Toast.LENGTH_SHORT)
        saveToastInfo.show()



        val beginQuery = "INSERT INTO ${TableInfo.TABLE_NAME} " +
                "(${TableInfo.TABLE_COLUMN_POLISH} , ${TableInfo.TABLE_COLUMN_ENGLISH} ,${TableInfo.TABLE_COLUMN_ID_PICTURE_SOUND} ,${TableInfo.TABLE_COLUMN_LEARNED_PL_TO_ANG} ,${TableInfo.TABLE_COLUMN_LEARNED_ANG_TO_PL} ) VALUES "

        Log.d("TAG",beginQuery+"" +
                "('zły','angry','angry','f','f'),\n" +
                "('kołdra/koc','coverlet','coverlet','f','f'),\n" +
                "('zasłona','curtain','curtain','f','f'),\n" +
                "('latarka','flashlight','flashlight','f','f'),\n" +
                "('panele podłogowe','floor panels','floor_panels','f','f'),\n" +
                "('szczęśliwy','happy','happy','f','f'),\n" +
                "('maska','mask','mask','f','f'),\n" +
                "('spodnie','pants','pants','f','f'),\n" +
                "('błyszczący','shiny','shiny','f','f'),\n" +
                "('skarpetki','socks','socks','f','f');")

        db.execSQL(beginQuery+"" +
                "('zły','angry','angry','f','f'),\n" +
                "('kołdra/koc','coverlet','coverlet','f','f'),\n" +
                "('zasłona','curtain','curtain','f','f'),\n" +
                "('latarka','flashlight','flashlight','f','f'),\n" +
                "('panele podłogowe','floor panels','floor_panels','f','f'),\n" +
                "('szczęśliwy','happy','happy','f','f'),\n" +
                "('maska','mask','mask','f','f'),\n" +
                "('spodnie','pants','pants','f','f'),\n" +
                "('błyszczący','shiny','shiny','f','f'),\n" +
                "('skarpetki','socks','socks','f','f');")


        val queueSelectNumWords = "SELECT COUNT() AS NUMBER FROM ${TableInfo.TABLE_NAME} ;"
        val result = db.rawQuery(queueSelectNumWords,null)
//        db.close()

        // ----- Return number of words in db ------
        if(result.moveToFirst()){
            var numStringRes = 0
            numStringRes =  result.getInt(0)
//            result.close()
            return  numStringRes
        }
        else{
//            result.close()
            return  0
        }

    }
}

// -----------------------------------------------
class DataBaseHelper(context: Context): SQLiteOpenHelper(context,TableInfo.TABLE_NAME, null, 1){
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(BasicCommand.SQL_DELETE_TABLE)
        p0?.execSQL(BasicCommand.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(BasicCommand.SQL_DELETE_TABLE)
        onCreate(p0)
    }

}

// Funkcje pozwalające ukrywać/zamykać otwartą klawiatue
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

