package br.com.fiap.inovagab.ui.screens.lider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.*
import br.com.fiap.inovagab.ui.components.categoryColor

@Composable
fun LiderEconomiaScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // ── TopBar ────────────────────────────────────────────────────────────
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF1A1A1A))
                }
                Text(
                    text = "Economia gerada por ideias",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(48.dp))
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {

            // ── Subtítulo ─────────────────────────────────────────────────────
            item {
                Text(
                    text = "Acompanhe como cada ideia aprovada está gerando impacto em cada área e contribuindo para o total de economia.",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    lineHeight = 18.sp
                )
            }

            // ── Card total de economia ────────────────────────────────────────
            item {
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    shape     = RoundedCornerShape(14.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Total economia gerada",
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "R$ 980K",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B4F7A)
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("+15% vs abr/24", fontSize = 12.sp, color = Color(0xFF4CAF50))
                            }
                        }
                        // Ícone moeda
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF3E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💰", fontSize = 28.sp)
                        }
                    }
                }
            }

            // ── Título seção ──────────────────────────────────────────────────
            item {
                Text(
                    "Resumo por área",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
                )
                Text(
                    "Top ideias que geram economia nesta área",
                    fontSize = 11.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // ── Lista de ideias top ───────────────────────────────────────────
            items(topIdeasEconomy) { idea ->
                EconomyIdeaRow(idea = idea)
            }

            // ── Link ver todas ────────────────────────────────────────────────
            item {
                Text(
                    "Ver todas as ideias da área >",
                    fontSize = 13.sp,
                    color = Color(0xFF1B4F7A),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // ── Outras áreas ─────────────────────────────────────────────────
            items(otherAreasEconomy) { area ->
                AreaEconomyRow(area = area)
            }
        }
    }
}

// ─── Linha de ideia com ranking ───────────────────────────────────────────────
@Composable
private fun EconomyIdeaRow(idea: IdeaEconomy) {
    val statusBg = if (idea.status == "Aprovado") Color(0xFF4CAF50) else Color(0xFFFF9800)

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(10.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Número do ranking
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = idea.rank.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444)
                )
            }
            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = idea.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2,
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(idea.period, fontSize = 10.sp, color = Color(0xFF999999))
                Spacer(Modifier.height(6.dp))
                // Barra de progresso
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFEEEEEE))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(idea.percentage / 100f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF4CAF50))
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = idea.value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${idea.percentage}%",
                    fontSize = 11.sp,
                    color = Color(0xFF888888)
                )
                Spacer(Modifier.height(4.dp))
                // Badge status
                Text(
                    text = idea.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(50))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// ─── Linha de área com total ──────────────────────────────────────────────────
@Composable
private fun AreaEconomyRow(area: AreaEconomy) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(10.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone colorido por área
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(categoryColor(area.name).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(categoryColor(area.name))
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = area.name,
                fontSize = 13.sp,
                color = Color(0xFF444444),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = area.value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "(${area.percentage}%)",
                fontSize = 12.sp,
                color = Color(0xFF888888)
            )
        }
    }
}
