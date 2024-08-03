package shayan.ahsan.currencyconverter.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import shayan.ahsan.currencyconverter.MainActivity
import shayan.ahsan.currencyconverter.R
import shayan.ahsan.currencyconverter.databinding.FragmentSplishBinding
import shayan.ahsan.currencyconverter.viewModels.SplashViewModel
import shayan.ahsan.currencyconverter.worker.FetchExchangeRatesService

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplishBinding?=null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding  = FragmentSplishBinding.inflate(inflater,container,false)
        viewModel.loadInterstitialAd(requireContext())
        dataObserver()
        setOnClickListeners()
        val intent = Intent(requireContext(), FetchExchangeRatesService::class.java)
        requireContext().startService(intent)
        return binding.root
    }

    private fun setOnClickListeners() {
        binding.tryAgain.setOnClickListener {
            val intent = Intent(requireContext(), FetchExchangeRatesService::class.java)
            requireContext().startService(intent)
            binding.tryAgain.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.loadInterstitialAd(requireContext())

        }
    }

    private fun dataObserver() {
        viewModel.interstitialAd.observe(viewLifecycleOwner) { interstitialAd ->
            if (interstitialAd != null) {
                binding.progressBar.visibility = View.GONE
                interstitialAd.show(requireActivity())
                findNavController().navigate(R.id.action_splishFragment_to_mainFragment)
            } else {
                binding.progressBar.visibility = View.GONE
                binding.tryAgain.visibility = View.VISIBLE
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}