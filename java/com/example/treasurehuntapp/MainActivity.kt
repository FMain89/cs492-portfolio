/*
Assignment 5: Mobile Treasure Hunt

MainActivity.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/

package com.example.treasurehuntapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.treasurehuntapp.ui.theme.TreasureHuntAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set the content of the activity to a composable function
            TreasureHuntAppTheme {
                // Create a NavController for managing navigation
                val navController = rememberNavController()
                var currentClueIndex by remember { mutableIntStateOf(0) }
                var elapsedTimeMillis by remember { mutableLongStateOf(0L) }
                var timerPaused by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val clues = remember { loadClues(context) }

                // Function to reset the timer and clue index
                fun resetHunt() {
                    elapsedTimeMillis = 0L
                    currentClueIndex = 0
                    timerPaused = false
                }

                // Define the navigation host for the app
                NavHost(navController = navController, startDestination = "permissions") {
                    // Define the composable destinations for the app

                    // Permissions page to request necessary permissions
                    composable("permissions") {
                        PermissionsPage {
                            // Navigate to the start page after permissions are granted
                            navController.navigate("start")
                        }
                    }

                    // Start page displaying the game title and rules
                    composable("start") {
                        StartPage {
                            // Reset the hunt and navigate to the clue page when the start button is clicked
                            resetHunt()
                            navController.navigate("clue")
                        }
                    }

                    // Clue page where the user solves clues to find locations
                    composable("clue") {
                        CluePage(
                            onFoundItClicked = {
                                timerPaused = true
                                navController.navigate("clueSolved")
                            },
                            onQuitClicked = {
                                // Reset the hunt and navigate to the start page
                                resetHunt()
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            },
                            currentClueIndex = currentClueIndex,
                            updateClueIndex = { currentClueIndex++ },
                            elapsedTimeMillis = elapsedTimeMillis,
                            updateElapsedTimeMillis = { newTime -> elapsedTimeMillis = newTime },
                            timerPaused = timerPaused
                        )
                    }

                    // Clue solved page displayed after a clue is correctly solved
                    composable("clueSolved") { backStackEntry ->
                        val clueInfo = clues.getOrNull(currentClueIndex)?.info ?: ""
                        ClueSolvedPage(
                            onContinueClicked = {
                                timerPaused = false
                                if (currentClueIndex < clues.size - 1) {
                                    currentClueIndex++
                                    navController.navigate("clue")
                                } else {
                                    navController.navigate("treasureHuntCompleted")
                                }
                            },
                            clueInfo = clueInfo,
                            totalElapsedTime = elapsedTimeMillis
                        )
                    }

                    // Treasure hunt completed page displayed after all clues are solved
                    composable("treasureHuntCompleted") {
                        TreasureHuntCompletedPage(
                            onHomeClicked = {
                                // Reset the hunt and navigate to the start page
                                resetHunt()
                                navController.navigate("start")
                            },
                            totalElapsedTime = elapsedTimeMillis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TreasureHuntAppTheme {
        Greeting("Android")
    }
}