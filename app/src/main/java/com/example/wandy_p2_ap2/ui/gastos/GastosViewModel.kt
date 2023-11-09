package com.example.wandy_p2_ap2.ui.gastos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wandy_p2_ap2.data.remote.dto.GastosDto
import com.example.wandy_p2_ap2.data.repository.GastosRepositoryImp
import com.example.wandy_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GastosListState(
    val isLoading: Boolean = false,
    val gastos: List<GastosDto> = emptyList(),
    val error: String = ""
)
data class GastosState(
    val isLoading: Boolean = false,
    val gasto: GastosDto? = null,
    val error: String = ""
)
@HiltViewModel
class GastosViewModel @Inject constructor(
    private val gastosRepository: GastosRepositoryImp

) : ViewModel() {
    var gastosId by mutableStateOf(0)

    var fecha by mutableStateOf("")
    var fechaError by mutableStateOf("")

    var suplidor by mutableStateOf("")
    var suplidorError by mutableStateOf("")

    var concepto by mutableStateOf("")
    var conceptoError by mutableStateOf("")

    var ncf by mutableStateOf("")
    var ncfError by mutableStateOf("")

    var monto by mutableStateOf(0)
    var montoError by mutableStateOf("")

    var uiState = MutableStateFlow(GastosListState())
        private set
    var uiStateGastos = MutableStateFlow(GastosState())
        private set

    init {
        gastosRepository.getGastos().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    uiState.update {
                        it.copy(gastos = result.data ?: emptyList())
                    }
                }

                is Resource.Error -> {
                    uiState.update { it.copy(error = result.message ?: "Error desconocido") }
                }

            }
        }.launchIn(viewModelScope)
    }
    init {
        gastosRepository.getGastos().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    uiState.update {
                        it.copy(gastos = result.data ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    uiState.update { it.copy(error = result.message ?: "Error desconocido") }
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
    fun putGastos(id: Int) {
        viewModelScope.launch {
            gastosId = id
            try {
                if (gastosId > 0) {
                    gastosRepository.putgastos(
                        gastosId, GastosDto(
                            idGasto = gastosId,
                            fecha = fecha,
                            suplidor = suplidor,
                            concepto = concepto,
                            ncf = ncf,
                            monto = monto
                        )
                    )
                } else {
                    throw NullPointerException("Value is null")
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }
}
