package br.com.fiap.inovagab.ui.screens.operador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.data.sampleStrategies
import br.com.fiap.inovagab.ui.components.ProfileHeader
import br.com.fiap.inovagab.ui.screens.StrategyCard

@Composable
fun OperadorStrategiesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            ProfileHeader(name = "João", role = "Operador")
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "Estratégias atuais da empresa",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = "*Consultar orientações sobre as estratégias da empresa",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF888888),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleStrategies) { strategy ->
                StrategyCard(strategy = strategy)
            }
        }
    }
}
