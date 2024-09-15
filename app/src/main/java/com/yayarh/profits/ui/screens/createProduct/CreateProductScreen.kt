package com.yayarh.profits.ui.screens.createProduct

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.Room
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.data.MyDatabase
import com.yayarh.profits.data.repos.imps.ProductsRepoImpl
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Failure
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Idle
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Loading
import com.yayarh.profits.ui.screens.createProduct.CreateProductVm.CreateProductState.Success
import com.yayarh.profits.ui.theme.ProfitsTheme
import com.yayarh.profits.ui.theme.defaultTextFieldColors
import com.yayarh.profits.ui.utils.UiText.Companion.getText

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun CreateProductsScreen(vm: CreateProductVm = hiltViewModel(), navController: DestinationsNavigator) {

    val state by vm.state.collectAsState()

    when (val it = state) {
        is Success -> {
            val ctx = LocalContext.current
            Toast.makeText(ctx, it.successMsg.getText(ctx), Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }

        is Failure -> {
            val ctx = LocalContext.current
            Toast.makeText(ctx, it.errorMsg.getText(ctx), Toast.LENGTH_SHORT).show()
            vm.setIdleState()
        }

        Idle -> Unit
        Loading -> Unit
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column {
                TopAppBar(
                    title = { TextRes(R.string.add_product) },
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
            Column(
                modifier = Modifier
                    .padding(paddings)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { TextRes(R.string.name) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    value = vm.name,
                    onValueChange = { vm.name = it },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = defaultTextFieldColors()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { TextRes(R.string.buy_price) },
                    value = vm.buyingPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    suffix = { TextRes(R.string.dzd) },
                    colors = defaultTextFieldColors(),
                    onValueChange = {
                        if (it.isNotEmpty() && it.toIntOrNull() == null) return@TextField
                        vm.buyingPrice = it
                    })

                Spacer(Modifier.height(8.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { TextRes(R.string.sell_price) },
                    value = vm.sellingPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    singleLine = true,
                    suffix = { TextRes(R.string.dzd) },
                    shape = RoundedCornerShape(16.dp),
                    colors = defaultTextFieldColors(),
                    onValueChange = {
                        if (it.isNotEmpty() && it.toIntOrNull() == null) return@TextField
                        vm.sellingPrice = it
                    })

            }

        }, bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                enabled = state != Loading,
                onClick = { vm.saveProduct() },
                content = {
                    TextRes(resId = if (state == Loading) R.string.loading else R.string.save)
                }
            )

        }
    )
}

@Preview
@Composable
fun CreateProductScreenPreview() {
    val mockRoom = Room.inMemoryDatabaseBuilder(LocalContext.current, MyDatabase::class.java).build()
    val mockVm = CreateProductVm(ProductsRepoImpl(mockRoom.productsDao()))

    ProfitsTheme {
        CreateProductsScreen(vm = mockVm, navController = EmptyDestinationsNavigator)
    }
}