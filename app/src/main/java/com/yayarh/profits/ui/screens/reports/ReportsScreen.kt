package com.yayarh.profits.ui.screens.reports

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.yayarh.profits.R
import com.yayarh.profits.common.toString
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.repos.mocks.FakeSalesRepo
import com.yayarh.profits.ui.composables.TextRes
import com.yayarh.profits.ui.theme.ProfitsTheme
import com.yayarh.profits.ui.utils.greenOrRed

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ReportsScreen(vm: ReportsVm = hiltViewModel(), navController: DestinationsNavigator) {

    /** State to hold the report to delete (used to show/hide the deletion dialog) */
    var reportToDelete: DailySalesEntity? by remember { mutableStateOf(null) }

    val selectedMonth by vm.selectedMonthState.collectAsState()
    val dailySales by vm.dailySales.collectAsState()

    Scaffold(
        modifier = Modifier.background(Color.White),
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
                            .clickable { vm.decrementMonth() }
                    )
                    Text(
                        text = selectedMonth.toString("MMMM yyyy"),
                        modifier = Modifier.align(Alignment.Center),
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next_day),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { vm.incrementMonth() }
                    )
                }
            }
        }, content = { paddings ->
            Box(
                Modifier
                    .padding(paddings)
                    .fillMaxSize()
            ) {
                if (dailySales.isEmpty()) {
                    TextRes(R.string.no_sales, Modifier.align(Alignment.Center))
                }
                LazyColumn {
                    items(dailySales) { dailySale ->
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            content = {
                                Column(
                                    modifier = Modifier
                                        .combinedClickable(onLongClick = { reportToDelete = dailySale }, onClick = {})
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = dailySale.date.toString("dd MMMM yyyy"))
                                    Text(text = dailySale.salesList)
                                    Card(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp)
                                    ) {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.value_dzd, dailySale.profits.toString()),
                                                modifier = Modifier.align(Alignment.Center),
                                                color = greenOrRed(dailySale.profits)
                                            )
                                        }
                                    }
                                }
                            })
                    }
                }

                /** Show dialog to delete report if available */
                reportToDelete?.let {
                    ReportDeletionDialog(
                        onDeleteClicked = { vm.deleteDailySale(it); reportToDelete = null },
                        onCancelClicked = { reportToDelete = null })
                }
            }
        },
        bottomBar = {
            if (dailySales.isNotEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                {
                    val profits = vm.getMonthProfits()
                    Text(
                        stringResource(R.string.value_dzd, profits),
                        Modifier.align(Alignment.Center),
                        color = greenOrRed(profits)
                    )
                }
            }
        }
    )

}

@Composable
fun ReportDeletionDialog(onDeleteClicked: () -> Unit, onCancelClicked: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onCancelClicked() },
        title = { TextRes(R.string.delete_report) },
        text = { TextRes(R.string.delete_this_report_q) },
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
fun ReportsScreenPreview() {
    val fakeVm = ReportsVm(FakeSalesRepo())
    ProfitsTheme {
        ReportsScreen(vm = fakeVm, navController = EmptyDestinationsNavigator)
    }
}

@Preview
@Composable
fun EmptyReportsScreenPreview() {
    val fakeVm = ReportsVm(FakeSalesRepo())
    fakeVm.incrementMonth()

    ProfitsTheme {
        ReportsScreen(vm = fakeVm, navController = EmptyDestinationsNavigator)
    }
}

@Preview
@Composable
fun ReportDeletionDialogPreview() {
    ProfitsTheme {
        ReportDeletionDialog({}, {})
    }
}
