package br.com.fiap.inovagab.ui.features.auth

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.data.model.Perfil
import br.com.fiap.inovagab.ui.components.InovagabButton
import br.com.fiap.inovagab.ui.components.InovagabTextField
import br.com.fiap.inovagab.ui.theme.InovagabTheme

private val PrimaryDark           = Color(0xFF1B3A52)
private val TextDark              = Color(0xFF1A1A1A)
private val TextSecondary         = Color(0xFF444444)
private val HintGray              = Color(0xFFBDBDBD)
private val ErrorRed              = Color(0xFFE53935)
private val SelectedBorderColor   = Color(0xFF1B3A52)
private val UnselectedBorderColor = Color(0xFFCCCCCC)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (Perfil) -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
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
            text = "GAB INSIGHTS",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryDark,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(52.dp))

        Text("Selecione um perfil", fontSize = 15.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Perfil.entries.forEach { perfil ->
                val isSelected = authViewModel.selectedPerfil == perfil
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) SelectedBorderColor else UnselectedBorderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { authViewModel.onPerfilChange(perfil) }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = perfil.label,
                        fontSize = 13.sp,
                        color = if (isSelected) PrimaryDark else TextDark,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        InovagabTextField(
            label = "Email*",
            value = authViewModel.email,
            onValueChange = authViewModel::onEmailChange,
            error = authViewModel.emailError,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        InovagabTextField(
            label = "Senha*",
            value = authViewModel.password,
            onValueChange = authViewModel::onPasswordChange,
            placeholder = "••••••••",
            error = authViewModel.passwordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye
                        ),
                        contentDescription = "Mostrar/ocultar senha",
                        tint = HintGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(36.dp))

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
                text = "Entrar",
                onClick = {
                    authViewModel.login(
                        onSuccess = { perfil ->
                            Toast.makeText(context, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(perfil)
                        },
                        onError = {}
                    )
                }
            )
        }

        authViewModel.loginError?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = error,
                    fontSize = 13.sp,
                    color = ErrorRed
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Esqueci a senha",
            fontSize = 14.sp,
            color = TextDark,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable {
                    Toast.makeText(context, "Recuperação de senha em breve", Toast.LENGTH_SHORT).show()
                }
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                append("Não possui uma conta? ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1B4F7A))) {
                    append("Criar conta")
                }
            },
            fontSize = 14.sp,
            color = TextDark,
            modifier = Modifier
                .clickable { onNavigateToRegister() }
                .padding(vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    InovagabTheme { LoginScreen() }
}
