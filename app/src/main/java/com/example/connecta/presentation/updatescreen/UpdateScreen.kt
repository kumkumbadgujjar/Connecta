package com.example.connecta.presentation.updatescreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.connecta.R
import com.example.connecta.models.StatusUpdate
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import com.example.connecta.presentation.viewmodel.UpdateViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun UpdateScreen(
    onCameraClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onChatsClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    viewModel: UpdateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val updates by viewModel.updates.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val currentUserId = viewModel.currentUserId ?: ""

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedStatusForViewing by remember { mutableStateOf<StatusUpdate?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            showCreateDialog = true
        }
    }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(200)
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }

    val myUpdates = updates.filter { it.userId == currentUserId }
    val otherUpdates = updates.filter { it.userId != currentUserId }

    val recentUpdates = otherUpdates.filter { !it.viewerIds.contains(currentUserId) }
    val viewedUpdates = otherUpdates.filter { it.viewerIds.contains(currentUserId) }

    val filteredRecent = if (searchText.isBlank()) recentUpdates else recentUpdates.filter {
        it.userName.contains(searchText, ignoreCase = true)
    }

    val filteredViewed = if (searchText.isBlank()) viewedUpdates else viewedUpdates.filter {
        it.userName.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            if (isSearching) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        placeholder = { Text(text = "Search updates") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp),
                                tint = ConnectaTeal
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        searchText = ""
                                        isSearching = false
                                    },
                                tint = ConnectaSecondaryText
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF1F5F6),
                            unfocusedContainerColor = Color(0xFFF1F5F6),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            } else {
                UpdateTopBar(
                    onCameraClick = { imagePickerLauncher.launch("image/*") },
                    onSearchClick = { isSearching = true },
                    onMenuClick = onMenuClick
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                containerColor = ConnectaTeal,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Create update",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1,
                onChatsClick = onChatsClick,
                onUpdatesClick = {},
                onPostsClick = onPostClick,
                onCallsClick = onCallsClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp)
        ) {
            item {
                Text(
                    text = "Updates",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ConnectaDarkText
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ConnectaTeal
                )

                Spacer(modifier = Modifier.height(14.dp))

                // My Status
                if (searchText.isBlank()) {
                    val latestMyUpdate = myUpdates.firstOrNull()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (latestMyUpdate != null) {
                                    selectedStatusForViewing = latestMyUpdate
                                } else {
                                    imagePickerLauncher.launch("image/*")
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            if (latestMyUpdate?.mediaUrl?.isNotBlank() == true) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = latestMyUpdate.mediaUrl),
                                    contentDescription = "My status",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(2.5.dp, ConnectaTeal, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.nodp),
                                    contentDescription = "My status",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(ConnectaTeal)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Column {
                            Text(
                                text = "My status",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ConnectaDarkText
                            )
                            Spacer(modifier = Modifier.size(3.dp))
                            Text(
                                text = if (latestMyUpdate != null) formatTimestamp(latestMyUpdate.timestamp) else "Tap to add status update",
                                fontSize = 14.sp,
                                color = ConnectaSecondaryText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                }

                // Recent updates header
                if (filteredRecent.isNotEmpty()) {
                    Text(
                        text = "Recent updates",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ConnectaSecondaryText
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Recent Updates items
            items(filteredRecent) { update ->
                RealStatusItem(
                    name = update.userName,
                    time = formatTimestamp(update.timestamp),
                    imageUrl = update.mediaUrl,
                    viewed = false,
                    onClick = { selectedStatusForViewing = update }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Viewed updates header
            item {
                if (filteredViewed.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Viewed updates",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ConnectaSecondaryText
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Viewed Updates items
            items(filteredViewed) { update ->
                RealStatusItem(
                    name = update.userName,
                    time = formatTimestamp(update.timestamp),
                    imageUrl = update.mediaUrl,
                    viewed = true,
                    onClick = { selectedStatusForViewing = update }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Empty State
            item {
                if (updates.isEmpty() && searchText.isBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.update_icon),
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = ConnectaSecondaryText.copy(alpha = 0.4f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No recent updates from contacts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = ConnectaSecondaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Status updates disappear after 24 hours.",
                                fontSize = 13.sp,
                                color = ConnectaSecondaryText.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }

    // Status Viewer Dialog
    if (selectedStatusForViewing != null) {
        val st = selectedStatusForViewing!!
        AlertDialog(
            onDismissRequest = { selectedStatusForViewing = null },
            confirmButton = {
                TextButton(onClick = { selectedStatusForViewing = null }) {
                    Text("Close", color = ConnectaTeal, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = st.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ConnectaDarkText
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formatTimestamp(st.timestamp),
                        fontSize = 12.sp,
                        color = ConnectaSecondaryText
                    )
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (st.mediaUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = st.mediaUrl),
                            contentDescription = "Status Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (st.caption.isNotBlank()) {
                        Text(
                            text = st.caption,
                            fontSize = 15.sp,
                            color = ConnectaDarkText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }

    // Create Status Dialog
    if (showCreateDialog) {
        var captionText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                if (!isSubmitting) {
                    showCreateDialog = false
                    selectedImageUri = null
                }
            },
            title = { Text("Add Status Update", fontWeight = FontWeight.Bold, color = ConnectaDarkText) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = selectedImageUri),
                            contentDescription = "Selected Photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = captionText,
                        onValueChange = { captionText = it },
                        label = { Text("Add a caption...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createStatus(
                            caption = captionText.trim(),
                            mediaUri = selectedImageUri,
                            onSuccess = {
                                showCreateDialog = false
                                selectedImageUri = null
                                Toast.makeText(context, "Status updated!", Toast.LENGTH_SHORT).show()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = ConnectaTeal)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Post")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        selectedImageUri = null
                    },
                    enabled = !isSubmitting
                ) {
                    Text("Cancel", color = ConnectaSecondaryText)
                }
            }
        )
    }
}

@Composable
fun RealStatusItem(
    name: String,
    time: String,
    imageUrl: String,
    viewed: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = if (viewed) ConnectaSecondaryText else ConnectaTeal,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.nodp),
                contentDescription = name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = if (viewed) ConnectaSecondaryText else ConnectaTeal,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            Text(
                text = name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = ConnectaDarkText
            )
            Spacer(modifier = Modifier.size(3.dp))
            Text(
                text = time,
                fontSize = 14.sp,
                color = ConnectaSecondaryText
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0) return ""
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} minutes ago"
        diff < 24 * 60 * 60 * 1000 -> {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            "Today, ${sdf.format(Date(timestamp))}"
        }
        else -> {
            val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}