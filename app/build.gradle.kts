import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

fun loadProperties(file: File): Properties {
    val properties = Properties()
    file.inputStream().use { properties.load(it) }
    return properties
}

val secretsFile = rootProject.file("secret.properties")
val secrets = loadProperties(secretsFile)

android {
    namespace = "com.ukrdroiddev.carchooser"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ukrdroiddev.carchooser"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        android.buildFeatures.buildConfig = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String", "ACCESS_TOKEN", "\"${secrets.getProperty("ACCESS_TOKEN")}\""
        )
        buildConfigField(
            "String", "BASE_URL", "\"${secrets.getProperty("BASE_URL")}\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Modules
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    //Koin
    implementation(libs.koin.android)

    //Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
}