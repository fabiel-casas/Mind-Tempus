@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "virtus.synergy.mindtempus"
    compileSdk = 33

    defaultConfig {
        applicationId = "virtus.synergy.mindtempus"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.activityCompose)
    implementation(project(mapOf("path" to ":analytics")))
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":design_system")))
    implementation(project(mapOf("path" to ":journal")))

    implementation(libs.accompanistPermissions)
    implementation(libs.appCompat)
    implementation(platform(libs.composeBom))
    implementation(libs.composeNavigation)
    implementation(libs.composeUi)
    implementation(libs.composeConstraintLayout)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.coreKtx)
    implementation(libs.coroutinesAndroid)
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.workRuntimeKtx)
    implementation(libs.serializationJson)
    implementation(libs.koinCore)
    implementation(libs.koinAndroidNavigation)
    implementation(libs.koinAndroidxCompose)

    //Ads
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAnalyticsKtx)
    implementation(libs.firebaseCrashlyticsKtx)

    testImplementation(libs.junit)
    testImplementation(libs.koinTestJunit4)

    androidTestImplementation(libs.androidxTestJunit)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.composeUiTestJunit4)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}