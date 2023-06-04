plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.core.music"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:repository"))

    implementation(libs.androidx.media.common)
    implementation(libs.androidx.media.session)
    implementation(libs.androidx.media.exoplayer)
}