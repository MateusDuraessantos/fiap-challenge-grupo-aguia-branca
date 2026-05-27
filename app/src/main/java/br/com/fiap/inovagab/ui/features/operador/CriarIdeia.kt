package br.com.fiap.inovagab.ui.features.operador

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
import br.com.fiap.inovagab.ui.components.InovagabOutlinedButton
import br.com.fiap.inovagab.ui.components.ProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarIdeia(
    operadorViewModel: OperadorViewModel,
    loggedUser: User?,
    uid: String,
    onCancel: () -> Unit = {},
    onSend: () -> Unit = {}
) {
    val isEditMode  = operadorViewModel.editingIdeiaId != null
    var expanded    by remember { mutableStateOf(false) }
    var isSending   by remember { mutableStateOf(false) }
    var tituloError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Column {
                ProfileHeader(
                    name = loggedUser?.nome ?: "Usuário",
                    role = loggedUser?.perfil?.label ?: "Operador"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF1A1A1A))
                    }
                    Text(
                        text       = if (isEditMode) "Editar ideia" else "Criar nova ideia",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp,
                        color      = Color(0xFF1A1A1A),
                        modifier   = Modifier.weight(1f),
                        textAlign  = TextAlign.Center
                    )
                    Spacer(Modifier.width(40.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text      = if (isEditMode) "*Altere os dados que desejar" else "*Preencha os dados da sua ideia",
                fontSize  = 13.sp,
                color     = Color(0xFF888888),
                fontStyle = FontStyle.Italic,
                modifier  = Modifier.padding(bottom = 18.dp)
            )

            // Título
            Text(
                text       = "Título da ideia",
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = Color(0xFF1A1A1A),
                modifier   = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value         = operadorViewModel.tituloForm,
                onValueChange = { operadorViewModel.onTituloChange(it); tituloError = null },
                placeholder   = { Text("Resuma sua ideia em uma frase", color = Color(0xFFBDBDBD), fontSize = 14.sp) },
                isError       = tituloError != null,
                supportingText = tituloError?.let { { Text(it, color = Color(0xFFE53935)) } },
                shape         = RoundedCornerShape(8.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = Color(0xFF1B4F7A),
                    unfocusedBorderColor    = Color(0xFFE0E0E0),
                    focusedContainerColor   = Color.White,
                    unfocusedContainerColor = Color.White
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
                expanded         = expanded,
                onExpandedChange = { expanded = it },
                modifier         = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value         = operadorViewModel.categoriaForm,
                    onValueChange = {},
                    readOnly      = true,
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                    expanded         = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    operadorViewModel.categorias.forEach { cat ->
                        DropdownMenuItem(
                            text    = { Text(cat, fontSize = 14.sp) },
                            onClick = { operadorViewModel.onCategoriaChange(cat); expanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Descrição
            Text(
                text       = "Descrição da ideia",
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = Color(0xFF1A1A1A),
                modifier   = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value         = operadorViewModel.descricaoForm,
                onValueChange = operadorViewModel::onDescricaoChange,
                placeholder   = { Text("Descreva sua ideia em detalhes", color = Color(0xFFBDBDBD), fontSize = 14.sp) },
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

            Spacer(Modifier.height(24.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InovagabOutlinedButton(
                    text     = "Cancelar",
                    onClick  = onCancel,
                    modifier = Modifier.weight(1f)
                )
                if (isSending) {
                    Box(
                        modifier         = Modifier
                            .weight(1f)
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1B4F7A), modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                    }
                } else {
                    Button(
                        onClick = {
                            if (operadorViewModel.tituloForm.isBlank() && operadorViewModel.descricaoForm.isBlank()) {
                                tituloError = "Informe o título da ideia"
                                return@Button
                            }
                            isSending = true
                            if (isEditMode) {
                                operadorViewModel.atualizarIdeia(
                                    onDone = { isSending = false; onSend() }
                                )
                            } else {
                                operadorViewModel.enviarIdeia(
                                    uid   = uid,
                                    nome  = loggedUser?.nome ?: "Usuário",
                                    onDone = { isSending = false; onSend() }
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape  = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A))
                    ) {
                        Text(
                            text       = if (isEditMode) "Salvar alterações" else "Enviar",
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )
                    }
                }
            }
        }
    }
}
