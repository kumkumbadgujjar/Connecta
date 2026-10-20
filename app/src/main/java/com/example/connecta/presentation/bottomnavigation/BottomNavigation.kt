package com.example.connecta.presentation.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.connecta.R


private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaSecondaryText = Color(0xFF607D86)


@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onChatsClick: () -> Unit = {},
    onUpdatesClick: () -> Unit,
    onPostsClick: () -> Unit = {},
    onCallsClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),

        horizontalArrangement = Arrangement.SpaceAround,

        verticalAlignment = Alignment.CenterVertically
    ) {

        // CHATS
        BottomNavigationItem(
            icon = R.drawable.chat_icon,
            label = "Chats",
            selected = selectedItem == 0,
            onClick = onChatsClick
        )

        // UPDATES
        BottomNavigationItem(
            icon = R.drawable.update_icon,
            label = "Updates",
            selected = selectedItem == 1,
            onClick = onUpdatesClick
        )

        // POSTS
        BottomNavigationItem(
            icon = R.drawable.communities_icon,
            label = "Post",
            selected = selectedItem == 2,
            onClick = onPostsClick
        )

        // CALLS
        BottomNavigationItem(
            icon = R.drawable.telephone,
            label = "Calls",
            selected = selectedItem == 3,
            onClick = onCallsClick
        )
    }
}


@Composable
fun BottomNavigationItem(
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
        modifier = Modifier.clickable {
            onClick()
        },

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(25.dp),
            tint = iconColor
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight =
                if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
            color = iconColor
        )
    }
}