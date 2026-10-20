package com.example.connecta.presentation.callscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.connecta.R

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)


@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CallScreen(
    onChatsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onKeypadClick: () -> Unit = {}
) {


    val calls = listOf(

        Call(
            userName = "Ananya Sharma",
            userImage = R.drawable.nodp,
            callType = "Incoming call",
            time = "2 min ago"
        ),

        Call(
            userName = "Rohan Verma",
            userImage = R.drawable.nodp,
            callType = "Missed call",
            time = "15 min ago",
            isMissed = true
        ),

        Call(
            userName = "Neha Patil",
            userImage = R.drawable.nodp,
            callType = "Outgoing call",
            time = "32 min ago"
        ),

        Call(
            userName = "Aditya Singh",
            userImage = R.drawable.nodp,
            callType = "Incoming call",
            time = "1 hr ago"
        ),

        Call(
            userName = "Priya Kapoor",
            userImage = R.drawable.nodp,
            callType = "Outgoing call",
            time = "2 hr ago"
        ),

        Call(
            userName = "Family",
            userImage = R.drawable.nodp,
            callType = "Incoming call",
            time = "4 hr ago"
        ),

        Call(
            userName = "Karan Mehta",
            userImage = R.drawable.nodp,
            callType = "Missed call",
            time = "5 hr ago",
            isMissed = true
        ),

        Call(
            userName = "Simran Kaur",
            userImage = R.drawable.nodp,
            callType = "Outgoing call",
            time = "1 day ago"
        )
    )


    // =================================================
    // SCAFFOLD
    // =================================================

    Scaffold(

        // =================================================
        // DIAL PAD BUTTON
        // =================================================

        floatingActionButton = {

            FloatingActionButton(
                onClick = onKeypadClick,
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

            CallBottomNavigation(
                onChatsClick = onChatsClick,
                onUpdatesClick = onUpdatesClick,
                onPostsClick = onPostsClick,
                onCallsClick = onCallsClick
            )
        }

    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Connecta",
                    color = ConnectaTeal,
                    fontSize = 30.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )



                IconButton(
                    onClick = { }
                ) {

                    Icon(
                        painter = painterResource(
                            id = R.drawable.search
                        ),
                        contentDescription = "Search",
                        modifier = Modifier.size(26.dp)
                    )
                }




                IconButton(
                    onClick = { }
                ) {

                    Icon(
                        painter = painterResource(
                            id = R.drawable.menu
                        ),
                        contentDescription = "Menu",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            CallFilters()


            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(calls.size) { index ->

                    CallItem(
                        call = calls[index],
                        onCallClick = { }
                    )
                }
            }
        }
    }
}


@Composable
fun CallFilters() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CallFilter(
            icon = Icons.Default.Call,
            text = "All Calls",
            selected = true
        )

        CallFilter(
            icon = Icons.Default.CallMissed,
            text = "Missed"
        )

        CallFilter(
            icon = Icons.Default.CallMade,
            text = "Outgoing"
        )

        CallFilter(
            icon = Icons.Default.CallReceived,
            text = "Incoming"
        )
    }
}



@Composable
fun CallFilter(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    selected: Boolean = false
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            horizontal = 4.dp
        )
    ) {

        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) {
                ConnectaTeal
            } else {
                ConnectaSecondaryText
            },
            modifier = Modifier.size(21.dp)
        )

        Spacer(
            modifier = Modifier.size(3.dp)
        )

        Text(
            text = text,
            color = if (selected) {
                ConnectaTeal
            } else {
                ConnectaSecondaryText
            },
            fontSize = 12.sp
        )
    }
}

@Composable
fun CallBottomNavigation(
    onChatsClick: () -> Unit,
    onUpdatesClick: () -> Unit,
    onPostsClick: () -> Unit,
    onCallsClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {


        IconButton(
            onClick = onChatsClick,
            modifier = Modifier.weight(1f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(
                        id = R.drawable.chat_icon
                    ),
                    contentDescription = "Chats",
                    tint = ConnectaSecondaryText,
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = "Chats",
                    color = ConnectaSecondaryText,
                    fontSize = 12.sp
                )
            }
        }



        IconButton(
            onClick = onUpdatesClick,
            modifier = Modifier.weight(1f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(
                        id = R.drawable.update_icon
                    ),
                    contentDescription = "Updates",
                    tint = ConnectaSecondaryText,
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = "Updates",
                    color = ConnectaSecondaryText,
                    fontSize = 12.sp
                )
            }
        }


        IconButton(
            onClick = onPostsClick,
            modifier = Modifier.weight(1f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(
                        id = R.drawable.communities_icon
                    ),
                    contentDescription = "Posts",
                    tint = ConnectaSecondaryText,
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = "Posts",
                    color = ConnectaSecondaryText,
                    fontSize = 12.sp
                )
            }
        }


        IconButton(
            onClick = onCallsClick,
            modifier = Modifier.weight(1f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Calls",
                    tint = ConnectaTeal,
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = "Calls",
                    color = ConnectaTeal,
                    fontSize = 12.sp
                )
            }
        }
    }
}