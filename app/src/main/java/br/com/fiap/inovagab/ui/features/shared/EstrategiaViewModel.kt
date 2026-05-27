package br.com.fiap.inovagab.ui.features.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.data.model.Estrategia
import br.com.fiap.inovagab.data.repository.EstrategiaRepository
import com.google.firebase.firestore.ListenerRegistration

class EstrategiaViewModel : ViewModel() {

    var estrategias by mutableStateOf<List<Estrategia>>(emptyList())
        private set

    var tituloForm by mutableStateOf("")
        private set

    var descricaoForm by mutableStateOf("")
        private set

    var categoriaForm by mutableStateOf("Operações")
        private set

    var editingId by mutableStateOf<String?>(null)
        private set

    val categorias = listOf("Operações", "RH", "Manutenção", "Atendimento", "Comercial")

    private var listener: ListenerRegistration? = null

    init {
        listener = EstrategiaRepository.listenAll { estrategias = it }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    fun setEditMode(estrategia: Estrategia) {
        editingId     = estrategia.id
        tituloForm    = estrategia.titulo
        descricaoForm = estrategia.descricao
        categoriaForm = estrategia.categoria
    }

    fun clearForm() {
        editingId     = null
        tituloForm    = ""
        descricaoForm = ""
        categoriaForm = "Operações"
    }

    fun onTituloChange(value: String)    { tituloForm    = value }
    fun onDescricaoChange(value: String) { descricaoForm = value }
    fun onCategoriaChange(value: String) { categoriaForm = value }

    fun submeter(uid: String, nome: String, onDone: () -> Unit) {
        if (tituloForm.isBlank() || descricaoForm.isBlank()) { onDone(); return }
        val id = editingId
        if (id != null) {
            val existing = estrategias.firstOrNull { it.id == id } ?: return
            EstrategiaRepository.update(
                existing.copy(titulo = tituloForm, descricao = descricaoForm, categoria = categoriaForm),
                onSuccess = { clearForm(); onDone() },
                onError   = { clearForm(); onDone() }
            )
        } else {
            EstrategiaRepository.create(
                Estrategia(
                    titulo        = tituloForm,
                    descricao     = descricaoForm,
                    categoria     = categoriaForm,
                    criadaPorUid  = uid,
                    criadaPorNome = nome,
                    criadaEm      = System.currentTimeMillis()
                ),
                onSuccess = { clearForm(); onDone() },
                onError   = { clearForm(); onDone() }
            )
        }
    }

    fun deletar(id: String) {
        EstrategiaRepository.delete(id)
    }
}
