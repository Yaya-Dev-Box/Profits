package com.yayarh.profits.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.common.toString
import com.yayarh.profits.data.repos.mocks.FakeProductsRepo
import com.yayarh.profits.data.repos.mocks.FakeRegisterRepo
import com.yayarh.profits.data.repos.mocks.FakeSalesRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Failure
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Idle
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Loading
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.SalesSavedSuccessfully
import com.yayarh.profits.ui.theme.ProfitsTheme
import com.yayarh.profits.ui.utils.ShowToast
import java.time.LocalDate
import java.util.Date

@Destination<RootGraph>(start = true)
@Composable
fun RegisterScreen(vm: RegisterVm = hiltViewModel(), navController: DestinationsNavigator) {
    var showSaveConfirmDialog by remember { mutableStateOf(false) }

    when (val state = vm.state.value) {
        is Loading, is Idle -> Unit
        is Failure -> ShowToast(uiText = state.msg).also { vm.setIdleState() }
        is SalesSavedSuccessfully -> ShowToast(R.string.sales_saved_successfully).also { vm.setIdleState() }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Box(Modifier.fillMaxWidth()) {
                // TODO: Make date changeable
                Text(text = Date().toString(), modifier = Modifier.align(Alignment.Center))
            }
        }, bottomBar = {
            Column(Modifier.padding(horizontal = 16.dp)) {
                LazyColumn {
                    items(vm.registerItems.value) { item ->
                        Box(Modifier.fillMaxWidth()) {
                            Text(text = item.name, modifier = Modifier.align(Alignment.CenterStart))
                            Text(text = "x" + item.amount, modifier = Modifier.align(Alignment.CenterEnd))
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    onClick = { showSaveConfirmDialog = vm.registerItems.value.isNotEmpty() },
                    content = { TextRes(R.string.end_of_day) }
                )
            }
        }, content = { paddings ->
            LazyColumn(
                Modifier
                    .padding(paddings)
                    .padding(top = 16.dp)
            ) {
                items(vm.productsList.value) { product ->
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
                                // TODO: Glow the date when it's not today?
                                // TODO: Long click to add many or remove many...
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .fillParentMaxWidth(0.2F),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    IconButton(
                                        onClick = { vm.addToRegister(product) },
                                        content = { Icon(Icons.Outlined.Add, stringResource(R.string.add)) }
                                    )
                                    // TODO: Subtract icon...
                                    IconButton(
                                        onClick = { vm.removeFromRegister(product) },
                                        content = { Icon(Icons.Filled.Delete, stringResource(R.string.delete)) }
                                    )
                                }
                            }
                        })
                }
            }

            if (showSaveConfirmDialog) {
                EndOfDayDialog(
                    date = vm.selectedDate.value,
                    onSave = { vm.saveTodaySales() },
                    onDismiss = { showSaveConfirmDialog = false }
                )
            }

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndOfDayDialog(date: LocalDate, onSave: () -> Unit, onDismiss: () -> Unit) {
    BasicAlertDialog(onDismiss) {
        Card {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    TextRes(R.string.save_today_sales)
                    Text(date.toString("dd-MM-yyyy"), color = if (date == LocalDate.now()) Color.Green else Color.Red)
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onSave()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(id = R.string.save))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    ProfitsTheme {
        val vm = RegisterVm(FakeProductsRepo(), FakeRegisterRepo(), FakeSalesRepo())
        RegisterScreen(vm = vm, EmptyDestinationsNavigator)
    }
}

@Preview
@Composable
fun SaveDialogPreview() {
    ProfitsTheme {
        EndOfDayDialog(LocalDate.now().minusDays(1), {}, {})
    }
}