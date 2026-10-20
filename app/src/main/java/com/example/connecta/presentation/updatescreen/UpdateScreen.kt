package com.example.connecta.presentation.updatescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.connecta.R
import kotlinx.coroutines.delay

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

data class UpdateUser(
    val name: String,
    val time: String,
    val image: Int
)

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun UpdateScreen(
    onStatusClick: (UpdateUser) -> Unit = {},
    onCameraClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onChatsClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
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

    val keyboardController =
        LocalSoftwareKeyboardController.current


    LaunchedEffect(isSearching) {

        if (isSearching) {

            delay(200)

            focusRequester.requestFocus()

            keyboardController?.show()

        } else {

            keyboardController?.hide()
        }
    }


    val recentUpdates = listOf(

        UpdateUser(
            name = "Ananya Sharma",
            time = "Just now",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Rohan Verma",
            time = "10 minutes ago",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Neha Patil",
            time = "25 minutes ago",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Travel Diaries",
            time = "Today, 8:45 AM",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Friends Forever",
            time = "Today, 7:30 AM",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "College Updates",
            time = "Today, 6:15 AM",
            image = R.drawable.nodp
        )
    )



    val viewedUpdates = listOf(

        UpdateUser(
            name = "Aditya Kulkarni",
            time = "Today, 5:40 AM",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Priya",
            time = "Today, 5:20 AM",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Rahul",
            time = "Today, 4:50 AM",
            image = R.drawable.nodp
        ),

        UpdateUser(
            name = "Karan",
            time = "Today, 4:10 AM",
            image = R.drawable.nodp
        )
    )


    val filteredRecentUpdates =
        if (searchText.isBlank()) {

            recentUpdates

        } else {

            recentUpdates.filter { update ->

                update.name.contains(
                    searchText,
                    ignoreCase = true
                )
            }
        }


    val filteredViewedUpdates =
        if (searchText.isBlank()) {

            viewedUpdates

        } else {

            viewedUpdates.filter { update ->

                update.name.contains(
                    searchText,
                    ignoreCase = true
                )
            }
        }


    Scaffold(


        topBar = {

            if (isSearching) {


                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
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
                                text = "Search updates"
                            )
                        },

                        leadingIcon = {

                            Icon(

                                painter = painterResource(
                                    id = R.drawable.search
                                ),

                                contentDescription =
                                    "Search",

                                modifier =
                                    Modifier.size(24.dp),

                                tint = ConnectaTeal
                            )
                        },

                        trailingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Close,

                                contentDescription =
                                    "Close search",

                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {

                                        // Clear search
                                        searchText = ""

                                        // Close search
                                        isSearching = false
                                    },

                                tint =
                                    ConnectaSecondaryText
                            )
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                imeAction =
                                    ImeAction.Search
                            ),

                        shape =
                            RoundedCornerShape(14.dp),

                        colors =
                            TextFieldDefaults.colors(

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


                UpdateTopBar(

                    onCameraClick =
                        onCameraClick,

                    onSearchClick = {



                        isSearching = true
                    },

                    onMenuClick =
                        onMenuClick
                )
            }
        },



        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                },

                containerColor =
                    ConnectaTeal,

                contentColor =
                    Color.White
            ) {

                Icon(

                    painter = painterResource(
                        id = R.drawable.camera
                    ),

                    contentDescription =
                        "Create update",

                    modifier =
                        Modifier.size(32.dp)
                )
            }
        },


        bottomBar = {

            UpdateBottomBar(

                onChatsClick =
                    onChatsClick,

                onPostClick =
                    onPostClick,

                onCallsClick =
                    onCallsClick
            )
        }

    ) { paddingValues ->



        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),

            contentPadding =
                PaddingValues(
                    top = 10.dp,
                    bottom = 120.dp
                ),

            userScrollEnabled = true
        ) {


            item {

                Text(

                    text = "Updates",

                    fontSize = 26.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        ConnectaDarkText
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(

                    text = "Status",

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        ConnectaTeal
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )



                if (searchText.isBlank()) {

                    StatusItem(

                        name = "My status",

                        time =
                            "Tap to add status update",

                        imageRes =
                            R.drawable.nodp,

                        viewed = false,

                        onClick = {
                            // Open status creator later
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(28.dp)
                    )
                }


                if (filteredRecentUpdates.isNotEmpty()) {

                    Text(

                        text =
                            "Recent updates",

                        fontSize = 19.sp,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            ConnectaSecondaryText
                    )

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )
                }
            }


            items(filteredRecentUpdates) { update ->

                StatusItem(

                    name =
                        update.name,

                    time =
                        update.time,

                    imageRes =
                        update.image,

                    viewed = false,

                    onClick = {

                        onStatusClick(update)
                    }
                )

                Spacer(
                    modifier =
                        Modifier.height(17.dp)
                )
            }



            item {

                if (filteredViewedUpdates.isNotEmpty()) {

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )

                    Text(

                        text =
                            "Viewed updates",

                        fontSize = 19.sp,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            ConnectaSecondaryText
                    )

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )
                }
            }


            items(filteredViewedUpdates) { update ->

                StatusItem(

                    name =
                        update.name,

                    time =
                        update.time,

                    imageRes =
                        update.image,

                    viewed = true,

                    onClick = {

                        onStatusClick(update)
                    }
                )

                Spacer(
                    modifier =
                        Modifier.height(17.dp)
                )
            }


            item {

                if (
                    searchText.isNotBlank() &&
                    filteredRecentUpdates.isEmpty() &&
                    filteredViewedUpdates.isEmpty()
                ) {

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 40.dp
                            ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                "No updates found",

                            fontSize = 17.sp,

                            color =
                                ConnectaSecondaryText
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(40.dp)
                )
            }
        }
    }
}



@Composable
fun UpdateBottomBar(
    onChatsClick: () -> Unit,
    onPostClick: () -> Unit,
    onCallsClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),

        horizontalArrangement =
            Arrangement.SpaceAround,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        BottomItem(

            icon =
                R.drawable.chat_icon,

            label =
                "Chats",

            selected =
                false,

            onClick =
                onChatsClick
        )

        BottomItem(

            icon =
                R.drawable.update_icon,

            label =
                "Updates",

            selected =
                true,

            onClick = {}
        )

        BottomItem(

            icon =
                R.drawable.communities_icon,

            label =
                "Post",

            selected =
                false,

            onClick =
                onPostClick

        )

        BottomItem(

            icon =
                R.drawable.telephone,

            label =
                "Calls",

            selected =
                false,

            onClick =
                onCallsClick
        )
    }
}




@Composable
fun BottomItem(
    icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val iconColor =
        if (selected) {

            ConnectaTeal

        } else {

            ConnectaSecondaryText
        }

    Column(

        modifier = Modifier
            .clickable {
                onClick()
            },

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Icon(

            painter =
                painterResource(
                    id = icon
                ),

            contentDescription =
                label,

            modifier =
                Modifier.size(25.dp),

            tint =
                iconColor
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(

            text =
                label,

            fontSize =
                12.sp,

            fontWeight =
                if (selected) {

                    FontWeight.SemiBold

                } else {

                    FontWeight.Normal
                },

            color =
                iconColor
        )
    }
}