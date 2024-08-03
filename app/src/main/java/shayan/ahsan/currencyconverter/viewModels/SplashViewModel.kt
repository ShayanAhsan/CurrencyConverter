package shayan.ahsan.currencyconverter.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel@Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _interstitialAd = MutableLiveData<InterstitialAd?>()
    val interstitialAd: LiveData<InterstitialAd?> = _interstitialAd

    fun loadInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                _interstitialAd.value = ad
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                _interstitialAd.value = null
            }

        })
    }
}