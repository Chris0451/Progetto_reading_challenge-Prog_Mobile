import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    // Firebase
    alias(libs.plugins.gms.google.services) // com.google.gms.google-services
}

android {
    namespace = "com.project.reading_challenge"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.project.reading_challenge"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProps = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        val booksApiKey: String = localProps.getProperty("BOOKS_API_KEY") ?: ""

        buildConfigField("String", "BOOKS_API_KEY", "\"$booksApiKey\"")
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
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Networking
    implementation(libs.retrofit)              // 3.0.0
    implementation(libs.converter.moshi)       // 3.0.0
    implementation(libs.logging.interceptor)   // 5.1.0

    // Hilt (KSP)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Images
    implementation(libs.coil.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // BoM: non mettere versioni sui moduli Firebase
    implementation(platform(libs.firebase.bom))

    // SDK Firebase (senza -ktx dal BoM 34+)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Coroutines helpers per Task.await()
    implementation(libs.kotlinx.coroutines.play.services)

    // Aggiungi esplicitamente la stdlib della stessa versione del plugin Kotlin
    implementation(kotlin("stdlib"))

    // Blocca qualsiasi tentativo di portarti alla 2.2.x
    constraints {
        val kotlinVer = libs.versions.kotlin.get()   // deve essere "2.0.21"
        listOf(
            "org.jetbrains.kotlin:kotlin-stdlib",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        ).forEach { m ->
            implementation("$m:$kotlinVer") {
                version { strictly(kotlinVer) }
                because("Allinea la stdlib alla versione del compilatore Kotlin ($kotlinVer)")
            }
        }
    }
    allprojects {
        configurations.all {
            resolutionStrategy.eachDependency {
                if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                    useVersion(libs.versions.kotlin.get()) // "2.0.21"
                    because("Forzo stdlib alla versione del plugin Kotlin")
                }
            }
        }
    }
}