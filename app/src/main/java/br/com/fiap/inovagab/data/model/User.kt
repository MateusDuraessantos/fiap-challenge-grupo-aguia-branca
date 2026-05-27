package br.com.fiap.inovagab.data.model

enum class Perfil(val label: String) {
    OPERADOR("Operador"),
    GESTOR("Gestor"),
    LIDER("Líder");

    companion object {
        fun fromLabel(label: String?): Perfil? =
            entries.firstOrNull { it.label == label }
    }
}

data class User(
    val nome: String,
    val email: String,
    val perfil: Perfil
)
