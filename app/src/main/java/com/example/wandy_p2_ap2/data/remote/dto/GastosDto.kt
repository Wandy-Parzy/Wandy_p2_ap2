package com.example.wandy_p2_ap2.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GastosDto (
    var idGasto : Int,
    var fecha: String = "",
    var idSuplidor: Int,
    var suplidor: String = "",
    var concepto: String? = "",
    var itbis: Int = 0,
    var ncf: String =  "",
    var monto: Int = 0
)