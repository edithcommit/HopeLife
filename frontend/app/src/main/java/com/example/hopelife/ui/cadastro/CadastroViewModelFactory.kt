package com.example.hopelife.ui.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hopelife.data.repository.AuthRepository

class CadastroViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CadastroViewModel::class.java)) {
            return CadastroViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecido: ${modelClass.name}"
        )
    }
}