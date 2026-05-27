package br.com.fiap.inovagab.ui.features.lider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.data.model.AreaBenchmark
import br.com.fiap.inovagab.data.model.AreaEconomia
import br.com.fiap.inovagab.data.model.AreaROI
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.model.IdeiaEconomia
import br.com.fiap.inovagab.data.model.Projeto
import br.com.fiap.inovagab.data.repository.EconomiaRepository
import br.com.fiap.inovagab.data.repository.IdeiaRepository
import br.com.fiap.inovagab.data.repository.ProjetoRepository
import com.google.firebase.firestore.ListenerRegistration

class LiderViewModel : ViewModel() {

    var projetos by mutableStateOf<List<Projeto>>(emptyList())
        private set

    var ideias by mutableStateOf<List<Ideia>>(emptyList())
        private set

    // Mock data kept for LiderEconomia screen
    var topIdeias by mutableStateOf<List<IdeiaEconomia>>(emptyList())
        private set

    var outrasAreas by mutableStateOf<List<AreaEconomia>>(emptyList())
        private set

    val projetosAtivos:    Int    get() = projetos.count { it.status == "Em andamento" }
    val totalIdeias:       Int    get() = ideias.size
    val investimentoTotal: Double get() = projetos.sumOf { it.investimento }
    val economiaGerada:    Double get() = projetos.sumOf {
        (it.retornoFinanceiro - it.investimento).coerceAtLeast(0.0)
    }

    val benchmarks: List<AreaBenchmark>
        get() = ideias.groupBy { it.categoria }
            .map { (cat, list) -> AreaBenchmark(cat, list.size) }
            .sortedByDescending { it.value }

    val roiAreas: List<AreaROI>
        get() {
            val total = projetos.sumOf { it.retornoFinanceiro }
            return projetos.groupBy { it.categoria }
                .map { (cat, list) ->
                    val catTotal = list.sumOf { it.retornoFinanceiro }
                    AreaROI(
                        name       = cat,
                        percentage = if (total > 0) (catTotal / total * 100).toFloat() else 0f,
                        value      = catTotal.formatCurrency()
                    )
                }
                .sortedByDescending { it.percentage }
        }

    private var projetosListener: ListenerRegistration? = null
    private var ideiasListener:   ListenerRegistration? = null

    init {
        projetosListener = ProjetoRepository.listenAll { projetos = it }
        ideiasListener   = IdeiaRepository.listenAll   { ideias   = it }
        topIdeias   = EconomiaRepository.listTopIdeias()
        outrasAreas = EconomiaRepository.listOutrasAreas()
    }

    override fun onCleared() {
        super.onCleared()
        projetosListener?.remove()
        ideiasListener?.remove()
    }
}

private fun Double.formatCurrency(): String = when {
    this >= 1_000_000 -> "R$ %.1fM".format(this / 1_000_000)
    this >= 1_000     -> "R$ %.0fK".format(this / 1_000)
    else              -> "R$ %.0f".format(this)
}
