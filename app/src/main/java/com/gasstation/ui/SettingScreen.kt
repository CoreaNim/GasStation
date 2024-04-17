package com.gasstation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun SettingScreen(scaffoldState: ScaffoldState, navController: NavHostController) {
    Card(shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .shadow(20.dp)
            .shadow(elevation = 10.dp),
        onClick = { }
    ) {
        Text("Settings1")
        Text("Settings2")
        Text("Settings3")
        Text("Settings4")
        Text("Settings5")
        Text("Settings6")
        Text("Settings7")
    }
}
