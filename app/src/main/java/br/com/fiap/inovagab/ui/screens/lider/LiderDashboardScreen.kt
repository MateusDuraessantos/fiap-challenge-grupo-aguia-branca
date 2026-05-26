package br.com.fiap.inovagab.ui.screens.lider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.*
import br.com.fiap.inovagab.ui.components.ProfileHeader

// ─── Cores por área ──────────────────────────────────────────────────────────
private val areaColors = listOf(
    Color(0xFF4CAF50), // Operações
    Color(0xFF2196F3), // Manutenção
    Color(0xFF9C27B0), // Atendimento
    Color(0xFFFF9800), // RH
    Color(0xFFF44336)  // Comercial
)

// ─── Tela principal ──────────────────────────────────────────────────────────
@Composable
fun LiderDashboardScreen(onEconomiaClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header branco
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column {
                ProfileHeader(name = "João", role = "Líder")
                // Título + período
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard Executivo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Chip de período
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFF0F0F0),
                    modifier = Modifier.padding(start = 20.dp, bottom = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Período: Maio/2024", fontSize = 13.sp, color = Color(0xFF444444))
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF666666)
                        )
                    }
                }
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {

            // ── Grid de métricas 2 × 2 ──────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(14.dp, 14.dp, 14.dp, 0.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MetricCard(
                            label = "ROI total", value = "R$ 1,250M", trend = "+18% vs abr/24",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(10.dp))
                        MetricCard(
                            label = "Projetos ativos", value = "28", trend = "+7 vs abr/24",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MetricCard(
                            label = "Ideias recebidas", value = "152", trend = "+23% vs abr/24",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(10.dp))
                        MetricCard(
                            label = "Economia gerada", value = "R$ 980K", trend = "+15% vs abr/24",
                            isHighlighted = true,
                            modifier = Modifier.weight(1f),
                            onClick = onEconomiaClick
                        )
                    }
                }
            }

            // ── Benchmark entre áreas ────────────────────────────────────────
            item {
                DashboardCard(title = "Benchmark entre áreas") {
                    BarChart(benchmarks = areaBenchmarks, colors = areaColors)
                }
            }

            // ── ROI por área ─────────────────────────────────────────────────
            item {
                DashboardCard(title = "ROI por área (R$)") {
                    ROIDonutSection(data = areaROI, colors = areaColors)
                }
            }
        }
    }
}

// ─── Componentes internos ─────────────────────────────────────────────────────

@Composable
private fun MetricCard(
    label: String,
    value: String,
    trend: String,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val bgColor    = if (isHighlighted) Color(0xFFE8F5E9) else Color.White
    val valueColor = if (isHighlighted) Color(0xFF2E7D32) else Color(0xFF1A1A1A)
    val trendColor = if (isHighlighted) Color(0xFF2E7D32) else Color(0xFF4CAF50)

    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        ),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, fontSize = 12.sp, color = Color(0xFF666666))
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Spacer(Modifier.height(4.dp))
            Text(trend, fontSize = 11.sp, color = trendColor)
        }
    }
}

@Composable
private fun DashboardCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, top = 12.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
                Text("Ver todas", fontSize = 12.sp, color = Color(0xFF1B4F7A))
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

// ── Gráfico de barras vertical ───────────────────────────────────────────────
@Composable
private fun BarChart(benchmarks: List<AreaBenchmark>, colors: List<Color>) {
    val maxVal = benchmarks.maxOf { it.value }.toFloat()
    val barHeight = 80.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        benchmarks.forEachIndexed { i, b ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = b.value.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height((b.value / maxVal * barHeight.value).dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(colors[i % colors.size])
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = b.name,
                    fontSize = 9.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 11.sp
                )
            }
        }
    }
}

// ── Donut chart + legenda ─────────────────────────────────────────────────────
@Composable
private fun ROIDonutSection(data: List<AreaROI>, colors: List<Color>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Donut
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(130.dp)) {
                val stroke = 24.dp.toPx()
                val half   = stroke / 2
                var startAngle = -90f
                data.forEachIndexed { i, roi ->
                    val sweep = roi.percentage * 3.6f
                    drawArc(
                        color      = colors[i % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweep - 1.5f,
                        useCenter  = false,
                        topLeft    = Offset(half, half),
                        size       = Size(size.width - stroke, size.height - stroke),
                        style      = Stroke(width = stroke, cap = StrokeCap.Butt)
                    )
                    startAngle += sweep
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("R$ 1,250M", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text("Total", fontSize = 10.sp, color = Color(0xFF888888))
            }
        }

        Spacer(Modifier.width(16.dp))

        // Legenda
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEachIndexed { i, roi ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(colors[i % colors.size])
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = roi.name,
                        fontSize = 12.sp,
                        color = Color(0xFF444444),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${roi.percentage.toInt()}%",
                        fontSize = 11.sp,
                        color = Color(0xFF888888)
                    )
                }
            }
        }
    }
}
