package com.ercoding.proteintracker.data.remote

import com.ercoding.proteintracker.domain.AnthropicInterface

class FakeAnthropicRepository : AnthropicInterface {
    override suspend fun requestProteinAmount(query: String): Result<Int> {
        return Result.success(35)
    }
}