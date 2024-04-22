package com.gasstation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gasstation.R
import com.gasstation.domain.model.DistanceType
import com.gasstation.domain.model.GasStationType
import com.gasstation.domain.model.MapType
import com.gasstation.domain.model.OilType
import com.gasstation.domain.model.SettingType
import com.gasstation.domain.model.SortType
import com.gasstation.ui.component.SettingDetailItem
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorGray
import com.gasstation.ui.theme.ColorGray4
import com.gasstation.ui.theme.ColorYellow
import com.gasstation.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    menu: String,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()

    Column(modifier = Modifier.background(ColorGray)) {
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
                IconButton(
                    colors = IconButtonColors(
                        contentColor = ColorYellow,
                        containerColor = ColorBlack,
                        disabledContainerColor = ColorGray,
                        disabledContentColor = ColorGray
                    ), onClick = {
                        navController.popBackStack()
                    }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
        )

        var details = listOf<String>()
        var settingType = SettingType.DISTANCE_TYPE
        when (menu) {
            stringResource(id = R.string.find_distance) -> {
                details = DistanceType.entries.map { it.distance }
                settingType = SettingType.DISTANCE_TYPE
            }

            stringResource(id = R.string.oil_type) -> {
                details = OilType.entries.map { it.oil }
                settingType = SettingType.OIL_TYPE
            }

            stringResource(id = R.string.gas_station_type) -> {
                details = GasStationType.entries.map { it.gasStation }
                settingType = SettingType.GAS_STATION_TYPE
            }

            stringResource(id = R.string.sort_type) -> {
                details = SortType.entries.map { it.sortType }
                settingType = SettingType.SORT_TYPE
            }

            stringResource(id = R.string.map_type) -> {
                details = MapType.entries.map { it.map }
                settingType = SettingType.MAP_TYPE
            }
        }
        val currentSettingType = homeViewModel.getCurrentSettingType(settingType)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(ColorGray4)
        ) {
            items(details) { type ->
                SettingDetailItem(type, currentSettingType == type) {
                    homeViewModel.saveSetting(settingType, type)
                    navController.popBackStack()
                }
            }
        }
    }
}


