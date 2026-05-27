package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.Ideia
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

object IdeiaRepository {

    private val db  get() = FirebaseFirestore.getInstance()
    private val col get() = db.collection("ideias")

    fun listenAll(onUpdate: (List<Ideia>) -> Unit): ListenerRegistration =
        col.orderBy("criadaEm", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onUpdate(snap?.documents?.mapNotNull { it.toIdeia() } ?: emptyList())
            }

    fun listenMinhas(uid: String, onUpdate: (List<Ideia>) -> Unit): ListenerRegistration =
        col.whereEqualTo("operadorUid", uid)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { it.toIdeia() }
                    ?.sortedByDescending { it.criadaEm }
                    ?: emptyList()
                onUpdate(list)
            }

    fun create(ideia: Ideia, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.add(
            mapOf(
                "titulo"        to ideia.titulo,
                "descricao"     to ideia.descricao,
                "categoria"     to ideia.categoria,
                "status"        to ideia.status,
                "prioridade"    to ideia.prioridade,
                "nota"          to ideia.nota,
                "colaboradores" to ideia.colaboradores,
                "operadorUid"   to ideia.operadorUid,
                "operadorNome"  to ideia.operadorNome,
                "criadaEm"      to ideia.criadaEm
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao salvar ideia") }
    }

    fun updateContent(
        ideaId: String,
        titulo: String,
        descricao: String,
        categoria: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        col.document(ideaId).update(
            mapOf(
                "titulo"    to titulo,
                "descricao" to descricao,
                "categoria" to categoria
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao atualizar ideia") }
    }

    fun delete(ideaId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.document(ideaId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao excluir ideia") }
    }

    fun update(ideia: Ideia, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val fields = mutableMapOf<String, Any>(
            "status"     to ideia.status,
            "prioridade" to ideia.prioridade,
            "nota"       to ideia.nota
        )
        if (ideia.aprovadoPorUid.isNotBlank()) {
            fields["aprovadoPorUid"]  = ideia.aprovadoPorUid
            fields["aprovadoPorNome"] = ideia.aprovadoPorNome
        }
        col.document(ideia.id).update(fields)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao atualizar ideia") }
    }

    @Suppress("UNCHECKED_CAST")
    private fun DocumentSnapshot.toIdeia(): Ideia? = try {
        Ideia(
            id              = id,
            titulo          = getString("titulo") ?: "",
            descricao       = getString("descricao") ?: "",
            categoria       = getString("categoria") ?: "",
            status          = getString("status") ?: "Em análise",
            prioridade      = getString("prioridade") ?: "Baixa",
            nota            = (getLong("nota") ?: 0L).toInt(),
            colaboradores   = (get("colaboradores") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            operadorUid     = getString("operadorUid") ?: "",
            operadorNome    = getString("operadorNome") ?: "",
            criadaEm        = getLong("criadaEm") ?: 0L,
            aprovadoPorUid  = getString("aprovadoPorUid") ?: "",
            aprovadoPorNome = getString("aprovadoPorNome") ?: ""
        )
    } catch (e: Exception) { null }
}
