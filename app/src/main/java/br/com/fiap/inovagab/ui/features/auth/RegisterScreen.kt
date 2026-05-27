package br.com.fiap.inovagab.ui.features.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.ui.components.InovagabButton
import br.com.fiap.inovagab.ui.components.InovagabTextField

private val RegPrimaryDark        = Color(0xFF1B3A52)
private val RegTextDark           = Color(0xFF1A1A1A)
private val RegTextSecondary      = Color(0xFF444444)
private val RegHintGray           = Color(0xFFBDBDBD)
private val RegLinkColor          = Color(0xFF1B4F7A)
private val RegErrorRed           = Color(0xFFE53935)
private val RegSelectedBorder     = Color(0xFF1B3A52)
private val RegUnselectedBorder   = Color(0xFFCCCCCC)

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current

    var nome          by remember { mutableStateOf("") }
    var email         by remember { mutableStateOf("") }
    var senha         by remember { mutableStateOf("") }
    var senhaVisible  by remember { mutableStateOf(false) }
    var selectedPerfil by remember { mutableStateOf(Perfil.OPERADOR) }

    var nomeError     by remember { mutableStateOf<String?>(null) }
    var emailError    by remember { mutableStateOf<String?>(null) }
    var senhaError    by remember { mutableStateOf<String?>(null) }
    var registerError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nomeError = when {
            nome.isBlank()         -> "Informe seu nome completo"
            nome.trim().length < 3 -> "Nome muito curto"
            else                   -> null
        }
        emailError = when {
            email.isBlank()                                  -> "Informe seu e-mail"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
            else                                             -> null
        }
        senhaError = when {
            senha.isBlank()  -> "Informe uma senha"
            senha.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
            else             -> null
        }
        return nomeError == null && emailError == null && senhaError == null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
            .padding(top = 56.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aguia),
            contentDescription = "Logo Águia Branca",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "InovaGAB",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = RegPrimaryDark,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Crie sua conta",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = RegTextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Preencha os dados abaixo para começar",
            fontSize = 14.sp,
            color = Color(0xFF888888)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Seletor de tipo de conta ─────────────────────────────────────
        Text(
            text = "Tipo de conta",
            fontSize = 15.sp,
            color = RegTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Perfil.entries.forEach { perfil ->
                val isSelected = selectedPerfil == perfil
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) RegSelectedBorder else RegUnselectedBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedPerfil = perfil }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = perfil.label,
                        fontSize = 13.sp,
                        color = if (isSelected) RegPrimaryDark else RegTextDark,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Campos do formulário ─────────────────────────────────────────
        InovagabTextField(
            label = "Nome completo*",
            value = nome,
            onValueChange = {
                nome = it
                if (nomeError != null) nomeError = null
            },
            placeholder = "João da Silva",
            error = nomeError
        )

        Spacer(modifier = Modifier.height(20.dp))

        InovagabTextField(
            label = "Email*",
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
                if (registerError != null) registerError = null
            },
            placeholder = "joao@exemplo.com",
            error = emailError,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        InovagabTextField(
            label = "Senha*",
            value = senha,
            onValueChange = {
                senha = it
                if (senhaError != null) senhaError = null
            },
            placeholder = "••••••••",
            error = senhaError,
            visualTransformation = if (senhaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { senhaVisible = !senhaVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (senhaVisible) R.drawable.ic_eye_off else R.drawable.ic_eye
                        ),
                        contentDescription = "Mostrar/ocultar senha",
                        tint = RegHintGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(36.dp))

        // ── Botão / loading ──────────────────────────────────────────────
        if (authViewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF1B4F7A),
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.5.dp
                )
            }
        } else {
            InovagabButton(
                text = "Criar conta",
                onClick = {
                    if (validate()) {
                        registerError = null
                        authViewModel.register(
                            nome     = nome.trim(),
                            email    = email,
                            password = senha,
                            perfil   = selectedPerfil,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Conta criada! Faça o login para continuar.",
                                    Toast.LENGTH_LONG
                                ).show()
                                onNavigateToLogin()
                            },
                            onError = { msg -> registerError = msg }
                        )
                    }
                }
            )
        }

        // ── Erro do Firebase ─────────────────────────────────────────────
        registerError?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = RegErrorRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = error,
                    fontSize = 13.sp,
                    color = RegErrorRed
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = buildAnnotatedString {
                append("Já possui uma conta? ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = RegLinkColor)) {
                    append("Entrar")
                }
            },
            fontSize = 14.sp,
            color = RegTextDark,
            modifier = Modifier
                .clickable { onNavigateToLogin() }
                .padding(vertical = 4.dp)
        )
    }
}
