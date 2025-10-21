buildscript {
    dependencies {
        classpath(libs.kotlinGradlePlugin)
    }
}
apply("app_version_code.gradle.kts")
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

subprojects {
    afterEvaluate { // Ensure this runs after the android plugin has been applied
        project.extensions.findByType(com.android.build.gradle.BaseExtension::class.java)?.apply {
            compileSdkVersion(36) // Or your desired API level
        }
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        // This block configures any module with the Kotlin Android plugin.
        // We need to get the 'kotlin' extension first.
        val kotlinExtension =
            extensions.getByType<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension>()
        kotlinExtension.apply {
            // Set the JVM toolchain for Kotlin, which also aligns Java compilation.
            // Let's set it to 17 to match your original Kotlin target.
            jvmToolchain(21)
        }
    }
}