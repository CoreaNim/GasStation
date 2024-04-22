package com.gasstation.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gasstation.R
import com.gasstation.ui.theme.ColorBlack
import com.gasstation.ui.theme.ColorWhite

@Composable
fun PermissionDialog(msg: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(modifier = Modifier.padding(12.dp),
        onDismissRequest = { onDismiss() },
        text = {
            Text(msg, style = MaterialTheme.typography.labelMedium)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = ColorBlack,
                    containerColor = ColorWhite,
                    disabledContentColor = ColorBlack,
                    disabledContainerColor = ColorWhite
                )
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = ColorBlack,
                    containerColor = ColorWhite,
                    disabledContentColor = ColorBlack,
                    disabledContainerColor = ColorWhite
                )
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        })
}

@Preview
@Composable
private fun PreviewPermissionDialog() {
    PermissionDialog(stringResource(id = R.string.auth_rationale_msg), {}, {})
}