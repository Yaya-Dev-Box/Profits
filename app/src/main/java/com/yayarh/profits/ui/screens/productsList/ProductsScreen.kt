package com.yayarh.profits.ui.screens.productsList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateProductsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.ui.composables.TextRes

@Destination<RootGraph>
@Composable
fun ProductsScreen(vm: ProductsVm = hiltViewModel(), navController: DestinationsNavigator) {
    Box(Modifier.fillMaxSize()) {

        LazyColumn {
            items(vm.productsList.value) { product ->
                Card(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = product.name)
                            Text(text = product.buyPrice.toString())
                            Text(text = product.sellPrice.toString())
                        }
                    })
            }
        }

        // TODO: Custom icons for each item (tacos, burgers Pizzas, omelettes...)

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            onClick = { navController.navigate(CreateProductsScreenDestination) },
            content = { TextRes(R.string.plus_button) }
        )

    }

}