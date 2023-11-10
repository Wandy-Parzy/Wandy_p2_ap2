package com.example.wandy_p2_ap2.util

sealed class ScreenModuleG(val route: String) {
    object GastoScreen : ScreenModuleG("gasto_screen")
    object Modifier_Screen : ScreenModuleG("modifier_gasto_screen")
}