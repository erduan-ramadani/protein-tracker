package com.ercoding.proteintracker.domain

interface AnthropicInterface {
    suspend fun requestProteinAmount(query: String): Result<Int>
}