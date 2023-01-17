package com.kiki.searchcustomization.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kiki.searchcustomization.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}