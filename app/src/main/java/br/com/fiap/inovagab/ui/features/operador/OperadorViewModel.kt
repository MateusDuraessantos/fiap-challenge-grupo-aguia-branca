package br.com.fiap.inovagab.ui.features.operador

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.repository.IdeiaRepository
import com.google.firebase.firestore.ListenerRegistration

class OperadorViewModel : ViewModel() {

    var minhasIdeias by mutableStateOf<List<Ideia>>(emptyList())
        private set

    var tituloForm by mutableStateOf("")
        private set

    var descricaoForm by mutableStateOf("")
        private set

    var categoriaForm by mutableStateOf("RH")
        private set

    var editingIdeiaId by mutableStateOf<String?>(null)
        private set

    val categorias = listOf("RH", "Operações", "Manutenção", "Atendimento", "Comercial")

    private var listener: ListenerRegistration? = null
    private var currentUid = ""

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    fun load(uid: String) {
        if (uid == currentUid && listener != null) return
        currentUid = uid
        listener?.remove()
        listener = IdeiaRepository.listenMinhas(uid) { minhasIdeias = it }
    }

    fun onTituloChange(value: String) { tituloForm = value }
    fun onDescricaoChange(value: String) { descricaoForm = value }
    fun onCategoriaChange(value: String) { categoriaForm = value }

    fun setEditMode(ideia: Ideia) {
        editingIdeiaId = ideia.id
        tituloForm     = ideia.titulo
        descricaoForm  = ideia.descricao
        categoriaForm  = ideia.categoria
    }

    fun clearEditMode() {
        editingIdeiaId = null
        tituloForm     = ""
        descricaoForm  = ""
        categoriaForm  = "RH"
    }

    fun atualizarIdeia(onDone: () -> Unit) {
        val id = editingIdeiaId ?: return
        val titulo = tituloForm.ifBlank { descricaoForm.take(60) }
        IdeiaRepository.updateContent(
            ideaId    = id,
            titulo    = titulo,
            descricao = descricaoForm,
            categoria = categoriaForm,
            onSuccess = { clearEditMode(); onDone() },
            onError   = { onDone() }
        )
    }

    fun deletarIdeia(ideaId: String, onDone: () -> Unit = {}) {
        IdeiaRepository.delete(ideaId, onSuccess = { onDone() }, onError = { onDone() })
    }

    fun resumoPorStatus(): Map<String, Int> {
        val by = minhasIdeias.groupingBy { it.status }.eachCount()
        return mapOf(
            "Em análise"    to (by["Em análise"]    ?: 0),
            "Aprovada"      to (by["Aprovada"]      ?: 0),
            "Virou projeto" to (by["Virou projeto"] ?: 0),
            "Rejeitada"     to (by["Rejeitada"]     ?: 0)
        )
    }

    fun enviarIdeia(uid: String, nome: String, onDone: () -> Unit) {
        if (tituloForm.isBlank() && descricaoForm.isBlank()) { onDone(); return }
        val titulo = tituloForm.ifBlank { descricaoForm.take(60) }
        IdeiaRepository.create(
            Ideia(
                titulo        = titulo,
                descricao     = descricaoForm,
                categoria     = categoriaForm,
                status        = "Em análise",
                colaboradores = listOf(nome),
                operadorUid   = uid,
                operadorNome  = nome,
                criadaEm      = System.currentTimeMillis()
            ),
            onSuccess = {
                tituloForm    = ""
                descricaoForm = ""
                categoriaForm = "RH"
                onDone()
            },
            onError = { onDone() }
        )
    }
}
