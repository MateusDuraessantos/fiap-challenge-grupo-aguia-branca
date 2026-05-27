package br.com.fiap.inovagab.data.model

data class AreaBenchmark(val name: String, val value: Int)

data class AreaROI(
    val name: String,
    val percentage: Float,
    val value: String
)

data class IdeiaEconomia(
    val rank: Int,
    val title: String,
    val period: String,
    val status: String,
    val value: String,
    val percentage: Int
)

data class AreaEconomia(
    val name: String,
    val value: String,
    val percentage: Int
)
