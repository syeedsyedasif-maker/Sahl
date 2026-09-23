import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Releases take their version from the git tag: the release workflow passes
// -Psahl.version=0.1.0 (or 0.1.0-rc.1). Everyday builds use the fallback in defaultConfig.
val releaseVersion: String? = providers.gradleProperty("sahl.version").orNull

// The release signing key comes from environment variables and is never committed.
// See "Releases" in the README.
val releaseKeystore: String? = providers.environmentVariable("SAHL_KEYSTORE_PATH").orNull

android {
    namespace = "com.sahl.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.sahl.app"
        minSdk = 24 // See "Device support" in the README before changing.
        targetSdk = 37
        versionCode = releaseVersion?.let(::versionCodeFor) ?: 1
        versionName = releaseVersion ?: "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (releaseKeystore != null) {
            create("release") {
                storeFile = file(releaseKeystore)
                storePassword = providers.environmentVariable("SAHL_KEYSTORE_PASSWORD").get()
                keyAlias = providers.environmentVariable("SAHL_KEY_ALIAS").get()
                keyPassword = providers.environmentVariable("SAHL_KEY_PASSWORD").get()
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            // Without the key the release APK is built unsigned, and Android won't install it.
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

/**
 * Turns a release version into Android's versionCode, which must grow with every release or
 * phones refuse the update.
 *
 * MAJOR.MINOR.PATCH becomes MAJOR·1,000,000 + MINOR·10,000 + PATCH·100 + 99. A pre-release,
 * MAJOR.MINOR.PATCH-label.N, uses N instead of 99, so it always sorts before the release it
 * leads up to: 0.1.0-rc.1 → 10001, 0.1.0 → 10099, 0.1.1 → 10199.
 */
fun versionCodeFor(version: String): Int {
    val match = Regex("""(\d+)\.(\d+)\.(\d+)(?:-[A-Za-z]+\.(\d+))?""").matchEntire(version)
        ?: error("Release version \"$version\" must look like 1.2.3 or 1.2.3-rc.4")
    val (major, minor, patch, preRelease) = match.destructured
    require(minor.toInt() < 100 && patch.toInt() < 100) {
        "Minor and patch must be below 100 to fit the versionCode scheme: $version"
    }
    require(preRelease.isEmpty() || preRelease.toInt() in 1..98) {
        "Pre-release number must be between 1 and 98: $version"
    }
    val build = if (preRelease.isEmpty()) 99 else preRelease.toInt()
    return major.toInt() * 1_000_000 + minor.toInt() * 10_000 + patch.toInt() * 100 + build
}
