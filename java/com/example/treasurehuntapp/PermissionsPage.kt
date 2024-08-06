/*
Assignment 5: Mobile Treasure Hunt

PermissionsPage.kt

Name: Freddie Main III
Oregon State University
Email: mainf@oregonstate.edu
Course: CS492 Mobile Software Development
*/

package com.example.treasurehuntapp

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log

@Composable
fun PermissionsPage(onPermissionsGranted: () -> Unit) {
    // List of permissions to be requested
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // State to track if permissions are denied
    var permissionsDenied by remember { mutableStateOf(false) }

    // Launcher to request multiple permissions
    // rememberLauncherForActivityResult is used to launch an activity and get a result back
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsGranted ->
        // Callback to handle the result of the permission request
        if (permissionsGranted.all { it.value }) {
            // All permissions granted, navigate to the StartPage
            Log.d("PermissionsPage", "All permissions granted")
            onPermissionsGranted()
        } else {
            // Some permissions denied
            permissionsDenied = true
            permissionsGranted.forEach { (permission, granted) ->
                if (!granted) {
                    Log.d("PermissionsPage", "$permission denied")
                }
            }
        }
    }

    // UI layout
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the maximum size of the parent
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.Center // Center vertically
    ) {
        // Text to prompt the user to grant permissions
        Text("Grant location permissions to start the treasure hunt")
        Spacer(modifier = Modifier.height(16.dp))

        // Row to arrange buttons horizontally
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button to request permissions
            Button(onClick = {
                permissionsDenied = false
                launcher.launch(permissions)
            }) {
                Text("Allow")
            }
            // Button to cancel and show the permissions required message
            Button(onClick = { permissionsDenied = true }) {
                Text("Cancel")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // If permissions are denied, show a message
        if (permissionsDenied) {
            Text("Permissions are required to play the game.")
        }
    }
}