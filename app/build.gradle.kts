plugins {
    alias(libs.plugins.kotlin.android)
    id("kotlin-android")
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tulonglegal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tulonglegal"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)

    // Exclude support-compat from compact-calendar-view if it's being pulled in
    implementation("com.github.sundeepk:compact-calendar-view:3.0.0") {
        exclude("com.android.support", "support-compat")
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
