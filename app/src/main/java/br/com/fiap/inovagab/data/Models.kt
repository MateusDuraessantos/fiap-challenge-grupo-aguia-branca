package br.com.fiap.inovagab.data

data class Strategy(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val date: String = "12/12/2026"
)

data class Idea(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val date: String = "12/12/2026",
    val priority: String = "Alta",
    val rating: Int = 5,
    val collaborators: List<String> = emptyList(),
    val collaboratorStatus: Map<String, String> = emptyMap()
)

val sampleStrategies = listOf(
    Strategy(
        id = 1,
        title = "Redução de custos com produtos de limpeza",
        description = "Identificar oportunidades para reduzir desperdícios, otimizar compras e melhorar o uso de materiais de limpeza nas operações diárias da empresa, gerando maior economia e eficiência operacional.",
        category = "Operações"
    ),
    Strategy(
        id = 2,
        title = "Maior controle de segurança dos funcionários.",
        description = "Fortalecer práticas e processos de segurança no ambiente de trabalho, reduzindo riscos operacionais, prevenindo acidentes e garantindo maior proteção para todos os colaboradores.",
        category = "RH"
    ),
    Strategy(
        id = 3,
        title = "Maior agilidade na linha de montagem",
        description = "Implementar melhorias nos processos produtivos para reduzir atrasos, aumentar a produtividade das equipes e tornar a linha de montagem mais rápida, organizada e eficiente.",
        category = "Manutenção"
    )
)

// ─── Modelos do Operador ──────────────────────────────────────────────────────

data class MyIdea(
    val id: String,
    val status: String,
    val date: String
)

val myIdeas = listOf(
    MyIdea("08635-106", "Virou projeto", "12/12/2026"),
    MyIdea("08635-106", "Em análise",    "12/12/2026"),
    MyIdea("01001-000", "Rejeitada",     "12/12/2026"),
    MyIdea("05038-160", "Aprovada",      "12/12/2026")
)

// ─── Modelos do Líder ────────────────────────────────────────────────────────

data class AreaBenchmark(val name: String, val value: Int)

data class AreaROI(
    val name: String,
    val percentage: Float,
    val value: String
)

data class IdeaEconomy(
    val rank: Int,
    val title: String,
    val period: String,
    val status: String,   // "Aprovado" | "Análise"
    val value: String,
    val percentage: Int
)

data class AreaEconomy(
    val name: String,
    val value: String,
    val percentage: Int
)

val areaBenchmarks = listOf(
    AreaBenchmark("Operações",   92),
    AreaBenchmark("Manutenção",  78),
    AreaBenchmark("Atendimento", 65),
    AreaBenchmark("RH",          58),
    AreaBenchmark("Comercial",   45)
)

val areaROI = listOf(
    AreaROI("Operações",   45f, "R$ 562K"),
    AreaROI("Manutenção",  25f, "R$ 313K"),
    AreaROI("Atendimento", 15f, "R$ 188K"),
    AreaROI("RH",          10f, "R$ 125K"),
    AreaROI("Comercial",    5f, "R\$ 62K")
)

val topIdeasEconomy = listOf(
    IdeaEconomy(1, "Redução de trabalho no processo X",      "01/09/10 · Aprovado em 12/10/24", "Aprovado", "R\$ 185K", 42),
    IdeaEconomy(2, "Padronização de rotina operacional",     "01/09/10 · Aprovado em 18/11/24", "Aprovado", "R\$ 122K", 30),
    IdeaEconomy(3, "Automação de checklist diário",          "03/09/10 · Aprovado em 12/10/24", "Aprovado", "R\$ 78K",  18),
    IdeaEconomy(4, "Redução de consumo de materiais",        "04/06/21 · Ainda em análise",     "Análise",  "R\$ 46K",  10)
)

val otherAreasEconomy = listOf(
    AreaEconomy("Manutenção",  "R\$ 245K", 25),
    AreaEconomy("Atendimento", "R\$ 147K", 15),
    AreaEconomy("RH",          "R\$ 98K",  10),
    AreaEconomy("Comercial",   "R\$ 49K",   5)
)

// ─── Ideias (sample) ─────────────────────────────────────────────────────────

val sampleIdeas = listOf(
    Idea(
        id = 1,
        title = "Redução de custos com produtos de limpeza",
        description = "Identificar oportunidades para reduzir desperdícios, otimizar compras e melhorar o uso de materiais de limpeza nas operações diárias da empresa, gerando maior economia e eficiência operacional.",
        category = "Operações"
    ),
    Idea(
        id = 2,
        title = "Maior agilidade na linha de montagem",
        description = "Implementar melhorias nos processos produtivos para reduzir atrasos, aumentar a produtividade das equipes e tornar a linha de montagem mais rápida, organizada e eficiente.",
        category = "Operações",
        priority = "Alta",
        rating = 5,
        collaborators = listOf("Mateus", "Gabrielle", "Rael", "Vinicius", "Jhonatan"),
        collaboratorStatus = mapOf("Mateus" to "Aprovado", "Gabrielle" to "Em análise")
    ),
    Idea(
        id = 3,
        title = "Maior controle de segurança dos funcionários",
        description = "Fortalecer práticas e processos de segurança no ambiente de trabalho, reduzindo riscos operacionais, prevenindo acidentes e garantindo maior proteção para todos os colaboradores.",
        category = "RH"
    )
)
