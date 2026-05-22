package com.example.simplecharts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform