package com.yayarh.profits.ui.screens.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ProductsScreen(vm: ProductsVm = hiltViewModel(), navController: DestinationsNavigator) {
    Column {
        LazyColumn {
            items(vm.productsList.value) { product ->
                Card(modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = product.name)
                        Text(text = product.buyPrice.toString())
                        Text(text = product.sellPrice.toString())
                    }
                }
            }
        }
    }
}