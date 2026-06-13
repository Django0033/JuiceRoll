plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp) // KSP BEFORE Hilt
    alias(libs.plugins.hilt) // Hilt AFTER KSP
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.juiceroll"
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "com.juiceroll"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val propsFile = rootProject.file("keystore.properties")
            if (propsFile.exists()) {
                val lines = propsFile.readLines().associate {
                    val parts = it.split("=", limit = 2)
                    parts[0].trim() to parts.getOrElse(1) { "" }.trim()
                }
                storeFile = rootProject.file(lines["storeFile"] ?: "")
                storePassword = lines["storePassword"]
                keyAlias = lines["keyAlias"]
                keyPassword = lines["keyPassword"]
            } else {
                storeFile = System.getenv("KEYSTORE_PATH")?.let { rootProject.file(it) }
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            // Only wire signing if credentials are available (local or CI)
            val hasSigning = rootProject.file("keystore.properties").exists() ||
                System.getenv("KEYSTORE_PASSWORD") != null
            if (hasSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kover {
    reports {
        filters {
            excludes {
                classes("com.juiceroll.BuildConfig")
                classes("com.juiceroll.Hilt_*")
                classes("com.juiceroll.*_Factory*")
                classes("com.juiceroll.*_GeneratedInjector*")
                classes("com.juiceroll.*_HiltModules*")
                classes("com.juiceroll.*_HiltComponents*")
                classes("com.juiceroll.*_Module*")
            }
        }

        total {
            xml {
                onCheck = true
            }

            html {
                onCheck = true
            }

            verify {
                onCheck = true

                rule {
                    minBound(80)
                }
            }
        }
    }
}

dependencies {
    // Compose BOM
    implementation(platform(libs.compose.bom))

    // Compose (versioned by BOM)
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons.extended)
    implementation(libs.compose.text.google.fonts)
    debugImplementation(libs.compose.debug.tooling)

    // Activity & Lifecycle
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Core (existing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
