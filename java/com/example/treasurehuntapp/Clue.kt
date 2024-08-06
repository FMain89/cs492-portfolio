/*
Assignment 5: Mobile Treasure Hunt

Clue.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/
package com.example.treasurehuntapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class to represent a Clue with its attributes
data class Clue(
    val clue: String,
    val hint: String,
    val latitude: Double,
    val longitude: Double,
    val info: String
)

// Function to load clues from a JSON resource file
fun loadClues(context: Context): List<Clue> {
    val inputStream = context.resources.openRawResource(R.raw.clues)
    val json = inputStream.bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<Clue>>() {}.type
    return Gson().fromJson(json, type)
}