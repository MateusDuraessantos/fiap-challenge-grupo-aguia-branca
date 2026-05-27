package br.com.fiap.inovagab.ui.features.lider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun LiderProjetos(
    liderViewModel: LiderViewModel,
    loggedUser: User?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(
                name = loggedUser?.nome ?: "Usuário",
                role = loggedUser?.perfil?.label ?: "Líder"
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
            Text(
                text     = "${liderViewModel.projetosAtivos} ativos",
                fontSize = 13.sp,
                color    = Color(0xFF1B4F7A),
                fontWeight = FontWeight.SemiBold
            )
        }

        if (liderViewModel.projetos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum projeto cadastrado", fontSize = 14.sp, color = Color(0xFF888888))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(liderViewModel.projetos) { projeto ->
                    LiderProjetoCard(projeto = projeto)
                }
            }
        }
    }
}

@Composable
private fun LiderProjetoCard(projeto: Projeto) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
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
                    text       = projeto.titulo,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF1A1A1A),
                    modifier   = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                StatusBadge(status = projeto.status)
            }

            Spacer(Modifier.height(6.dp))

            CategoryBadge(category = projeto.categoria)

            Spacer(Modifier.height(10.dp))

            Text(
                text       = projeto.descricao,
                fontSize   = 13.sp,
                color      = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines   = 2
            )

            Spacer(Modifier.height(10.dp))

            Text("Progresso: ${projeto.progresso}%", fontSize = 12.sp, color = Color(0xFF666666))
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress   = { projeto.progresso / 100f },
                modifier   = Modifier.fillMaxWidth().height(6.dp),
                color      = Color(0xFF1B4F7A),
                trackColor = Color(0xFFE0E0E0)
            )

            if (projeto.resultados.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text       = "Resultados: ${projeto.resultados}",
                    fontSize   = 12.sp,
                    color      = Color(0xFF555555),
                    lineHeight = 16.sp,
                    maxLines   = 2
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Investimento: R$ %.0f".format(projeto.investimento),
                    fontSize = 11.sp,
                    color    = Color(0xFF888888)
                )
                Text(
                    "Retorno: R$ %.0f".format(projeto.retornoFinanceiro),
                    fontSize   = 11.sp,
                    color      = Color(0xFF4CAF50),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(
                "Gestor: ${projeto.gestorNome}",
                fontSize = 11.sp,
                color    = Color(0xFF999999)
            )
        }
    }
}
