package br.com.fiap.inovagab.ui.features.lider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import br.com.fiap.inovagab.data.model.AreaBenchmark
import br.com.fiap.inovagab.data.model.AreaROI
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.ProfileHeader

private val areaColors = listOf(
    Color(0xFF4CAF50),
    Color(0xFF2196F3),
    Color(0xFF9C27B0),
    Color(0xFFFF9800),
    Color(0xFFF44336)
)

@Composable
fun LiderDashboard(
    liderViewModel: LiderViewModel,
    loggedUser: User?,
    onEconomiaClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column {
                ProfileHeader(
                    name = loggedUser?.nome ?: "Usuário",
                    role = loggedUser?.perfil?.label ?: "Líder"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Dashboard Executivo",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp,
                        color      = Color(0xFF1A1A1A)
                    )
                    Icon(
                        imageVector        = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint               = Color(0xFF888888),
                        modifier           = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            item {
                Column(modifier = Modifier.padding(14.dp, 14.dp, 14.dp, 0.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MetricCard(
                            label    = "ROI total",
                            value    = liderViewModel.economiaGerada.formatCurrency(),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(10.dp))
                        MetricCard(
                            label    = "Projetos ativos",
                            value    = liderViewModel.projetosAtivos.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MetricCard(
                            label    = "Ideias recebidas",
                            value    = liderViewModel.totalIdeias.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(10.dp))
                        MetricCard(
                            label         = "Economia gerada",
                            value         = liderViewModel.economiaGerada.formatCurrency(),
                            isHighlighted = true,
                            modifier      = Modifier.weight(1f),
                            onClick       = onEconomiaClick
                        )
                    }
                }
            }

            if (liderViewModel.benchmarks.isNotEmpty()) {
                item {
                    DashboardCard(title = "Benchmark entre áreas") {
                        BarChart(benchmarks = liderViewModel.benchmarks, colors = areaColors)
                    }
                }
            }

            if (liderViewModel.roiAreas.isNotEmpty()) {
                item {
                    DashboardCard(title = "ROI por área (R$)") {
                        ROIDonutSection(
                            data   = liderViewModel.roiAreas,
                            colors = areaColors,
                            total  = liderViewModel.economiaGerada.formatCurrency()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val bgColor    = if (isHighlighted) Color(0xFFE8F5E9) else Color.White
    val valueColor = if (isHighlighted) Color(0xFF2E7D32) else Color(0xFF1A1A1A)

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
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun BarChart(benchmarks: List<AreaBenchmark>, colors: List<Color>) {
    if (benchmarks.isEmpty()) return
    val maxVal  = benchmarks.maxOf { it.value }.toFloat()
    val barHeight = 80.dp

    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.Bottom
    ) {
        benchmarks.forEachIndexed { i, b ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier.weight(1f)
            ) {
                Text(b.value.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height((if (maxVal > 0) b.value / maxVal * barHeight.value else 4f).dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(colors[i % colors.size])
                )
                Spacer(Modifier.height(6.dp))
                Text(b.name, fontSize = 9.sp, color = Color(0xFF666666), textAlign = TextAlign.Center, lineHeight = 11.sp)
            }
        }
    }
}

@Composable
private fun ROIDonutSection(data: List<AreaROI>, colors: List<Color>, total: String) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(130.dp), contentAlignment = Alignment.Center) {
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
                Text(total, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text("Total", fontSize = 10.sp, color = Color(0xFF888888))
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            data.forEachIndexed { i, roi ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(colors[i % colors.size])
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(roi.name, fontSize = 12.sp, color = Color(0xFF444444), modifier = Modifier.weight(1f))
                    Text("${roi.percentage.toInt()}%", fontSize = 11.sp, color = Color(0xFF888888))
                }
            }
        }
    }
}

private fun Double.formatCurrency(): String = when {
    this >= 1_000_000 -> "R$ %.1fM".format(this / 1_000_000)
    this >= 1_000     -> "R$ %.0fK".format(this / 1_000)
    else              -> "R$ %.0f".format(this)
}
