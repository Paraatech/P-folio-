package com.parastech.firstapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.parastech.firstapp.model.Portfolio

class PortfolioViewModel : ViewModel() {

    private val _portfolio = mutableStateOf(Portfolio())
    val portfolio: State<Portfolio> = _portfolio

    fun updatePortfolio(updater: (Portfolio) -> Portfolio) {
        _portfolio.value = updater(_portfolio.value)
    }

}