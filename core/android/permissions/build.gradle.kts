plugins {
    id(ThunderbirdPlugins.Library.android)
}

android {
    namespace = "app.k9mail.core.android.permissions"
}

dependencies {
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.accompanist.permission)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.assertk)
}
