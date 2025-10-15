package org.warehouse.app.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.warehouse.app.model.Fornecedor
import org.warehouse.app.model.User

object ApiContext {

    private const val BASE_URL = "http://72.60.50.138:8080"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getUsers(): List<User> {
        return client.get("$BASE_URL/user") {
            contentType(ContentType.Application.Json)
        }.body<List<User>>()
    }

    suspend fun createUser(user: User): User {
        return client.post("$BASE_URL/user") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
    }

    suspend fun deleteUser(id: Int) {
        try {
            val response = client.delete("$BASE_URL/user/$id") {
                contentType(ContentType.Application.Json)
            }
            if (!response.status.isSuccess()) {
                println("Erro ao deletar usuário: ${response.status}")
            } else {
                println("Usuário deletado com sucesso: $id")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun updateUser(id: Int, user: User): User? {
        return try {
            val response = client.patch("$BASE_URL/user/$id") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            if (response.status.isSuccess()) {
                response.body()
            } else {
                println("Erro: ${response.status}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = ApiContext.client.post("${ApiContext.BASE_URL}/login") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("email" to email, "password" to password))
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getFornecedores(): List<Fornecedor> {
        return client.get("$BASE_URL/fornecedor") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun createFornecedor(fornecedor: Fornecedor): Fornecedor {
        return client.post("$BASE_URL/fornecedor") {
            contentType(ContentType.Application.Json)
            setBody(fornecedor)
        }.body()
    }

    suspend fun updateFornecedor(id: Int, fornecedor: Fornecedor): Fornecedor? {
        return try {
            val response = client.put("$BASE_URL/fornecedor/$id") {
                contentType(ContentType.Application.Json)
                setBody(fornecedor)
            }
            if (response.status.isSuccess()) {
                response.body()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteFornecedor(id: Int) {
        try {
            val response = client.delete("$BASE_URL/fornecedor/$id") {
                contentType(ContentType.Application.Json)
            }
            if (!response.status.isSuccess()) {
                println("Erro ao deletar fornecedor: ${response.status}")
            } else {
                println("Fornecedor deletado com sucesso: $id")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}
