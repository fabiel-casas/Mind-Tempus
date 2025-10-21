plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "virtus.synergy.design_system"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
}

dependencies {
    implementation(project(mapOf("path" to ":analytics")))
    implementation(libs.appCompat)
    implementation(libs.coreKtx)

    implementation(libs.composeMarkdown)
    implementation(platform(libs.composeBom))
    implementation(libs.composeCoil)
    implementation(libs.composeNavigation)
    implementation(libs.composeUi)
    implementation(libs.composeConstraintLayout)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.material)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}