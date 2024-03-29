plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.github.goutarouh.notionboost.repository"
    compileSdk = 34

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":data"))
    testImplementation(project(":test"))

    testImplementation("junit:junit:4.13.2")

    // Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // DataStore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-work:1.1.0")
    ksp("com.google.dagger:hilt-compiler:2.49")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    testImplementation("com.google.dagger:hilt-android-testing:2.49")
    kspTest("com.google.dagger:hilt-compiler:2.49")
    kspTest("androidx.hilt:hilt-compiler:1.1.0")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.49")

    // Test
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Coroutine
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}