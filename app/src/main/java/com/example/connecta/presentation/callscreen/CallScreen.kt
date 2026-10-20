package com.example.connecta.presentation.callscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.connecta.R
import com.example.connecta.models.CallRecord
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import com.example.connecta.presentation.viewmodel.CallViewModel
import java.text.SimpleDateFormat
import java.util.*

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun CallScreen(
    onChatsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    viewModel: CallViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val calls by viewModel.calls.collectAsState()

    var selectedFilter by remember { mutableStateOf("All Calls") }
    var showDialpadDialog by remember { mutableStateOf(false) }
    var activeCallingPartner by remember { mutableStateOf<String?>(null) }

    val filteredCalls = when (selectedFilter) {
        "Missed" -> calls.filter { it.isMissed }
        "Outgoing" -> calls.filter { it.callType == "Outgoing call" }
        "Incoming" -> calls.filter { it.callType == "Incoming call" }
        else -> calls
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialpadDialog = true },
                containerColor = ConnectaTeal,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Dialpad,
                    contentDescription = "Open keypad",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 3,
                onChatsClick = onChatsClick,
                onUpdatesClick = onUpdatesClick,
                onPostsClick = onPostsClick,
                onCallsClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Connecta",
                    color = ConnectaTeal,
                    fontSize = 30.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        modifier = Modifier.size(26.dp)
                    )
                }

                IconButton(onClick = onMenuClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallFilterItem(
                    icon = Icons.Default.Call,
                    text = "All Calls",
                    selected = selectedFilter == "All Calls",
                    onClick = { selectedFilter = "All Calls" }
                )
                CallFilterItem(
                    icon = Icons.Default.CallMissed,
                    text = "Missed",
                    selected = selectedFilter == "Missed",
                    onClick = { selectedFilter = "Missed" }
                )
                CallFilterItem(
                    icon = Icons.Default.CallMade,
                    text = "Outgoing",
                    selected = selectedFilter == "Outgoing",
                    onClick = { selectedFilter = "Outgoing" }
                )
                CallFilterItem(
                    icon = Icons.Default.CallReceived,
                    text = "Incoming",
                    selected = selectedFilter == "Incoming",
                    onClick = { selectedFilter = "Incoming" }
                )
            }

            // Call Records List
            if (filteredCalls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = ConnectaSecondaryText.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No call history",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ConnectaDarkText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap the dialpad button below to place a call to any contact or phone number.",
                            fontSize = 14.sp,
                            color = ConnectaSecondaryText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredCalls) { call ->
                        RealCallRow(
                            call = call,
                            onCallClick = {
                                activeCallingPartner = call.partnerName.ifBlank { "Contact" }
                                viewModel.logCall(partnerName = call.partnerName, callType = "Outgoing call")
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialpad Dialog
    if (showDialpadDialog) {
        var dialNumber by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialpadDialog = false },
            title = {
                Text(
                    text = "Keypad Dialer",
                    fontWeight = FontWeight.Bold,
                    color = ConnectaDarkText
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dialNumber.ifBlank { "Enter Number" },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (dialNumber.isNotBlank()) ConnectaDarkText else ConnectaSecondaryText,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val keys = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("*", "0", "#")
                    )

                    keys.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { digit ->
                                Button(
                                    onClick = { dialNumber += digit },
                                    shape = CircleShape,
                                    modifier = Modifier.size(52.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF1F5F6),
                                        contentColor = ConnectaDarkText
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = digit, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dialNumber.isNotBlank()) {
                            val target = dialNumber.trim()
                            showDialpadDialog = false
                            activeCallingPartner = target
                            viewModel.logCall(partnerName = target, callType = "Outgoing call")
                        } else {
                            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ConnectaTeal)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialpadDialog = false }) {
                    Text("Cancel", color = ConnectaSecondaryText)
                }
            }
        )
    }

    // Active Simulated Call Screen Dialog
    if (activeCallingPartner != null) {
        val partner = activeCallingPartner!!
        AlertDialog(
            onDismissRequest = { activeCallingPartner = null },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = partner,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ConnectaDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connecta Voice Call • Connected",
                        fontSize = 13.sp,
                        color = ConnectaTeal
                    )
                }
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nodp),
                        contentDescription = partner,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeCallingPartner = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("End Call", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun CallFilterItem(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) ConnectaTeal else ConnectaSecondaryText,
            modifier = Modifier.size(21.dp)
        )
        Spacer(modifier = Modifier.size(3.dp))
        Text(
            text = text,
            color = if (selected) ConnectaTeal else ConnectaSecondaryText,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun RealCallRow(
    call: CallRecord,
    onCallClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!call.partnerImage.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = call.partnerImage),
                contentDescription = call.partnerName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.nodp),
                contentDescription = call.partnerName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = call.partnerName,
                color = ConnectaDarkText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.size(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val callIcon = when {
                    call.isMissed -> Icons.Default.CallMissed
                    call.callType == "Incoming call" -> Icons.Default.CallReceived
                    else -> Icons.Default.CallMade
                }

                val callColor = if (call.isMissed) Color.Red else ConnectaTeal

                Icon(
                    imageVector = callIcon,
                    contentDescription = call.callType,
                    tint = callColor,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = call.callType,
                    color = ConnectaSecondaryText,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "•", color = ConnectaSecondaryText, fontSize = 13.sp)
                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = formatCallTimestamp(call.timestamp),
                    color = ConnectaSecondaryText,
                    fontSize = 13.sp
                )
            }
        }

        IconButton(onClick = onCallClick) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = ConnectaTeal,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

private fun formatCallTimestamp(timestamp: Long): String {
    if (timestamp <= 0) return ""
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h ago"
        else -> {
            val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}