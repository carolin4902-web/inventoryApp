package com.example.inventoryapp.feature.product.data.repository

import com.example.inventoryapp.feature.product.data.local.ProductDao
import com.example.inventoryapp.feature.product.data.mapper.toDomain
import com.example.inventoryapp.feature.product.data.mapper.toDto
import com.example.inventoryapp.feature.product.data.mapper.toEntity
import com.example.inventoryapp.feature.product.data.remote.FirestoreProductDataSource
import com.example.inventoryapp.feature.product.domain.model.Product
import com.example.inventoryapp.feature.product.domain.repository.ProductRepository
import com.example.inventoryapp.feature.product.domain.repository.SyncReport
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val remote: FirestoreProductDataSource,
    private val auth: FirebaseAuth
) : ProductRepository {

    private val syncMutex = Mutex()

    override fun observeProducts(): Flow<List<Product>> =
        dao.observeVisible().map { rows -> rows.map { it.toDomain() } }

    override suspend fun findById(id: String): Product? =
        dao.find(id)?.takeUnless { it.isDeleted }?.toDomain()

    override suspend fun save(product: Product) {
        dao.upsert(
            product.copy(updatedAt = System.currentTimeMillis()).toEntity()
        )
    }

    override suspend fun delete(id: String) {
        val current = dao.find(id) ?: return
        dao.upsert(
            current.copy(
                isDeleted = true,
                pending = true,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun syncNow(): SyncReport = syncMutex.withLock {
        val uid = auth.currentUser?.uid
            ?: auth.signInAnonymously().await().user?.uid
            ?: error("Anonymous sign-in failed")

        var uploaded = 0
        for (local in dao.getPending()) {
            remote.push(uid, local.toDto())
            dao.markSynced(local.id, local.updatedAt)
            uploaded++
        }

        val cloudItems = remote.fetchFromServer(uid)
        for (dto in cloudItems) {
            val cloud = dto.toEntity()
            val local = dao.find(cloud.id)

            if (local?.pending == true) continue
            if (local == null || cloud.updatedAt > local.updatedAt) {
                dao.upsert(cloud)
            }
        }
        SyncReport(uploaded, cloudItems.size)
    }
}