package com.yayarh.profits.ui.screens.productsList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateProductsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.data.repos.mocks.FakeProductsRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.theme.ProfitsTheme

@Destination<RootGraph>
@Composable
fun ProductsScreen(vm: ProductsVm = hiltViewModel(), navController: DestinationsNavigator) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

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

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(25.dp),
            onClick = { navController.navigate(CreateProductsScreenDestination) },
            content = { TextRes(R.string.plus_button, fontSize = 24.sp) }
        )

    }

}

@Preview
@Composable
fun ProductsScreenPreview() {
    ProfitsTheme {
        ProductsScreen(ProductsVm(FakeProductsRepo()), EmptyDestinationsNavigator)
    }

}