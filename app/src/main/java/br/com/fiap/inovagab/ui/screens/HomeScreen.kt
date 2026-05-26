package br.com.fiap.inovagab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.Idea
import br.com.fiap.inovagab.data.sampleIdeas
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader

@Composable
fun HomeScreen(onIdeaClick: (Int) -> Unit = {}) {

    val filters = listOf("Todas", "Em análise", "Prioritárias", "Aprovadas")
    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredIdeas = when (selectedFilter) {
        "Em análise"  -> sampleIdeas.filter { it.collaboratorStatus.values.contains("Em análise") }
        "Aprovadas"   -> sampleIdeas.filter { it.collaboratorStatus.values.all { s -> s == "Aprovado" } }
        "Prioritárias"-> sampleIdeas.filter { it.priority == "Alta" || it.priority == "Muito alta" }
        else          -> sampleIdeas
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader()
        }

        // Título + ícone de filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ideias para análise",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtrar",
                tint = Color(0xFF888888)
            )
        }

        // Chips de filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1B3A52),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF444444)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color(0xFFCCCCCC),
                        selectedBorderColor = Color.Transparent
                    )
                )
            }
        }

        // Lista de ideias
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredIdeas) { idea ->
                IdeaCard(idea = idea, onClick = { onIdeaClick(idea.id) })
            }
        }
    }
}

@Composable
fun IdeaCard(idea: Idea, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título + badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = idea.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                CategoryBadge(category = idea.category)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = idea.description,
                fontSize = 13.sp,
                color = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Data de criação: ${idea.date}",
                fontSize = 11.sp,
                color = Color(0xFF999999)
            )
        }
    }
}
