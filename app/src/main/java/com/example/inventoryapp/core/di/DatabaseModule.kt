package com.example.inventoryapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.inventoryapp.core.database.AppDatabase
import com.example.inventoryapp.feature.product.data.local.ProductDao
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
    fun database(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory.db"
        ).build()

    @Provides
    fun productDao(database: AppDatabase): ProductDao = database.productDao()
}