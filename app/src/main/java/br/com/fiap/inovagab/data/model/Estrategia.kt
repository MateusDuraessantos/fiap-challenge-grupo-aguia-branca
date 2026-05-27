package br.com.fiap.inovagab.data.model

data class Estrategia(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val criadaPorUid: String = "",
    val criadaPorNome: String = "",
    val criadaEm: Long = 0L
)
