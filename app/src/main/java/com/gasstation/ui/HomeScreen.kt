@file:OptIn(ExperimentalMaterial3Api::class)

package com.gasstation.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    Scaffold(topBar = { HomeHeader() }) {
        HomeHeader()
    }
}

@Composable
fun HomeHeader() {
    TopAppBar(
        title = { Text("거리순") },
        navigationIcon = {},
        actions = {
            IconButton(onClick = {

            }) {
                Icon(Icons.Filled.Settings, "Settings")
            }
        }
    )
}