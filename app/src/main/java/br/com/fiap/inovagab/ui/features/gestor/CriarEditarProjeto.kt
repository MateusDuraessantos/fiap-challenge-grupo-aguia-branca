package br.com.fiap.inovagab.ui.features.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Projeto
import br.com.fiap.inovagab.data.model.User
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarEditarProjeto(
    editingProjeto: Projeto? = null,
    gestorViewModel: GestorViewModel,
    loggedUser: User?,
    onBack: () -> Unit = {}
) {
    val isEditing = editingProjeto != null

    var titulo      by remember { mutableStateOf(editingProjeto?.titulo ?: "") }
    var descricao   by remember { mutableStateOf(editingProjeto?.descricao ?: "") }
    var resultados  by remember { mutableStateOf(editingProjeto?.resultados ?: "") }
    var progresso   by remember { mutableStateOf((editingProjeto?.progresso ?: 0).toFloat()) }
    var investimento by remember { mutableStateOf(editingProjeto?.investimento?.let { "%.0f".format(it) } ?: "") }
    var retorno     by remember { mutableStateOf(editingProjeto?.retornoFinanceiro?.let { "%.0f".format(it) } ?: "") }
    var categoriaSelected by remember { mutableStateOf(editingProjeto?.categoria ?: "Operações") }
    var statusSelected    by remember { mutableStateOf(editingProjeto?.status ?: "Em andamento") }
    var categoriaExpanded by remember { mutableStateOf(false) }
    var statusExpanded    by remember { mutableStateOf(false) }
    var isSaving          by remember { mutableStateOf(false) }
    var tituloError       by remember { mutableStateOf<String?>(null) }

    val categorias  = listOf("Operações", "RH", "Manutenção", "Atendimento", "Comercial")
    val statusOpcoes = listOf("Em andamento", "Concluído", "Pausado")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Row(
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF1A1A1A)
                    )
                }
                Text(
                    text       = if (isEditing) "Editar projeto" else "Novo projeto",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp,
                    color      = Color(0xFF1A1A1A),
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.width(48.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            OutlinedTextField(
                value         = titulo,
                onValueChange = { titulo = it; tituloError = null },
                label         = { Text("Título do projeto*") },
                isError       = tituloError != null,
                supportingText = tituloError?.let { { Text(it, color = Color(0xFFE53935)) } },
                shape         = RoundedCornerShape(8.dp),
                colors        = fieldColors(),
                modifier      = Modifier.fillMaxWidth()
            )

            // Categoria
            ExposedDropdownMenuBox(
                expanded         = categoriaExpanded,
                onExpandedChange = { categoriaExpanded = !categoriaExpanded }
            ) {
                OutlinedTextField(
                    value         = categoriaSelected,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Categoria") },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) },
                    shape         = RoundedCornerShape(8.dp),
                    colors        = fieldColors(),
                    modifier      = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded         = categoriaExpanded,
                    onDismissRequest = { categoriaExpanded = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text    = { Text(cat, fontSize = 14.sp) },
                            onClick = { categoriaSelected = cat; categoriaExpanded = false }
                        )
                    }
                }
            }

            // Descrição
            OutlinedTextField(
                value         = descricao,
                onValueChange = { descricao = it },
                label         = { Text("Descrição") },
                minLines      = 3,
                shape         = RoundedCornerShape(8.dp),
                colors        = fieldColors(),
                modifier      = Modifier.fillMaxWidth()
            )

            // Status
            ExposedDropdownMenuBox(
                expanded         = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value         = statusSelected,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Status") },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    shape         = RoundedCornerShape(8.dp),
                    colors        = fieldColors(),
                    modifier      = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded         = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOpcoes.forEach { s ->
                        DropdownMenuItem(
                            text    = { Text(s, fontSize = 14.sp) },
                            onClick = { statusSelected = s; statusExpanded = false }
                        )
                    }
                }
            }

            // Progresso slider
            Column {
                Text(
                    text       = "Progresso: ${progresso.toInt()}%",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF1A1A1A)
                )
                Spacer(Modifier.height(4.dp))
                Slider(
                    value         = progresso,
                    onValueChange = { progresso = it },
                    valueRange    = 0f..100f,
                    steps         = 19,
                    colors        = SliderDefaults.colors(
                        thumbColor       = Color(0xFF1B4F7A),
                        activeTrackColor = Color(0xFF1B4F7A)
                    )
                )
            }

            // Investimento e Retorno
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value         = investimento,
                    onValueChange = { investimento = it.filter { c -> c.isDigit() || c == '.' } },
                    label         = { Text("Investimento (R$)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape         = RoundedCornerShape(8.dp),
                    colors        = fieldColors(),
                    modifier      = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value         = retorno,
                    onValueChange = { retorno = it.filter { c -> c.isDigit() || c == '.' } },
                    label         = { Text("Retorno (R$)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape         = RoundedCornerShape(8.dp),
                    colors        = fieldColors(),
                    modifier      = Modifier.weight(1f)
                )
            }

            // Resultados
            OutlinedTextField(
                value         = resultados,
                onValueChange = { resultados = it },
                label         = { Text("Resultados alcançados") },
                minLines      = 3,
                shape         = RoundedCornerShape(8.dp),
                colors        = fieldColors(),
                modifier      = Modifier.fillMaxWidth()
            )

            // Salvar
            if (isSaving) {
                Box(
                    modifier         = Modifier.fillMaxWidth().height(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1B4F7A), modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
                }
            } else {
                Button(
                    onClick = {
                        if (titulo.isBlank()) { tituloError = "Informe o título"; return@Button }
                        isSaving = true
                        val uid  = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        val nome = loggedUser?.nome ?: ""
                        val projeto = Projeto(
                            id                = editingProjeto?.id ?: "",
                            titulo            = titulo.trim(),
                            descricao         = descricao.trim(),
                            categoria         = categoriaSelected,
                            progresso         = progresso.toInt(),
                            investimento      = investimento.toDoubleOrNull() ?: 0.0,
                            retornoFinanceiro = retorno.toDoubleOrNull() ?: 0.0,
                            resultados        = resultados.trim(),
                            status            = statusSelected,
                            gestorUid         = editingProjeto?.gestorUid ?: uid,
                            gestorNome        = editingProjeto?.gestorNome ?: nome,
                            criadoEm          = editingProjeto?.criadoEm ?: System.currentTimeMillis()
                        )
                        if (isEditing) {
                            gestorViewModel.atualizarProjeto(projeto) { isSaving = false; onBack() }
                        } else {
                            gestorViewModel.criarProjeto(projeto) { isSaving = false; onBack() }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A52))
                ) {
                    Text(
                        if (isEditing) "Salvar alterações" else "Criar projeto",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = Color(0xFF1B4F7A),
    unfocusedBorderColor    = Color(0xFFE0E0E0),
    focusedContainerColor   = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor             = Color(0xFF1B4F7A)
)
