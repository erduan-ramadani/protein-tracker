package com.ercoding.proteintracker.data.remote

import com.ercoding.proteintracker.domain.AnthropicInterface

class FakeAnthropicRepository(private val returnValue: Int = 35) : AnthropicInterface {
    override suspend fun requestProteinAmount(query: String): Result<Int> {
        return Result.success(returnValue)
    }
}