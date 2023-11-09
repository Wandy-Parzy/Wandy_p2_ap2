package com.example.wandy_p2_ap2.data.repository

import com.example.wandy_p2_ap2.data.remote.GastosApi
import com.example.wandy_p2_ap2.data.remote.dto.GastosDto
import com.example.wandy_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GastosRepositoryImp @Inject constructor(
    private val gastosApi: GastosApi
): GastosRepository {

    override fun getGastos(): Flow<Resource<List<GastosDto>>> = flow {
        try {
            emit(Resource.Loading())

            val gastos = gastosApi.getGastos()

            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            //error general HTTP
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {

            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }
    override fun getGastosId(id: Int): Flow<Resource<GastosDto>> = flow {
        try {
            emit(Resource.Loading())

            val gastos =
                gastosApi.getGastosId(id)

            emit(Resource.Success(gastos))
        } catch (e: HttpException) {

            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {

            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }

    override suspend fun putgastos(id: Int, gastosDto: GastosDto) {
        gastosApi.putGastos(id, gastosDto)
    }
    override suspend fun deleteGastos(id: Int){
        gastosApi.deleteGastos(id)
    }
    override suspend fun postGastos(gastosDto: GastosDto) {
        gastosApi.postGastos(gastosDto)
    }
}