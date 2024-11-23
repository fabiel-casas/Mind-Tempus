import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.github.triplet.play)
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("key.properties")
// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()
// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "virtus.synergy.mindtempus"
    compileSdk = 35
    val appVersionCode: Int by rootProject.extra

    defaultConfig {
        applicationId = "virtus.synergy.mindtempus"
        minSdk = 28
        targetSdk = 35
        versionCode = appVersionCode
        versionName = "0.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
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

play {
    serviceAccountCredentials.set(file("mind-tempus.json"))
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