package br.com.fiap.inovagab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.Strategy
import br.com.fiap.inovagab.data.sampleStrategies
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader

@Composable
fun StrategiesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader()
        }

        // Título
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "Estratégias atuais da empresa",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = "*Consultar orientações sobre as estratégias da empresa",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF888888),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Lista
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleStrategies) { strategy ->
                StrategyCard(strategy = strategy)
            }
        }
    }
}

@Composable
fun StrategyCard(strategy: Strategy) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = strategy.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                CategoryBadge(category = strategy.category)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = strategy.description,
                fontSize = 13.sp,
                color = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Data de criação: ${strategy.date}",
                fontSize = 11.sp,
                color = Color(0xFF999999)
            )
        }
    }
}
