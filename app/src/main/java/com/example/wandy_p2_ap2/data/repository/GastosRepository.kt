package com.example.wandy_p2_ap2.data.repository

import com.example.wandy_p2_ap2.data.remote.dto.GastosDto
import com.example.wandy_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow

interface GastosRepository
{
    fun getGastos(): Flow<Resource<List<GastosDto>>>
    fun getGastosId(id: Int): Flow<Resource<GastosDto>>
    suspend fun putGastos(id: Int, gastosDto: GastosDto)
    suspend fun deleteGastos(id: Int)
    suspend fun postGastos(gastosDto: GastosDto)
}