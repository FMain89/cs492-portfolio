/*
Assignment 5: Mobile Treasure Hunt

CluePage.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/

package com.example.treasurehuntapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.compose.material3.MaterialTheme

@SuppressLint("MissingPermission")
@Composable
fun CluePage(
    onFoundItClicked: () -> Unit,
    onQuitClicked: () -> Unit,
    currentClueIndex: Int,
    updateClueIndex: () -> Unit,
    elapsedTimeMillis: Long,
    updateElapsedTimeMillis: (Long) -> Unit,
    timerPaused: Boolean
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var location by remember { mutableStateOf<Location?>(null) }
    val clues = remember { loadClues(context) }
    val currentClue = clues.getOrNull(currentClueIndex)
    var showHint by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(timerPaused) {
        if (!timerPaused) {
            val startTime = System.currentTimeMillis() - elapsedTimeMillis
            val locationRequest = LocationRequest.create().apply {
                interval = 1000
                fastestInterval = 500
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    location = locationResult.lastLocation
                    Log.d("CluePage", "Current location: $location")
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

            while (true) {
                delay(10L)
                updateElapsedTimeMillis(System.currentTimeMillis() - startTime)
            }
        }
    }

    fun handleFoundIt() {
        if (isLocationCorrect(location, currentClue?.latitude ?: 0.0, currentClue?.longitude ?: 0.0)) {
            Log.d("CluePage", "Location is correct")
            message = "Location is correct!"
            onFoundItClicked()
        } else {
            Log.d("CluePage", "Location is incorrect")
            message = "Location is incorrect. Try again."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        currentClue?.let { clue ->
            Text("Clue: ${clue.clue}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showHint = true }) {
                Text("Hint")
            }
            if (showHint) {
                Text("Hint: ${clue.hint}")
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            TimerDisplay(elapsedTimeMillis)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                handleFoundIt()
            }) {
                Text("Found It!")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onQuitClicked) {
                Text("Quit")
            }
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(message)
            }
        } ?: run {
            Text("No more clues available.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onQuitClicked) {
                Text("Quit")
            }
        }
    }
}

// Function to calculate distance between two lat/lon points using the Haversine formula
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371e3 // meters
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}

// Function to check if the current location is within a threshold distance of the target location
fun isLocationCorrect(location: Location?, targetLat: Double, targetLon: Double): Boolean {
    val thresholdDistance = 25 // meters
    location?.let {
        val distance = calculateDistance(it.latitude, it.longitude, targetLat, targetLon)
        return distance <= thresholdDistance
    }
    return false
}

// Function to display the timer
@Composable
fun TimerDisplay(elapsedTimeMillis: Long) {
    val minutes = (elapsedTimeMillis / 60000)
    val seconds = (elapsedTimeMillis / 1000) % 60
    val milliseconds = (elapsedTimeMillis % 1000) / 10 // Showing only two decimal places for milliseconds
    Text(
        text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds),
        style = MaterialTheme.typography.displayMedium, // Typography style for the timer text
        color = MaterialTheme.colorScheme.primary // Color for the timer text
    )
}