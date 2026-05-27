package br.com.fiap.inovagab.ui.components

import androidx.compose.runtime.Composable

@Composable
fun OperadorBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    BottomNavBar(
        currentRoute = currentRoute,
        items        = operadorNavItems,
        onNavigate   = onNavigate
    )
}
