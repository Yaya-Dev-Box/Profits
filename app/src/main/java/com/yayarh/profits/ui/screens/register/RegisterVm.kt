package com.yayarh.profits.ui.screens.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.R
import com.yayarh.profits.common.zeroIfNull
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.models.RegisterEntity
import com.yayarh.profits.data.repos.DailySalesRepo
import com.yayarh.profits.data.repos.ProductsRepo
import com.yayarh.profits.data.repos.RegisterRepo
import com.yayarh.profits.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegisterVm @Inject constructor(
    private val productsRepo: ProductsRepo,
    private val registerRepo: RegisterRepo,
    private val salesRepo: DailySalesRepo
) : ViewModel() {

    private val _state = mutableStateOf<RegisterState>(RegisterState.Idle)
    val state: State<RegisterState> = _state

    private val _productsList: MutableState<List<ProductEntity>> = mutableStateOf(emptyList())
    val productsList: State<List<ProductEntity>> = _productsList

    private val _registerItems: MutableStateFlow<List<RegisterEntity>> = MutableStateFlow(emptyList())
    val registerItems: StateFlow<List<RegisterEntity>> = _registerItems

    var selectedDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now())

    init {
        listenToProductsList()
        listenToRegisterItems()
    }

    private fun listenToProductsList() {
        viewModelScope.launch {
            productsRepo.getAllProducts().collect { _productsList.value = it }
        }
    }

    private fun listenToRegisterItems() {
        viewModelScope.launch {
            registerRepo.getRegisterItems().collect { _registerItems.value = it }
        }
    }

    fun addToRegister(product: ProductEntity) {
        viewModelScope.launch {
            val registerItem = RegisterEntity(product.id, product.name, 1)
            val itemAlreadyInRegister = registerItems.value.firstOrNull { it.id == registerItem.id } != null

            if (itemAlreadyInRegister) registerRepo.updateRegisterItemAmount(registerItem.id, 1)
            else registerRepo.insertRegisterItem(registerItem)
        }
    }

    fun removeFromRegister(product: ProductEntity) {
        viewModelScope.launch {
            val registerItem = RegisterEntity(product.id, product.name, 1)
            val itemAlreadyInRegister = registerItems.value.firstOrNull { it.id == registerItem.id } != null

            if (itemAlreadyInRegister) {
                val item = registerItems.value.first { it.id == registerItem.id }
                if (item.amount <= 1) registerRepo.deleteRegisterItem(registerItem.id)
                else registerRepo.updateRegisterItemAmount(registerItem.id, -1)
            }
        }
    }

    fun saveTodaySales() {
        viewModelScope.launch {
            try {
                val saleEntity = generateDailySaleEntity() ?: return@launch
                _state.value = RegisterState.Loading

                salesRepo.insertDailySalesItem(saleEntity)
                registerRepo.clearRegister()
                selectedDate.value = selectedDate.value.plusDays(1)

                _state.value = RegisterState.SalesSavedSuccessfully
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = RegisterState.Failure(UiText.fromOrDefault(e, R.string.failed_to_save_sales))
            }
        }
    }

    private fun generateDailySaleEntity(): DailySalesEntity? {
        if (registerItems.value.isEmpty()) return null

        val saleSummary = registerItems.value.joinToString(", ") { it.name + " x" + it.amount }
        val totalSales = registerItems.value.sumOf {
            val item = _productsList.value.firstOrNull { prod -> prod.id == it.id }
            item?.buyPrice.zeroIfNull() * it.amount
        }
        val totalProfits = registerItems.value.sumOf {
            val item = _productsList.value.firstOrNull { prod -> prod.id == it.id }
            item?.getProfit().zeroIfNull() * it.amount
        }

        return DailySalesEntity(0, selectedDate.value, saleSummary, totalSales, totalProfits)
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