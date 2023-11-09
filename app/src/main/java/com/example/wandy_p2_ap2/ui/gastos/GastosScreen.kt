package com.example.wandy_p2_ap2.ui.gastos

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wandy_p2_ap2.data.remote.dto.GastosDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    viewModel: GastosViewModel = hiltViewModel(),

    ) {
    Column( modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(20.dp))
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Lista de gastos", fontSize = 30.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            val uiState by viewModel.uiState.collectAsState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
            {
                GastosListBody(uiState.gastos)
            }
        }
    }
}
@Composable
fun GastosListBody(gastoList: List<GastosDto>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn {
            items(gastoList) { gastos ->
                GastosRow(gastos)
            }
        }
    }
}

@Composable
fun GastosRow(gastos: GastosDto) {

    Spacer(modifier = Modifier.padding(5.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .wrapContentSize(Alignment.Center)
                .border(
                    2.dp, Color(0xA88E24AA),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Card(
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(onClick = { gastos.idGasto?.let {  } })
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = gastos.fecha,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xC3303030),
                            modifier = Modifier.weight(8f)
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopStart),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = gastos.suplidor,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xC3303030),
                            modifier = Modifier.weight(8f)
                        )
                        Text(
                            text = gastos.concepto.toString(),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xC3303030),
                            modifier = Modifier.weight(8f)
                        )
                        Text(
                            text = gastos.ncf,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xC3303030),
                            modifier = Modifier.weight(8f)
                        )

                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopCenter)
        ) {}
    }
}