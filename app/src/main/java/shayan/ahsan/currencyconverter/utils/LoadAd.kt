package shayan.ahsan.currencyconverter.utils

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import shayan.ahsan.currencyconverter.R


object LoadAd {

    var nativeAd: NativeAd? = null

    fun loadNative(mActivity: Activity) {
        val adLoader = AdLoader.Builder(mActivity, mActivity.getString(R.string.admob_native_ad))
            .forNativeAd { nati: NativeAd ->
                nativeAd = nati
                val frameLayout = mActivity.findViewById<FrameLayout>(R.id.ad_container)
                if (frameLayout != null) {
                    frameLayout.visibility = View.VISIBLE
                    val adView = mActivity.layoutInflater.inflate(R.layout.ad_media, null) as NativeAdView
                    populateUnifiedNativeAdView(nativeAd!!, adView)
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                }
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("AdManager", "Failed to load native ad: ${adError.message}")
                }

                override fun onAdClicked() {
                    Log.d("AdManager", "Ad clicked")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView = mediaView

        adView.headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        adView.bodyView = adView.findViewById<TextView>(R.id.ad_body)
        adView.callToActionView = adView.findViewById<TextView>(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView).text = nativeAd.headline
        adView.bodyView?.apply {
            isVisible = nativeAd.body != null
            (this as TextView).text = nativeAd.body
        }
        adView.callToActionView?.apply {
            isVisible = nativeAd.callToAction != null
            (this as TextView).text = nativeAd.callToAction
        }
        adView.iconView?.apply {
            isVisible = nativeAd.icon != null
            (this as ImageView).setImageDrawable(nativeAd.icon?.drawable)
        }
        adView.priceView?.apply {
            isVisible = nativeAd.price != null
            (this as TextView).text = nativeAd.price
        }
        adView.storeView?.apply {
            isVisible = nativeAd.store != null
            (this as TextView).text = nativeAd.store
        }
        adView.starRatingView?.apply {
            isVisible = nativeAd.starRating != null
            (this as RatingBar).rating = nativeAd.starRating!!.toFloat()
        }
        adView.advertiserView?.apply {
            isVisible = nativeAd.advertiser != null
            (this as TextView).text = nativeAd.advertiser
        }

        adView.setNativeAd(nativeAd)
    }



}
