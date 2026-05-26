package br.com.fiap.inovagab.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home          : BottomNavItem("home",          "Início",      Icons.Default.Home)
    object Strategies    : BottomNavItem("strategies",    "Estratégias", Icons.Default.GridView)
    object Notifications : BottomNavItem("notifications", "Avisos",      Icons.Default.Notifications)
    object Profile       : BottomNavItem("profile",       "Perfil",      Icons.Default.Person)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Strategies,
        BottomNavItem.Notifications,
        BottomNavItem.Profile
    )

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color(0xFF1B4F7A),
                    selectedTextColor   = Color(0xFF1B4F7A),
                    unselectedIconColor = Color(0xFF888888),
                    unselectedTextColor = Color(0xFF888888),
                    indicatorColor      = Color(0xFFE8F0FB)
                )
            )
        }
    }
}
