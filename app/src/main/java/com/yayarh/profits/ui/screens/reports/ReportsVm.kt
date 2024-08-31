package com.yayarh.profits.ui.screens.reports

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.repos.DailySalesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ReportsVm @Inject constructor(private val salesRepo: DailySalesRepo) : ViewModel() {

    private val _dailySales: MutableState<List<DailySalesEntity>> = mutableStateOf(emptyList())
    val dailySales: State<List<DailySalesEntity>> = _dailySales

    init {
        listenToSalesList()
    }

    private fun listenToSalesList() {
        viewModelScope.launch {
            val startDate = YearMonth.now().atDay(1)
            val endDate = YearMonth.now().atEndOfMonth()

            salesRepo.getDailySalesItems(startDate, endDate).collect { _dailySales.value = it }
        }
    }

}