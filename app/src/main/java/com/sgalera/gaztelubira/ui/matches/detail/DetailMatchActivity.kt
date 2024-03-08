package com.sgalera.gaztelubira.ui.matches.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityDetailMatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMatchBinding
    private val matchViewModel: DetailMatchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)
        binding = ActivityDetailMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}