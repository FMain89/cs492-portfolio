/*
Assignment 5: Mobile Treasure Hunt

StartPage.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/

package com.example.treasurehuntapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartPage(onStartClicked: () -> Unit) {
    // Column layout to arrange elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the maximum size of the parent
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Center elements horizontally
        verticalArrangement = Arrangement.Center // Center elements vertically
    ) {
        // Welcome text for the game
        Text("Welcome to the Treasure Hunt Game!")
        Spacer(modifier = Modifier.height(16.dp))

        // Game rules title
        Text("Game Rules:", modifier = Modifier.align(Alignment.Start))
        // Text block for game rules, wrapped in a scrollable container
        Text(
            "1. You will be given a series of clues.\n" +
                    "2. Solve each clue to find the next location.\n" +
                    "3. Use the 'Found It!' button when you think you have found the location.\n" +
                    "4. The game ends when you find the final treasure.\n",
            modifier = Modifier
                .fillMaxWidth() // Fill the maximum width of the parent
                .verticalScroll(rememberScrollState()) // Make the text scrollable if it overflows
        )
        Spacer(modifier = Modifier.height(16.dp)) // Spacer to add space between elements

        // Start button to begin the treasure hunt game
        Button(onClick = { onStartClicked() }) {
            Text("Start")
        }
    }
}