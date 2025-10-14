package org.warehouse.app.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val typeAccess: String,
    val password: String
)
