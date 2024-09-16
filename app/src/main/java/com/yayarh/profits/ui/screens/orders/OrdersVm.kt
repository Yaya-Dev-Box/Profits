package com.yayarh.profits.ui.screens.orders

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.R
import com.yayarh.profits.data.repos.base.DailySalesRepo
import com.yayarh.profits.data.repos.base.OrdersRepo
import com.yayarh.profits.domain.models.CombinedOrderItem
import com.yayarh.profits.domain.utils.DataGenerators
import com.yayarh.profits.domain.utils.DataMappers.toCombinedOrderItem
import com.yayarh.profits.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrdersVm @Inject constructor(
    private val ordersRepo: OrdersRepo,
    private val salesRepo: DailySalesRepo
) : ViewModel() {

    private val _state = mutableStateOf<RegisterState>(RegisterState.Idle)
    val state: State<RegisterState> = _state

    private val _ordersList: MutableStateFlow<List<CombinedOrderItem>> = MutableStateFlow(emptyList())
    val ordersList = _ordersList.asStateFlow()

    private val _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    init {
        listenToOrdersList()
    }

    private fun listenToOrdersList() {
        viewModelScope.launch {
            ordersRepo.getAllOrders().collect {
                _ordersList.value = it.toCombinedOrderItem()
            }
        }
    }

    fun saveTodaySales() {
        viewModelScope.launch {
            try {
                val saleEntity = DataGenerators.generateDailySaleEntity(ordersList.value, selectedDate.value) ?: return@launch
                _state.value = RegisterState.Loading

                salesRepo.insertDailySalesItem(saleEntity)
                ordersRepo.deleteAllOrders()
                incrementDate()

                _state.value = RegisterState.SalesSavedSuccessfully
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = RegisterState.Failure(UiText.fromOrDefault(e, R.string.failed_to_save_sales))
            }
        }
    }

    fun incrementDate() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    fun decrementDate() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun setIdleState() {
        _state.value = RegisterState.Idle
    }

    fun deleteCombinedOrderItem(combinedOrder: CombinedOrderItem) {
        viewModelScope.launch {
            ordersRepo.deleteOrderByOrderId(combinedOrder.id)
        }
    }

    sealed interface RegisterState {
        data object Loading : RegisterState
        data object Idle : RegisterState

        data object SalesSavedSuccessfully : RegisterState
        data class Failure(val msg: UiText) : RegisterState

    }

}