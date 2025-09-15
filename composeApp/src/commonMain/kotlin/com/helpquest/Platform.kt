package com.helpquest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform