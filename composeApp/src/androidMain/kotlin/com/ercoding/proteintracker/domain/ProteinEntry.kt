package com.ercoding.proteintracker.domain

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProteinEntry(
    val id: String = UUID.randomUUID().toString(),
    val meal: String,
    val proteinAmount: Int
)