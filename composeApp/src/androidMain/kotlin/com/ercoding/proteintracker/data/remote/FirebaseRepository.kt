package com.ercoding.proteintracker.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await


class FirebaseRepository {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private var apiKey: String = ""

    suspend fun fetchAnthropicApiKey(): String {
        if (apiKey.isBlank()) {
            remoteConfig.fetchAndActivate().await()
            apiKey = remoteConfig.getString("anthropicApiKey")
        }
        return apiKey
    }
}