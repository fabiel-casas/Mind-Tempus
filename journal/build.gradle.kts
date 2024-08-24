@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "virtus.synergy.journal"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}

dependencies {

    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    annotationProcessor(libs.roomCompiler)
    kapt(libs.roomCompiler)

    implementation(project(mapOf("path" to ":analytics")))
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":design_system")))

    implementation(libs.accompanistPermissions)
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
    implementation(libs.coroutinesAndroid)
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.gson)
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