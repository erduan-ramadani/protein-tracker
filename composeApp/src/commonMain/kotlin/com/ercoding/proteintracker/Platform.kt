package com.ercoding.proteintracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform