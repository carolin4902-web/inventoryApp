package com.example.inventoryapp.feature.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.feature.product.domain.model.Product
import com.example.inventoryapp.feature.product.domain.repository.ProductRepository
import com.example.inventoryapp.feature.product.domain.usecase.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val saveProductUseCase: SaveProductUseCase
) : ViewModel() {

    val products: StateFlow<List<Product>> =
        repository.observeProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            saveProductUseCase(product)
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }
}