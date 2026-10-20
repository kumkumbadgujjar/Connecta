package com.example.connecta.presentation.userregistrationscreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.connecta.presentation.navigation.Routes
import com.example.connecta.presentation.viewmodel.AuthState
import com.example.connecta.presentation.viewmodel.PhoneAuthViewModel

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun UserRegistrationScreen(
    navController: NavHostController,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val authState by phoneAuthViewModel.authState.collectAsState()
    val resendCountdown by phoneAuthViewModel.resendCountdown.collectAsState()
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
        "Canada" to "+1",
        "United Kingdom" to "+44",
        "Australia" to "+61",
        "UAE" to "+971"
    )

    // Check if code was sent
    val isCodeSent = authState is AuthState.CodeSent

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            val user = (authState as AuthState.Success).user
            // If user has already configured their profile, go to Home, else go to UserProfileScreen
            if (user.name.isNotBlank()) {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo<Routes.UserRegistrationScreen> { inclusive = true }
                }
            } else {
                navController.navigate(Routes.UserProfileScreen) {
                    popUpTo<Routes.UserRegistrationScreen> { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Back Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCodeSent) {
                IconButton(onClick = { phoneAuthViewModel.resetAuthState() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = ConnectaDarkText
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                text = if (isCodeSent) "Verify Phone" else "Register Phone",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ConnectaDarkText,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isCodeSent) {
            // ============================================
            // PHONE NUMBER ENTRY SECTION
            // ============================================
            Text(
                text = "Enter your phone number",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ConnectaTeal
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Connecta will send an SMS message to verify your phone number. Carrier SMS rates may apply.",
                fontSize = 14.sp,
                color = ConnectaSecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Country Selector Dropdown
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.width(260.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = selectedCountry,
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 16.sp,
                                color = ConnectaDarkText,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select country",
                                modifier = Modifier.align(Alignment.CenterEnd),
                                tint = ConnectaTeal
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.width(260.dp),
                        thickness = 2.dp,
                        color = ConnectaTeal
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        countryCodes.keys.forEach { countryName ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = countryName, color = ConnectaDarkText)
                                        Text(
                                            text = countryCodes[countryName] ?: "",
                                            color = ConnectaSecondaryText,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                onClick = {
                                    selectedCountry = countryName
                                    countryCode = countryCodes[countryName] ?: "+91"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Phone Input Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = countryCode,
                    onValueChange = { countryCode = it },
                    modifier = Modifier.width(80.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = ConnectaDarkText,
                        unfocusedTextColor = ConnectaDarkText,
                        focusedIndicatorColor = ConnectaTeal,
                        unfocusedIndicatorColor = ConnectaSecondaryText,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it.filter { ch -> ch.isDigit() } },
                    placeholder = { Text(text = "Phone Number", color = ConnectaSecondaryText) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = ConnectaDarkText,
                        unfocusedTextColor = ConnectaDarkText,
                        focusedIndicatorColor = ConnectaTeal,
                        unfocusedIndicatorColor = ConnectaSecondaryText,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val isLoading = authState is AuthState.Loading

            Button(
                onClick = {
                    if (phoneNumber.trim().length >= 6 && activity != null) {
                        val fullPhoneNumber = "${countryCode.trim()}${phoneNumber.trim()}"
                        phoneAuthViewModel.sendVerificationCode(fullPhoneNumber, activity)
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter a valid phone number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = !isLoading && phoneNumber.isNotBlank(),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ConnectaTeal,
                    disabledContainerColor = ConnectaTeal.copy(alpha = 0.5f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Send Verification Code",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

        } else {
            // ============================================
            // REAL OTP VERIFICATION SECTION
            // ============================================
            Text(
                text = "Enter Verification Code",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ConnectaTeal
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "We sent an SMS with a 6-digit verification code to $countryCode $phoneNumber",
                fontSize = 14.sp,
                color = ConnectaSecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Clean 6-digit OTP Input Field
            TextField(
                value = otp,
                onValueChange = {
                    if (it.length <= 6) {
                        otp = it.filter { ch -> ch.isDigit() }
                    }
                },
                placeholder = {
                    Text(
                        text = "• • • • • •",
                        color = ConnectaSecondaryText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .width(260.dp)
                    .height(64.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = ConnectaDarkText,
                    letterSpacing = 8.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = ConnectaDarkText,
                    unfocusedTextColor = ConnectaDarkText,
                    focusedIndicatorColor = ConnectaTeal,
                    unfocusedIndicatorColor = ConnectaTeal.copy(alpha = 0.6f),
                    focusedContainerColor = Color(0xFFF1F5F6),
                    unfocusedContainerColor = Color(0xFFF1F5F6)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            val isLoading = authState is AuthState.Loading

            Button(
                onClick = {
                    if (otp.length == 6) {
                        phoneAuthViewModel.verifyCode(otp.trim())
                    } else {
                        Toast.makeText(context, "Please enter the complete 6-digit code", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading && otp.length == 6,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ConnectaTeal,
                    disabledContainerColor = ConnectaTeal.copy(alpha = 0.5f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Verify & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Resend Code with Countdown Cooldown
            if (resendCountdown > 0) {
                Text(
                    text = "Resend SMS in $resendCountdown seconds",
                    fontSize = 14.sp,
                    color = ConnectaSecondaryText,
                    fontWeight = FontWeight.Medium
                )
            } else {
                TextButton(
                    onClick = {
                        if (activity != null) {
                            phoneAuthViewModel.resendVerificationCode(activity)
                        }
                    }
                ) {
                    Text(
                        text = "Didn't receive code? Resend SMS",
                        color = ConnectaTeal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    phoneAuthViewModel.resetAuthState()
                    otp = ""
                }
            ) {
                Text(
                    text = "Wrong number? Change phone number",
                    color = ConnectaSecondaryText,
                    fontSize = 13.sp
                )
            }
        }

        // Error message display
        if (authState is AuthState.Error) {
            val errorMessage = (authState as AuthState.Error).message
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFC62828),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}