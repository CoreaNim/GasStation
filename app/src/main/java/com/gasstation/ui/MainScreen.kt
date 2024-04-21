package com.gasstation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.gasstation.ui.navigation.NavKey
import com.gasstation.ui.navigation.NavTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data, modifier = Modifier.padding(50.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }) {
        NavigationScreen(navController, scaffoldState)
    }
}

@Composable
fun NavigationScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {
    NavHost(
        navController = navController,
        startDestination = NavTarget.GraphHome.route,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        navigation(startDestination = NavTarget.Home.route, route = NavTarget.GraphHome.route) {
            composable(
                route = NavTarget.Home.route
            ) {
                HomeScreen(scaffoldState, navController)
            }
            composable(
                route = NavTarget.Setting.route
            ) {
                SettingScreen(scaffoldState, navController)
            }
            composable(
                route = NavTarget.SettingDetail.route, arguments = listOf(
                    navArgument(NavKey.SETTING_TYPE) {
                        type = NavType.StringType
                    }
                )
            ) {
                SettingDetailScreen(
                    it.arguments?.getString(NavKey.SETTING_TYPE).orEmpty(),
                    scaffoldState,
                    navController
                )
            }
        }
    }
}

fun popupSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    message: String,
    onDismissCallback: () -> Unit = {}
) {
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message = message)
        onDismissCallback.invoke()
    }
}
