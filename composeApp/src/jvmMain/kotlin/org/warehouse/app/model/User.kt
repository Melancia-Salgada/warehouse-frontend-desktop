package org.warehouse.app.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val typeAccess: String? = null,
    val password: String? = null,
    val status: Boolean? = null,
    val dataCriacao: String? = null
)
