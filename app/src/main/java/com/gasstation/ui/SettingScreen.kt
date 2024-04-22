package com.gasstation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gasstation.R
import com.gasstation.ui.component.SettingItem
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorGray4
import com.gasstation.ui.theme.ColorYellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.background(ColorGray4)) {
        TopAppBar(
            title = { },
            colors = TopAppBarColors(
                containerColor = ColorBlack,
                titleContentColor = ColorYellow,
                actionIconContentColor = ColorYellow,
                navigationIconContentColor = ColorBlack,
                scrolledContainerColor = ColorBlack
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
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.find_setting),
                style = MaterialTheme.typography.titleSmall,
                color = ColorBlack
            )
        }
        val menu = listOf(*stringArrayResource(id = R.array.setting_detail_menu))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(ColorGray4)
        ) {
            items(menu) { setting ->
                SettingItem(setting = setting) {
                    navController.navigate("home/setting/detail/${setting}")
                }
            }
        }
    }
}
