package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.Estrategia
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

object EstrategiaRepository {

    private val db  get() = FirebaseFirestore.getInstance()
    private val col get() = db.collection("estrategias")

    fun listenAll(onUpdate: (List<Estrategia>) -> Unit): ListenerRegistration =
        col.orderBy("criadaEm", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onUpdate(snap?.documents?.mapNotNull { it.toEstrategia() } ?: emptyList())
            }

    fun create(estrategia: Estrategia, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.add(
            mapOf(
                "titulo"        to estrategia.titulo,
                "descricao"     to estrategia.descricao,
                "categoria"     to estrategia.categoria,
                "criadaPorUid"  to estrategia.criadaPorUid,
                "criadaPorNome" to estrategia.criadaPorNome,
                "criadaEm"      to estrategia.criadaEm
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao salvar estratégia") }
    }

    fun update(estrategia: Estrategia, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.document(estrategia.id).update(
            mapOf(
                "titulo"    to estrategia.titulo,
                "descricao" to estrategia.descricao,
                "categoria" to estrategia.categoria
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao atualizar estratégia") }
    }

    fun delete(id: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao deletar estratégia") }
    }

    private fun DocumentSnapshot.toEstrategia(): Estrategia? = try {
        Estrategia(
            id            = id,
            titulo        = getString("titulo") ?: "",
            descricao     = getString("descricao") ?: "",
            categoria     = getString("categoria") ?: "",
            criadaPorUid  = getString("criadaPorUid") ?: "",
            criadaPorNome = getString("criadaPorNome") ?: "",
            criadaEm      = getLong("criadaEm") ?: 0L
        )
    } catch (e: Exception) { null }
}
