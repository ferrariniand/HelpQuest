package com.helpquest.core.presentation.util

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

const val EARTH_RADIUS = 6371000.0 //meters

fun Double.toRadians() = this * PI / 180

fun distanceTo(
    latitude: Double,
    otherLatitude: Double,
    longitude: Double,
    otherLongitude: Double
): Double {
    val dLat = (otherLatitude - latitude).toRadians()
    val dLon = (otherLongitude - longitude).toRadians()

    val a = sin(dLat / 2).pow(2) +
            cos(latitude.toRadians()) *
            cos(otherLatitude.toRadians()) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return EARTH_RADIUS * c
}