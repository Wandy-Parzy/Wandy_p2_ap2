package com.example.wandy_p2_ap2.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GastosDto (
    @PrimaryKey(autoGenerate = true)
    var idGasto : Int?=null,
    var fecha: String = "",
    var suplidor: String = "",
    var concepto: String? = "",
    var itbis: Int = 0,
    var ncf: String =  "",
    var monto: Int = 0
)