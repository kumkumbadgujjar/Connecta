package com.example.connecta.presentation.callscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaSecondaryText = Color(0xFF607D86)
private val ConnectaDarkText = Color(0xFF102A32)

@Composable
fun CallItem(
    call: Call,
    onCallClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Image(
            painter = painterResource(
                id = call.userImage
            ),
            contentDescription = call.userName,
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.size(14.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = call.userName,
                color = ConnectaDarkText,
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.size(3.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                val callIcon =
                    when {
                        call.isMissed -> Icons.Default.CallMissed

                        call.callType == "Incoming call" ->
                            Icons.Default.CallReceived

                        else ->
                            Icons.Default.CallMade
                    }

                val callColor =
                    if (call.isMissed) {
                        Color.Red
                    } else {
                        ConnectaTeal
                    }

                Icon(
                    imageVector = callIcon,
                    contentDescription = call.callType,
                    tint = callColor,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = call.callType,
                    color = ConnectaSecondaryText,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = "•",
                    color = ConnectaSecondaryText,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = call.time,
                    color = ConnectaSecondaryText,
                    fontSize = 14.sp
                )
            }
        }


        IconButton(
            onClick = onCallClick
        ) {

            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call ${call.userName}",
                tint = ConnectaTeal,
                modifier = Modifier.size(27.dp)
            )
        }
    }
}