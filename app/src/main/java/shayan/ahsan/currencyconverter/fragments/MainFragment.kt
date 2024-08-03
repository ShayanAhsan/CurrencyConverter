package shayan.ahsan.currencyconverter.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import shayan.ahsan.currencyconverter.databinding.FragmentMainBinding
import shayan.ahsan.currencyconverter.utils.LoadAd
import shayan.ahsan.currencyconverter.viewModels.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    private var selectedBaseCurrency: String? = null
    private var selectedTargetCurrency: String? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        dataObserver()
        LoadAd.loadNative(requireActivity())
        setONClickListeners()
        return binding.root
    }

    private fun setONClickListeners() {
        binding.inputTv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                performConversion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })
    }

    private fun dataObserver() {
        lifecycleScope.launch {
            viewModel.exchangeRates.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        val exchangeRates = it.getOrNull()
                        exchangeRates?.conversion_rates?.let { ratesMap ->
                            setupBaseSpinner(ratesMap)
                            setupConvertedToSpinner(ratesMap)
                        }
                        Log.d("Currency", "Success")
                    } else {
                        val exception = it.exceptionOrNull()
                        Log.d("Currency", "failed----->${exception}")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.convertedAmount.collect { amount ->
                binding.outputTv.setText(amount)
            }
        }
    }

    private fun setupBaseSpinner(rates: Map<String, Double>) {
        val currencyList = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), shayan.ahsan.currencyconverter.R.layout.spinner_item, currencyList)
        adapter.setDropDownViewResource(shayan.ahsan.currencyconverter.R.layout.spinner_item)
        binding.baseSpinner.adapter = adapter

        binding.baseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedBaseCurrency = parent?.getItemAtPosition(position) as String
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when no item is selected
            }
        }
    }

    private fun setupConvertedToSpinner(rates: Map<String, Double>) {
        val currencyList = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), shayan.ahsan.currencyconverter.R.layout.spinner_item, currencyList)
        adapter.setDropDownViewResource(shayan.ahsan.currencyconverter.R.layout.spinner_item)
        binding.convertedToSpinner.adapter = adapter

        binding.convertedToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTargetCurrency = parent?.getItemAtPosition(position) as String
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when no item is selected
            }
        }
    }
    private fun performConversion() {
        viewModel.convertCurrency(binding.inputTv.text.toString(), selectedBaseCurrency, selectedTargetCurrency)
    }

    override fun onResume() {
        super.onResume()
        LoadAd.loadNative(requireActivity())
    }
}
