plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.aslansoft.deneme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aslansoft.deneme"
        minSdk = 30
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 13
        versionName = "1.1.3-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.tv:tv-material:1.0.0-alpha10")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.2")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database")

    //Navigation
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    //Painter
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    //barcode
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
    // CameraX core library using the camera2 implementation

    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:1.4.0-alpha04")
    implementation("androidx.camera:camera-camera2:1.4.0-alpha04")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:1.4.0-alpha04")
    // If you want to additionally use the CameraX VideoCapture library
    implementation("androidx.camera:camera-video:1.4.0-alpha04")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:1.4.0-alpha04")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:1.4.0-alpha04")
    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    //SplashScreen
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
}