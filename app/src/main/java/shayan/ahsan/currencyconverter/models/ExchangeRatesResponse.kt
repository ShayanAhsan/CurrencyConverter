package shayan.ahsan.currencyconverter.models

data class ExchangeRatesResponse(
    val base_code: String,
    val conversion_rates: Map<String, Double>
)