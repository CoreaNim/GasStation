package com.gasstation.ui.navigation

sealed class NavTarget(val route: String) {

    data object GraphHome : NavTarget("home")
    data object Home : NavTarget("home/main")
    data object Settings : NavTarget("home/setting")

}