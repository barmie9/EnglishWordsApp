package com.example.angielski

object checkAnswere{
    fun check(answere: String, correct:String): Boolean{

        return answere.lowercase() == correct.lowercase()

    }
}