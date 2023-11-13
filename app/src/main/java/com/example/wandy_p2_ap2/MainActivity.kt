package com.example.wandy_p2_ap2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wandy_p2_ap2.ui.counter.CounterViewModel
import com.example.wandy_p2_ap2.ui.gastos.GastosScreen
import com.example.wandy_p2_ap2.ui.gastos.Modifier_Gastos_Screen
import com.example.wandy_p2_ap2.ui.theme.Wandy_p2_ap2Theme
import com.example.wandy_p2_ap2.util.ScreenModuleG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wandy_p2_ap2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenModuleG.GastoScreen.route
                    ) {
                        composable(ScreenModuleG.GastoScreen.route) {
                            GastosScreen(navController = navController)
                        }
                        composable(
                            ScreenModuleG.Modifier_Screen.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { capturar ->
                            var idGasto = capturar.arguments?.getInt("id") ?: 0

                            Modifier_Gastos_Screen(idGasto = idGasto, navController = navController){
                                navController.navigate(ScreenModuleG.GastoScreen.route)
                            }
                        }
                    }
                }
            }
        }
    }
}
