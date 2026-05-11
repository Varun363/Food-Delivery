package Zomato.Copy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController : NavController) {
    val greenPrimary = Color(0xFF2E7D32)
    val greenLight = Color(0xFFE8F5E9)
    val textGray = Color(0xFF6E6E6E)

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Registration Successful") },
            text = { Text("Welcome $fullName! Your account has been created successfully.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate(NavigationProperties.LoginScreen.route)
                    }
                ) {
                    Text("Go to Login", color = greenPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close", color = textGray)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(greenLight)
    ) {

        // Top curved background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    color = greenPrimary,
                    shape = RoundedCornerShape(
                        bottomStart = 40.dp,
                        bottomEnd = 40.dp
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            Icon(
                imageVector = Icons.Filled.Fastfood,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Sign up to get started",
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Input Card
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val user = User(fullName, email, password.toIntOrNull() ?: 0, confirmPassword.toIntOrNull() ?: 0)
                            saveUserToFirestore(user)
                            showDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        )
                    ) {
                        Text("Register", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Already have an account? ", color = textGray)
                Text(
                    text = "Login",
                    color = greenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(NavigationProperties.LoginScreen.route)
                    }
                )
            }
        }
    }
}
