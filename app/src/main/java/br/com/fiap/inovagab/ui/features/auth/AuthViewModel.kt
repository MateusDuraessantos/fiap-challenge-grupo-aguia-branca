package br.com.fiap.inovagab.ui.features.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.data.repository.AuthRepository

class AuthViewModel : ViewModel() {

    var loggedUser by mutableStateOf<User?>(null)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var selectedPerfil by mutableStateOf(Perfil.OPERADOR)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val isAuthenticated: Boolean get() = loggedUser != null
    val isGestor: Boolean        get() = loggedUser?.perfil == Perfil.GESTOR
    val isLider: Boolean         get() = loggedUser?.perfil == Perfil.LIDER
    val isOperador: Boolean      get() = loggedUser?.perfil == Perfil.OPERADOR

    fun onEmailChange(value: String) {
        email = value
        emailError = validateEmail(value)
        loginError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        passwordError = validatePassword(value)
        loginError = null
    }

    fun onPerfilChange(perfil: Perfil) {
        selectedPerfil = perfil
    }

    fun login(onSuccess: (Perfil) -> Unit, onError: (String) -> Unit) {
        emailError = validateEmail(email)
        passwordError = validatePassword(password)

        if (emailError != null || passwordError != null) return

        isLoading = true
        loginError = null

        AuthRepository.login(
            email    = email,
            password = password,
            onSuccess = { user ->
                isLoading  = false
                loggedUser = user
                onSuccess(user.perfil)
            },
            onError = { msg ->
                isLoading  = false
                loginError = msg
                onError(msg)
            }
        )
    }

    fun register(
        nome: String,
        email: String,
        password: String,
        perfil: Perfil,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true
        AuthRepository.register(
            email    = email,
            password = password,
            nome     = nome,
            perfil   = perfil,
            onSuccess = { _ ->
                isLoading = false
                onSuccess()
            },
            onError = { msg ->
                isLoading = false
                onError(msg)
            }
        )
    }

    fun logout() {
        AuthRepository.logout()
        loggedUser = null
        email      = ""
        password   = ""
        loginError = null
    }

    private fun validateEmail(value: String): String? = when {
        value.isBlank() -> "Informe seu e-mail"
        !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "E-mail inválido"
        else -> null
    }

    private fun validatePassword(value: String): String? = when {
        value.isBlank() -> "Informe sua senha"
        value.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
        else -> null
    }
}
