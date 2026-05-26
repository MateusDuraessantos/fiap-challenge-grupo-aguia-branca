package br.com.fiap.inovagab.ui.screens.operador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.MyIdea
import br.com.fiap.inovagab.data.myIdeas
import br.com.fiap.inovagab.ui.components.ProfileHeader

// ─── Cor de status da ideia ───────────────────────────────────────────────────
fun ideaStatusColor(status: String): Color = when (status) {
    "Em análise"    -> Color(0xFF2196F3)
    "Aprovada"      -> Color(0xFF4CAF50)
    "Virou projeto" -> Color(0xFF9C27B0)
    "Rejeitada"     -> Color(0xFFE53935)
    else            -> Color(0xFF888888)
}

// ─── Tela principal ───────────────────────────────────────────────────────────
@Composable
fun OperadorHomeScreen(onCreateIdeaClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(name = "João", role = "Operador")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Título ─────────────────────────────────────────────────────────
            Text(
                text = "Minhas ideias",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 14.dp)
            )

            // ── Cards de status ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusSummaryCard(
                    count = 3, label = "Em análise",
                    color = Color(0xFF2196F3), icon = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count = 1, label = "Aprovada",
                    color = Color(0xFF4CAF50), icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count = 0, label = "Virou projeto",
                    color = Color(0xFF9C27B0), icon = Icons.Default.RocketLaunch,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count = 1, label = "Rejeitada",
                    color = Color(0xFFE53935), icon = Icons.Default.Cancel,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Botão nova ideia ───────────────────────────────────────────────
            Button(
                onClick = onCreateIdeaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(64.dp),
                shape  = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Nova ideia",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            "Envie uma ideia ou problema",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            // ── Tabela de ideias ───────────────────────────────────────────────
            Text(
                text = "Minhas ideias",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            )

            IdeasTable(ideas = myIdeas)

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─── Card de status resumido ──────────────────────────────────────────────────
@Composable
private fun StatusSummaryCard(
    count: Int,
    label: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF888888),
            textAlign = TextAlign.Center,
            lineHeight = 11.sp
        )
    }
}

// ─── Tabela de ideias ─────────────────────────────────────────────────────────
@Composable
private fun IdeasTable(ideas: List<MyIdea>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeaderCell("Ideia",   Modifier.weight(2f))
            TableHeaderCell("Status",  Modifier.weight(2f))
            TableHeaderCell("Data",    Modifier.weight(1.8f))
            TableHeaderCell("Editar",  Modifier.weight(0.8f))
        }

        ideas.forEach { idea ->
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    idea.id,
                    fontSize = 12.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.weight(2f)
                )
                Text(
                    idea.status,
                    fontSize = 11.sp,
                    color = ideaStatusColor(idea.status),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    idea.date,
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.weight(1.8f)
                )
                Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.CenterStart) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Opções",
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
    }
}

@Composable
private fun TableHeaderCell(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF444444))
        if (text != "Editar") {
            Spacer(Modifier.width(2.dp))
            Icon(Icons.Default.UnfoldMore, contentDescription = null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(14.dp))
        }
    }
}
