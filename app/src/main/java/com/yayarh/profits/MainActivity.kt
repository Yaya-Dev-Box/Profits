package com.yayarh.profits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.OrdersScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProductsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ReportsScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.ramcosta.composedestinations.utils.startDestination
import com.yayarh.profits.ui.theme.ProfitsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfitsTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    },
                    content = { padding ->
                        DestinationsNavHost(
                            navController = navController,
                            modifier = Modifier.padding(padding),
                            navGraph = NavGraphs.root
                        )
                    })
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val destinationsNavigator = navController.rememberDestinationsNavigator()
    val currentDestination: DestinationSpec = navController.currentDestinationAsState().value ?: NavGraphs.root.startDestination

    /** Hide the BottomBar if the current destination isn't a [BottomBarDestination] */
    if (currentDestination !in BottomBarDestination.entries.map { it.direction }) return

    NavigationBar {
        BottomBarDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    destinationsNavigator.navigate(destination.direction) {
                        popUpTo(NavGraphs.root.startDestination) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = stringResource(destination.label)) },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

enum class BottomBarDestination(val direction: DirectionDestinationSpec, val icon: ImageVector, @StringRes val label: Int) {
    Register(OrdersScreenDestination, Icons.Default.Home, R.string.register),
    Articles(ProductsScreenDestination, Icons.AutoMirrored.Filled.List, R.string.products),
    Reports(ReportsScreenDestination, Icons.Default.Star, R.string.reports),
}