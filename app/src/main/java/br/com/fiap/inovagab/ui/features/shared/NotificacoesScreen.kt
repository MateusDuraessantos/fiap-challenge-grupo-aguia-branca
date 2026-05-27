package br.com.fiap.inovagab.ui.features.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.model.Estrategia
import br.com.fiap.inovagab.data.model.Ideia
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.data.model.User
import br.com.fiap.inovagab.ui.components.ProfileHeader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private sealed class NotifItem(val timestamp: Long) {
    class IdeiaAprovada(val ideia: Ideia)        : NotifItem(ideia.criadaEm)
    class NovaEstrategia(val estrategia: Estrategia) : NotifItem(estrategia.criadaEm)
}

@Composable
fun NotificacoesScreen(
    perfil: Perfil,
    loggedUser: User?,
    ideias: List<Ideia>,
    estrategias: List<Estrategia>,
    onLoad: (() -> Unit)? = null
) {
    LaunchedEffect(Unit) { onLoad?.invoke() }

    val ideiasAprovadas = ideias.filter {
        it.status == "Aprovada" || it.status == "Virou projeto"
    }

    val notifItems: List<NotifItem> = buildList {
        ideiasAprovadas.forEach { add(NotifItem.IdeiaAprovada(it)) }
        estrategias.forEach    { add(NotifItem.NovaEstrategia(it)) }
    }.sortedByDescending { it.timestamp }

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

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text       = "Central de Avisos",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp,
                        color      = Color(0xFF1A1A1A)
                    )
                    Text(
                        text     = "Ideias aprovadas e novas orientações estratégicas",
                        fontSize = 13.sp,
                        color    = Color(0xFF888888),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            if (notifItems.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sem avisos no momento", fontSize = 14.sp, color = Color(0xFF888888))
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text     = "Novas notificações aparecerão aqui",
                                fontSize = 12.sp,
                                color    = Color(0xFFAAAAAA)
                            )
                        }
                    }
                }
            } else {
                items(notifItems) { item ->
                    when (item) {
                        is NotifItem.IdeiaAprovada   -> IdeiaAprovadaCard(item.ideia)
                        is NotifItem.NovaEstrategia  -> EstrategiaNotifCard(item.estrategia)
                    }
                }
            }
        }
    }
}

@Composable
private fun IdeiaAprovadaCard(ideia: Ideia) {
    val isVirou      = ideia.status == "Virou projeto"
    val iconColor    = if (isVirou) Color(0xFF9C27B0) else Color(0xFF4CAF50)
    val labelColor   = iconColor
    val typeLabel    = if (isVirou) "Virou projeto" else "Ideia aprovada"

    NotifCard(
        icon           = Icons.Default.CheckCircle,
        iconColor      = iconColor,
        typeLabel      = typeLabel,
        typeLabelColor = labelColor,
        title          = ideia.titulo,
        timestamp      = ideia.criadaEm
    ) {
        Text(
            text     = "Enviada por: ${ideia.operadorNome.ifBlank { "—" }}",
            fontSize = 12.sp,
            color    = Color(0xFF555555)
        )
        if (ideia.aprovadoPorNome.isNotBlank()) {
            Text(
                text     = "Aprovada por: ${ideia.aprovadoPorNome}",
                fontSize = 12.sp,
                color    = Color(0xFF555555),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun EstrategiaNotifCard(estrategia: Estrategia) {
    NotifCard(
        icon           = Icons.Default.Lightbulb,
        iconColor      = Color(0xFF1B4F7A),
        typeLabel      = "Nova orientação estratégica",
        typeLabelColor = Color(0xFF1B4F7A),
        title          = estrategia.titulo,
        timestamp      = estrategia.criadaEm
    ) {
        if (estrategia.descricao.isNotBlank()) {
            Text(
                text       = estrategia.descricao,
                fontSize   = 12.sp,
                color      = Color(0xFF777777),
                maxLines   = 2,
                lineHeight = 16.sp
            )
        }
        if (estrategia.criadaPorNome.isNotBlank()) {
            Text(
                text     = "Por: ${estrategia.criadaPorNome}",
                fontSize = 11.sp,
                color    = Color(0xFF999999),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun NotifCard(
    icon: ImageVector,
    iconColor: Color,
    typeLabel: String,
    typeLabelColor: Color,
    title: String,
    timestamp: Long,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 5.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier          = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier         = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = iconColor,
                    modifier           = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = typeLabel,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = typeLabelColor
                    )
                    Text(
                        text     = timestamp.toDateString(),
                        fontSize = 10.sp,
                        color    = Color(0xFFAAAAAA)
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text       = title,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Color(0xFF1A1A1A),
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(6.dp))

                content()
            }
        }
    }
}

private fun Long.toDateString(): String =
    if (this == 0L) "—"
    else SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(this))
