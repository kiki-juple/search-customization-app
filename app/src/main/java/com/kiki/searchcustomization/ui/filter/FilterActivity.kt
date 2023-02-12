package com.kiki.searchcustomization.ui.filter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kiki.searchcustomization.data.entity.Factor
import com.kiki.searchcustomization.databinding.ActivityFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFilterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<FilterViewModel>()
    private var factor = Factor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        with(binding) {
            lifecycleScope.launch {
                viewModel.getFactor
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest {
                        switchHarga.isChecked = it.price
                        switchDistance.isChecked = it.distance
                        switchRating.isChecked = it.rating
                        switchReview.isChecked = it.review
                    }
            }
            switchHarga.setOnCheckedChangeListener { _, isChecked ->
                factor.price = isChecked
            }
            switchDistance.setOnCheckedChangeListener { _, isChecked ->
                factor.distance = isChecked
            }
            switchRating.setOnCheckedChangeListener { _, isChecked ->
                factor.rating = isChecked
            }
            switchReview.setOnCheckedChangeListener { _, isChecked ->
                factor.review = isChecked
            }
            btnTerapkan.setOnClickListener {
                viewModel.setFactor(factor)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}