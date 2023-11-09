package com.example.wandy_p2_ap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wandy_p2_ap2.ui.counter.CounterViewModel
import com.example.wandy_p2_ap2.ui.gastos.GastosScreen
import com.example.wandy_p2_ap2.ui.gastos.GastosViewModel
import com.example.wandy_p2_ap2.ui.theme.Wandy_p2_ap2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wandy_p2_ap2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val gastosViewModel: GastosViewModel = viewModel()
                    GastosScreen(viewModel = gastosViewModel)
                }
            }
        }
    }
}