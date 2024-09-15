package com.yayarh.profits.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateOrderScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.common.toString
import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.repos.mocks.FakeOrdersRepo
import com.yayarh.profits.data.repos.mocks.FakeSalesRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Failure
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Idle
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.Loading
import com.yayarh.profits.ui.screens.register.RegisterVm.RegisterState.SalesSavedSuccessfully
import com.yayarh.profits.ui.theme.ProfitsTheme
import com.yayarh.profits.ui.utils.ShowToast
import com.yayarh.profits.ui.utils.greenOrRed
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

@Destination<RootGraph>(start = true)
@Composable
fun RegisterScreen(vm: RegisterVm = hiltViewModel(), navController: DestinationsNavigator) {
    var showSaveConfirmDialog by remember { mutableStateOf(false) }
    val ordersList by vm.ordersList.collectAsState()
    val selectedDate by vm.selectedDate.collectAsState()


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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), shape = CutCornerShape(25.dp), elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .clickable { }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.previous_day),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable { vm.decrementDate() }
                    )
                    Text(
                        text = selectedDate.toString("dd-MM-yyyy"),
                        modifier = Modifier.align(Alignment.Center),
                        color = greenOrRed(selectedDate)
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next_day),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { vm.incrementDate() }
                    )
                }
            }
        }, content = { paddings ->
            Box(Modifier.fillMaxSize()) {
                if (ordersList.isEmpty()) {
                    TextRes(R.string.no_orders, Modifier.align(Alignment.Center))
                }

                LazyColumn(
                    Modifier
                        .padding(paddings)
                        .padding(top = 16.dp)
                ) {
                    items(ordersList) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            content = {
                                Row {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(0.8F),
                                        Arrangement.Center
                                    ) {
                                        Text(text = product.generateSaleSummary())
                                        val profits = product.getProfits()
                                        Text(text = profits.toString(), color = greenOrRed(profits))
                                    }
                                }
                            })
                    }
                }
            }

            if (showSaveConfirmDialog) {
                EndOfDayDialog(
                    date = selectedDate,
                    onSave = { vm.saveTodaySales(); showSaveConfirmDialog = false },
                    onDismiss = { showSaveConfirmDialog = false }
                )
            }

        },
        bottomBar = {
            Column(Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    shape = RoundedCornerShape(25.dp),
                    onClick = { navController.navigate(CreateOrderScreenDestination) },
                    content = { TextRes(R.string.plus_button, fontSize = 24.sp) }
                )


                if (ordersList.isNotEmpty()) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onClick = { showSaveConfirmDialog = ordersList.isNotEmpty() },
                        content = { TextRes(R.string.end_of_day) }
                    )
                }
            }
        }
    )

}

@Composable
fun EndOfDayDialog(date: LocalDate, onSave: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { TextRes(R.string.end_of_day) },
        text = {
            Column {
                TextRes(R.string.save_current_orders)
                Text(date.toString("dd-MM-yyyy"), color = greenOrRed(date))
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { onSave() },
                content = { TextRes(R.string.save) }
            )
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { onDismiss() },
                content = { TextRes(R.string.cancel) }
            )
        }
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
    ProfitsTheme {
        val vm = RegisterVm(FakeOrdersRepo(), FakeSalesRepo())
        RegisterScreen(vm = vm, EmptyDestinationsNavigator)
    }
}

@Preview
@Composable
fun EmptyRegisterScreenPreview() {
    ProfitsTheme {
        val vm = RegisterVm(object : FakeOrdersRepo() {
            override suspend fun getAllOrders() = flowOf<List<OrderEntity>>(emptyList())
        }, FakeSalesRepo())
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