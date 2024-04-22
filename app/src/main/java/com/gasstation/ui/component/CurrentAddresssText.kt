package com.gasstation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gasstation.ui.theme.ColorBlack

@Composable
fun CurrentAddresssText(address: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = address,
        textAlign = TextAlign.Center,
        fontSize = 15.sp,
        fontWeight = FontWeight(600),
        color = ColorBlack
    )
}

@Preview
@Composable
fun PreviewCurrentAddresssText() {
    CurrentAddresssText(address = "서울 영등포구 당산동 194-32")
}