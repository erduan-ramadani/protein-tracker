package com.ercoding.proteintracker.data.remote

import com.ercoding.proteintracker.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AnthropicRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun requestProteinAmount(query: String): Result<Int> {
        val anthropicQuery =
            "Wie viel Gramm Eiweiß enthält folgende Mahlzeit: $query. Wenn die Menge unklar ist, schätze eine typische Portion. Antworte nur mit einer ganzen Zahl, ohne Einheit oder Erklärung."

        return runCatching {
            client.post(BuildConfig.BASE_URL) {
                contentType(ContentType.Application.Json)
                header("x-api-key", BuildConfig.API_KEY)
                header("anthropic-version", "2023-06-01")
                setBody(
                    MessageRequest(
                        model = "claude-sonnet-4-6",
                        max_tokens = 1024,
                        messages = listOf(
                            Message(
                                role = "user",
                                content = anthropicQuery
                            )
                        )
                    )
                )
            }.body<MessageResponse>()
                .content.firstOrNull()?.text?.toIntOrNull() ?: 0
        }
    }
}