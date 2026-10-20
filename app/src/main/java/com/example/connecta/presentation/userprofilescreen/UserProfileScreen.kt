package com.example.connecta.presentation.userprofilescreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.connecta.R
import com.example.connecta.presentation.navigation.Routes
import com.example.connecta.presentation.viewmodel.PhoneAuthViewModel

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun UserProfileScreen(
    navController: NavHostController,
    viewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Hey there! I am using Connecta.") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Profile Info",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ConnectaTeal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please provide your name and an optional profile photo",
            fontSize = 14.sp,
            color = ConnectaSecondaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Profile Photo with Camera Badge
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.nodp),
                    contentDescription = "Default Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ConnectaTeal)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Add Photo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Name input
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Type your name here", color = ConnectaSecondaryText) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = ConnectaDarkText,
                unfocusedTextColor = ConnectaDarkText,
                focusedIndicatorColor = ConnectaTeal,
                unfocusedIndicatorColor = ConnectaSecondaryText,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Status input
        TextField(
            value = status,
            onValueChange = { status = it },
            placeholder = { Text(text = "Status / About", color = ConnectaSecondaryText) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = ConnectaDarkText,
                unfocusedTextColor = ConnectaDarkText,
                focusedIndicatorColor = ConnectaTeal,
                unfocusedIndicatorColor = ConnectaSecondaryText,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = {
                if (name.trim().isBlank()) {
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isSaving = true
                viewModel.saveProfile(
                    name = name.trim(),
                    status = status.trim(),
                    imageUri = selectedImageUri,
                    onSuccess = {
                        isSaving = false
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo<Routes.UserProfileScreen> { inclusive = true }
                        }
                    },
                    onError = { error ->
                        isSaving = false
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            },
            enabled = !isSaving && name.isNotBlank(),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ConnectaTeal,
                disabledContainerColor = ConnectaTeal.copy(alpha = 0.5f)
            )
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Next",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
