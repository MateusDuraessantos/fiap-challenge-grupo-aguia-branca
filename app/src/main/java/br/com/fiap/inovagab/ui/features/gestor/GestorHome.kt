package br.com.fiap.inovagab.ui.features.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GestorHome(
    gestorViewModel: GestorViewModel,
    loggedUser: User?,
    onIdeaClick: (String) -> Unit = {}
) {
    val ideiasFiltradas = gestorViewModel.ideiasFiltradas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(
                name = loggedUser?.nome ?: "Usuário",
                role = loggedUser?.perfil?.label ?: "Gestor"
            )
        }

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            gestorViewModel.filtros.forEach { filter ->
                val isSelected = gestorViewModel.filtroSelecionado == filter
                FilterChip(
                    selected = isSelected,
                    onClick  = { gestorViewModel.onFiltroChange(filter) },
                    label    = { Text(filter, fontSize = 12.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1B3A52),
                        selectedLabelColor     = Color.White,
                        containerColor         = Color.White,
                        labelColor             = Color(0xFF444444)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled             = true,
                        selected            = isSelected,
                        borderColor         = Color(0xFFCCCCCC),
                        selectedBorderColor = Color.Transparent
                    )
                )
            }
        }

        if (ideiasFiltradas.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text      = "Nenhuma ideia encontrada",
                    fontSize  = 14.sp,
                    color     = Color(0xFF888888)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ideiasFiltradas) { idea ->
                    IdeaCard(ideia = idea, onClick = { onIdeaClick(idea.id) })
                }
            }
        }
    }
}

@Composable
fun IdeaCard(ideia: Ideia, onClick: () -> Unit = {}) {
    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text       = ideia.titulo,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF1A1A1A),
                    modifier   = Modifier.weight(1f).padding(end = 8.dp)
                )
                CategoryBadge(category = ideia.categoria)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text       = ideia.descricao,
                fontSize   = 13.sp,
                color      = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines   = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text     = "Por: ${ideia.operadorNome}",
                    fontSize = 11.sp,
                    color    = Color(0xFF888888)
                )
                Text(
                    text     = ideia.criadaEm.toDateString(),
                    fontSize = 11.sp,
                    color    = Color(0xFF999999)
                )
            }
        }
    }
}

private fun Long.toDateString(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(this))
