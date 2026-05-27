package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.Projeto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

object ProjetoRepository {

    private val db  get() = FirebaseFirestore.getInstance()
    private val col get() = db.collection("projetos")

    fun listenAll(onUpdate: (List<Projeto>) -> Unit): ListenerRegistration =
        col.orderBy("criadoEm", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                onUpdate(snap?.documents?.mapNotNull { it.toProjeto() } ?: emptyList())
            }

    fun create(projeto: Projeto, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.add(toMap(projeto))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao salvar projeto") }
    }

    fun update(projeto: Projeto, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.document(projeto.id).update(toMap(projeto))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao atualizar projeto") }
    }

    fun delete(id: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        col.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erro ao deletar projeto") }
    }

    private fun toMap(p: Projeto): Map<String, Any> = mapOf(
        "titulo"            to p.titulo,
        "descricao"         to p.descricao,
        "categoria"         to p.categoria,
        "progresso"         to p.progresso,
        "investimento"      to p.investimento,
        "retornoFinanceiro" to p.retornoFinanceiro,
        "resultados"        to p.resultados,
        "status"            to p.status,
        "gestorUid"         to p.gestorUid,
        "gestorNome"        to p.gestorNome,
        "criadoEm"          to p.criadoEm
    )

    private fun DocumentSnapshot.toProjeto(): Projeto? = try {
        Projeto(
            id                 = id,
            titulo             = getString("titulo") ?: "",
            descricao          = getString("descricao") ?: "",
            categoria          = getString("categoria") ?: "",
            progresso          = (getLong("progresso") ?: 0L).toInt(),
            investimento       = getDouble("investimento") ?: 0.0,
            retornoFinanceiro  = getDouble("retornoFinanceiro") ?: 0.0,
            resultados         = getString("resultados") ?: "",
            status             = getString("status") ?: "Em andamento",
            gestorUid          = getString("gestorUid") ?: "",
            gestorNome         = getString("gestorNome") ?: "",
            criadoEm           = getLong("criadoEm") ?: 0L
        )
    } catch (e: Exception) { null }
}
