package br.com.fiap.inovagab.ui.features.lider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.InovagabButton
import br.com.fiap.inovagab.ui.features.shared.EstrategiaViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaEstrategia(
    estrategiaViewModel: EstrategiaViewModel,
    loggedUser: User?,
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val isEditing  = estrategiaViewModel.editingId != null
    var isSaving   by remember { mutableStateOf(false) }
    var catExpanded by remember { mutableStateOf(false) }

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
                IconButton(onClick = { estrategiaViewModel.clearForm(); onBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF1A1A1A))
                }
                Text(
                    text       = if (isEditing) "Editar estratégia" else "Criar nova estratégia",
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
                .padding(16.dp)
        ) {
            Text(
                text      = if (isEditing) "*Edite os dados da estratégia" else "*Adicione novas estratégias para a empresa",
                fontSize  = 13.sp,
                color     = Color(0xFF888888),
                fontStyle = FontStyle.Italic,
                modifier  = Modifier.padding(bottom = 20.dp)
            )

            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Título
                    Text(
                        text       = "Título",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color(0xFF1A1A1A),
                        modifier   = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value         = estrategiaViewModel.tituloForm,
                        onValueChange = estrategiaViewModel::onTituloChange,
                        placeholder   = { Text("Título da estratégia", color = Color(0xFFBDBDBD), fontSize = 14.sp) },
                        shape         = RoundedCornerShape(8.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = Color(0xFF1B4F7A),
                            unfocusedBorderColor    = Color(0xFFE0E0E0),
                            focusedContainerColor   = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor             = Color(0xFF1B4F7A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Categoria
                    Text(
                        text       = "Categoria",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color(0xFF1A1A1A),
                        modifier   = Modifier.padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded         = catExpanded,
                        onExpandedChange = { catExpanded = !catExpanded }
                    ) {
                        OutlinedTextField(
                            value         = estrategiaViewModel.categoriaForm,
                            onValueChange = {},
                            readOnly      = true,
                            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                            shape         = RoundedCornerShape(8.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor      = Color(0xFF1B4F7A),
                                unfocusedBorderColor    = Color(0xFFE0E0E0),
                                focusedContainerColor   = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded         = catExpanded,
                            onDismissRequest = { catExpanded = false }
                        ) {
                            estrategiaViewModel.categorias.forEach { cat ->
                                DropdownMenuItem(
                                    text    = { Text(cat, fontSize = 14.sp) },
                                    onClick = { estrategiaViewModel.onCategoriaChange(cat); catExpanded = false }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Descrição
                    Text(
                        text       = "Descrição da estratégia",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Color(0xFF1A1A1A),
                        modifier   = Modifier.padding(bottom = 10.dp)
                    )
                    OutlinedTextField(
                        value         = estrategiaViewModel.descricaoForm,
                        onValueChange = estrategiaViewModel::onDescricaoChange,
                        placeholder   = { Text("Descreva a estratégia", color = Color(0xFFBDBDBD), fontSize = 14.sp) },
                        minLines      = 6,
                        shape         = RoundedCornerShape(8.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = Color(0xFF1B4F7A),
                            unfocusedBorderColor    = Color(0xFFE0E0E0),
                            focusedContainerColor   = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor             = Color(0xFF1B4F7A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (isSaving) {
                Box(
                    modifier         = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1B4F7A), modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
                }
            } else {
                InovagabButton(
                    text    = if (isEditing) "Salvar alterações" else "Submeter nova estratégia",
                    onClick = {
                        isSaving = true
                        estrategiaViewModel.submeter(
                            uid   = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            nome  = loggedUser?.nome ?: "",
                            onDone = { isSaving = false; onSubmit() }
                        )
                    },
                    background = Color(0xFF1B3A52)
                )
            }
        }
    }
}
