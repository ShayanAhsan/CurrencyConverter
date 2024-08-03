package shayan.ahsan.currencyconverter.retrofit

import com.google.android.gms.common.api.Response
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import shayan.ahsan.currencyconverter.models.ExchangeRatesResponse

interface ApiInterface {

    @GET("{base}")
    suspend fun getExchangeRates(@Path("base") base: String): ExchangeRatesResponse

}