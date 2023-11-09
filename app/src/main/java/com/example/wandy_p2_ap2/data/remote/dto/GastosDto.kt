package com.example.wandy_p2_ap2.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gastos")
data class GastosDto (
    @PrimaryKey
    var idGasto : Int?=null,
    var fecha: String = "",
    var suplidor: String = "",
    var concepto: String? = "",
    var ncf: String =  "",
    var monto: Int? = 0
)