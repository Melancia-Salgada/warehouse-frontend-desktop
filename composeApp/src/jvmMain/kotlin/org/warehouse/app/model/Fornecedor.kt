package org.warehouse.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Fornecedor(
    val id: Int? = null,
    val nome: String? = null,
    val cnpj: String? = null,
    val telefone: String? = null,
    val email: String? = null,
    val dataCriacao: String? = null
)