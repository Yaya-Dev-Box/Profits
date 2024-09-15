package com.yayarh.profits.domain.models

import com.yayarh.profits.data.models.ProductEntity

data class CartItem(val product: ProductEntity, val quantity: Int)