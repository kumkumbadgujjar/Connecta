package com.example.connecta.presentation.userregistrationscreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.connecta.presentation.navigation.Routes
import com.example.connecta.presentation.viewmodel.AuthState
import com.example.connecta.presentation.viewmodel.PhoneAuthViewModel

private val ConnectaTeal = Color(0xFF06B6D4)

@Composable
fun UserRegistrationScreen(
    navController: NavHostController,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val authState by phoneAuthViewModel.authState.collectAsState()
    val context = LocalContext.current
    val activity = LocalActivity.current as? Activity

    var otp by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("India") }
    var countryCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }

    val countryCodes = mapOf(
        "India" to "+91",
        "USA" to "+1",
        "China" to "+86",
        "Pakistan" to "+92",
        "Canada" to "+1"
    )

    // Derive verification ID directly from AuthState if present
    val verificationId = (authState as? AuthState.CodeSent)?.verificationId

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter your phone number",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ConnectaTeal
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Connecta will need to verify your phone number")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "What's", color = ConnectaTeal)
        }

        Text(text = "my number?", color = ConnectaTeal)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.width(230.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = selectedCountry,
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select country",
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.width(230.dp),
                    thickness = 2.dp,
                    color = ConnectaTeal
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countryCodes.keys.forEach { countryName ->
                        DropdownMenuItem(
                            text = { Text(text = countryName) },
                            onClick = {
                                selectedCountry = countryName
                                countryCode = countryCodes[countryName] ?: ""
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        when (authState) {
            is AuthState.Ideal, is AuthState.Loading, is AuthState.CodeSent -> {
                if (verificationId == null) {
                    // --- Phone Input Section ---
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = countryCode,
                            onValueChange = { countryCode = it },
                            modifier = Modifier.width(70.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedIndicatorColor = ConnectaTeal
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholder = { Text(text = "Phone Number") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedIndicatorColor = ConnectaTeal
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.isNotEmpty() && activity != null) {
                                val fullPhoneNumber = "$countryCode$phoneNumber"
                                phoneAuthViewModel.sendVerificationCode(fullPhoneNumber, activity)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter your valid Phone Number",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ConnectaTeal)
                    ) {
                        Text(text = "Send OTP")
                    }

                    if (authState is AuthState.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = ConnectaTeal)
                    }
                } else {
                    // --- OTP Input Section ---
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Enter the OTP sent to your phone number",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ConnectaTeal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedIndicatorColor = ConnectaTeal
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (otp.isNotEmpty()) {
                                phoneAuthViewModel.verifyCode(otp, context)
                            } else {
                                Toast.makeText(context, "Please enter the OTP", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ConnectaTeal)
                    ) {
                        Text(text = "Verify OTP")
                    }

                    if (authState is AuthState.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = ConnectaTeal)
                    }
                }
            }

            is AuthState.Success -> {
                Log.d("PhoneAuth", "Login Successful")
                phoneAuthViewModel.resetAuthState()
                navController.navigate(Routes.UserProfileScreen) {
                    popUpTo<Routes.UserRegistrationScreen> {
                        inclusive = true
                    }
                }
            }

            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                phoneAuthViewModel.resetAuthState()
            }
        }
    }
}