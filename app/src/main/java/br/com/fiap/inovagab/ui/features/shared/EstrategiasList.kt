package br.com.fiap.inovagab.ui.features.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Estrategia
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EstrategiasList(
    perfil: Perfil,
    loggedUser: User?,
    estrategiaViewModel: EstrategiaViewModel,
    onCreateClick: () -> Unit = {},
    onEditClick: (Estrategia) -> Unit = {}
) {
    var confirmDelete by remember { mutableStateOf<Estrategia?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(
                name = loggedUser?.nome ?: "Usuário",
                role = perfil.label
            )
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {

            if (perfil == Perfil.LIDER) {
                item {
                    Button(
                        onClick  = onCreateClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .height(48.dp),
                        shape  = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Criar nova estratégia", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        text       = "Estratégias atuais da empresa",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = Color(0xFF1A1A1A)
                    )
                    if (perfil != Perfil.LIDER) {
                        Text(
                            text      = "*Consultar orientações sobre as estratégias da empresa",
                            fontSize  = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color     = Color(0xFF888888),
                            modifier  = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            if (estrategiaViewModel.estrategias.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhuma estratégia cadastrada", fontSize = 14.sp, color = Color(0xFF888888))
                    }
                }
            } else {
                items(estrategiaViewModel.estrategias) { estrategia ->
                    EstrategiaCard(
                        estrategia = estrategia,
                        isLider    = perfil == Perfil.LIDER,
                        onEdit     = { onEditClick(estrategia) },
                        onDelete   = { confirmDelete = estrategia }
                    )
                }
            }
        }
    }

    confirmDelete?.let { est ->
        AlertDialog(
            onDismissRequest = { confirmDelete = null },
            title   = { Text("Excluir estratégia") },
            text    = { Text("Deseja excluir \"${est.titulo}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    estrategiaViewModel.deletar(est.id)
                    confirmDelete = null
                }) {
                    Text("Excluir", color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun EstrategiaCard(
    estrategia: Estrategia,
    isLider: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Text(
                    text       = estrategia.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Color(0xFF1A1A1A),
                    modifier   = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    lineHeight = 18.sp
                )
                Row {
                    CategoryBadge(category = estrategia.categoria)
                    if (isLider) {
                        Spacer(Modifier.width(4.dp))
                        IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1B4F7A), modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text       = estrategia.descricao,
                fontSize   = 13.sp,
                color      = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines   = 4
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text     = "Por: ${estrategia.criadaPorNome.ifBlank { "—" }}",
                    fontSize = 11.sp,
                    color    = Color(0xFF999999)
                )
                Text(
                    text     = estrategia.criadaEm.toDateString(),
                    fontSize = 11.sp,
                    color    = Color(0xFF999999)
                )
            }
        }
    }
}

private fun Long.toDateString(): String =
    if (this == 0L) "—"
    else SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(this))
