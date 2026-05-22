package br.com.fiap.inovagab

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.ui.theme.InovagabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InovagabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var selectedProfile by remember { mutableStateOf("Operador") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aguia),
            contentDescription = "Logo Águia Branca",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "GAB INSIGHTS",
            fontSize = 32.sp
        )

        Text(text = "Selecione um perfil")

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ProfileButton(
                text = "Operador",
                selectedProfile = selectedProfile,
                onClick = { selectedProfile = "Operador" }
            )

            ProfileButton(
                text = "Gestor",
                selectedProfile = selectedProfile,
                onClick = { selectedProfile = "Gestor" }
            )

            ProfileButton(
                text = "Líder",
                selectedProfile = selectedProfile,
                onClick = { selectedProfile = "Líder" }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = validateEmail(it)
            },
            label = { Text("E-mail") },
            isError = emailError != null,
            supportingText = {
                if (emailError != null) Text(emailError!!)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = validatePassword(it)
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null) Text(passwordError!!)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = validateEmail(email)
                passwordError = validatePassword(password)

                if (emailError == null && passwordError == null) {
                    Toast.makeText(
                        context,
                        "Entrando como $selectedProfile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text("Entrar")
        }

        TextButton(
            onClick = {
                Toast.makeText(
                    context,
                    "Recuperação de senha em breve",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            Text("Esqueci minha senha")
        }
    }
}

@Composable
fun ProfileButton(
    text: String,
    selectedProfile: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(
            text = if (selectedProfile == text) "✓ $text" else text
        )
    }
}

fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Informe seu e-mail"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Informe sua senha"
        password.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InovagabTheme {
        LoginScreen()
    }
}