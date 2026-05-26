package br.com.fiap.inovagab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun categoryColor(category: String): Color = when (category) {
    "Operações"   -> Color(0xFF4CAF50)
    "RH"          -> Color(0xFF9C27B0)
    "Manutenção"  -> Color(0xFF2196F3)
    "Atendimento" -> Color(0xFFFF9800)
    "Comercial"   -> Color(0xFFF44336)
    else          -> Color(0xFF9E9E9E)
}

fun statusColor(status: String): Color = when (status) {
    "Aprovado"   -> Color(0xFF4CAF50)
    "Em análise" -> Color(0xFFFF9800)
    "Prioritária"-> Color(0xFFF44336)
    else         -> Color(0xFF9E9E9E)
}

@Composable
fun CategoryBadge(category: String) {
    Text(
        text = category,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier
            .background(color = categoryColor(category), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

@Composable
fun StatusBadge(status: String) {
    Text(
        text = status,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier
            .background(color = statusColor(status), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}
