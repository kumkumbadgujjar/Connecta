package com.example.connecta.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.example.connecta.R
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import kotlinx.coroutines.delay

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

data class ChatItem(
    val name: String,
    val message: String,
    val time: String,
    val image: Int,
    val unreadCount: Int = 0
)

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreen(
    onChatClick: (ChatItem) -> Unit = {},
    onNewChatClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {}
) {


    var isSearching by remember {
        mutableStateOf(false)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val focusRequester = remember {
        FocusRequester()
    }

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



    val chatData = listOf(

        ChatItem(
            image = R.drawable.nodp,
            name = "College",
            time = "10:01 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "School",
            time = "10:00 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "Him",
            time = "10:30 PM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "Her",
            time = "10:40 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "It",
            time = "10:07 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "They",
            time = "12:00 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "You",
            time = "11:00 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "WE",
            time = "10:30 AM",
            message = "Hii"
        ),

        ChatItem(
            image = R.drawable.nodp,
            name = "MOM",
            time = "10:00 PM",
            message = "Hii"
        )
    )


    val filteredChats = if (searchText.isBlank()) {

        chatData

    } else {

        chatData.filter { chat ->

            chat.name.contains(
                searchText,
                ignoreCase = true
            ) ||
                    chat.message.contains(
                        searchText,
                        ignoreCase = true
                    )
        }
    }


    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = ConnectaTeal,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {

                Icon(
                    painter = painterResource(
                        id = R.drawable.chat_icon
                    ),
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



            if (isSearching) {



                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(

                        value = searchText,

                        onValueChange = { text ->

                            searchText = text
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(
                                focusRequester
                            ),

                        singleLine = true,

                        placeholder = {

                            Text(
                                text = "Search"
                            )
                        },

                        leadingIcon = {

                            Icon(

                                painter = painterResource(
                                    id = R.drawable.search
                                ),
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

                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(

                            focusedContainerColor =
                                Color(0xFFF1F5F6),
                            unfocusedContainerColor =
                                Color(0xFFF1F5F6),
                            disabledContainerColor =
                                Color(0xFFF1F5F6),
                            focusedIndicatorColor =
                                Color.Transparent,
                            unfocusedIndicatorColor =
                                Color.Transparent,
                            disabledIndicatorColor =
                                Color.Transparent
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

                        painter = painterResource(
                            id = R.drawable.camera
                        ),

                        contentDescription = "Camera",

                        modifier = Modifier
                            .size(32.dp)
                            .clickable {

                                onCameraClick()
                            }
                    )

                    Spacer(
                        modifier = Modifier.width(20.dp)
                    )



                    Icon(

                        painter = painterResource(
                            id = R.drawable.search
                        ),

                        contentDescription = "Search",

                        modifier = Modifier
                            .size(26.dp)
                            .clickable {


                                isSearching = true
                            }
                    )

                    Spacer(
                        modifier = Modifier.width(20.dp)
                    )



                    Icon(

                        painter = painterResource(
                            id = R.drawable.menu
                        ),

                        contentDescription = "Menu",

                        modifier = Modifier
                            .size(26.dp)
                            .clickable {

                                onMenuClick()
                            }
                    )
                }
            }



            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
            ) {

                items(filteredChats) { chat ->

                    ChatRow(

                        chat = chat,

                        onClick = {

                            onChatClick(chat)
                        }
                    )
                }

                item {

                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
fun ChatRow(
    chat: ChatItem,
    onClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onClick()
            }
            .padding(
                horizontal = 20.dp,
                vertical = 9.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(

            painter = painterResource(
                id = chat.image
            ),

            contentDescription = chat.name,

            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape),

            contentScale = ContentScale.Crop
        )
        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ConnectaDarkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = chat.message,
                fontSize = 15.sp,
                color = ConnectaSecondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.time,
                fontSize = 13.sp,
                color = ConnectaSecondaryText
            )
            if (chat.unreadCount > 0) {
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Box(
                    modifier = Modifier
                        .size(23.dp)
                        .clip(CircleShape)
                        .background(ConnectaTeal),
                    contentAlignment = Alignment.Center
                ) {

                    Text(

                        text = chat.unreadCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}