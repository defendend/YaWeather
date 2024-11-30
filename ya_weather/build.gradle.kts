plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  kotlin("kapt")
  kotlin("plugin.serialization") version "2.0.20"
}

android {
  namespace = "com.yandex.yaweather"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.yandex.yaweather"
    minSdk = 28
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  //network
  implementation(libs.retrofit)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.retrofit2.kotlinx.serialization.converter)
  implementation(libs.okhttp)
  implementation(libs.logging.interceptor)

  //dagger
  implementation(libs.dagger)
  kapt(libs.dagger.compiler)
  implementation(libs.dagger.android)
  kapt(libs.dagger.android.processor)

  //coroutines
  implementation(libs.kotlinx.coroutines.android)

  //ui
  implementation (libs.androidx.material3)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.material)
  implementation (libs.androidx.ui.tooling.preview)

  //compose
  implementation(libs.androidx.activity)
  implementation(libs.androidx.navigation.compose)
}