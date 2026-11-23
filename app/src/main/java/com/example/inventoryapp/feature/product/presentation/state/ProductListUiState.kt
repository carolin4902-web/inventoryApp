package com.example.inventoryapp.feature.product.presentation.state

import com.example.inventoryapp.feature.product.domain.model.Product

/** Datos que ProductListScreen necesita renderizar. */
data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isSyncing: Boolean = false,
    val message: String? = null
)