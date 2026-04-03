plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(projects.composeApp)

    implementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)

    implementation(libs.koin.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.core.splashscreen)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //api command is like implementation command, but extends the usage of the lib to the modules that depends on this module
    api(libs.play.feature.delivery)
    api(libs.play.feature.delivery.ktx)
    api(libs.play.review)
    api(libs.play.review.ktx)
    api(libs.play.app.update)
    api(libs.play.app.update.ktx)
    api(libs.play.asset.delivery)
    api(libs.play.asset.delivery.ktx)

    //Test
    implementation(libs.mockk)
}