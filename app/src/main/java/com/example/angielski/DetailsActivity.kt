package com.example.angielski

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

// --------------   OPIS TABELI ------------
object TableInfo: BaseColumns{
    const val TABLE_NAME = "My_table"
    const val TABLE_COLUMN_POLISH= "polish_word"
    const val TABLE_COLUMN_ENGLISH = "english_word"
    const val TABLE_COLUMN_ID_PICTURE = "id_picture"
    const val TABLE_COLUMN_ID_SOUND = "id_sound"
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
                "${TableInfo.TABLE_COLUMN_ID_PICTURE} INTEGER NOT NULL,"+
                "${TableInfo.TABLE_COLUMN_ID_SOUND} INTEGER ,"+
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


    fun fill(context: Context){
        val dbHelper = DataBaseHelper(context)
        val db = dbHelper.writableDatabase

        //     -----------------    USUWA I TWORZY TA SAMA TABELE -----------------------
        db.execSQL(BasicCommand.SQL_DELETE_TABLE)
        db.execSQL(BasicCommand.SQL_CREATE_TABLE)
        //  -------------------------------------------------------------------------------

        val saveToastInfo = Toast.makeText(context, "Baza Utworzona", Toast.LENGTH_SHORT)
        saveToastInfo.show()

        value_1.put("polish_word", "zły")
        value_1.put("english_word", "angry")
        value_1.put("id_picture", "angry")
//        value_1.put("id_sound", R.raw.angry.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "kołdra/koc")
        value_1.put("english_word", "coverlet")
        value_1.put("id_picture", "coverlet")
//        value_1.put("id_sound", R.raw.coverlet.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "zasłona")
        value_1.put("english_word", "curtain")
        value_1.put("id_picture", "curtain")
//        value_1.put("id_sound", R.raw.curtain.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "latarka")
        value_1.put("english_word", "flashlight")
        value_1.put("id_picture", "flashlight")
//        value_1.put("id_sound", R.raw.flashlight.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "panele podłogowe")
        value_1.put("english_word", "floor panels")
        value_1.put("id_picture", "floor_panels")
//        value_1.put("id_sound", R.raw.floor_panels.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "szczęśliwy")
        value_1.put("english_word", "happy")
        value_1.put("id_picture", "happy")
//        value_1.put("id_sound", R.raw.happy.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "maska")
        value_1.put("english_word", "mask")
        value_1.put("id_picture", "mask")
//        value_1.put("id_sound", R.raw.mask.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "spodnie")
        value_1.put("english_word", "pants")
        value_1.put("id_picture", "pants")
//        value_1.put("id_sound", R.raw.pants.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "błyszczący")
        value_1.put("english_word", "shiny")
        value_1.put("id_picture", "shiny")
//        value_1.put("id_sound", R.raw.shiny.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )
        //   --------------------------------
        value_1.clear()
        value_1.put("polish_word", "skarpetki")
        value_1.put("english_word", "socks")
        value_1.put("id_picture", "socks")
//        value_1.put("id_sound", R.raw.socks.toString())
        value_1.put("learned_pl_to_ang", "f")
        value_1.put("learned_ang_to_pl", "f")
        db.insertOrThrow(TableInfo.TABLE_NAME, null, value_1 )


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

