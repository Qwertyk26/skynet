package ru.spb.skynet.lk.data.models.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route = "home"
        ),
        BarItem(
            title = "News",
            image = Icons.Filled.Newspaper,
            route = "news"
        ),
        BarItem(
            title = "Connect",
            image = Icons.Filled.AddCircleOutline,
            route = "connect"
        ),
        BarItem(
            title = "Settings",
            image = Icons.Filled.Settings,
            route = "settings"
        )
    )
}