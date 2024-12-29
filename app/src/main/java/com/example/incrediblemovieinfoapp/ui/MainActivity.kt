package com.example.incrediblemovieinfoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.incrediblemovieinfoapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : ActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        }
}