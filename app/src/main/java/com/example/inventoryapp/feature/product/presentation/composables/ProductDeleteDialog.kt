package com.example.inventoryapp.feature.product.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** Modal solo visual. La operación real se decide en la Screen. */
@Composable
fun ProductDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar producto") },
        text = { Text("Se ocultará localmente y se eliminará al sincronizar.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Eliminar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}