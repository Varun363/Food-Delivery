package Zomato.Copy

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val greenPrimary = Color(0xFF2E7D32)
    val greenLight = Color(0xFFE8F5E9)
    val textGray = Color(0xFF6E6E6E)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            // App Illustration (placeholder)
            Icon(
                imageVector = Icons.Filled.Fastfood,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome Back",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Login to your account",
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Card for inputs
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                db.collection("users")
                                    .whereEqualTo("email", email)
                                    .whereEqualTo("password", password.toIntOrNull() ?: 0)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        isLoading = false
                                        if (!documents.isEmpty) {
                                            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                            // Updated navigation to MainScreen
                                            navController.navigate(NavigationProperties.MainScreen.route) {
                                                // Clear backstack so user can't go back to login screen
                                                popUpTo(NavigationProperties.LoginScreen.route) { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        ),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Login", fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Don’t have an account? ", color = textGray)
                Text(
                    text = "Sign up",
                    color = greenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(NavigationProperties.RegisterScreen.route)
                    }
                )
            }
        }
    }
}
