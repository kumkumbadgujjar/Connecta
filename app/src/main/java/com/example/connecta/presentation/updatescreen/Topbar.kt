package com.example.connecta.presentation.updatescreen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
private val ConnectaDarkText = Color(0xFF102A32)

@Composable
fun UpdateTopBar(
    onCameraClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
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

        Icon(
            painter = painterResource(
                id = R.drawable.camera
            ),
            contentDescription = "Camera",
            modifier = Modifier
                .size(27.dp)
                .clickable {
                    onCameraClick()
                },
            tint = ConnectaDarkText
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
                .size(27.dp)
                .clickable {
                    onSearchClick()
                },
            tint = ConnectaDarkText
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
                .size(27.dp)
                .clickable {
                    onMenuClick()
                },
            tint = ConnectaDarkText
        )
    }
}
