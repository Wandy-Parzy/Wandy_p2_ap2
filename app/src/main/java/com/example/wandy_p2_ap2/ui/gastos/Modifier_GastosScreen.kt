package com.example.wandy_p2_ap2.ui.gastos

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ScrollingView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wandy_p2_ap2.ui.viewModel.GastosApiViewModel
import com.example.wandy_p2_ap2.util.ScreenModuleG
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Modifier_Gastos_Screen(
    navController: NavController,
    idGasto: Int,
    viewModel: GastosApiViewModel = hiltViewModel(),
    onSaveClick: () -> Unit
) {
    remember {
        viewModel.GastobyId(idGasto)
        0
    }

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
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterEnd)
        ) {

            Text(
                text = "Modificar gastos", fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )


    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentSize(Alignment.Center),
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
                    })
        },
        label = { Text(text = "Fecha") }
    )



            // Concepto Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .wrapContentSize(Alignment.Center),
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

        // Suplidor Field
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .wrapContentSize(Alignment.Center),
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

        // ITBIS Field
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .wrapContentSize(Alignment.Center),
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

        // Monto Field
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .wrapContentSize(Alignment.Center),
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



        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .size(470.dp, 50.dp),
                containerColor = Color.Blue,
                text = { Text("Modificar", fontSize = 26.sp, color = Color.White, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                },
                onClick = {
                    viewModel.putGastos(idGasto)
                    navController.navigate(ScreenModuleG.GastoScreen.route)
                    onSaveClick()
                }
            )
        }
    }
}


