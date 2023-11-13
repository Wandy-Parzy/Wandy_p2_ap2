package com.example.wandy_p2_ap2.ui.gastos

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wandy_p2_ap2.data.remote.dto.GastosDto
import com.example.wandy_p2_ap2.ui.viewModel.GastosApiViewModel
import com.example.wandy_p2_ap2.util.ScreenModuleG
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GastosScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        GastosBody()
        Gastos_List_Screen(navController = navController) { id ->
            navController.navigate(ScreenModuleG.Modifier_Screen.route + "/${id}")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun GastosBody(
    viewModel: GastosApiViewModel = hiltViewModel()
) {
    val anio: Int
    val mes: Int
    val dia: Int

    val mCalendar = Calendar.getInstance()
    anio = mCalendar.get(Calendar.YEAR)
    mes = mCalendar.get(Calendar.MONTH)
    dia = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current, { _: DatePicker, anio: Int, mes: Int, dia: Int ->
            viewModel.fecha = "$dia/${mes + 1}/$anio"
        }, anio, mes, dia
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "Gastos",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date Field
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                value = viewModel.fecha,
                onValueChange = { viewModel.fecha = it },
                enabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .size(33.dp)
                            .padding(4.dp)
                            .clickable {
                                mDatePickerDialog.show()
                            }
                    )
                },
                label = { Text(text = "Fecha") }
            )
        }
        Spacer(modifier = Modifier.padding(17.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Suplidor Field
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                value = viewModel.suplidor,
                label = { Text(text = "Suplidor") },
                onValueChange = viewModel::onSuplidorChanged,
                isError = viewModel.suplidorError.isNotBlank(),
                trailingIcon = {
                    if (viewModel.suplidorError.isBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Check,
                            contentDescription = "success",
                            modifier = Modifier.clickable(onClick = { viewModel.suplidor = "" })
                        )
                    } else if (viewModel.suplidorError.isNotBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Close,
                            contentDescription = "error",
                            modifier = Modifier.clickable(onClick = { viewModel.LimpiarSuplidor() })
                        )
                    }
                }
            )
            // Concepto Field
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                value = viewModel.concepto,
                label = { Text(text = "Concepto") },
                onValueChange = viewModel::onConceptoChanged,
                isError = viewModel.conceptoError.isNotBlank(),
                trailingIcon = {
                    if (viewModel.conceptoError.isBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Check,
                            contentDescription = "success",
                            modifier = Modifier.clickable(onClick = { viewModel.concepto = "" })
                        )
                    } else if (viewModel.conceptoError.isNotBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Close,
                            contentDescription = "error",
                            modifier = Modifier.clickable(onClick = { viewModel.LimpiarConcepto() })
                        )
                    }
                }
            )
        }
        if (viewModel.conceptoError.isNotBlank()) {
            Text(
                text = viewModel.conceptoError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (viewModel.conceptoError.isBlank()) {
            Text(
                text = viewModel.conceptoError,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
        if (viewModel.suplidorError.isNotBlank()) {
            Text(
                text = viewModel.suplidorError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (viewModel.suplidorError.isBlank()) {
            Text(
                text = viewModel.suplidorError,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Monto Field
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                value = viewModel.monto.toString(),
                label = { Text(text = "Monto") },
                onValueChange = viewModel::onMontoChanged,
                isError = viewModel.montoError.isNotBlank(),
                trailingIcon = {
                    if (viewModel.montoError.isBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Check,
                            contentDescription = "success",
                            modifier = Modifier.clickable(onClick = { viewModel.monto = 0 })
                        )
                    } else if (viewModel.montoError.isNotBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Close,
                            contentDescription = "error",
                            modifier = Modifier.clickable(onClick = { viewModel.LimpiarMonto() })
                        )
                    }
                }
            )
            // ITBIS Field
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                value = viewModel.itbis.toString(),
                label = { Text(text = "ITBIS") },
                onValueChange = viewModel::onItbisChanged,
                isError = viewModel.itbisError.isNotBlank(),
                trailingIcon = {
                    if (viewModel.itbisError.isBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Check,
                            contentDescription = "success",
                            modifier = Modifier.clickable(onClick = { viewModel.itbis = 0 })
                        )
                    } else if (viewModel.itbisError.isNotBlank()) {
                        Icon(
                            imageVector = Icons.TwoTone.Close,
                            contentDescription = "error",
                            modifier = Modifier.clickable(onClick = { viewModel.LimpiarItbis() })
                        )
                    }
                }
            )
        }
        if (viewModel.itbisError.isNotBlank()) {
            Text(
                text = viewModel.itbisError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (viewModel.itbisError.isBlank()) {
            Text(
                text = viewModel.itbisError,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
        if (viewModel.montoError.isNotBlank()) {
            Text(
                text = viewModel.montoError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (viewModel.montoError.isBlank()) {
            Text(
                text = viewModel.montoError,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .size(470.dp, 50.dp),
                containerColor = Color.Blue,
                text = { Text("Guardar", fontSize = 26.sp, color = Color.White, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                },
                onClick = {
                    viewModel.postGastos()
                    viewModel.Limpiar()
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gastos_List_Screen(
    navController: NavController,
    viewModel: GastosApiViewModel = hiltViewModel(),
    onGastoClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(3.dp))
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Lista de Gastos", fontSize = 30.sp,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(ScreenModuleG.GastoScreen.route) }
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Save")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            val uiState by viewModel.uiStateGastosLS.collectAsState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
            {
                GastosListBody(uiState.gastoLS) {
                    onGastoClick(it)
                }
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GastosListBody(gastoList: List<GastosDto>, onGastoClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn {
            items(gastoList) { gastoList ->
                GastosRow(gastoList)
                {
                    onGastoClick(it)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GastosRow(gastoRow: GastosDto, onGastoClick: (Int) -> Unit) {

    val viewModel: GastosApiViewModel = hiltViewModel()

    Spacer(modifier = Modifier.padding(5.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopStart)
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            String.format("ID: %d", gastoRow.idGasto),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier.weight(8f)
                        )
                        Text(
                            String.format("%.10s", gastoRow.fecha),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier
                                .weight(8f)
                                .wrapContentWidth(Alignment.End)
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(2.dp),
                        color = Color(0xFF1D1D1D),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.padding(2.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = gastoRow.suplidor,
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier
                                .weight(8f)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))

                    Column(modifier = Modifier.padding(3.dp)) {
                        Text(
                            String.format("Concepto: %s ", gastoRow.concepto),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                        )
                        Text(
                            String.format("Ncf: %s", gastoRow.ncf),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D)
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))
                    Divider(
                        modifier = Modifier.padding(2.dp),
                        color = Color(0xFF1D1D1D),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopStart)
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            String.format("Itbis: %d", gastoRow.itbis),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier.weight(8f)
                        )
                        Text(
                            String.format("$%d", gastoRow.monto),
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier
                                .weight(8f)
                                .wrapContentWidth(Alignment.End)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            Box {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .size(60.dp, 50.dp),
                    containerColor = Color.Cyan,
                    text = { Text("Modificar") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Modifier",
                            tint = Color.White
                        )
                    },
                    onClick = { gastoRow.idGasto?.let { onGastoClick(it) } }
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Box {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .size(60.dp, 50.dp),
                    containerColor = Color.Red,
                    text = { Text("Eliminar") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    },
                    onClick = {
                        gastoRow.idGasto?.let { viewModel.deleteGastos(it) }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
    }
}