package br.com.fiap.inovagab

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.inovagab.ui.navigation.AppNavigation
import br.com.fiap.inovagab.ui.theme.InovagabTheme

private val PrimaryDark = Color(0xFF1B3A52)
private val ButtonPrimary = Color(0xFF1B4F7A)
private val TextDark = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF444444)
private val HintGray = Color(0xFFBDBDBD)
private val InputBorderColor = Color(0xFFE0E0E0)
private val SelectedBorderColor = Color(0xFF1B3A52)
private val UnselectedBorderColor = Color(0xFFCCCCCC)
private val ErrorColor = Color(0xFFE53935)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            InovagabTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (profile: String) -> Unit = {}
) {

    val context = LocalContext.current

    var selectedProfile by remember { mutableStateOf("Operador") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
            .padding(top = 56.dp, bottom = 40.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO
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

        // PERFIL
        Text(
            text = "Selecione um perfil",
            fontSize = 15.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf("Operador", "Gestor", "Líder").forEach { profile ->

                val isSelected = selectedProfile == profile

                Box(
                    contentAlignment = Alignment.Center,

                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected)
                                SelectedBorderColor
                            else
                                UnselectedBorderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedProfile = profile
                        }
                        .padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        )
                ) {

                    Text(
                        text = profile,
                        fontSize = 13.sp,
                        color = if (isSelected)
                            PrimaryDark
                        else
                            TextDark,

                        fontWeight = if (isSelected)
                            FontWeight.SemiBold
                        else
                            FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // EMAIL
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Email*",
                fontSize = 14.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = email,

                onValueChange = {
                    email = it
                    emailError = validateEmail(it)
                },

                placeholder = {
                    Text(
                        text = "Digite",
                        color = HintGray,
                        fontSize = 14.sp
                    )
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),

                isError = emailError != null,

                shape = RoundedCornerShape(8.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonPrimary,
                    unfocusedBorderColor = InputBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = ButtonPrimary
                ),

                modifier = Modifier.fillMaxWidth()
            )

            emailError?.let {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = it,
                    color = ErrorColor,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SENHA
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Senha*",
                fontSize = 14.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = password,

                onValueChange = {
                    password = it
                    passwordError = validatePassword(it)
                },

                placeholder = {
                    Text(
                        text = "••••••••",
                        color = HintGray,
                        fontSize = 14.sp
                    )
                },

                singleLine = true,

                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {

                        Icon(
                            painter = painterResource(
                                id =
                                    if (passwordVisible)
                                        R.drawable.ic_eye_off
                                    else
                                        R.drawable.ic_eye
                            ),

                            contentDescription = "Mostrar/ocultar senha",

                            tint = HintGray,

                            modifier = Modifier.size(22.dp)
                        )
                    }
                },

                isError = passwordError != null,

                shape = RoundedCornerShape(8.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonPrimary,
                    unfocusedBorderColor = InputBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = ButtonPrimary
                ),

                modifier = Modifier.fillMaxWidth()
            )

            passwordError?.let {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = it,
                    color = ErrorColor,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // BOTÃO ENTRAR
        Button(

            onClick = {

                emailError = validateEmail(email)
                passwordError = validatePassword(password)

                when {

                    emailError != null || passwordError != null -> {

                        Toast.makeText(
                            context,
                            "Corrija os campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {

                        Toast.makeText(
                            context,
                            "Entrando como $selectedProfile",
                            Toast.LENGTH_SHORT
                        ).show()

                        onLoginSuccess(selectedProfile)
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonPrimary
            )
        ) {

            Text(
                text = "Entrar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ESQUECI SENHA
        Text(
            text = "Esqueci a senha",

            fontSize = 14.sp,

            color = TextDark,

            textDecoration = TextDecoration.Underline,

            modifier = Modifier
                .align(Alignment.Start)
                .clickable {

                    Toast.makeText(
                        context,
                        "Recuperação de senha em breve",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .padding(vertical = 4.dp)
        )
    }
}

fun validateEmail(email: String): String? {

    return when {

        email.isBlank() ->
            "Informe seu e-mail"

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            "E-mail inválido"

        else -> null
    }
}

fun validatePassword(password: String): String? {

    return when {

        password.isBlank() ->
            "Informe sua senha"

        password.length < 6 ->
            "A senha deve ter pelo menos 6 caracteres"

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