package com.gasstation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorGray
import com.gasstation.ui.theme.ColorWhite

@Composable
fun SettingItem(setting: String, onSettingClick: () -> Unit) {
    Divider(modifier = Modifier, color = ColorGray, thickness = 0.5.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorWhite)
            .padding(12.dp)
            .clickable {
                onSettingClick()
            },
    ) {
        Text(text = setting, color = ColorBlack)
    }
    Divider(modifier = Modifier, color = ColorGray, thickness = 0.5.dp)
}

@Preview
@Composable
private fun PreviewSettingItem() {
    SettingItem(
        "오일타입"
    ) {}
}