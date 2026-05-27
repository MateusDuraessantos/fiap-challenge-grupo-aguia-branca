package br.com.fiap.inovagab.ui.features.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.CategoryBadge
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalisarIdeia(
    ideaId: String,
    gestorViewModel: GestorViewModel,
    loggedUser: User? = null,
    onBack: () -> Unit = {}
) {
    val ideia = gestorViewModel.getIdeia(ideaId) ?: return

    var selectedNota       by remember { mutableStateOf(ideia.nota) }
    var selectedPrioridade by remember { mutableStateOf(ideia.prioridade) }
    var selectedStatus     by remember { mutableStateOf(ideia.status) }
    var prioridadeExpanded by remember { mutableStateOf(false) }
    var statusExpanded     by remember { mutableStateOf(false) }
    var isSaving           by remember { mutableStateOf(false) }

    val prioridades  = listOf("Baixa", "Média", "Alta", "Muito alta")
    val statusOpcoes = listOf("Em análise", "Aprovada", "Rejeitada", "Virou projeto")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                IconButton(
                    onClick  = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint               = Color(0xFF1A1A1A)
                    )
                }
                Text(
                    text       = "Sobre a ideia",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF1A1A1A),
                    modifier   = Modifier.align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Informações da ideia ─────────────────────────────────────────
            Card(
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
                            text       = ideia.titulo,
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF1A1A1A),
                            modifier   = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        CategoryBadge(category = ideia.categoria)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text       = ideia.descricao,
                        fontSize   = 13.sp,
                        color      = Color(0xFF555555),
                        lineHeight = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text       = "Enviado por: ${ideia.operadorNome}",
                        fontSize   = 12.sp,
                        color      = Color(0xFF888888),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = "Data: ${ideia.criadaEm.toDateString()}",
                        fontSize = 11.sp,
                        color    = Color(0xFF999999)
                    )
                }
            }

            // ── Painel de análise ────────────────────────────────────────────
            Card(
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "Análise do gestor",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF1A1A1A)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Colaboradores (read-only — nome do operador que enviou)
                    Text(
                        text       = "Colaboradores responsáveis",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ideia.colaboradores.forEach { collab ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFF1B4F7A)
                            ) {
                                Text(
                                    text     = collab,
                                    fontSize = 12.sp,
                                    color    = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Nota por estrelas (clicável)
                    Text(
                        text       = "Nota",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(5) { index ->
                            IconButton(
                                onClick  = { selectedNota = index + 1 },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector        = if (index < selectedNota) Icons.Default.Star else Icons.Default.StarOutline,
                                    contentDescription = null,
                                    tint               = Color(0xFFFFC107),
                                    modifier           = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Prioridade
                    Text(
                        text       = "Prioridade",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded         = prioridadeExpanded,
                        onExpandedChange = { prioridadeExpanded = !prioridadeExpanded }
                    ) {
                        OutlinedTextField(
                            value         = selectedPrioridade,
                            onValueChange = {},
                            readOnly      = true,
                            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prioridadeExpanded) },
                            shape         = RoundedCornerShape(8.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor   = Color(0xFF1B4F7A)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded         = prioridadeExpanded,
                            onDismissRequest = { prioridadeExpanded = false }
                        ) {
                            prioridades.forEach { option ->
                                DropdownMenuItem(
                                    text    = { Text(option, fontSize = 14.sp) },
                                    onClick = {
                                        selectedPrioridade = option
                                        prioridadeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Status
                    Text(
                        text       = "Status",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded         = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded }
                    ) {
                        OutlinedTextField(
                            value         = selectedStatus,
                            onValueChange = {},
                            readOnly      = true,
                            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            shape         = RoundedCornerShape(8.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor   = Color(0xFF1B4F7A)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded         = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            statusOpcoes.forEach { option ->
                                DropdownMenuItem(
                                    text    = { Text(option, fontSize = 14.sp) },
                                    onClick = {
                                        selectedStatus = option
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    if (isSaving) {
                        Box(
                            modifier         = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color       = Color(0xFF1B4F7A),
                                modifier    = Modifier.size(26.dp),
                                strokeWidth = 2.5.dp
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                isSaving = true
                                gestorViewModel.salvarAnalise(
                                    ideaId     = ideaId,
                                    nota       = selectedNota,
                                    prioridade = selectedPrioridade,
                                    status     = selectedStatus,
                                    gestorUid  = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                    gestorNome = loggedUser?.nome ?: "",
                                    onDone     = { isSaving = false; onBack() }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape  = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A))
                        ) {
                            Text("Salvar análise", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

private fun Long.toDateString(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(this))
