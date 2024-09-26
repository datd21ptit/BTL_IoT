package com.b21dccn216.smarthome.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import com.b21dccn216.smarthome.ui.screen.items

@Composable
fun BottomNavigationApp(
    onClickNavItem: (Pair<Int, String>) -> Unit,
    currentIndex: Int
){
    NavigationBar {
        items.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = index == currentIndex,
                onClick = { onClickNavItem(item.title) },
                icon = {
                    Icon(
                        imageVector = if(index == currentIndex) item.selectedIon else item.unselectedIcon,
                        contentDescription = null)
                })
        }
    }
}