package com.example.connecta.presentation.homescreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.connecta.R
import com.example.connecta.models.Conversation
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import com.example.connecta.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun HomeScreen(
    onChatClick: (conversationId: String, partnerName: String, partnerImage: String, partnerPhone: String) -> Unit = { _, _, _, _ -> },
    onCameraClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val conversations by viewModel.conversations.collectAsState()
    val currentUserId = viewModel.currentUserId ?: ""

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var showNewChatDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(200)
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }

    val filteredConversations = if (searchText.isBlank()) {
        conversations
    } else {
        conversations.filter { conv ->
            val partnerName = conv.participantNames.filterKeys { it != currentUserId }.values.firstOrNull() ?: ""
            partnerName.contains(searchText, ignoreCase = true) || conv.lastMessage.contains(searchText, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewChatDialog = true },
                containerColor = ConnectaTeal,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_icon),
                    contentDescription = "New Chat",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 0,
                onChatsClick = {},
                onUpdatesClick = onUpdatesClick,
                onPostsClick = onPostsClick,
                onCallsClick = onCallsClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Top Bar
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
                        placeholder = { Text(text = "Search chats") },
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Connecta",
                        modifier = Modifier.weight(1f),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = ConnectaTeal
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onCameraClick() }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { isSearching = true }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { onMenuClick() }
                    )
                }
            }

            // Real Conversations List
            if (filteredConversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.chat_icon),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            alpha = 0.35f
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchText.isBlank()) "No conversations yet" else "No matching chats found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ConnectaDarkText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchText.isBlank()) "Tap the chat button below to start a new real conversation with any contact." else "Try searching for a different name.",
                            fontSize = 14.sp,
                            color = ConnectaSecondaryText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredConversations) { conv ->
                        val partnerName = conv.participantNames.filterKeys { it != currentUserId }.values.firstOrNull() ?: "Chat"
                        val partnerImage = conv.participantImages.filterKeys { it != currentUserId }.values.firstOrNull() ?: ""
                        val partnerPhone = conv.participantPhones.filterKeys { it != currentUserId }.values.firstOrNull() ?: ""

                        RealChatRow(
                            name = partnerName.ifBlank { partnerPhone },
                            lastMessage = conv.lastMessage,
                            time = formatTimestamp(conv.lastMessageTime),
                            imageUrl = partnerImage,
                            unreadCount = conv.unreadCounts[currentUserId] ?: 0,
                            onClick = {
                                onChatClick(conv.id, partnerName.ifBlank { partnerPhone }, partnerImage, partnerPhone)
                            }
                        )
                    }

                    item {
                        HorizontalDivider(color = Color(0xFFF1F5F6))
                    }
                }
            }
        }
    }

    // Dialog: Start a New Real Chat
    if (showNewChatDialog) {
        var newPhone by remember { mutableStateOf("") }
        var newContactName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewChatDialog = false },
            title = {
                Text(
                    text = "New Conversation",
                    fontWeight = FontWeight.Bold,
                    color = ConnectaDarkText
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Enter the contact's name and phone number to start a real-time conversation:",
                        fontSize = 14.sp,
                        color = ConnectaSecondaryText
                    )
                    OutlinedTextField(
                        value = newContactName,
                        onValueChange = { newContactName = it },
                        label = { Text("Contact Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("Phone Number (with country code)") },
                        placeholder = { Text("+91 9876543210") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPhone.trim().isNotBlank()) {
                            val contactName = newContactName.trim().ifBlank { newPhone.trim() }
                            viewModel.startNewConversation(
                                partnerPhone = newPhone.trim(),
                                partnerName = contactName,
                                onSuccess = { convId ->
                                    showNewChatDialog = false
                                    onChatClick(convId, contactName, "", newPhone.trim())
                                },
                                onError = { err ->
                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ConnectaTeal)
                ) {
                    Text("Start Chat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewChatDialog = false }) {
                    Text("Cancel", color = ConnectaSecondaryText)
                }
            }
        )
    }
}

@Composable
fun RealChatRow(
    name: String,
    lastMessage: String,
    time: String,
    imageUrl: String,
    unreadCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.nodp),
                contentDescription = name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = ConnectaDarkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lastMessage,
                fontSize = 14.sp,
                color = ConnectaSecondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = time,
                fontSize = 12.sp,
                color = ConnectaSecondaryText
            )
            if (unreadCount > 0) {
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(ConnectaTeal),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = unreadCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0) return ""
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago"
        diff < 24 * 60 * 60 * 1000 -> {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
        else -> {
            val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}