package com.yayarh.profits.ui.screens.createOrder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.R
import com.yayarh.profits.common.nullIfEmpty
import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.base.OrdersRepo
import com.yayarh.profits.data.repos.base.ProductsRepo
import com.yayarh.profits.domain.models.CartItem
import com.yayarh.profits.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateOrderVm @Inject constructor(
    private val productsRepo: ProductsRepo,
    private val ordersRepo: OrdersRepo,
) : ViewModel() {

    private val _state = mutableStateOf<CreateOrderState>(CreateOrderState.Idle)
    val state: State<CreateOrderState> = _state

    private val _productsList: MutableStateFlow<List<ProductEntity>> = MutableStateFlow(emptyList())
    val productsList = _productsList.asStateFlow()

    private val _cartItems: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    val cartItems = _cartItems.asStateFlow()

    init {
        listenToProductsList()
    }

    private fun listenToProductsList() {
        viewModelScope.launch {
            productsRepo.getAllProducts().collect { _productsList.value = it }
        }
    }

    fun addToCart(product: ProductEntity, quantity: Int) {
        viewModelScope.launch {
            val itemAlreadyInRegister = cartItems.value.firstOrNull { it.product.id == product.id } != null

            if (itemAlreadyInRegister) {
                _cartItems.value = _cartItems.value.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else _cartItems.value += CartItem(product, quantity)
        }
    }

    fun removeFromCart(product: ProductEntity, quantity: Int) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity - quantity)
                else it
            }.filter { it.quantity > 0 }
        }
    }

    fun saveOrder() {
        viewModelScope.launch {
            try {
                val ordersList = generateOrdersList().nullIfEmpty() ?: return@launch
                _state.value = CreateOrderState.Loading

                ordersRepo.insertOrders(ordersList)

                _state.value = CreateOrderState.OrderSavedSuccessfully
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = CreateOrderState.Failure(UiText.fromOrDefault(e, R.string.failed_to_save_sales))
            }
        }
    }

    private fun generateOrdersList(): List<OrderEntity> {
        if (cartItems.value.isEmpty()) return emptyList()

        /** Generate a random order id to facilitate fetching the order items later */
        val orderId = Random(System.currentTimeMillis()).nextInt()

        return cartItems.value.map {
            OrderEntity(
                id = 0,
                quantity = it.quantity,
                orderId = orderId,
                product = it.product
            )
        }

    }

    fun setIdleState() {
        _state.value = CreateOrderState.Idle
    }

    sealed interface CreateOrderState {
        data object Loading : CreateOrderState
        data object Idle : CreateOrderState

        data object OrderSavedSuccessfully : CreateOrderState
        data class Failure(val msg: UiText) : CreateOrderState

    }

}