package com.yayarh.profits.ui.screens.createProduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.R
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.ProductsRepo
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Failure
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Idle
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Loading
import com.yayarh.profits.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductVm @Inject constructor(private val productsRepo: ProductsRepo) : ViewModel() {

    private val _state: MutableStateFlow<CreateProductState> = MutableStateFlow(Idle)
    val state: StateFlow<CreateProductState> = _state

    var name: String by mutableStateOf("")
    var buyingPrice: String by mutableStateOf("")
    var sellingPrice: String by mutableStateOf("")

    fun saveProduct() {
        _state.value = Loading

        val product = validateAndGenerateProductItem()

        if (product == null) {
            _state.value = Failure(UiText.from(R.string.fill_all_fields))
            return
        }

        viewModelScope.launch {
            try {
                productsRepo.insertProduct(product)
                _state.value = CreateProductState.Success(UiText.from(R.string.product_saved_successfully))
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = Failure(UiText.fromOrDefault(e.message, R.string.failed_to_save_product))
            }
        }
    }

    // TODO: Negative sell and buy...

    private fun validateAndGenerateProductItem(): ProductEntity? {
        val name = name.trim()
        val buyPrice = buyingPrice.toIntOrNull()
        val sellPrice = sellingPrice.toIntOrNull()

        if (name.isEmpty() || buyPrice == null || sellPrice == null) {
            return null
        }

        return ProductEntity(
            id = 0,
            name = name,
            buyPrice = buyingPrice.toInt(),
            sellPrice = sellingPrice.toInt()
        )
    }

    fun setIdleState() {
        _state.value = Idle
    }

    sealed interface CreateProductState {
        data object Idle : CreateProductState
        data object Loading : CreateProductState

        class Success(val successMsg: UiText) : CreateProductState
        class Failure(val errorMsg: UiText) : CreateProductState
    }


}