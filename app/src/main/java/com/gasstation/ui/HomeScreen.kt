package com.gasstation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gasstation.R
import com.gasstation.common.ResultWrapper
import com.gasstation.const.Const
import com.gasstation.domain.model.Coords
import com.gasstation.domain.model.MapType
import com.gasstation.ui.component.CurrentAddresssText
import com.gasstation.ui.component.GasStationItem
import com.gasstation.ui.component.PermissionButton
import com.gasstation.ui.component.PermissionDialog
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
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    var openRationaleDialog by remember { mutableStateOf(false) }
    val permissionStates = rememberMultiplePermissionsState(
        permissions = getRequirePermissions()
    )
    var isGPSEnabled by remember {
        mutableStateOf(isGPSEnabled(context))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorYellow)
    ) {
        HomeTopAppBar(navController = navController,
            sortType = homeViewModel.getSortType(),
            onChangeSortType = { homeViewModel.changeSortType() })

        val lifecycleEvent = rememberLifecycleEvent()
        LaunchedEffect(lifecycleEvent) {
            if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
                isGPSEnabled = isGPSEnabled(context)
            }
        }

        if (permissionStates.allPermissionsGranted && isGPSEnabled) {
            RequestPermission(scaffoldState)
            val currentAddress by homeViewModel.currentAddress.collectAsState()
            if (currentAddress is ResultWrapper.Success) {
                CurrentAddresssText(address = currentAddress.takeValueOrThrow())
            }
            when (val response = homeViewModel.gasStationsResult.collectAsState().value) {
                is ResultWrapper.Success -> {
                    val gasStations = response.value.OIL
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1F),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(gasStations) { item ->
                            GasStationItem(gasStations = item, homeViewModel.getOilType()) {
                                val mapType = MapType.getMap(homeViewModel.getMapType())
                                val isAppInstalled = isAppInstalled(context, mapType)
                                if (isAppInstalled) {
                                    coroutine.launch {
                                        homeViewModel.landingMap(
                                            item.GIS_X_COOR.toDouble(), item.GIS_Y_COOR.toDouble()
                                        ) { x, y ->
                                            val url = getMapUrl(x, y, mapType, item.OS_NM)
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                            context.startActivity(intent)
                                        }
                                    }
                                } else {
                                    landingMapMarket(context, mapType)
                                }
                            }
                        }
                    }
                }

                is ResultWrapper.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1F)
                    ) {
                        CircularProgressIndicator(
                            color = ColorBlack,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }

                else -> {}
            }
        } else {
            RequestPermission(scaffoldState)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val msg = if (!permissionStates.allPermissionsGranted) {
                    stringResource(id = R.string.auth_rationale_msg)
                } else {
                    stringResource(id = R.string.location_setting_msg)
                }
                PermissionButton(msg = msg) {
                    scope.launch {
                        openRationaleDialog = true
                    }
                }
            }
        }
    }

    if (openRationaleDialog) {
        val msg = if (!permissionStates.allPermissionsGranted) {
            stringResource(id = R.string.auth_rationale_msg)
        } else {
            stringResource(id = R.string.location_setting_msg)
        }
        PermissionDialog(msg = msg, onConfirm = {
            scope.launch {
                if (!permissionStates.allPermissionsGranted) {
                    permissionStates.launchMultiplePermissionRequest()
                } else {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                openRationaleDialog = false
            }
        }, onDismiss = {
            openRationaleDialog = false
        })
    }
}


@Composable
fun RequestPermission(scaffoldState: ScaffoldState) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val scope = rememberCoroutineScope()
    RequestLocationPermission(onPermissionGranted = {
        getCurrentLocation(context = context,
            fusedLocationProviderClient = fusedLocationProviderClient,
            onGetCurrentLocationSuccess = {
                Timber.i("latitude = " + it.first + " , longitude = " + it.second)
                scope.launch {
                    homeViewModel.getCurrentAddress(it.second, it.first, Coords.WGS84.name)
                    homeViewModel.getGasStationList(
                        it.second, it.first, Coords.WGS84.name, Coords.KTM.name
                    )
                }
            },
            onGetCurrentLocationFailed = {
                popupSnackBar(
                    scope,
                    scaffoldState,
                    context.getString(R.string.fail_get_location_info)
                )
                Timber.i(it.localizedMessage ?: "Error Getting Current Location")
            })
    }, onPermissionDenied = {
        popupSnackBar(scope, scaffoldState, context.getString(R.string.auth_denied_msg))
    }, onPermissionsRevoked = {
        popupSnackBar(scope, scaffoldState, context.getString(R.string.auth_denied_msg))
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
    TopAppBar(title = {
        Text(text = sortType, modifier = Modifier.clickable {
            onChangeSortType()
        })
    }, colors = TopAppBarColors(
        containerColor = ColorBlack,
        titleContentColor = ColorYellow,
        actionIconContentColor = ColorYellow,
        navigationIconContentColor = ColorBlack,
        scrolledContainerColor = ColorBlack
    ), modifier = modifier, navigationIcon = {}, actions = {
        IconButton(onClick = {
            navController.navigate(NavTarget.Setting.route)
        }) {
            Icon(Icons.Filled.Settings, "Setting")
        }
    })
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
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
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

        Timber.i("LaunchedEffect() allPermissionsRevoked = $allPermissionsRevoked")
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
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )
}

private fun isAppInstalled(context: Context, mapType: MapType): Boolean {
    val packageName = getPackageName(mapType)
    return try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

private fun landingMapMarket(context: Context, mapType: MapType) {
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW, Uri.parse("market://details?id=${getPackageName(mapType)}")
        )
    )
}

private fun getPackageName(mapType: MapType): String {
    return when (mapType) {
        MapType.TMAP -> {
            Const.T_MAP_MARKET_URL
        }

        MapType.KAKAO -> {
            Const.KAKAO_MAP_MARKET_URL
        }

        MapType.NAVER -> {
            Const.NAVER_MAP_MARKET_URL
        }
    }
}

private fun getMapUrl(x: String, y: String, mapType: MapType, name: String): String {
    return when (mapType) {
        MapType.TMAP -> {
            "tmap://route?goalx=${x}&goaly=${y}&reqCoordType=KTM&resCoordType=WGS84"
        }

        MapType.KAKAO -> {
            "kakaomap://route?ep=${y},${x}&by=CAR"
        }

        MapType.NAVER -> {
            "nmap://route/car?dlat=${y}&dlng=${x}&dname=${name}"
        }
    }
}

private fun isGPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}
