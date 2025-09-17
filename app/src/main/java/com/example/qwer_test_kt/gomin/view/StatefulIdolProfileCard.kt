package com.example.qwer_test_kt.gomin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qwer_test_kt.R

@Composable
fun StatefulIdolProfileCard(
    name: String,
    group: String,
    profileImageRes: Int,
    initialLikesCount: Int
) {
    // 1. mutableStateOf와 remember를 사용하여 상태 변수를 만듭니다.
    // by 키워드로 value에 직접 접근하는 것을 피합니다.
    var likesCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = profileImageRes),
                contentDescription = "$name's profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = group,
                )
            }
            // 2. 좋아요 버튼을 추가합니다.
            IconButton(
                onClick = {
                    likesCount++
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // 4. likesCount 변수의 값을 화면에 표시합니다.
            // 이 텍스트는 likesCount가 변경될 때마다 자동으로 재구성됩니다.
            Text(
                text = "$likesCount",
            )
        }
    }
}

// 이 함수에서 상태를 가진 컴포넌트를 사용합니다.
@Composable
fun InteractiveIdolProfileScreen() {
    Column {
        StatefulIdolProfileCard(
            name = "이시연 ",
            group = "메인 보컬",
            profileImageRes = R.drawable.siyeon, // 이미지 리소스 ID
            initialLikesCount = 0
        )
    }
}

@Composable
@Preview(showBackground = true)
fun InteractiveIdolProfileScreenPreview() {
    InteractiveIdolProfileScreen()
}