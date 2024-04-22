package com.gasstation.ui.navigation

sealed class NavTarget(val route: String) {
    data object GraphHome : NavTarget("home")
    data object Home : NavTarget("home/main")
    data object Setting : NavTarget("home/setting")
    data object SettingDetail : NavTarget("home/setting/detail/{${NavKey.SETTING_TYPE}}")

}