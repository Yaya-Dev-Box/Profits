package com.yayarh.profits.ui.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ReportsScreen(vm: ReportsVm = hiltViewModel(), navController: DestinationsNavigator) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn {
            items(vm.dailySales.value) { dailySale ->
                Card(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = dailySale.date.toString())
                            Text(text = dailySale.salesList)
                            Text(text = dailySale.totalSales.toString())
                            Text(text = dailySale.profits.toString())
                        }
                    })
            }
        }

    }

}