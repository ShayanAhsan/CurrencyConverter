package shayan.ahsan.currencyconverter.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import shayan.ahsan.currencyconverter.models.ExchangeRatesResponse
import shayan.ahsan.currencyconverter.retrofit.ApiInterface
import javax.inject.Inject

class MainRepository @Inject constructor(private  val apiInterface: ApiInterface) {

    fun getExchangeRates(base: String): Flow<Result<ExchangeRatesResponse>> = flow {
        try {
            val response = apiInterface.getExchangeRates(base)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}