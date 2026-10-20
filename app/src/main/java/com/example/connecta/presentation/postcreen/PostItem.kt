package com.example.connecta.presentation.postscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
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
fun PostItem(
    post: Post
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(
                    id = post.userImage
                ),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = post.userName,
                    color = ConnectaDarkText,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = post.time,
                    color = ConnectaSecondaryText,
                    fontSize = 13.sp
                )
            }

            IconButton(
                onClick = { }
            ) {

            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )



        Text(
            text = post.caption,
            color = ConnectaDarkText,
            fontSize = 16.sp,
            modifier = Modifier.padding(
                bottom = 10.dp
            )
        )



        Image(
            painter = painterResource(
                id = post.postImage
            ),
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            IconButton(
                onClick = { }
            ) {

                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = ConnectaSecondaryText,
                    modifier = Modifier.size(27.dp)
                )
            }

            Text(
                text = post.likes.toString(),
                color = ConnectaSecondaryText,
                fontSize = 14.sp
            )
        }
    }
}