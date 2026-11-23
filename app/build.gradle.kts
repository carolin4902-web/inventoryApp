plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.inventoryapp"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.inventoryapp"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf(
                    "androidx.**",
                    "kotlin.**",
                    "kotlinx.**"
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    val roomVersion = "2.8.5"
    val lifecycleVersion = "2.11.1"
    val navigation3Version = "1.2.2"
    val hiltVersion = "2.60.1"

    // Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.compose.foundation:foundation")

    // Lifecycle y Navigation 3
    implementation(
        "androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion"
    )
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    )
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-navigation3:$lifecycleVersion"
    )
    implementation(
        "androidx.navigation3:navigation3-runtime:$navigation3Version"
    )
    implementation(
        "androidx.navigation3:navigation3-ui:$navigation3Version"
    )
    implementation(
        "androidx.hilt:hilt-lifecycle-viewmodel-compose:1.4.0"
    )

    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")

    // Firebase
    implementation(
        platform("com.google.firebase:firebase-bom:35.2.0")
    )
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.11.0"
    )
    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0"
    )

    // Pruebas
    testImplementation(libs.junit)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )
    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )
    androidTestImplementation(
        libs.androidx.espresso.core
    )
    androidTestImplementation(
        libs.androidx.junit
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}
