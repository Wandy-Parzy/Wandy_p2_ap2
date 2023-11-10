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
        getGastos()
    }
    fun getGastos(){
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
                    uiStateGastosLS.update { it.copy(error = result.message ?: "Error desconocido") }
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
                    uiStateGastoS .update {
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

    fun putGastos(id: Int?) {
        viewModelScope.launch {
            idGasto = id!!
            try {
                if (idGasto != null) {
                    gastosRepository.putGastos(
                        idGasto, GastosDto(
                            idGasto = idGasto,
                            fecha = fecha,
                            suplidor = suplidor,
                            concepto = concepto,
                            ncf = "GHDDDFR",
                            itbis = itbis,
                            monto = monto
                        )
                    )
                } else {
                    throw IllegalArgumentException("idGasto is null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postGastos() {
        viewModelScope.launch {
            try {
                if(idGasto == 0) {
                    idGasto += 1
                }
                gastosRepository.postGastos(
                    GastosDto(
                        idGasto = idGasto,
                        fecha = fecha,
                        suplidor = suplidor,
                        concepto = concepto,
                        itbis = itbis,
                        ncf = "GHDDDFR",
                        monto = monto
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteGastos(id: Int?, navController: NavController) {
        viewModelScope.launch {
            if (id == null) {
                Log.w("DeleteGastos", "id is null")
                return@launch
            }

            try {
                gastosRepository.deleteGastos(id)

                // Después de eliminar el gasto, navega de nuevo a la misma pantalla
                val action = ScreenModuleG.GastoScreen.route
                navController.navigate(action)

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
        this.itbis = itbis.toIntOrNull()?:0
        hayErrores()
    }

    fun onMontoChanged(monto: String) {
        this.monto = monto.toIntOrNull()?:0
        hayErrores()
    }

    fun hayErrores(): Boolean {
        var hayError = false

        suplidorError = ""
        if (suplidor.isBlank()) {
            suplidorError = "Ingrese el suplidor"
            hayError = true
        }

        conceptoError = ""
        if (concepto.isBlank()) {
            conceptoError = "Ingrese el concepto"
            hayError = true
        }

        itbisError = ""
        if (itbis < 1 || itbis.toString().isBlank()) {
            itbisError = "El ITBIS no puede ser negativo"
            hayError = true
        }

        montoError = ""
        if (monto < 1 || itbis.toString().isBlank()) {
            montoError = "El monto debe ser mayor que cero"
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
}
