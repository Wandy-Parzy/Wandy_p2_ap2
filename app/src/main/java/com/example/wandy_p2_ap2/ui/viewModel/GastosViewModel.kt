package com.example.wandy_p2_ap2.ui.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wandy_p2_ap2.data.remote.dto.GastosDto
import com.example.wandy_p2_ap2.data.repository.GastosRepositoryImp
import com.example.wandy_p2_ap2.util.Resource
import com.example.wandy_p2_ap2.util.ScreenModuleG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class GastosListState(
    val isLoading: Boolean = false,
    val gastoLS: List<GastosDto> = emptyList(),
    val error: String = ""
)

data class GastoState(
    val isLoading: Boolean = false,
    val gastoS: GastosDto? = null,
    val error: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GastosApiViewModel @Inject constructor(
    private val gastosRepository: GastosRepositoryImp

) : ViewModel() {

    var idGasto by mutableIntStateOf(0)
    var idSuplidor by mutableIntStateOf(0)
    var fecha by mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    var suplidor by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableIntStateOf(0)
    var monto by mutableIntStateOf(0)

    var suplidorError by mutableStateOf("")
    var conceptoError by mutableStateOf("")
    var itbisError by mutableStateOf("")
    var montoError by mutableStateOf("")

    var uiStateGastosLS = MutableStateFlow(GastosListState())
        private set
    var uiStateGastoS = MutableStateFlow(GastoState())
        private set

    init {
        gastosRepository.getGastos().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiStateGastosLS.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    uiStateGastosLS.update {
                        it.copy(gastoLS = result.data ?: emptyList())
                    }
                }

                is Resource.Error -> {
                    uiStateGastosLS.update {
                        it.copy(
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun GastobyId(id: Int) {
        idGasto = id
        Limpiar()
        gastosRepository.getGastosId(idGasto).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiStateGastoS.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    uiStateGastoS.update {
                        it.copy(gastoS = result.data)
                    }
                    fecha = uiStateGastoS.value.gastoS!!.fecha
                    suplidor = uiStateGastoS.value.gastoS!!.suplidor
                    concepto = uiStateGastoS.value.gastoS!!.concepto.toString()
                    ncf = uiStateGastoS.value.gastoS!!.ncf
                    itbis = uiStateGastoS.value.gastoS!!.itbis
                    monto = uiStateGastoS.value.gastoS!!.monto
                }

                is Resource.Error -> {
                    uiStateGastoS.update { it.copy(error = result.message ?: "Error desconocido") }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun putGastos(id: Int) {
        viewModelScope.launch {
            try {
                gastosRepository.putGastos(
                    idGasto, GastosDto(
                        idGasto = id,
                        fecha = fecha,
                        idSuplidor = idSuplidor,
                        suplidor = suplidor,
                        concepto = concepto,
                        itbis = itbis,
                        ncf = "DRTHHTCS",
                        monto = monto
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postGastos() {
        viewModelScope.launch {
            try {
                if (idGasto == 0) {
                    idGasto += 1
                }
                if (idSuplidor == 0) {
                    idSuplidor += 1
                }
                gastosRepository.postGastos(
                    GastosDto(
                        idGasto = idGasto,
                        fecha = fecha,
                        idSuplidor = idSuplidor,
                        suplidor = suplidor,
                        concepto = concepto,
                        itbis = itbis,
                        ncf = "DRTHHTCS",
                        monto = monto
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteGastos(id: Int?) {
        viewModelScope.launch {
            if (id == null) {
                Log.w("DeleteGastos", "id is null")
                return@launch
            }

            try {
                gastosRepository.deleteGastos(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSuplidorChanged(suplidor: String) {
        this.suplidor = suplidor
        hayErrores()
    }

    fun onConceptoChanged(concepto: String) {
        this.concepto = concepto
        hayErrores()
    }

    fun onItbisChanged(itbis: String) {
        this.itbis = itbis.toIntOrNull() ?: 0
        hayErrores()
    }

    fun onMontoChanged(monto: String) {
        this.monto = monto.toIntOrNull() ?: 0
        hayErrores()
    }

    fun hayErrores(): Boolean {
        var hayError = false

        suplidorError = ""
        if (suplidor.isBlank()) {
            suplidorError = "Ingrese el suplidor"
            hayError = true
        } else if (suplidor.length < 5) {
            suplidorError = "El suplidor debe tener al menos 10 caracteres"
            hayError = true
        }

        conceptoError = ""
        if (concepto.isBlank()) {
            conceptoError = "Ingrese el concepto"
            hayError = true
        } else if (concepto.length < 5) {
            conceptoError = "El concepto debe tener al menos 10 caracteres"
            hayError = true
        }

        itbisError = ""
        if (itbis < 1 || itbis.toString().isBlank()) {
            itbisError = "El ITBIS no puede ser negativo"
            hayError = true
        } else if (itbis > 100) {
            itbisError = "El ITBIS no puede ser mayor a 100"
            hayError = true
        } else if (itbis > monto) {
            itbisError = "El ITBIS no puede ser mayor al monto"
            hayError = true
        }

        montoError = ""
        if (monto < 1 || monto.toString().isBlank()) {
            montoError = "El monto debe ser mayor que cero"
            hayError = true
        } else if (monto > 1000000) {
            montoError = "El monto no puede ser mayor a 1,000,000"
            hayError = true
        } else if (monto < itbis) {
            montoError = "El monto no puede ser menor al ITBIS"
            hayError = true
        }

        return hayError
    }

    fun Limpiar() {
        fecha = ""
        suplidor = ""
        concepto = ""
        concepto = ""
        itbis = 0
        monto = 0
    }

    fun LimpiarSuplidor() {
        suplidor = ""
    }

    fun LimpiarConcepto() {
        concepto = ""
    }

    fun LimpiarItbis() {
        itbis = 0
    }

    fun LimpiarMonto() {
        monto = 0
    }
}
