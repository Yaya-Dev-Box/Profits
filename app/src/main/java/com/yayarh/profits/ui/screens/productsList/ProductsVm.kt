package com.yayarh.profits.ui.screens.productsList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.base.ProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsVm @Inject constructor(private val productsRepo: ProductsRepo) : ViewModel() {

    private val _productsList: MutableState<List<ProductEntity>> = mutableStateOf(emptyList())
    val productsList: State<List<ProductEntity>> = _productsList

    init {
        listenToProductsList()
    }

    private fun listenToProductsList() {
        viewModelScope.launch {
            productsRepo.getAllProducts().collect { _productsList.value = it }
        }
    }

}