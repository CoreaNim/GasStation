package com.gasstation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gasstation.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.background(Color.Gray)) {
        TopAppBar(
            title = { },
            colors = TopAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.Yellow,
                actionIconContentColor = Color.Yellow,
                navigationIconContentColor = Color.Black,
                scrolledContainerColor = Color.Black
            ),
            modifier = modifier,
            navigationIcon = {
            },
            actions = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.Close, "Close")
                }
            }
        )
        Column {
            Text(text = stringResource(id = R.string.find_setting))
        }
        val menu = listOf(*stringArrayResource(id = R.array.setting_detail_menu))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Color.White)
        ) {
            items(menu) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(text = it, modifier = Modifier.clickable {
                        navController.navigate("home/setting/detail/${it}")
                    })
                }
                Divider(modifier = Modifier, color = Color.Gray, thickness = 0.5.dp)
            }
        }
    }
}
