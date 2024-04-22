package com.gasstation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorGray
import com.gasstation.ui.theme.ColorWhite
import com.gasstation.ui.theme.ColorYellow

@Composable
fun SettingDetailItem(type: String, isShowCheck: Boolean, onSettingDetailClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(ColorWhite)
            .padding(12.dp)
            .clickable {
                onSettingDetailClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = type, color = ColorBlack, style = MaterialTheme.typography.labelMedium)
        if (isShowCheck) {
            Icon(Icons.Filled.Check, "check", tint = ColorYellow)
        }
    }
    Divider(modifier = Modifier, color = ColorGray, thickness = 0.5.dp)
}

@Preview
@Composable
private fun PreviewSettingDetailItem() {
    SettingDetailItem(
        "휘발유", true
    ) {}
}