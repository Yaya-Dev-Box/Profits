package com.yayarh.profits.ui.screens.createOrder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.data.repos.mocks.FakeOrdersRepo
import com.yayarh.profits.data.repos.mocks.FakeProductsRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.screens.createOrder.CreateOrderVm.CreateOrderState.Failure
import com.yayarh.profits.ui.screens.createOrder.CreateOrderVm.CreateOrderState.Idle
import com.yayarh.profits.ui.screens.createOrder.CreateOrderVm.CreateOrderState.Loading
import com.yayarh.profits.ui.screens.createOrder.CreateOrderVm.CreateOrderState.OrderSavedSuccessfully
import com.yayarh.profits.ui.theme.ProfitsTheme
import com.yayarh.profits.ui.utils.ShowToast

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun CreateOrderScreen(vm: CreateOrderVm = hiltViewModel(), navController: DestinationsNavigator) {
    val cartItems by vm.cartItems.collectAsState()
    val productsList by vm.productsList.collectAsState()


    when (val state = vm.state.value) {
        is Loading, is Idle -> Unit
        is Failure -> ShowToast(uiText = state.msg).also { vm.setIdleState() }
        is OrderSavedSuccessfully -> {
            ShowToast(R.string.order_saved_successfully)
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column {
                TopAppBar(
                    title = { TextRes(R.string.add_order) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            }
        }, content = { paddings ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddings)) {
                if (productsList.isEmpty()) {
                    TextRes(R.string.no_products, Modifier.align(Alignment.Center))
                }

                LazyColumn(
                    Modifier
                        .padding(top = 16.dp)
                ) {
                    items(productsList) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            content = {
                                Row {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillParentMaxHeight()
                                            .fillMaxWidth(0.8F),
                                        Arrangement.Center
                                    ) {
                                        Text(text = product.name)
                                        Text(text = product.sellPrice.toString())
                                    }
                                    // TODO: Long click to add many or remove many...
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .fillParentMaxWidth(0.2F),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        IconButton(
                                            onClick = { vm.addToCart(product, 1) },
                                            content = { Icon(painterResource(R.drawable.add), stringResource(R.string.add)) }
                                        )
                                        IconButton(
                                            onClick = { vm.removeFromCart(product) },
                                            content = { Icon(painterResource(R.drawable.remove), stringResource(R.string.delete)) }
                                        )
                                    }
                                }
                            })
                    }
                }
            }
        },
        bottomBar = {
            Column(Modifier.padding(horizontal = 16.dp)) {
                LazyColumn {
                    items(cartItems) { item ->
                        Box(Modifier.fillMaxWidth()) {
                            Text(text = item.product.name, modifier = Modifier.align(Alignment.CenterStart))
                            Text(text = "x" + item.quantity, modifier = Modifier.align(Alignment.CenterEnd))
                        }
                    }
                }
                if (cartItems.isNotEmpty()) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onClick = { vm.saveOrder() },
                        content = { TextRes(R.string.save_order) }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun CreateOrderScreenPreview() {
    val fakeProductsRepo = FakeProductsRepo()
    val fakeVm = CreateOrderVm(fakeProductsRepo, FakeOrdersRepo()).apply {
        addToCart(fakeProductsRepo.productsList[0], 1)
        addToCart(fakeProductsRepo.productsList[1], 2)
        addToCart(fakeProductsRepo.productsList[2], 3)
    }

    ProfitsTheme {
        CreateOrderScreen(vm = fakeVm, navController = EmptyDestinationsNavigator)
    }
}