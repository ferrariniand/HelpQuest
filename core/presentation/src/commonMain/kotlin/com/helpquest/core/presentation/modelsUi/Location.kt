package com.helpquest.core.presentation.modelsUi

import com.helpquest.core.presentation.util.distanceTo


//TODO use expect to distinguish between android.location.Location and CLLocation on iOS
class Location(
    val latitude: Double,
    val longitude: Double
) {
    fun distanceTo(other: Location): Double {
        return distanceTo(
            latitude,
            longitude,
            other.latitude,
            other.longitude
        )
    }
}

