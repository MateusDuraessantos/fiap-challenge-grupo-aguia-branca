package br.com.fiap.inovagab.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var loggedProfile by mutableStateOf<String?>(null)
        private set

    val isAuthenticated: Boolean
        get() = loggedProfile != null

    val isGestor: Boolean
        get() = loggedProfile == "Gestor"

    val isLider: Boolean
        get() = loggedProfile == "Líder"

    val isOperador: Boolean
        get() = loggedProfile == "Operador"

    fun login(profile: String) {
        loggedProfile = profile
    }

    fun logout() {
        loggedProfile = null
    }
}
