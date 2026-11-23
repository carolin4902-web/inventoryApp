package com.example.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventoryapp.feature.product.domain.model.Product
import com.example.inventoryapp.feature.product.presentation.ProductViewModel
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InventoryAppTheme {
                ProductScreen()
            }
        }
    }
}

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Inventario",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    productToEdit = null
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar producto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (products.isEmpty()) {

                Text(
                    text = "No hay productos registrados."
                )

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = products,
                        key = { it.id }
                    ) { product ->

                        ProductCard(
                            product = product,
                            onEdit = {
                                productToEdit = product
                                showDialog = true
                            },
                            onDelete = {
                                viewModel.deleteProduct(product.id)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {

        ProductDialog(
            product = productToEdit,
            onDismiss = {
                showDialog = false
                productToEdit = null
            },
            onSave = { product ->

                viewModel.saveProduct(product)

                showDialog = false
                productToEdit = null
            }
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.description
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Precio: $${product.priceCop}"
            )

            Text(
                text = "Cantidad: ${product.quantity}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun ProductDialog(
    product: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {

    var name by remember(product) {
        mutableStateOf(product?.name ?: "")
    }

    var description by remember(product) {
        mutableStateOf(product?.description ?: "")
    }

    var price by remember(product) {
        mutableStateOf(
            product?.priceCop?.toString() ?: ""
        )
    }

    var quantity by remember(product) {
        mutableStateOf(
            product?.quantity?.toString() ?: ""
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                if (product == null) {
                    "Nuevo producto"
                } else {
                    "Editar producto"
                }
            )
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text("Nombre")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text("Descripción")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it.filter { char -> char.isDigit() }
                    },
                    label = {
                        Text("Precio")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it.filter { char -> char.isDigit() }
                    },
                    label = {
                        Text("Cantidad")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val priceValue = price.toLongOrNull() ?: 0L
                    val quantityValue = quantity.toIntOrNull() ?: 0

                    val newProduct = Product(
                        id = product?.id ?: UUID.randomUUID().toString(),
                        name = name,
                        description = description,
                        priceCop = priceValue,
                        quantity = quantityValue,
                        updatedAt = System.currentTimeMillis()
                    )

                    onSave(newProduct)
                }
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}