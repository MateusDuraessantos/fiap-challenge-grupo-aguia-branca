package br.com.fiap.inovagab.ui.features.gestor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.model.Projeto
import br.com.fiap.inovagab.data.repository.IdeiaRepository
import br.com.fiap.inovagab.data.repository.ProjetoRepository
import com.google.firebase.firestore.ListenerRegistration

class GestorViewModel : ViewModel() {

    var ideias by mutableStateOf<List<Ideia>>(emptyList())
        private set

    var projetos by mutableStateOf<List<Projeto>>(emptyList())
        private set

    var filtroSelecionado by mutableStateOf("Todas")
        private set

    val filtros = listOf("Todas", "Em análise", "Prioritárias", "Aprovadas")

    private var ideiasListener: ListenerRegistration? = null
    private var projetosListener: ListenerRegistration? = null

    init {
        ideiasListener  = IdeiaRepository.listenAll  { ideias   = it }
        projetosListener = ProjetoRepository.listenAll { projetos = it }
    }

    override fun onCleared() {
        super.onCleared()
        ideiasListener?.remove()
        projetosListener?.remove()
    }

    fun onFiltroChange(value: String) { filtroSelecionado = value }

    fun ideiasFiltradas(): List<Ideia> = when (filtroSelecionado) {
        "Em análise"   -> ideias.filter { it.status == "Em análise" }
        "Aprovadas"    -> ideias.filter { it.status == "Aprovada" }
        "Prioritárias" -> ideias.filter { it.prioridade == "Alta" || it.prioridade == "Muito alta" }
        else           -> ideias
    }

    fun getIdeia(id: String): Ideia? = ideias.firstOrNull { it.id == id }

    fun salvarAnalise(
        ideaId: String,
        nota: Int,
        prioridade: String,
        status: String,
        gestorUid: String = "",
        gestorNome: String = "",
        onDone: () -> Unit = {}
    ) {
        val ideia = ideias.firstOrNull { it.id == ideaId } ?: return
        val isAprovada = status == "Aprovada" || status == "Virou projeto"
        IdeiaRepository.update(
            ideia.copy(
                nota            = nota,
                prioridade      = prioridade,
                status          = status,
                aprovadoPorUid  = if (isAprovada) gestorUid  else ideia.aprovadoPorUid,
                aprovadoPorNome = if (isAprovada) gestorNome else ideia.aprovadoPorNome
            ),
            onSuccess = { onDone() },
            onError   = { onDone() }
        )
    }

    fun criarProjeto(projeto: Projeto, onDone: () -> Unit = {}) {
        ProjetoRepository.create(projeto, onSuccess = { onDone() }, onError = { onDone() })
    }

    fun atualizarProjeto(projeto: Projeto, onDone: () -> Unit = {}) {
        ProjetoRepository.update(projeto, onSuccess = { onDone() }, onError = { onDone() })
    }

    fun deletarProjeto(id: String, onDone: () -> Unit = {}) {
        ProjetoRepository.delete(id, onSuccess = { onDone() }, onError = { onDone() })
    }
}
