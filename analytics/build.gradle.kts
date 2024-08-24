plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "virtus.synergy.analytics"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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

    implementation(libs.coreKtx)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAnalyticsKtx)
    implementation(libs.firebaseCrashlyticsKtx)
    implementation(libs.koinCore)
    implementation(platform(libs.composeBom))
    implementation(libs.composeUi)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterial3WindowSizeClass)
}