package shayan.ahsan.currencyconverter.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import shayan.ahsan.currencyconverter.models.ExchangeRatesResponse
import shayan.ahsan.currencyconverter.repositories.MainRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val repository: MainRepository) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("exchange_rates", Context.MODE_PRIVATE)
    private val sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "rates") {
            fetchExchangeRatesFromPreferences()
        }
    }

    private val _exchangeRates = MutableStateFlow<Result<ExchangeRatesResponse>?>(null)
    val exchangeRates: StateFlow<Result<ExchangeRatesResponse>?> = _exchangeRates

    private val _convertedAmount = MutableStateFlow<String>("")
    val convertedAmount: StateFlow<String> get() = _convertedAmount

    private var rates: Map<String, Double> = emptyMap()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        fetchExchangeRatesFromPreferences()
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    private fun fetchExchangeRatesFromPreferences() {
        viewModelScope.launch {
            try {
                val ratesString = sharedPreferences.getString("rates", null)
                if (ratesString != null) {
                    val ratesJson = JSONObject(ratesString)
                    val ratesMap = mutableMapOf<String, Double>()
                    ratesJson.keys().forEach { key ->
                        ratesMap[key] = ratesJson.getDouble(key)
                    }
                    rates = ratesMap
                    val exchangeRatesResponse = ExchangeRatesResponse("USD", rates)
                    _exchangeRates.value = Result.success(exchangeRatesResponse)
                } else {
                    _exchangeRates.value = Result.failure(Exception("No exchange rates found in SharedPreferences"))
                }
            } catch (e: Exception) {
                _exchangeRates.value = Result.failure(e)
            }
        }
    }

    fun convertCurrency(amount: String, baseCurrency: String?, targetCurrency: String?) {
        val amountValue = amount.toDoubleOrNull() ?: 0.0
        if (baseCurrency != null && targetCurrency != null) {
            val baseRate = rates[baseCurrency] ?: return
            val targetRate = rates[targetCurrency] ?: return
            val convertedAmount = (amountValue / baseRate) * targetRate
            _convertedAmount.value = String.format(Locale.US, "%.3f", convertedAmount)
        }
    }
}
