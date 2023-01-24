package com.kiki.searchcustomization.ui.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kiki.searchcustomization.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.searchBox.requestFocus()

        binding.searchBox.addTextChangedListener {
            val query = it.toString()
            if (query.length >= 3) {
                viewModel.setQuery(query)
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.searchResult.collectLatest { resource ->
                            Log.e("resource", resource.toString())
                            Log.e("resource", resource.data.toString())
                            Log.e("resource", resource.message.toString())
                        }
                    }
                }
            }
        }
    }
}