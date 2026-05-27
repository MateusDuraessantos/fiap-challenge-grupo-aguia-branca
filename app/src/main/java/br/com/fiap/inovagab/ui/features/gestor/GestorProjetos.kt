package br.com.fiap.inovagab.ui.features.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Projeto
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.CategoryBadge
import br.com.fiap.inovagab.ui.components.ProfileHeader
import br.com.fiap.inovagab.ui.components.StatusBadge

@Composable
fun GestorProjetos(
    gestorViewModel: GestorViewModel,
    loggedUser: User?,
    onCreateClick: () -> Unit = {},
    onEditClick: (Projeto) -> Unit = {}
) {
    var confirmDelete by remember { mutableStateOf<Projeto?>(null) }

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
                text       = "Projetos",
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF1A1A1A)
            )
            Button(
                onClick = onCreateClick,
                shape  = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F7A)),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Novo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        if (gestorViewModel.projetos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum projeto cadastrado", fontSize = 14.sp, color = Color(0xFF888888))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(gestorViewModel.projetos) { projeto ->
                    ProjetoCard(
                        projeto    = projeto,
                        onEdit     = { onEditClick(projeto) },
                        onDelete   = { confirmDelete = projeto }
                    )
                }
            }
        }
    }

    confirmDelete?.let { projeto ->
        AlertDialog(
            onDismissRequest = { confirmDelete = null },
            title   = { Text("Excluir projeto") },
            text    = { Text("Deseja excluir \"${projeto.titulo}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    gestorViewModel.deletarProjeto(projeto.id)
                    confirmDelete = null
                }) {
                    Text("Excluir", color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProjetoCard(
    projeto: Projeto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onEdit() },
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
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text       = projeto.titulo,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        CategoryBadge(category = projeto.categoria)
                        StatusBadge(status = projeto.status)
                    }
                }
                IconButton(
                    onClick  = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint               = Color(0xFFE53935),
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text       = projeto.descricao,
                fontSize   = 13.sp,
                color      = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines   = 2
            )

            Spacer(Modifier.height(10.dp))

            // Barra de progresso
            Text("Progresso: ${projeto.progresso}%", fontSize = 12.sp, color = Color(0xFF666666))
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress    = { projeto.progresso / 100f },
                modifier    = Modifier.fillMaxWidth().height(6.dp),
                color       = Color(0xFF1B4F7A),
                trackColor  = Color(0xFFE0E0E0)
            )

            if (projeto.investimento > 0 || projeto.retornoFinanceiro > 0) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text     = "Investimento: R$ %.0f".format(projeto.investimento),
                        fontSize = 11.sp,
                        color    = Color(0xFF888888)
                    )
                    Text(
                        text     = "Retorno: R$ %.0f".format(projeto.retornoFinanceiro),
                        fontSize = 11.sp,
                        color    = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}
