package com.example.connecta.presentation.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.connecta.R
import com.example.connecta.presentation.navigation.Routes

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkTeal = Color(0xFF0891B2)
private val ConnectaText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)
private val ConnectaLightTeal = Color(0xFFE8FAFC)

@Composable
fun WelcomeScreen(
    navHostController: NavHostController,
    onPhoneNumberClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(45.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Connecta Logo",
            modifier = Modifier.size(75.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Connecta",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = ConnectaText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome to Connecta",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = ConnectaTeal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Simple. Secure. Connected.",
            fontSize = 17.sp,
            color = ConnectaSecondaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(35.dp))

        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(ConnectaLightTeal),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(225.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.qr),
                    contentDescription = "QR Scanner",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Read our Privacy Policy. Tap 'Link with phone number' to accept the Terms of Service.",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = ConnectaSecondaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = {
                navHostController.navigate(Routes.UserRegistrationScreen)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = ConnectaTeal
            )
        ) {

            Icon(
                imageVector = Icons.Outlined.Link,
                contentDescription = null,
                modifier = Modifier.size(23.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Link with phone number",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = ConnectaSecondaryText
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = "End-to-end encrypted",
                fontSize = 14.sp,
                color = ConnectaSecondaryText
            )
        }
    }
}




