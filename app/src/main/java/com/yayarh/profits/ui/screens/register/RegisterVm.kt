package com.yayarh.profits.ui.screens.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.R
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.base.DailySalesRepo
import com.yayarh.profits.data.repos.base.OrdersRepo
import com.yayarh.profits.domain.models.CombinedOrderItem
import com.yayarh.profits.domain.utils.DataMappers.toCombinedOrderItem
import com.yayarh.profits.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegisterVm @Inject constructor(
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
                val saleEntity = generateDailySaleEntity() ?: return@launch
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

    private fun generateDailySaleEntity(): DailySalesEntity? {
        if (ordersList.value.isEmpty()) return null

        // TODO: Unit tests for these functions...

        val fullCartList = ordersList.value.map { it.products }.flatten()

        val productsQuantities: Map<ProductEntity, Int> = buildMap {
            fullCartList.forEach {
                val quantityInMap = this.getOrDefault(it.product, 0)
                put(it.product, quantityInMap + it.quantity)
            }
        }

        val saleSummary = productsQuantities.entries.joinToString("") { "${it.key.name} x${it.value} \n" }.trim()

        val totalSales = fullCartList.sumOf { it.product.buyPrice * it.quantity }
        val totalProfits = fullCartList.sumOf { (it.product.sellPrice - it.product.buyPrice) * it.quantity }

        return DailySalesEntity(0, _selectedDate.value, saleSummary, totalSales, totalProfits)
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

    sealed interface RegisterState {
        data object Loading : RegisterState
        data object Idle : RegisterState

        data object SalesSavedSuccessfully : RegisterState
        data class Failure(val msg: UiText) : RegisterState

    }

}