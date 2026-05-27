package br.com.fiap.inovagab.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.data.model.Projeto
import br.com.fiap.inovagab.ui.components.BottomNavBar
import br.com.fiap.inovagab.ui.components.LiderBottomNavBar
import br.com.fiap.inovagab.ui.components.OperadorBottomNavBar
import br.com.fiap.inovagab.ui.features.auth.AuthViewModel
import br.com.fiap.inovagab.ui.features.auth.LoginScreen
import br.com.fiap.inovagab.ui.features.auth.RegisterScreen
import br.com.fiap.inovagab.ui.features.gestor.AnalisarIdeia
import br.com.fiap.inovagab.ui.features.gestor.CriarEditarProjeto
import br.com.fiap.inovagab.ui.features.gestor.GestorHome
import br.com.fiap.inovagab.ui.features.gestor.GestorProjetos
import br.com.fiap.inovagab.ui.features.gestor.GestorViewModel
import br.com.fiap.inovagab.ui.features.lider.LiderDashboard
import br.com.fiap.inovagab.ui.features.lider.LiderEconomia
import br.com.fiap.inovagab.ui.features.lider.LiderProjetos
import br.com.fiap.inovagab.ui.features.lider.LiderViewModel
import br.com.fiap.inovagab.ui.features.lider.NovaEstrategia
import br.com.fiap.inovagab.ui.features.operador.CriarIdeia
import br.com.fiap.inovagab.ui.features.operador.OperadorHome
import br.com.fiap.inovagab.ui.features.operador.OperadorViewModel
import br.com.fiap.inovagab.ui.features.shared.EstrategiasList
import br.com.fiap.inovagab.ui.features.shared.EstrategiaViewModel
import br.com.fiap.inovagab.ui.features.shared.NotificacoesScreen
import br.com.fiap.inovagab.ui.screens.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

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

