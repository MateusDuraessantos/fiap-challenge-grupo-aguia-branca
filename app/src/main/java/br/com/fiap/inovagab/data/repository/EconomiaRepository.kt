package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.AreaBenchmark
import br.com.fiap.inovagab.data.model.AreaEconomia
import br.com.fiap.inovagab.data.model.AreaROI
import br.com.fiap.inovagab.data.model.IdeiaEconomia

object EconomiaRepository {

    fun listBenchmarks(): List<AreaBenchmark> = listOf(
        AreaBenchmark("Operações",   92),
        AreaBenchmark("Manutenção",  78),
        AreaBenchmark("Atendimento", 65),
        AreaBenchmark("RH",          58),
        AreaBenchmark("Comercial",   45)
    )

    fun listROIAreas(): List<AreaROI> = listOf(
        AreaROI("Operações",   45f, "R$ 562K"),
        AreaROI("Manutenção",  25f, "R$ 313K"),
        AreaROI("Atendimento", 15f, "R$ 188K"),
        AreaROI("RH",          10f, "R$ 125K"),
        AreaROI("Comercial",    5f, "R\$ 62K")
    )

    fun listTopIdeias(): List<IdeiaEconomia> = listOf(
        IdeiaEconomia(1, "Redução de trabalho no processo X",      "01/09/10 · Aprovado em 12/10/24", "Aprovado", "R\$ 185K", 42),
        IdeiaEconomia(2, "Padronização de rotina operacional",     "01/09/10 · Aprovado em 18/11/24", "Aprovado", "R\$ 122K", 30),
        IdeiaEconomia(3, "Automação de checklist diário",          "03/09/10 · Aprovado em 12/10/24", "Aprovado", "R\$ 78K",  18),
        IdeiaEconomia(4, "Redução de consumo de materiais",        "04/06/21 · Ainda em análise",     "Análise",  "R\$ 46K",  10)
    )

    fun listOutrasAreas(): List<AreaEconomia> = listOf(
        AreaEconomia("Manutenção",  "R\$ 245K", 25),
        AreaEconomia("Atendimento", "R\$ 147K", 15),
        AreaEconomia("RH",          "R\$ 98K",  10),
        AreaEconomia("Comercial",   "R\$ 49K",   5)
    )
}
