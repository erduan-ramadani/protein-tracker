package com.ercoding.proteintracker.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProteinEntry(
    val meal: String,
    val proteinAmount: Int
)