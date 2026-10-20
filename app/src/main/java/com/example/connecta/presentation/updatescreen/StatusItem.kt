package com.example.connecta.presentation.updatescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ConnectaTeal = Color(0xFF06B6D4)
private val ConnectaDarkText = Color(0xFF102A32)
private val ConnectaSecondaryText = Color(0xFF607D86)

@Composable
fun StatusItem(
    name: String,
    time: String,
    imageRes: Int,
    viewed: Boolean = false,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(
                id = imageRes
            ),
            contentDescription = name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = if (viewed) {
                        ConnectaSecondaryText
                    } else {
                        ConnectaTeal
                    },
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Column {

            Text(
                text = name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = ConnectaDarkText
            )

            Spacer(
                modifier = Modifier.size(3.dp)
            )

            Text(
                text = time,
                fontSize = 14.sp,
                color = ConnectaSecondaryText
            )
        }
    }
}

