package com.kiki.searchcustomization.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.PagerSnapHelper
import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: WartegAdapter
    private lateinit var adapterWithDistance: WartegAdapter
    private lateinit var adapterPrice: WartegAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        cardWartegDistance()
        cardWartegRating()
        cardWartegPrice()
    }

    private fun cardWartegRating() {
        adapter = WartegAdapter {
            Toast.makeText(this, it.warteg.name, Toast.LENGTH_SHORT).show()
        }
        binding.rvWartegRating.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvWartegRating)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topWarteg.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> binding.progress.isVisible = true
                        is Resource.Success -> {
                            binding.progress.isVisible = false
                            adapter.submitList(resource.data)
                        }
                        is Resource.Error -> binding.progress.isVisible = false
                    }
                }
            }
        }
    }

    private fun cardWartegDistance() {
        adapterWithDistance = WartegAdapter {
            Toast.makeText(this, it.warteg.name, Toast.LENGTH_SHORT).show()
        }
        binding.rvWartegTerdekat.adapter = adapterWithDistance
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvWartegTerdekat)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.warteg.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> binding.progress.isVisible = true
                        is Resource.Success -> {
                            binding.progress.isVisible = false
                            adapterWithDistance.submitList(resource.data)
                        }
                        is Resource.Error -> binding.progress.isVisible = false
                    }
                }
            }
        }
    }

    private fun cardWartegPrice() {
        adapterPrice = WartegAdapter {
            Toast.makeText(this, it.warteg.name, Toast.LENGTH_SHORT).show()
        }
        binding.rvWartegHarga.adapter = adapterPrice
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvWartegHarga)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cheapestWarteg.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> binding.progress.isVisible = true
                        is Resource.Success -> {
                            binding.progress.isVisible = false
                            Log.e("warteg", resource.data.toString())
                            adapterPrice.submitList(resource.data)
                        }
                        is Resource.Error -> binding.progress.isVisible = false
                    }
                }
            }
        }
    }
}