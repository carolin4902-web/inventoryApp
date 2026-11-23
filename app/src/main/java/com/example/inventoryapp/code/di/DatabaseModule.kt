package com.example.inventoryapp.code.di

import android.content.Context
import androidx.room.Room
import com.example.inventoryapp.core.database.AppDatabase
import com.example.inventoryapp.feature.product.data.local.ProductDao
import com.example.inventoryapp.feature.product.data.repository.ProductRepositoryImpl
import com.example.inventoryapp.feature.product.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_database"
        ).build()
    }

    @Provides
    fun provideProductDao(
        database: AppDatabase
    ): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        repositoryImpl: ProductRepositoryImpl
    ): ProductRepository {
        return repositoryImpl
    }
}