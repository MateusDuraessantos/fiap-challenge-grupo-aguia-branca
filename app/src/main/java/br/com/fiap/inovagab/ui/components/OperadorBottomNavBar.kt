package br.com.fiap.inovagab.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

private val operadorItems = listOf(
    BottomNavItem("operador_home",          "Início",      Icons.Default.Home),
    BottomNavItem("operador_strategies",    "Estratégias", Icons.Default.GridView),
    BottomNavItem("operador_notifications", "Avisos",      Icons.Default.Notifications),
    BottomNavItem("operador_profile",       "Perfil",      Icons.Default.Person)
)

@Composable
fun OperadorBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        operadorItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick  = { onNavigate(item.route) },
                icon     = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label    = { Text(item.label, fontSize = 10.sp) },
                colors   = NavigationBarItemDefaults.colors(
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