@Composable
fun AppNavigation() {
    val navController        = rememberNavController()
    val authViewModel: AuthViewModel         = viewModel()
    val gestorViewModel: GestorViewModel     = viewModel()
    val operadorViewModel: OperadorViewModel = viewModel()
    val liderViewModel: LiderViewModel       = viewModel()
    val estrategiaViewModel: EstrategiaViewModel = viewModel()

    // Projeto being edited (passed via state, not nav args, to avoid serialization)
    var editingProjeto by remember { mutableStateOf<Projeto?>(null) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute      = navBackStackEntry?.destination?.route

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val isLiderRoute    = currentRoute?.startsWith("lider") == true
    val isOperadorRoute = currentRoute?.startsWith("operador") == true
    val showBottomBar   = currentRoute != null &&
        currentRoute != Routes.LOGIN &&
        currentRoute != Routes.REGISTER

    val activeGestorRoute = when {
        currentRoute?.startsWith("${Routes.GESTOR_IDEA_DETAIL}/") == true -> "home"
        currentRoute == Routes.GESTOR_PROJETOS ||
            currentRoute == Routes.GESTOR_CREATE_PROJ ||
            currentRoute == Routes.GESTOR_EDIT_PROJ -> "projetos"
        currentRoute == Routes.GESTOR_NOTIFS -> "notifications"
        else -> currentRoute ?: "home"
    }

    val activeLiderRoute = when (currentRoute) {
        Routes.LIDER_HOME            -> "home"
        Routes.LIDER_ECONOMIA        -> "home"
        Routes.LIDER_STRATEGIES      -> "strategies"
        Routes.LIDER_CREATE_STRATEGY -> "strategies"
        Routes.LIDER_EDIT_STRATEGY   -> "strategies"
        Routes.LIDER_PROJETOS        -> "projetos"
        Routes.LIDER_NOTIFS          -> "notifications"
        Routes.LIDER_PROFILE         -> "profile"
        else                         -> "home"
    }

    val activeOperadorRoute = when (currentRoute) {
        Routes.OPERADOR_HOME        -> "home"
        Routes.OPERADOR_STRATEGIES  -> "strategies"
        Routes.OPERADOR_NOTIFS      -> "notifications"
        Routes.OPERADOR_PROFILE     -> "profile"
        Routes.OPERADOR_CREATE_IDEA -> "home"
        Routes.OPERADOR_EDIT_IDEA   -> "home"
        else                        -> "home"
    }

    val navigateTo: (String) -> Unit = { route ->
        val targetRoute = when (route) {
            "home" -> when {
                authViewModel.isLider    -> Routes.LIDER_HOME
                authViewModel.isOperador -> Routes.OPERADOR_HOME
                else                     -> Routes.GESTOR_HOME
            }
            "strategies" -> when {
                authViewModel.isLider    -> Routes.LIDER_STRATEGIES
                authViewModel.isOperador -> Routes.OPERADOR_STRATEGIES
                else                     -> Routes.GESTOR_STRATEGIES
            }
            "projetos" -> when {
                authViewModel.isLider    -> Routes.LIDER_PROJETOS
                else                     -> Routes.GESTOR_PROJETOS
            }
            "notifications" -> when {
                authViewModel.isLider    -> Routes.LIDER_NOTIFS
                authViewModel.isOperador -> Routes.OPERADOR_NOTIFS
                else                     -> Routes.GESTOR_NOTIFS
            }
            "profile" -> when {
                authViewModel.isLider    -> Routes.LIDER_PROFILE
                authViewModel.isOperador -> Routes.OPERADOR_PROFILE
                else                     -> Routes.GESTOR_PROFILE
            }
            else -> route
        }

        val baseRoute = when {
            authViewModel.isLider    -> Routes.LIDER_HOME
            authViewModel.isOperador -> Routes.OPERADOR_HOME
            else                     -> Routes.GESTOR_HOME
        }

        navController.navigate(targetRoute) {
            popUpTo(baseRoute) { saveState = true }
            launchSingleTop = true
            restoreState    = true
        }
    }

    val backToLogin: () -> Unit = {
        navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                when {
                    isLiderRoute    -> LiderBottomNavBar(currentRoute = activeLiderRoute,    onNavigate = navigateTo)
                    isOperadorRoute -> OperadorBottomNavBar(currentRoute = activeOperadorRoute, onNavigate = navigateTo)
                    else            -> BottomNavBar(currentRoute = activeGestorRoute, onNavigate = navigateTo)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Routes.LOGIN,
            modifier         = Modifier.padding(innerPadding)
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { perfil ->
                        val dest = when (perfil) {
                            Perfil.LIDER    -> Routes.LIDER_HOME
                            Perfil.OPERADOR -> Routes.OPERADOR_HOME
                            Perfil.GESTOR   -> Routes.GESTOR_HOME
                        }
                        navController.navigate(dest) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    authViewModel     = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            // ─── Gestor ──────────────────────────────────────────────────────
            composable(Routes.GESTOR_HOME) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    GestorHome(
                        gestorViewModel = gestorViewModel,
                        loggedUser      = authViewModel.loggedUser,
                        onIdeaClick     = { id ->
                            navController.navigate(Routes.gestorIdeaDetail(id))
                        }
                    )
                }
            }

            composable(Routes.GESTOR_STRATEGIES) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    EstrategiasList(
                        perfil              = Perfil.GESTOR,
                        loggedUser          = authViewModel.loggedUser,
                        estrategiaViewModel = estrategiaViewModel
                    )
                }
            }

            composable(Routes.GESTOR_NOTIFS) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    NotificacoesScreen(
                        perfil      = Perfil.GESTOR,
                        loggedUser  = authViewModel.loggedUser,
                        ideias      = gestorViewModel.ideias,
                        estrategias = estrategiaViewModel.estrategias
                    )
                }
            }

            composable(Routes.GESTOR_PROFILE) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    ProfileScreen(authViewModel = authViewModel, onLogout = backToLogin)
                }
            }

            composable("${Routes.GESTOR_IDEA_DETAIL}/{ideaId}") { backStackEntry ->
                val ideaId = backStackEntry.arguments?.getString("ideaId") ?: ""
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    AnalisarIdeia(
                        ideaId          = ideaId,
                        gestorViewModel = gestorViewModel,
                        loggedUser      = authViewModel.loggedUser,
                        onBack          = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.GESTOR_PROJETOS) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    GestorProjetos(
                        gestorViewModel = gestorViewModel,
                        loggedUser      = authViewModel.loggedUser,
                        onCreateClick   = {
                            editingProjeto = null
                            navController.navigate(Routes.GESTOR_CREATE_PROJ)
                        },
                        onEditClick     = { projeto ->
                            editingProjeto = projeto
                            navController.navigate(Routes.GESTOR_EDIT_PROJ)
                        }
                    )
                }
            }

            composable(Routes.GESTOR_CREATE_PROJ) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    CriarEditarProjeto(
                        editingProjeto  = null,
                        gestorViewModel = gestorViewModel,
                        loggedUser      = authViewModel.loggedUser,
                        onBack          = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.GESTOR_EDIT_PROJ) {
                RouteGuard(authViewModel.isGestor, backToLogin) {
                    CriarEditarProjeto(
                        editingProjeto  = editingProjeto,
                        gestorViewModel = gestorViewModel,
                        loggedUser      = authViewModel.loggedUser,
                        onBack          = { navController.popBackStack() }
                    )
                }
            }

            // ─── Líder ───────────────────────────────────────────────────────
            composable(Routes.LIDER_HOME) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    LiderDashboard(
                        liderViewModel = liderViewModel,
                        loggedUser     = authViewModel.loggedUser,
                        onEconomiaClick = { navController.navigate(Routes.LIDER_ECONOMIA) }
                    )
                }
            }

            composable(Routes.LIDER_ECONOMIA) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    LiderEconomia(
                        liderViewModel = liderViewModel,
                        onBack         = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.LIDER_STRATEGIES) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    EstrategiasList(
                        perfil              = Perfil.LIDER,
                        loggedUser          = authViewModel.loggedUser,
                        estrategiaViewModel = estrategiaViewModel,
                        onCreateClick = {
                            estrategiaViewModel.clearForm()
                            navController.navigate(Routes.LIDER_CREATE_STRATEGY)
                        },
                        onEditClick   = { estrategia ->
                            estrategiaViewModel.setEditMode(estrategia)
                            navController.navigate(Routes.LIDER_EDIT_STRATEGY)
                        }
                    )
                }
            }

            composable(Routes.LIDER_CREATE_STRATEGY) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    NovaEstrategia(
                        estrategiaViewModel = estrategiaViewModel,
                        loggedUser          = authViewModel.loggedUser,
                        onBack    = { navController.popBackStack() },
                        onSubmit  = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.LIDER_EDIT_STRATEGY) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    NovaEstrategia(
                        estrategiaViewModel = estrategiaViewModel,
                        loggedUser          = authViewModel.loggedUser,
                        onBack    = { navController.popBackStack() },
                        onSubmit  = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.LIDER_PROJETOS) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    LiderProjetos(
                        liderViewModel = liderViewModel,
                        loggedUser     = authViewModel.loggedUser
                    )
                }
            }

            composable(Routes.LIDER_NOTIFS) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    NotificacoesScreen(
                        perfil      = Perfil.LIDER,
                        loggedUser  = authViewModel.loggedUser,
                        ideias      = liderViewModel.ideias,
                        estrategias = estrategiaViewModel.estrategias
                    )
                }
            }

            composable(Routes.LIDER_PROFILE) {
                RouteGuard(authViewModel.isLider, backToLogin) {
                    ProfileScreen(authViewModel = authViewModel, onLogout = backToLogin)
                }
            }

            // ─── Operador ────────────────────────────────────────────────────
            composable(Routes.OPERADOR_HOME) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    OperadorHome(
                        operadorViewModel = operadorViewModel,
                        loggedUser        = authViewModel.loggedUser,
                        uid               = uid,
                        onCreateIdeaClick = {
                            operadorViewModel.clearEditMode()
                            navController.navigate(Routes.OPERADOR_CREATE_IDEA)
                        },
                        onEditIdeaClick   = { ideia ->
                            operadorViewModel.setEditMode(ideia)
                            navController.navigate(Routes.OPERADOR_EDIT_IDEA)
                        }
                    )
                }
            }

            composable(Routes.OPERADOR_CREATE_IDEA) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    CriarIdeia(
                        operadorViewModel = operadorViewModel,
                        loggedUser        = authViewModel.loggedUser,
                        uid               = uid,
                        onCancel          = { navController.popBackStack() },
                        onSend            = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.OPERADOR_EDIT_IDEA) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    CriarIdeia(
                        operadorViewModel = operadorViewModel,
                        loggedUser        = authViewModel.loggedUser,
                        uid               = uid,
                        onCancel          = {
                            operadorViewModel.clearEditMode()
                            navController.popBackStack()
                        },
                        onSend            = { navController.popBackStack() }
                    )
                }
            }

            composable(Routes.OPERADOR_STRATEGIES) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    EstrategiasList(
                        perfil              = Perfil.OPERADOR,
                        loggedUser          = authViewModel.loggedUser,
                        estrategiaViewModel = estrategiaViewModel
                    )
                }
            }

            composable(Routes.OPERADOR_NOTIFS) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    NotificacoesScreen(
                        perfil      = Perfil.OPERADOR,
                        loggedUser  = authViewModel.loggedUser,
                        ideias      = operadorViewModel.minhasIdeias,
                        estrategias = estrategiaViewModel.estrategias,
                        onLoad      = { operadorViewModel.load(uid) }
                    )
                }
            }

            composable(Routes.OPERADOR_PROFILE) {
                RouteGuard(authViewModel.isOperador, backToLogin) {
                    ProfileScreen(authViewModel = authViewModel, onLogout = backToLogin)
                }
            }
        }
    }
}
