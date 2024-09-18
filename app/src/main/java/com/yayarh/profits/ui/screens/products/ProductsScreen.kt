package com.yayarh.profits.ui.screens.products

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.mocks.FakeProductsRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.screens.orders.OrderDeletionDialog
import com.yayarh.profits.ui.theme.ProfitsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ProductsScreen(vm: ProductsVm = hiltViewModel(), navController: DestinationsNavigator) {

    /** State to hold the product to delete (used to show/hide the deletion dialog) */
    var productToDelete: ProductEntity? by remember { mutableStateOf(null) }

    Column(
        Modifier
            .fillMaxSize()
    ) {

        TopAppBar(
            title = { TextRes(R.string.products) },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                }
            }
        )

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

        Box(Modifier.fillMaxSize()) {
            if (vm.productsList.value.isEmpty()) {
                TextRes(R.string.no_products, Modifier.align(Alignment.Center))
            }

            LazyColumn {
                items(vm.productsList.value) { product ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        content = {
                            Column(
                                modifier = Modifier
                                    .combinedClickable(onLongClick = { productToDelete = product }, onClick = {})
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
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
        /** Show dialog to delete report if available */
        productToDelete?.let {
            ProductDeletionDialog(
                onDeleteClicked = { vm.deleteProduct(it); productToDelete = null },
                onCancelClicked = { productToDelete = null })
        }
    }
}

@Composable
fun ProductDeletionDialog(onDeleteClicked: () -> Unit, onCancelClicked: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onCancelClicked() },
        title = { TextRes(R.string.delete_product) },
        text = { TextRes(R.string.delete_this_product_q) },
        confirmButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { onDeleteClicked() },
                content = { TextRes(R.string.delete) }
            )
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { onCancelClicked() },
                content = { TextRes(R.string.cancel) }
            )
        }
    )
}

@Preview
@Composable
fun ProductsScreenPreview() {
    ProfitsTheme {
        Box(Modifier.background(Color.White)) {
            ProductsScreen(ProductsVm(FakeProductsRepo()), EmptyDestinationsNavigator)
        }
    }
}

@Preview
@Composable
fun OrderDeletionDialogPreview() {
    ProfitsTheme {
        OrderDeletionDialog({}, {})
    }
}
