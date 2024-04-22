package com.gasstation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gasstation.R
import com.gasstation.common.ResultWrapper
import com.gasstation.domain.model.Coords
import com.gasstation.ui.component.CurrentAddresssText
import com.gasstation.ui.component.GasStationItem
import com.gasstation.ui.navigation.NavTarget
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorYellow
import com.gasstation.viewmodel.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(scaffoldState: ScaffoldState, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    var openRationaleDialog by remember { mutableStateOf(false) }
    val permissionStates = rememberMultiplePermissionsState(
        permissions = getRequirePermissions()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorYellow)
    ) {
        HomeTopAppBar(
            navController = navController,
            sortType = homeViewModel.getSortType(),
            onChangeSortType = { homeViewModel.changeSortType() })
        RequestPermission(scaffoldState)
        if (permissionStates.allPermissionsGranted) {
            val currentAddress by homeViewModel.currentAddress.collectAsState()
            if (currentAddress is ResultWrapper.Success) {
                CurrentAddresssText(address = currentAddress.takeValueOrThrow())
            }
            when (val response = homeViewModel.gasStationsResult.collectAsState().value) {
                is ResultWrapper.Success -> {
                    val scrollState = rememberLazyListState()
                    val gasStations = response.value.OIL
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1F),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Timber.i("homeScreen() homeViewModel.getOilType() = " + homeViewModel.getOilType())
                        items(gasStations) { item ->
                            GasStationItem(gasStations = item, homeViewModel.getOilType())
                        }
                    }
                }

                else -> {

                }

            }

        } else {
            Card(shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .shadow(20.dp)
                    .shadow(elevation = 10.dp),
                onClick = { }
            ) {
                Text(
                    text = stringResource(id = R.string.auth_rationale_msg),
                    modifier = Modifier.clickable {
                        scope.launch {
                            openRationaleDialog = true
                        }
                    })
            }
        }
    }

    if (openRationaleDialog) {
        AlertDialog(
            modifier = Modifier.padding(horizontal = 12.dp),
            onDismissRequest = { openRationaleDialog = false },
            title = {
                Text(text = stringResource(id = R.string.auth_rationale_msg))
            },
            text = {
                Text(stringResource(id = R.string.auth_rationale_msg))
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        permissionStates.launchMultiplePermissionRequest()
                        openRationaleDialog = false
                    }
                }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                Button(onClick = { openRationaleDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}


@Composable
fun RequestPermission(scaffoldState: ScaffoldState) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val scope = rememberCoroutineScope()
    RequestLocationPermission(
        onPermissionGranted = {
            getCurrentLocation(
                context = context,
                fusedLocationProviderClient = fusedLocationProviderClient,
                onGetCurrentLocationSuccess = {
                    Timber.i("latitude = " + it.first + " , longitude = " + it.second)
                    scope.launch {
                        homeViewModel.getCurrentAddress(it.second, it.first, Coords.WGS84.name)
                        homeViewModel.getGasStationList(
                            it.second,
                            it.first,
                            Coords.WGS84.name,
                            Coords.KTM.name
                        )
                    }
                },
                onGetCurrentLocationFailed = {
                    popupSnackBar(scope, scaffoldState, "위치정보를 가지고 오지 못했습니다.")
                    Timber.i(it.localizedMessage ?: "Error Getting Current Location")
                }
            )
        }, onPermissionDenied = {
            popupSnackBar(scope, scaffoldState, "주유주유소를 이용하시려면 위치 권한을 허용해주세요.")
        }, onPermissionsRevoked = {
            popupSnackBar(scope, scaffoldState, "주유주유소를 이용하시려면 위치 권한을 허용해주세요.")
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    navController: NavHostController,
    sortType: String,
    modifier: Modifier = Modifier,
    onChangeSortType: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = sortType, modifier = Modifier.clickable {
                onChangeSortType()
            })
        },
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
                navController.navigate(NavTarget.Setting.route)
            }) {
                Icon(Icons.Filled.Settings, "Setting")
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
) {
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    if (areLocationPermissionsGranted(context)) {
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            onGetCurrentLocationFailed(exception)
        }
    }
}

private fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = permissionState) {
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        Timber.i("onPermissionDenied = " + allPermissionsRevoked)
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

fun getRequirePermissions(): List<String> {
    return listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}
