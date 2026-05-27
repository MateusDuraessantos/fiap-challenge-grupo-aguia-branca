package br.com.fiap.inovagab.ui.navigation

object Routes {

    const val LOGIN    = "login"
    const val REGISTER = "register"

    // Gestor
    const val GESTOR_HOME         = "home"
    const val GESTOR_STRATEGIES   = "strategies"
    const val GESTOR_NOTIFS       = "notifications"
    const val GESTOR_PROFILE      = "profile"
    const val GESTOR_IDEA_DETAIL  = "idea_detail"
    const val GESTOR_PROJETOS     = "gestor_projetos"
    const val GESTOR_CREATE_PROJ  = "gestor_create_projeto"
    const val GESTOR_EDIT_PROJ    = "gestor_edit_projeto"

    fun gestorIdeaDetail(id: String) = "$GESTOR_IDEA_DETAIL/$id"

    // Líder
    const val LIDER_HOME            = "lider_home"
    const val LIDER_ECONOMIA        = "lider_economia"
    const val LIDER_STRATEGIES      = "lider_strategies"
    const val LIDER_CREATE_STRATEGY = "lider_create_strategy"
    const val LIDER_EDIT_STRATEGY   = "lider_edit_strategy"
    const val LIDER_NOTIFS          = "lider_notifications"
    const val LIDER_PROFILE         = "lider_profile"
    const val LIDER_PROJETOS        = "lider_projetos"

    // Operador
    const val OPERADOR_HOME        = "operador_home"
    const val OPERADOR_CREATE_IDEA = "operador_create_idea"
    const val OPERADOR_EDIT_IDEA   = "operador_edit_idea"
    const val OPERADOR_STRATEGIES  = "operador_strategies"
    const val OPERADOR_NOTIFS      = "operador_notifications"
    const val OPERADOR_PROFILE     = "operador_profile"
}
