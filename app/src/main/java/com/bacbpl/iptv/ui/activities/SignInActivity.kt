package com.bacbpl.iptv.ui.activities

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Regular Material 3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bacbpl.iptv.JetStreamActivity
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.MainActivity

@Composable
fun SignInActivity(
    onNavigateToOTP: (String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSkip: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current //  Get context here

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        AsyncImage(
            model = R.drawable.bg_movies,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Dark Overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(450.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp)
                ) {
                    // Title Row with Skip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sign In",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        TextButton(
                            onClick = {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                        ) {
                            Text(
                                text = "Skip >>",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // Phone Input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 10) {
                                phoneNumber = it
                                phoneError = null
                            }
                        },
                        label = { Text("Enter Phone number") },
                        placeholder = { Text("Enter Phone number") },
                        isError = phoneError != null,
                        supportingText = phoneError?.let {
                            { Text(text = it) }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign In Button
                    Button(
                        onClick = {
                            when {
                                phoneNumber.isEmpty() -> {
                                    phoneError = "Phone number cannot be empty"
                                }
                                phoneNumber.length != 10 -> {
                                    phoneError = "Enter valid 10-digit number"
                                }
                                else -> {
                                    onNavigateToOTP("+91$phoneNumber")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914)
                        )
                    ) {
                        Text(
                            text = "Sign In",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Remember Me and Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.White,
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(
                                text = "Remember me",
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        TextButton(
                            onClick = onNavigateToForgotPassword
                        ) {
                            Text(
                                text = "Forgot Password",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign Up
                    TextButton(
                        onClick = onNavigateToSignUp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "New here? Sign up now.",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}