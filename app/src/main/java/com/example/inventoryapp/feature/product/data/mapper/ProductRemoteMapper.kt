package com.example.inventoryapp.feature.product.data.mapper

import com.example.inventoryapp.feature.product.data.local.ProductEntity
import com.example.inventoryapp.feature.product.data.remote.ProductDto

fun ProductEntity.toDto(): ProductDto = ProductDto(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt,
    isDeleted = isDeleted
)

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt,
    pending = false,
    isDeleted = isDeleted
)