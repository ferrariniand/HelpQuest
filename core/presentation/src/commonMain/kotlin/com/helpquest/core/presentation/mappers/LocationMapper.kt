package com.helpquest.core.presentation.mappers

import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.presentation.modelsUi.Location

fun GeoLocation.toLocation(): Location = Location(latitude, longitude)

fun Location.toGeoLocation(): GeoLocation = GeoLocation(latitude, longitude)