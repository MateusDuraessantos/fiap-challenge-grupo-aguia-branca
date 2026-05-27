package br.com.fiap.inovagab.ui.components

import androidx.compose.runtime.Composable

@Composable
fun LiderBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    BottomNavBar(
        currentRoute = currentRoute,
        items        = liderNavItems,
        onNavigate   = onNavigate
    )
}
