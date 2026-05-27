package br.com.fiap.inovagab.data.model

data class Ideia(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val status: String = "Em análise",
    val prioridade: String = "Baixa",
    val nota: Int = 0,
    val colaboradores: List<String> = emptyList(),
    val operadorUid: String = "",
    val operadorNome: String = "",
    val criadaEm: Long = 0L,
    val aprovadoPorUid: String = "",
    val aprovadoPorNome: String = ""
)
