import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ingegneriasoftware.ecoquest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ingegneriasoftware.ecoquest"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    // Base AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // UI e Material Design
    implementation(libs.material3)
    implementation(libs.androidx.ui.text.google.fonts)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Dependency Injection - Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.common.ktx)
    kapt(libs.hilt.android.compiler)

    // Supabase SDK
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.realtime)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.functions)

    // Navigazione
    implementation(libs.androidx.navigation.compose)

    // Serializzazione JSON
    implementation(libs.kotlinx.serialization.json)

    // Networking - Ktor
    implementation(libs.ktor.client.android)

    // Gestione immagini - Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // CameraX (Fotocamera)
    implementation(libs.androidx.camera.core.v141)
    implementation(libs.androidx.camera.camera2.v141)
    implementation(libs.androidx.camera.lifecycle.v141)
    implementation(libs.androidx.camera.view.v141)
    implementation(libs.androidx.camera.extensions.v141)

    // Icone Material
    implementation(libs.androidx.material.icons.core)
    implementation(libs.material.icons.extended)

    //gemini
    implementation(libs.generativeai)
}

// Permette riferimenti a codice generato
kapt {
    correctErrorTypes = true
}

configurations.all {
    resolutionStrategy.force("io.ktor:ktor-client-okhttp:2.3.2")
}
