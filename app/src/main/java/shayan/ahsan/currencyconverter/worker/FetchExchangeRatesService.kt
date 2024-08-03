package shayan.ahsan.currencyconverter.worker

import android.app.IntentService
import android.content.Intent
import android.util.Log
import shayan.ahsan.currencyconverter.repositories.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FetchExchangeRatesService : IntentService("FetchExchangeRatesService") {

    @Inject
    lateinit var repository: MainRepository

    init {
        Log.d("FetchExchangeRatesService", "Service created")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("FetchExchangeRatesService", "Service started")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getExchangeRates("USD")
                response.collect {
                    if (it.isSuccess) {
                        val exchangeRates = it.getOrNull()?.conversion_rates
                        if (exchangeRates != null) {
                            val sharedPreferences = getSharedPreferences("exchange_rates", MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putString("rates", exchangeRates.toString())
                                apply()
                            }
                            Log.d("FetchExchangeRatesService", "Exchange rates updated successfully")
                        }
                    } else {
                        Log.e("FetchExchangeRatesService", "Failed to fetch exchange rates", it.exceptionOrNull())
                    }
                }
            } catch (e: Exception) {
                Log.e("FetchExchangeRatesService", "Error in fetching exchange rates", e)
            }
        }
    }
}
