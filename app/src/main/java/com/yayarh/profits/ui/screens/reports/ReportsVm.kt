package com.yayarh.profits.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.repos.DailySalesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ReportsVm @Inject constructor(private val salesRepo: DailySalesRepo) : ViewModel() {

    private val _dailySales: MutableStateFlow<List<DailySalesEntity>> = MutableStateFlow(emptyList())
    val dailySales = _dailySales.asStateFlow()

    private val selectedMonth: MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val selectedMonthState = selectedMonth.asStateFlow()

    init {
        listenToSelectedMonth()
    }

    /** Listen to sales list changes for the selected month */
    private fun listenToSalesList() {
        viewModelScope.launch {
            val startDate = selectedMonth.value.atDay(1)
            val endDate = selectedMonth.value.atEndOfMonth()

            salesRepo.getDailySalesItems(startDate, endDate).collect { _dailySales.value = it }
        }
    }

    /** Listen to selected month changes and reload the sales list accordingly */
    private fun listenToSelectedMonth() {
        viewModelScope.launch {
            selectedMonth.collect {
                listenToSalesList()
            }
        }
    }

    fun incrementMonth() {
        selectedMonth.value = selectedMonth.value.plusMonths(1)
    }

    fun decrementMonth() {
        selectedMonth.value = selectedMonth.value.minusMonths(1)
    }

    fun getMonthProfits(): Int {
        return dailySales.value.sumOf { it.profits }
    }

    fun deleteDailySale(dailySale: DailySalesEntity) {
        viewModelScope.launch {
            salesRepo.deleteDailySalesItem(dailySale.id)
        }
    }

}