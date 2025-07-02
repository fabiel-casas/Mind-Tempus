plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "virtus.synergy.core"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    implementation(libs.accompanistPermissions)
    implementation(libs.appCompat)
    implementation(libs.coreKtx)
    implementation(libs.coroutinesAndroid)
    implementation(libs.lifecycleRuntimeKtx)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseCrashlyticsKtx)
}