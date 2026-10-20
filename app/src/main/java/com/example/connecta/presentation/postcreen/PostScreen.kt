package com.example.connecta.presentation.postscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

import com.example.connecta.R
import com.example.connecta.presentation.bottomnavigation.BottomNavigationBar
import kotlinx.coroutines.delay


private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaSecondaryText = Color(0xFF607D86)
private val ConnectaDarkText = Color(0xFF102A32)

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PostScreen(
    onChatsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onAddPostClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {

    var isSearching by remember { mutableStateOf(false) }

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




    val posts = listOf(

        Post(
            userName = "Ananya Sharma",
            userImage = R.drawable.nodp,
            postImage = R.drawable.sunset,
            caption = "Sunsets hit different 🌅💛",
            time = "10m ago",
            likes = 24
        ),

        Post(
            userName = "Rohan Verma",
            userImage = R.drawable.nodp,
            postImage = R.drawable.food,
            caption = "Good food, good mood! 🍕☕",
            time = "2h ago",
            likes = 32
        )
    )

    Scaffold(


        floatingActionButton = {

            FloatingActionButton(
                onClick = onAddPostClick,
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
                onChatsClick = {},
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
                    modifier = Modifier.weight(1f),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = ConnectaTeal
                )

                IconButton(
                    onClick = { }
                ) {

                    Icon(
                        painter = painterResource(
                            id = R.drawable.search
                        ),
                        contentDescription = "Search",
                        modifier = Modifier.size(27.dp)
                            .clickable {


                                isSearching = true
                            }
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
                        modifier = Modifier.size(22.dp)
                    )
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(posts.size) { index ->

                    val post = posts[index]

                    PostItem(
                        post = post
                    )
                }
            }
        }
    }
}