package br.com.fiap.inovagab.ui.screens.lider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.sampleStrategies
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader

@Composable
fun LiderStrategiesScreen(onCreateClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(name = "João", role = "Líder")
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {

            // ── Botão criar nova estratégia ───────────────────────────────────
            item {
                Button(
                    onClick = onCreateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .height(48.dp),
                    shape  = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Criar nova estratégia",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            // ── Título ────────────────────────────────────────────────────────
            item {
                Text(
                    text = "Estratégias atuais da empresa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                )
            }

            // ── Cards de estratégia ───────────────────────────────────────────
            items(sampleStrategies) { strategy ->
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    shape     = RoundedCornerShape(12.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = strategy.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.weight(1f),
                                lineHeight = 18.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            CategoryBadge(category = strategy.category)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = strategy.description,
                            fontSize = 12.sp,
                            color = Color(0xFF555555),
                            lineHeight = 17.sp,
                            maxLines = 4
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Data de criação: ${strategy.date}",
                            fontSize = 11.sp,
                            color = Color(0xFF999999)
                        )
                    }
                }
            }
        }
    }
}
