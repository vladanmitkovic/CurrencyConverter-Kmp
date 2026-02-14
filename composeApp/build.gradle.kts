import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
}

kotlin {

    // Suppress expect/actual classes Beta warning (applies globally)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidLibrary {
        namespace = "me.mitkovic.kmp.currencyconverter.shared"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        // Enable Android resources for the shared module (needed for Compose Multiplatform)
        androidResources {
            enable = true
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
        val androidMain by getting

        commonMain.dependencies {
            // Compose & UI
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.components.uiToolingPreview)
            implementation(libs.compose.material.iconsExtended)

            // AndroidX & Lifecycle
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.composeVM)
            implementation(libs.koin.annotations)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Ktor (shared)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        androidMain.dependencies {
            // Koin Android
            implementation(libs.koin.android)
            // DataStore
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore.core)
            // Timber
            implementation(libs.timber)

            // Ktor engine + logging
            implementation(libs.ktor.client.okhttp)
            implementation(libs.okhttp.logging)
        }

        iosMain.dependencies {
            // Ktor engine for iOS
            implementation(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // Ktor engine for Desktop
            implementation(libs.ktor.client.cio)
        }

        // KSP generated sources
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)

    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosX64", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
    add("kspDesktop", libs.koin.ksp.compiler)

    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
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

room {
    schemaDirectory("$projectDir/schemas")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

// Make Kotlin compilation depend on KSP metadata
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// KSP task dependencies - ensure proper ordering
tasks.withType<KspAATask>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
