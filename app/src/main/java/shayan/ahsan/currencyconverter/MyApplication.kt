package shayan.ahsan.currencyconverter

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import dagger.hilt.android.HiltAndroidApp
import shayan.ahsan.currencyconverter.worker.FetchExchangeRatesService
import java.util.Calendar

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleFetchExchangeRatesService()

    }

    private fun scheduleFetchExchangeRatesService() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, FetchExchangeRatesService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val intervalMillis = AlarmManager.INTERVAL_HOUR
        val startTime = System.currentTimeMillis()

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            intervalMillis,
            pendingIntent
        )
    }
}
