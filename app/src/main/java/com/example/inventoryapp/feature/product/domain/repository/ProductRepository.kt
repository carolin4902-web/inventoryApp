package com.example.inventoryapp.feature.product.domain.repository

import com.example.inventoryapp.feature.product.domain.model.Product
import kotlinx.coroutines.flow.Flow

data class SyncReport(
    val uploaded: Int,
    val downloaded: Int
)

interface ProductRepository {

    fun observeProducts(): Flow<List<Product>>

    suspend fun findById(id: String): Product?

    suspend fun save(product: Product)

    suspend fun delete(id: String)

    suspend fun syncNow(): SyncReport
}