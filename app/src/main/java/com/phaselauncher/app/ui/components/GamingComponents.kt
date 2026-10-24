package com.phaselauncher.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.phaselauncher.app.R
import com.phaselauncher.app.ui.theme.*

@Composable
fun DiscordAvatarCircle(
    avatarUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(BorderStroke(1.5.dp, NeonCyan), CircleShape)
            .background(CardDark),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl.isNotBlank() && avatarUrl.startsWith("http")) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Discord Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Avatar",
                tint = NeonCyan,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

// Sobrecarga para aceitar Int e evitar erros de tipo
@Composable
fun DiscordAvatarCircle(
    avatarUrl: String,
    modifier: Modifier = Modifier,
    size: Int
) {
    DiscordAvatarCircle(avatarUrl = avatarUrl, modifier = modifier, size = size.dp)
}

@Composable
fun VBucksBadge(
    amount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark,
        border = BorderStroke(1.dp, VBucksGold)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_vbucks),
                contentDescription = "V-Bucks",
                tint = VBucksGold,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "%,d".format(amount),
                color = VBucksGold,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun PhasePlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "PLAY RUIN"
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(BorderStroke(1.5.dp, NeonCyan), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                letterSpacing = 2.sp
            )
        }
    }
}
