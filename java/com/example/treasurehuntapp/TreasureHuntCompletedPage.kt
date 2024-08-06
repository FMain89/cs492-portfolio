/*
Assignment 5: Mobile Treasure Hunt

TreasureHuntCompletedPage.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/
package com.example.treasurehuntapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TreasureHuntCompletedPage(onHomeClicked: () -> Unit, totalElapsedTime: Long) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Congratulations! You've completed the treasure hunt.")
        Spacer(modifier = Modifier.height(16.dp))
        val minutes = (totalElapsedTime / 60000)
        val seconds = (totalElapsedTime / 1000) % 60
        val milliseconds = (totalElapsedTime % 1000) / 10 // Showing only two decimal places for milliseconds
        Text("Total elapsed time: ${String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onHomeClicked) {
            Text("Home")
        }
    }
}