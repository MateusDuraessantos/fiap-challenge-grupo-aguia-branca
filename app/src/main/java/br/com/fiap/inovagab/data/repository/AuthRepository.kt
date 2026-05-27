package br.com.fiap.inovagab.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.data.model.User

object AuthRepository {

    private val auth: FirebaseAuth         get() = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore      get() = FirebaseFirestore.getInstance()

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: run { onError("Erro ao autenticar"); return@addOnSuccessListener }
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val nome  = doc.getString("nome")
                            ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                        val perfilName = doc.getString("perfil") ?: Perfil.OPERADOR.name
                        val perfil = runCatching { Perfil.valueOf(perfilName) }.getOrDefault(Perfil.OPERADOR)
                        onSuccess(User(nome = nome, email = email, perfil = perfil))
                    }
                    .addOnFailureListener { onError("Não foi possível carregar os dados do usuário") }
            }
            .addOnFailureListener { e ->
                val msg = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
                    else                                       -> "E-mail ou senha incorretos"
                }
                onError(msg)
            }
    }

    fun register(
        email: String,
        password: String,
        nome: String,
        perfil: Perfil,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: run { onError("Erro ao criar conta"); return@addOnSuccessListener }
                val data = hashMapOf(
                    "nome"   to nome,
                    "email"  to email,
                    "perfil" to perfil.name
                )
                db.collection("users").document(uid).set(data)
                    .addOnSuccessListener { onSuccess(User(nome = nome, email = email, perfil = perfil)) }
                    .addOnFailureListener { onError("Conta criada, mas não foi possível salvar os dados") }
            }
            .addOnFailureListener { e ->
                val msg = when (e) {
                    is FirebaseAuthWeakPasswordException       -> "A senha deve ter pelo menos 6 caracteres"
                    is FirebaseAuthUserCollisionException      -> "Este e-mail já está cadastrado"
                    is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
                    else                                       -> e.message ?: "Erro ao criar conta"
                }
                onError(msg)
            }
    }

    fun logout() {
        auth.signOut()
    }
}
