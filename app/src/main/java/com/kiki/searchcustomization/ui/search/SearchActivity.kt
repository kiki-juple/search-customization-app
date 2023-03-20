package com.kiki.searchcustomization.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kiki.searchcustomization.R
import com.kiki.searchcustomization.data.Resource
import com.kiki.searchcustomization.databinding.ActivitySearchBinding
import com.kiki.searchcustomization.ui.filter.FilterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()
    private val adapter by lazy {
        SearchAdapter {
            Toast.makeText(this, it.menu.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.searchBox.requestFocus()
        binding.rvSearch.adapter = adapter
        binding.btnFilter.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }
        binding.searchBox.addTextChangedListener { viewModel.setQuery(it.toString()) }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResult.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.tvCount.isVisible = false
                            binding.loading.isVisible = true
                            binding.tvError.isVisible = false
                            binding.rvSearch.isVisible = false
                        }
                        is Resource.Success -> {
                            if (resource.data?.size!! > 0) {
                                binding.tvCount.isVisible = true
                                binding.tvCount.text = getString(
                                    R.string.hasil_pencarian,
                                    resource.data.size
                                )
                            }
                            adapter.submitList(resource.data) {
                                binding.tvError.isVisible = false
                                binding.rvSearch.isVisible = true
                                binding.rvSearch.scrollToPosition(0)
                            }
                            binding.loading.isVisible = false
                        }
                        is Resource.Error -> {
                            binding.tvCount.isVisible = false
                            binding.tvError.isVisible = true
                            binding.rvSearch.isVisible = false
                            binding.loading.isVisible = false
                            binding.tvError.text = resource.message
                        }
                    }
                }
            }
        }
    }
}