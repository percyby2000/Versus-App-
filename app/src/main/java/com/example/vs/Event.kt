package com.example.vs

data class Event(
    val id: String = "",
    val sportType: String = "",
    val eventName: String = "",
    val duration: String = "",
    val price: Double = 0.0,
    val location: String = "",
    val creatorId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)