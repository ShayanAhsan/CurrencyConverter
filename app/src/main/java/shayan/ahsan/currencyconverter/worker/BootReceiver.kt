package shayan.ahsan.currencyconverter.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import shayan.ahsan.currencyconverter.MyApplication

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            (context.applicationContext as MyApplication).setupPeriodicWork()
        }
    }
}
