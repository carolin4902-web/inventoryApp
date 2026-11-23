package com.example.inventoryapp.feature.product.domain.usecase

import com.example.inventoryapp.feature.product.domain.model.Product
import com.example.inventoryapp.feature.product.domain.repository.ProductRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(product: Product) {
        require(product.name.isNotBlank()) {
            "Nombre obligatorio"
        }

        require(product.name.length <= 80) {
            "Maximo 80 caracteres"
        }

        require(product.description.length <= 500) {
            "Maximo 500 caracteres"
        }

        require(product.priceCop > 0) {
            "El precio debe ser positivo"
        }

        require(product.quantity >= 0) {
            "Cantidad invalida"
        }

        repository.save(product)
    }
}