package com.example.inventoryapp.feature.product.data.mapper

import com.example.inventoryapp.feature.product.data.local.ProductEntity
import com.example.inventoryapp.feature.product.domain.model.Product

fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt,
    pending = true,
    isDeleted = false
)