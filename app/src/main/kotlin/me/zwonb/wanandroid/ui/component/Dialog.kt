package me.zwonb.wanandroid.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun TipDialog(
    text: String?,
    dismiss: Boolean = false,
    confirmText: String = "确定",
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onDismiss, Modifier.fillMaxWidth()) {
                Text(confirmText)
            }
        },
        text = { Text(text ?: "", fontSize = 16.sp) },
        properties = DialogProperties(dismiss, dismiss)
    )
}

@Composable
fun TipDialog(
    text: String?,
    confirmText: String = "确定",
    dismissText: String = "取消",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onDismiss, Modifier.weight(1f)) {
                    Text(dismissText, color = MaterialTheme.colors.primary)
                }
                TextButton(onConfirm, Modifier.weight(1f)) {
                    Text(confirmText, color = MaterialTheme.colors.primary)
                }
            }
        },
        text = { Text(text ?: "", fontSize = 16.sp) },
        properties = DialogProperties(false, dismissOnClickOutside = false)
    )
}

@Composable
fun TipDialog(
    text: @Composable (() -> Unit)? = null,
    confirmText: String = "确定",
    dismissText: String = "取消",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onDismiss, Modifier.weight(1f)) {
                    Text(dismissText, color = MaterialTheme.colors.primary)
                }
                TextButton(onConfirm, Modifier.weight(1f)) {
                    Text(confirmText, color = MaterialTheme.colors.primary)
                }
            }
        },
        text = text,
        properties = DialogProperties(false, dismissOnClickOutside = false)
    )
}