package com.b21dccn216.smarthome

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.b21dccn216.smarthome.model.BottomNavigationItem
import com.b21dccn216.smarthome.model.Destinations.ACTION_DATA_TABLE
import com.b21dccn216.smarthome.model.Destinations.DASHBOARD
import com.b21dccn216.smarthome.model.Destinations.PROFILE
import com.b21dccn216.smarthome.model.Destinations.SENSOR_DATA_TABLE
import com.b21dccn216.smarthome.model.uistate.AppState.LOADING
import com.b21dccn216.smarthome.ui.components.BottomNavigationApp
import com.b21dccn216.smarthome.ui.screen.DashboardScreen
import com.b21dccn216.smarthome.ui.screen.LoadingScreen
import com.b21dccn216.smarthome.ui.screen.ProfileScreen
import com.b21dccn216.smarthome.ui.screen.TableScreen

val items = listOf(
    BottomNavigationItem(
        DASHBOARD,
        selectedIon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
    ),
    BottomNavigationItem(
        SENSOR_DATA_TABLE,
        selectedIon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List,
        hasNews = false,
    ),
    BottomNavigationItem(
        ACTION_DATA_TABLE,
        selectedIon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow,
        hasNews = false,
    ),
    BottomNavigationItem(
        PROFILE,
        selectedIon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        hasNews = false,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmarthomeNavigation(
    viewmodel: SmartHomeViewmodel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val currentScreen by viewmodel.currentScreen.collectAsState()
    val appState by viewmodel.appState.collectAsState()
    val uiStateTable by viewmodel.uiStateTable.collectAsState()

    Scaffold(
        topBar = {
            if(currentScreen != PROFILE){
                TopAppBar(title = { Text(text = getScreenTitle(currentScreen)) })
            }
        },
        bottomBar = {
            BottomNavigationApp(
                onClickNavItem = {
                    navController.navigate(route = it.second)
                    viewmodel.navigateTo(screen = it)
                },
                currentIndex = currentScreen.first,
            )
        }
    ) { innerPadding ->
        if(appState == LOADING){
            LoadingScreen(Modifier.padding(innerPadding))
        }else{
            NavHost(
                navController = navController,
                startDestination = DASHBOARD.second
            ) {
                composable(DASHBOARD.second,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                    },
                    exitTransition = {
                         fadeOut()
                    },
                ) {
                    DashboardScreen(
                        viewmodel = viewmodel,
                        innerPadding = innerPadding)
                }
                composable(SENSOR_DATA_TABLE.second,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = {
                            val previous = navController.previousBackStackEntry?.destination?.route
                            when(previous){
                                DASHBOARD.second -> 1000
                                else -> -1000
                            }}) + fadeIn()
                    },
                    exitTransition = {
                        fadeOut()
                    }) {
                    val sensorTitleColumn = remember { listOf("Temp", "Humid", "Light", "Wind") }
//                    val sensorTableData = remember {  }
                    TableScreen(
                        viewmodel = viewmodel,
                        titleColumn = sensorTitleColumn,
                        innerPadding = innerPadding,
                        tableData = uiStateTable.tableSensorData,
                        countTurnOn = uiStateTable.count,
                    )
                }
                composable(ACTION_DATA_TABLE.second,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = {
                            when(navController.previousBackStackEntry?.destination?.route){
                                PROFILE.second -> -1000
                                else -> 1000
                            }
                        }) + fadeIn()
                    },
                    exitTransition = {
                        fadeOut()
                    }) {
                    val actionTitleColumn = remember { listOf("Device", "State") }
//                    val actionTableData = remember { a.tableActionData }

                    TableScreen(
                        viewmodel = viewmodel,
                        titleColumn = actionTitleColumn,
                        innerPadding = innerPadding,
                        tableData = uiStateTable.tableActionData,
                        countTurnOn = uiStateTable.count
                    )
                }
                composable(PROFILE.second,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                    },
                    exitTransition = {
                         fadeOut()
                    }) {
                    ProfileScreen(modifier = Modifier, innerPadding = innerPadding)
                }
            }
        }

    }
}

private fun getScreenTitle(currentScreen: Pair<Int, String>): String {
    return when (currentScreen.second) {
        DASHBOARD.second -> "Dashboard"
        SENSOR_DATA_TABLE.second -> "Sensor Table"
        ACTION_DATA_TABLE.second -> "Control Table"
        PROFILE.second -> "Profile"
        else -> "Unknown"
    }
}