package br.com.fiap.inovagab.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.inovagab.LoginScreen
import br.com.fiap.inovagab.data.AuthViewModel
import br.com.fiap.inovagab.ui.components.BottomNavBar
import br.com.fiap.inovagab.ui.components.LiderBottomNavBar
import br.com.fiap.inovagab.ui.components.OperadorBottomNavBar
import br.com.fiap.inovagab.ui.screens.HomeScreen
import br.com.fiap.inovagab.ui.screens.IdeaDetailScreen
import br.com.fiap.inovagab.ui.screens.StrategiesScreen
import br.com.fiap.inovagab.ui.screens.lider.LiderCreateStrategyScreen
import br.com.fiap.inovagab.ui.screens.lider.LiderDashboardScreen
import br.com.fiap.inovagab.ui.screens.lider.LiderEconomiaScreen
import br.com.fiap.inovagab.ui.screens.lider.LiderStrategiesScreen
import br.com.fiap.inovagab.ui.screens.operador.OperadorCreateIdeaScreen
import br.com.fiap.inovagab.ui.screens.operador.OperadorHomeScreen
import br.com.fiap.inovagab.ui.screens.operador.OperadorStrategiesScreen

// ─── Guarda de rota ───────────────────────────────────────────────────────────
@Composable
fun RouteGuard(
    isAuthorized: Boolean,
    onUnauthorized: () -> Unit,
    content: @Composable () -> Unit
) {
    LaunchedEffect(isAuthorized) {
        if (!isAuthorized) onUnauthorized()
    }
    if (isAuthorized) content()
}

// ─── Navegação principal ──────────────────────────────────────────────────────
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLiderRoute    = currentRoute?.startsWith("lider") == true
    val isOperadorRoute = currentRoute?.startsWith("operador") == true
    val showBottomBar   = currentRoute != null && currentRoute != "login"

    // Rota ativa para highlight no bottom nav
    val activeGestorRoute = when {
        currentRoute?.startsWith("idea_detail") == true -> "home"
        else -> currentRoute ?: "home"
    }
    val activeLiderRoute = when (currentRoute) {
        "lider_economia"        -> "lider_home"
        "lider_create_strategy" -> "lider_strategies"
        else                    -> currentRoute ?: "lider_home"
    }
    val activeOperadorRoute = when (currentRoute) {
        "operador_create_idea" -> "operador_home"
        else                   -> currentRoute ?: "operador_home"
    }

    // Função de navegação reutilizável
    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState    = true
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                when {
                    isLiderRoute    -> LiderBottomNavBar(
                        currentRoute = activeLiderRoute,
                        onNavigate   = navigateTo
                    )
                    isOperadorRoute -> OperadorBottomNavBar(
                        currentRoute = activeOperadorRoute,
                        onNavigate   = navigateTo
                    )
                    else            -> BottomNavBar(
                        currentRoute = activeGestorRoute,
                        onNavigate   = navigateTo
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "login",
            modifier         = Modifier.padding(innerPadding)
        ) {

            // ── Login (público) ──────────────────────────────────────────────
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { profile ->
                        authViewModel.login(profile)
                        val dest = when (profile) {
                            "Líder"    -> "lider_home"
                            "Operador" -> "operador_home"
                            else       -> "home"   // Gestor
                        }
                        navController.navigate(dest) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // ════════════════════════════════════════════════════════════════
            //  ROTAS DO GESTOR
            // ════════════════════════════════════════════════════════════════

            composable("home") {
                RouteGuard(
                    isAuthorized  = authViewModel.isGestor,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    HomeScreen(onIdeaClick = { ideaId ->
                        navController.navigate("idea_detail/$ideaId")
                    })
                }
            }

            composable("strategies") {
                RouteGuard(
                    isAuthorized  = authViewModel.isGestor,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) { StrategiesScreen() }
            }

            composable("notifications") {
                RouteGuard(
                    isAuthorized  = authViewModel.isGestor,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Avisos — em breve")
                    }
                }
            }

            composable("profile") {
                RouteGuard(
                    isAuthorized  = authViewModel.isGestor,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Perfil — em breve")
                    }
                }
            }

            composable("idea_detail/{ideaId}") { backStackEntry ->
                val ideaId = backStackEntry.arguments?.getString("ideaId")?.toIntOrNull() ?: 0
                RouteGuard(
                    isAuthorized  = authViewModel.isGestor,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    IdeaDetailScreen(
                        ideaId = ideaId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // ════════════════════════════════════════════════════════════════
            //  ROTAS DO LÍDER
            // ════════════════════════════════════════════════════════════════

            composable("lider_home") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    LiderDashboardScreen(onEconomiaClick = {
                        navController.navigate("lider_economia")
                    })
                }
            }

            composable("lider_economia") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    LiderEconomiaScreen(onBack = { navController.popBackStack() })
                }
            }

            composable("lider_strategies") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    LiderStrategiesScreen(onCreateClick = {
                        navController.navigate("lider_create_strategy")
                    })
                }
            }

            composable("lider_create_strategy") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    LiderCreateStrategyScreen(
                        onBack   = { navController.popBackStack() },
                        onSubmit = { navController.popBackStack() }
                    )
                }
            }

            composable("lider_notifications") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Avisos — em breve")
                    }
                }
            }

            composable("lider_profile") {
                RouteGuard(
                    isAuthorized  = authViewModel.isLider,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Perfil — em breve")
                    }
                }
            }

            // ════════════════════════════════════════════════════════════════
            //  ROTAS DO OPERADOR
            // ════════════════════════════════════════════════════════════════

            composable("operador_home") {
                RouteGuard(
                    isAuthorized  = authViewModel.isOperador,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    OperadorHomeScreen(onCreateIdeaClick = {
                        navController.navigate("operador_create_idea")
                    })
                }
            }

            composable("operador_create_idea") {
                RouteGuard(
                    isAuthorized  = authViewModel.isOperador,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    OperadorCreateIdeaScreen(
                        onCancel = { navController.popBackStack() },
                        onSend   = { navController.popBackStack() }
                    )
                }
            }

            composable("operador_strategies") {
                RouteGuard(
                    isAuthorized  = authViewModel.isOperador,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) { OperadorStrategiesScreen() }
            }

            composable("operador_notifications") {
                RouteGuard(
                    isAuthorized  = authViewModel.isOperador,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Avisos — em breve")
                    }
                }
            }

            composable("operador_profile") {
                RouteGuard(
                    isAuthorized  = authViewModel.isOperador,
                    onUnauthorized = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Perfil — em breve")
                    }
                }
            }
        }
    }
}
