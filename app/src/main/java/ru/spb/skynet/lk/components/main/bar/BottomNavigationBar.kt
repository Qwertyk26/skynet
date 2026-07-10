package ru.spb.skynet.lk.components.main.bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.spb.skynet.lk.data.models.components.bar.NavBarItems
import ru.spb.skynet.lk.data.models.components.bar.NavRoutes
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar(containerColor = Color.White) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            val isSelected = currentRoute == navItem.route ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.Notifications.route) ||
                    (navItem.route == NavRoutes.Settings.route && currentRoute == NavRoutes.ChangePinCode.route) ||
                    (navItem.route == NavRoutes.Settings.route && currentRoute == NavRoutes.ChangePassword.route) ||
                    (navItem.route == NavRoutes.Settings.route && currentRoute == NavRoutes.Sessions.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.PaymentsHistory.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.BankCard.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.Orders.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.Checks.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.CashReceipt.route) ||
                    (navItem.route == NavRoutes.Home.route && currentRoute == NavRoutes.Chat.route)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute == NavRoutes.Notifications.route && navItem.route == NavRoutes.Home.route
                        || currentRoute == NavRoutes.ChangePinCode.route && navItem.route == NavRoutes.Settings.route ||
                        currentRoute == NavRoutes.ChangePassword.route && navItem.route == NavRoutes.Settings.route ||
                        currentRoute == NavRoutes.Sessions.route && navItem.route == NavRoutes.Settings.route ||
                        currentRoute == NavRoutes.PaymentsHistory.route && navItem.route == NavRoutes.Home.route ||
                        currentRoute == NavRoutes.BankCard.route && navItem.route == NavRoutes.Home.route ||
                        currentRoute == NavRoutes.Orders.route && navItem.route == NavRoutes.Home.route ||
                        currentRoute == NavRoutes.Checks.route && navItem.route == NavRoutes.Home.route ||
                        currentRoute == NavRoutes.CashReceipt.route && navItem.route == NavRoutes.Home.route ||
                        currentRoute == NavRoutes.Chat.route && navItem.route == NavRoutes.Home.route) {
                            navController.popBackStack()
                    } else if (currentRoute != navItem.route) {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(imageVector = navItem.image,
                        contentDescription = navItem.title)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SkynetGreen,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}