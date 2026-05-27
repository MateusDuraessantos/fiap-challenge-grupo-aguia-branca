package br.com.fiap.inovagab.ui.features.operador

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.ProfileHeader
import br.com.fiap.inovagab.ui.components.statusColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OperadorHome(
    operadorViewModel: OperadorViewModel,
    loggedUser: User?,
    uid: String,
    onCreateIdeaClick: () -> Unit = {},
    onEditIdeaClick: (Ideia) -> Unit = {}
) {
    LaunchedEffect(uid) {
        operadorViewModel.load(uid)
    }

    val resumo = operadorViewModel.resumoPorStatus()
    var confirmDeleteId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(
                name = loggedUser?.nome ?: "Usuário",
                role = loggedUser?.perfil?.label ?: "Operador"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text       = "Minhas ideias",
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = Color(0xFF1A1A1A),
                modifier   = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 14.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusSummaryCard(
                    count    = resumo["Em análise"] ?: 0,
                    label    = "Em análise",
                    color    = Color(0xFF2196F3),
                    icon     = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count    = resumo["Aprovada"] ?: 0,
                    label    = "Aprovada",
                    color    = Color(0xFF4CAF50),
                    icon     = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count    = resumo["Virou projeto"] ?: 0,
                    label    = "Virou projeto",
                    color    = Color(0xFF9C27B0),
                    icon     = Icons.Default.RocketLaunch,
                    modifier = Modifier.weight(1f)
                )
                StatusSummaryCard(
                    count    = resumo["Rejeitada"] ?: 0,
                    label    = "Rejeitada",
                    color    = Color(0xFFE53935),
                    icon     = Icons.Default.Cancel,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

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
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier         = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nova ideia", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        Text("Envie uma ideia ou problema", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text       = "Histórico de ideias",
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = Color(0xFF1A1A1A),
                modifier   = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            )

            IdeasTable(
                ideas           = operadorViewModel.minhasIdeias,
                onEditClick     = onEditIdeaClick,
                onDeleteRequest = { id -> confirmDeleteId = id }
            )

            Spacer(Modifier.height(16.dp))
        }
    }

    // Confirmação de exclusão
    confirmDeleteId?.let { id ->
        AlertDialog(
            onDismissRequest = { confirmDeleteId = null },
            title   = { Text("Excluir ideia") },
            text    = { Text("Deseja excluir esta ideia? Esta ação não poderá ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    operadorViewModel.deletarIdeia(id)
                    confirmDeleteId = null
                }) {
                    Text("Excluir", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDeleteId = null }) { Text("Cancelar") }
            }
        )
    }
}

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
            modifier         = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.height(5.dp))
        Text(text = count.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A1A1A))
        Text(text = label, fontSize = 9.sp, color = Color(0xFF888888), textAlign = TextAlign.Center, lineHeight = 11.sp)
    }
}

@Composable
private fun IdeasTable(
    ideas: List<Ideia>,
    onEditClick: (Ideia) -> Unit,
    onDeleteRequest: (String) -> Unit
) {
    var menuExpandedId by remember { mutableStateOf<String?>(null) }

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
            TableHeaderCell("Título",  Modifier.weight(2.2f))
            TableHeaderCell("Status",  Modifier.weight(1.8f))
            TableHeaderCell("Data",    Modifier.weight(1.5f))
            // Espaço reservado para o ícone de menu
            Spacer(Modifier.width(36.dp))
        }

        if (ideas.isEmpty()) {
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma ideia enviada ainda", fontSize = 13.sp, color = Color(0xFF888888))
            }
        } else {
            ideas.forEach { idea ->
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Título com ellipsis
                    Text(
                        text     = idea.titulo,
                        fontSize = 12.sp,
                        color    = Color(0xFF1A1A1A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2.2f)
                    )

                    // Status
                    Text(
                        text       = idea.status,
                        fontSize   = 11.sp,
                        color      = statusColor(idea.status),
                        fontWeight = FontWeight.Medium,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1.8f)
                    )

                    // Data
                    Text(
                        text     = idea.criadaEm.toDateString(),
                        fontSize = 11.sp,
                        color    = Color(0xFF666666),
                        maxLines = 1,
                        modifier = Modifier.weight(1.5f)
                    )

                    // Menu de 3 pontos — apenas para ideias ainda não avaliadas
                    Box(modifier = Modifier.width(36.dp)) {
                        if (idea.status == "Em análise") {
                            IconButton(
                                onClick  = { menuExpandedId = idea.id },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector        = Icons.Default.MoreVert,
                                    contentDescription = "Opções",
                                    tint               = Color(0xFF888888),
                                    modifier           = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded         = menuExpandedId == idea.id,
                                onDismissRequest = { menuExpandedId = null }
                            ) {
                                DropdownMenuItem(
                                    text         = { Text("Editar", fontSize = 14.sp) },
                                    onClick      = { menuExpandedId = null; onEditClick(idea) },
                                    leadingIcon  = {
                                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF1B4F7A))
                                    }
                                )
                                DropdownMenuItem(
                                    text        = { Text("Excluir", fontSize = 14.sp, color = Color(0xFFE53935)) },
                                    onClick     = { menuExpandedId = null; onDeleteRequest(idea.id) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE53935))
                                    }
                                )
                            }
                        }
                    }
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
        Spacer(Modifier.width(2.dp))
        Icon(Icons.Default.UnfoldMore, contentDescription = null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(14.dp))
    }
}

private fun Long.toDateString(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(this))
