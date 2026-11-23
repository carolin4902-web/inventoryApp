package com.example.inventoryapp.feature.product.data.repository

import com.example.inventoryapp.feature.product.domain.repository.SyncReport
import com.example.inventoryapp.feature.product.data.local.ProductDao
import com.example.inventoryapp.feature.product.data.mapper.toDomain
import com.example.inventoryapp.feature.product.data.mapper.toEntity
import com.example.inventoryapp.feature.product.domain.model.Product
import com.example.inventoryapp.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> {
        return dao.observeVisible().map { products ->
            products.map { it.toDomain() }
        }
    }

    override suspend fun findById(id: String): Product? {
        return dao.find(id)?.toDomain()
    }

    override suspend fun save(product: Product) {
        dao.upsert(product.toEntity())
    }

    override suspend fun delete(id: String) {
        val product = dao.find(id) ?: return

        dao.upsert(
            product.copy(
                isDeleted = true,
                pending = true,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun syncNow(): SyncReport {
        return SyncReport(
            uploaded = 0,
            downloaded = 0
        )
    }
}