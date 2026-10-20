package com.example.connecta.presentation.postscreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.*
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
import com.example.connecta.models.CommunityPost
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import com.example.connecta.presentation.viewmodel.PostViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaSecondaryText = Color(0xFF607D86)
private val ConnectaDarkText = Color(0xFF102A32)

@Composable
fun PostScreen(
    onChatsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    viewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val posts by viewModel.posts.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val currentUserId = viewModel.currentUserId ?: ""

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
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

    val filteredPosts = if (searchText.isBlank()) {
        posts
    } else {
        posts.filter { post ->
            post.authorName.contains(searchText, ignoreCase = true) ||
                    post.caption.contains(searchText, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                containerColor = ConnectaTeal
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create post",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 2,
                onChatsClick = onChatsClick,
                onUpdatesClick = onUpdatesClick,
                onPostsClick = {},
                onCallsClick = onCallsClick
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
                        placeholder = { Text(text = "Search posts") },
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
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Connecta",
                        modifier = Modifier.weight(1f),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = ConnectaTeal
                    )

                    IconButton(onClick = { isSearching = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    IconButton(onClick = onMenuClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Real Posts Feed
            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.communities_icon),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = ConnectaSecondaryText.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchText.isBlank()) "No posts yet" else "No matching posts found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ConnectaDarkText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchText.isBlank()) "Tap the '+' button below to create and share the first community post with your contacts!" else "Try searching with a different keyword.",
                            fontSize = 14.sp,
                            color = ConnectaSecondaryText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredPosts) { post ->
                        val isLiked = post.likedBy.contains(currentUserId)
                        val isMyPost = post.authorId == currentUserId

                        RealPostItem(
                            post = post,
                            isLiked = isLiked,
                            isMyPost = isMyPost,
                            onLikeClick = { viewModel.toggleLike(post.id) },
                            onDeleteClick = { viewModel.deletePost(post.id) }
                        )
                    }
                }
            }
        }
    }

    // Create Post Dialog
    if (showCreateDialog) {
        var captionText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                if (!isSubmitting) {
                    showCreateDialog = false
                    selectedImageUri = null
                }
            },
            title = { Text("Create New Post", fontWeight = FontWeight.Bold, color = ConnectaDarkText) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = selectedImageUri),
                            contentDescription = "Selected Photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = captionText,
                        onValueChange = { captionText = it },
                        label = { Text("Write a caption...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (captionText.trim().isNotBlank()) {
                            viewModel.createPost(
                                caption = captionText.trim(),
                                imageUri = selectedImageUri,
                                onSuccess = {
                                    showCreateDialog = false
                                    selectedImageUri = null
                                    Toast.makeText(context, "Post published!", Toast.LENGTH_SHORT).show()
                                },
                                onError = { err ->
                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Please write a caption", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isSubmitting && captionText.isNotBlank(),
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
fun RealPostItem(
    post: CommunityPost,
    isLiked: Boolean,
    isMyPost: Boolean,
    onLikeClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!post.authorImage.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(model = post.authorImage),
                    contentDescription = post.authorName,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.nodp),
                    contentDescription = post.authorName,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.authorName,
                    color = ConnectaDarkText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatPostTimestamp(post.timestamp),
                    color = ConnectaSecondaryText,
                    fontSize = 13.sp
                )
            }

            if (isMyPost) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ConnectaSecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Caption
        if (post.caption.isNotBlank()) {
            Text(
                text = post.caption,
                color = ConnectaDarkText,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        // Post Image
        if (post.imageUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = post.imageUrl),
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Like Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color(0xFFE91E63) else ConnectaSecondaryText,
                    modifier = Modifier.size(27.dp)
                )
            }

            Text(
                text = "${post.likesCount}",
                color = if (isLiked) Color(0xFFE91E63) else ConnectaSecondaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatPostTimestamp(timestamp: Long): String {
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