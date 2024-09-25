package com.b21dccn216.smarthome.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: Pair<Int, String>,
    val selectedIon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)