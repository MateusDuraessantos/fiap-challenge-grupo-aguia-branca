package br.com.fiap.inovagab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NavSelected   = Color(0xFF1B4F7A)
private val NavUnselected = Color(0xFF888888)

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home          : BottomNavItem("home",          "Início",      Icons.Default.Home)
    object Strategies    : BottomNavItem("strategies",    "Estratégias", Icons.Default.GridView)
    object Notifications : BottomNavItem("notifications", "Avisos",      Icons.Default.Notifications)
    object Projetos      : BottomNavItem("projetos",      "Projetos",    Icons.Default.Folder)
    object Profile       : BottomNavItem("profile",       "Perfil",      Icons.Default.Person)
}

val gestorNavItems    = listOf(BottomNavItem.Home, BottomNavItem.Strategies, BottomNavItem.Projetos, BottomNavItem.Notifications, BottomNavItem.Profile)
val liderNavItems     = listOf(BottomNavItem.Home, BottomNavItem.Strategies, BottomNavItem.Projetos, BottomNavItem.Notifications, BottomNavItem.Profile)
val operadorNavItems  = listOf(BottomNavItem.Home, BottomNavItem.Strategies, BottomNavItem.Notifications, BottomNavItem.Profile)

@Composable
fun BottomNavBar(
    currentRoute: String,
    items: List<BottomNavItem> = gestorNavItems,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = Color(0xFFEAEAEA), thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val color = if (isSelected) NavSelected else NavUnselected

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication       = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector    = item.icon,
                        contentDescription = item.label,
                        tint           = color,
                        modifier       = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text       = item.label,
                        fontSize   = 10.sp,
                        color      = color,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
