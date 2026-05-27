package br.com.fiap.inovagab.data.model

data class Projeto(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val progresso: Int = 0,
    val investimento: Double = 0.0,
    val retornoFinanceiro: Double = 0.0,
    val resultados: String = "",
    val status: String = "Em andamento",
    val gestorUid: String = "",
    val gestorNome: String = "",
    val criadoEm: Long = 0L
)
