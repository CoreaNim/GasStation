package com.gasstation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.R
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorYellow

@Composable
fun PermissionButton(msg: String, onButtonClick: () -> Unit) {
    Button(modifier = Modifier
        .wrapContentSize()
        .padding(top = 60.dp), onClick = {
        onButtonClick()
    }, colors = androidx.compose.material3.ButtonDefaults.buttonColors(
        contentColor = ColorYellow,
        containerColor = ColorBlack,
        disabledContentColor = ColorYellow,
        disabledContainerColor = ColorBlack
    ), content = {
        Text(
            modifier = Modifier.background(ColorBlack),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = ColorYellow,
            text = msg
        )
    })
}

@Preview
@Composable
private fun PreviewPermissionButton() {
    PermissionButton(
        stringResource(id = R.string.auth_rationale_msg)
    ) {}
}