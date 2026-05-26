package br.com.fiap.inovagab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import br.com.fiap.inovagab.data.sampleIdeas
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailScreen(
    ideaId: Int,
    onBack: () -> Unit = {}
) {
    val idea = sampleIdeas.find { it.id == ideaId } ?: sampleIdeas[1]

    var selectedPriority by remember { mutableStateOf(idea.priority) }
    var priorityExpanded by remember { mutableStateOf(false) }
    val priorities = listOf("Baixa", "Média", "Alta", "Muito alta")

    val allCollaborators = listOf("Mateus", "Gabrielle", "Rael", "Vinicius", "Jhonatan")
    var selectedCollaborators by remember {
        mutableStateOf(idea.collaboratorStatus.keys.toMutableSet())
    }
    var collaboratorsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // TopBar
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF1A1A1A)
                    )
                }
                Text(
                    text = "Sobre a ideia",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Conteúdo com scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card da ideia
            Card(
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
                            text = idea.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        CategoryBadge(category = idea.category)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = idea.description,
                        fontSize = 13.sp,
                        color = Color(0xFF555555),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data de criação: ${idea.date}",
                        fontSize = 11.sp,
                        color = Color(0xFF999999)
                    )
                }
            }

            // Seção de análise
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Análise sobre o projeto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Nota (estrelas)
                    Text(
                        text = "Nota",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < idea.rating) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prioridade
                    Text(
                        text = "Prioridade",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = priorityExpanded,
                        onExpandedChange = { priorityExpanded = !priorityExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPriority,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = Color(0xFF1B4F7A)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false }
                        ) {
                            Text(
                                text = "Selecionar prioridade",
                                fontSize = 12.sp,
                                color = Color(0xFF888888),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            priorities.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontSize = 14.sp) },
                                    onClick = {
                                        selectedPriority = option
                                        priorityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Colaboradores responsáveis
                    Text(
                        text = "Colaboradores responsáveis",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Dropdown multi-select
                    ExposedDropdownMenuBox(
                        expanded = collaboratorsExpanded,
                        onExpandedChange = { collaboratorsExpanded = !collaboratorsExpanded }
                    ) {
                        OutlinedTextField(
                            value = "Selecionar responsável",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = collaboratorsExpanded) },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = Color(0xFF1B4F7A),
                                unfocusedTextColor = Color(0xFFAAAAAA)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = collaboratorsExpanded,
                            onDismissRequest = { collaboratorsExpanded = false }
                        ) {
                            Text(
                                text = "Selecionar responsável",
                                fontSize = 12.sp,
                                color = Color(0xFF888888),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            allCollaborators.forEach { collab ->
                                val isChecked = selectedCollaborators.contains(collab)
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = null,
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Color(0xFF1B4F7A)
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(collab, fontSize = 14.sp)
                                        }
                                    },
                                    onClick = {
                                        selectedCollaborators = selectedCollaborators.toMutableSet().apply {
                                            if (isChecked) remove(collab) else add(collab)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Chips dos selecionados
                    if (selectedCollaborators.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            selectedCollaborators.forEach { collab ->
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (collab == "Mateus") Color(0xFF1B4F7A) else Color(0xFF7B1FA2)
                                ) {
                                    Text(
                                        text = collab,
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status
                    Text(
                        text = "Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    idea.collaboratorStatus.forEach { (name, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$name:",
                                fontSize = 13.sp,
                                color = Color(0xFF444444),
                                modifier = Modifier.width(80.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(status = status)
                        }
                    }
                }
            }
        }
    }
}
