plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
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
    resValue("string", "google_map_key", "AIzaSyAU9u-OLQ-tRYAVcs_q1z8HAs4sgHBkTx0")

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
  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
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
  implementation(libs.androidx.material3)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.tooling.preview)

  //Navigation
  implementation(libs.navigation.compose)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.dynamic.features.fragment)
  androidTestImplementation(libs.androidx.navigation.testing)


  //compose
  implementation(libs.androidx.activity)

  //map
  implementation(libs.play.services.maps)
  implementation(libs.maps.compose)

  implementation(libs.accompanist.systemuicontroller)


  //Chuck
  debugImplementation(libs.library)
  releaseImplementation(libs.library.no.op)

  //glance
  implementation("androidx.glance:glance:1.0.0-alpha05")
  implementation("androidx.glance:glance-appwidget:1.0.0-alpha05")


  //Gif
  implementation("com.google.accompanist:accompanist-drawablepainter:0.35.0-alpha")


}