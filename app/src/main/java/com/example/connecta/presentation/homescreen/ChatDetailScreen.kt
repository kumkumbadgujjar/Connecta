package com.example.connecta.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.connecta.R
import com.example.connecta.models.Message
import com.example.connecta.presentation.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun ChatDetailScreen(
    conversationId: String,
    partnerName: String,
    partnerImage: String,
    partnerPhone: String,
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel(),
    onCallClick: (String) -> Unit = {}
) {
    val messages by viewModel.currentMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = ConnectaDarkText
                    )
                }

                if (partnerImage.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = partnerImage),
                        contentDescription = partnerName,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.nodp),
                        contentDescription = partnerName,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = partnerName.ifBlank { partnerPhone },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ConnectaDarkText
                    )
                    Text(
                        text = "Connecta User",
                        fontSize = 12.sp,
                        color = ConnectaSecondaryText
                    )
                }

                IconButton(onClick = { onCallClick(partnerName.ifBlank { partnerPhone }) }) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = ConnectaTeal
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F5F6))
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No messages yet. Say hello!",
                                fontSize = 14.sp,
                                color = ConnectaSecondaryText
                            )
                        }
                    }
                } else {
                    items(messages) { message ->
                        val isMe = message.senderId == viewModel.currentUserId
                        MessageBubble(message = message, isMe = isMe)
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text(text = "Message...", color = ConnectaSecondaryText) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = ConnectaDarkText,
                        unfocusedTextColor = ConnectaDarkText,
                        focusedContainerColor = Color(0xFFF1F5F6),
                        unfocusedContainerColor = Color(0xFFF1F5F6),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(conversationId, inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(ConnectaTeal)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isMe: Boolean) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = if (message.timestamp > 0) timeFormat.format(Date(message.timestamp)) else ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(if (isMe) ConnectaTeal else Color.White)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 15.sp,
                color = if (isMe) Color.White else ConnectaDarkText
            )
            if (formattedTime.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedTime,
                    fontSize = 11.sp,
                    color = if (isMe) Color.White.copy(alpha = 0.7f) else ConnectaSecondaryText,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
