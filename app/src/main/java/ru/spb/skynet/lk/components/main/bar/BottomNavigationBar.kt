package ru.spb.skynet.lk.components.main.bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.spb.skynet.lk.data.models.components.bar.NavBarItems
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.image,
                        contentDescription = navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,          // Цвет иконки при выборе
                    unselectedIconColor = Color.Gray,        // Цвет иконки в пассивном состоянии
                    selectedTextColor = SkynetGreen,          // Цвет текста при выборе
                    unselectedTextColor = Color.Black,        // Цвет текста в пассивном состоянии
                    indicatorColor = SkynetGreen       // Цвет "таблетки" вокруг активной иконки
                )
            )
        }
    }
}