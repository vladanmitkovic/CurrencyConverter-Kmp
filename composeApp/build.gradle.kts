import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            // Compose & UI
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // AndroidX & Lifecycle
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.composeVM)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            api(libs.sqldelight.coroutines)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Ktor (shared)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Icons
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            // Android UI
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Koin Android
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // Timber & DataStore
            implementation(libs.timber)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore.core)

            // SQLDelight Android driver
            implementation(libs.sqldelight.android.driver)

            // Ktor engine + logging
            implementation(libs.ktor.client.okhttp)
            implementation(libs.okhttp.logging)
        }

        iosMain.dependencies {
            implementation(libs.koin.core)

            // Ktor engine for iOS
            implementation(libs.ktor.client.darwin)

            // SQLDelight native driver
            implementation(libs.sqldelight.native.driver)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.koin.core)

            // Ktor engine for Desktop
            implementation(libs.ktor.client.cio)

            // SQLDelight SQLite driver
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}

android {
    namespace = "me.mitkovic.kmp.currencyconverter"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "me.mitkovic.kmp.currencyconverter"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "me.mitkovic.kmp.currencyconverter.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "me.mitkovic.kmp.currencyconverter"
            packageVersion = "1.0.0"

            modules("java.sql")
        }
    }
}

// SQLDelight configuration
sqldelight {
    databases {
        create("CurrencyConverterDatabase") {
            packageName.set("me.mitkovic.kmp.currencyconverter.data.local.database")
        }
    }
}
