package com.example.angielski

import android.util.Log

object checkAnswere{
    fun check(answere: String, correct:String): Boolean{
        var lAnswer = answere.lowercase().normalize()
        var lCorrect = correct.lowercase().normalize()
//        Log.d("TAG", "${lAnswer} , <${lCorrect}> ")
        if(lCorrect.contains("/") ) // To be improved
            return ( lCorrect.contains(lAnswer)  )
        else
            return ( lCorrect.contains(lAnswer)  && (lAnswer.length >= lCorrect.length ) )


    }

    //Replace all polish diacritics
    fun String.normalize(): String {
        val original = arrayOf("Ą", "ą", "Ć", "ć", "Ę", "ę", "Ł", "ł", "Ń", "ń", "Ó", "ó", "Ś", "ś", "Ź", "ź", "Ż", "ż")
        val normalized = arrayOf("A", "a", "C", "c", "E", "e", "L", "l", "N", "n", "O", "o", "S", "s", "Z", "z", "Z", "z")

        return this.map { char ->
            val index = original.indexOf(char.toString())
            if (index >= 0) normalized[index] else char
        }.joinToString("")
    }
}