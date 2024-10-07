plugins {
    id(ThunderbirdPlugins.App.androidCompose)
    alias(libs.plugins.dependency.guard)
    id("thunderbird.quality.badging")
}

val testCoverageEnabled: Boolean by extra
if (testCoverageEnabled) {
    apply(plugin = "jacoco")
}

dependencies {
    implementation(projects.appCommon)
    implementation(projects.core.ui.compose.theme2.k9mail)
    implementation(projects.core.ui.legacy.theme2.k9mail)
    implementation(projects.feature.launcher)

    implementation(projects.legacy.core)
    implementation(projects.legacy.ui.legacy)

    implementation(projects.feature.widget.messageList)
    implementation(projects.feature.widget.shortcut)
    implementation(projects.feature.widget.unread)

    implementation(libs.androidx.work.runtime)

    implementation(projects.feature.autodiscovery.api)
    debugImplementation(projects.backend.demo)
    debugImplementation(projects.feature.autodiscovery.demo)

    testImplementation(libs.robolectric)

    // Required for DependencyInjectionTest to be able to resolve OpenPgpApiManager
    testImplementation(projects.plugins.openpgpApiLib.openpgpApi)
    testImplementation(projects.feature.account.setup)
}

android {
    namespace = "com.fsck.k9"

    dataBinding{
        enable = true
    }

    defaultConfig {
        applicationId = "com.emailclient.mailchecker.outlook.hotmail"
//        applicationId = "com.hungbang.email2018"
        testApplicationId = "email.swift.ai.smart.allmail"

        versionCode = 216
        versionName = "216"

        // Keep in sync with the resource string array "supported_languages"
        resourceConfigurations.addAll(
            listOf(
                "ar",
                "be",
                "bg",
                "br",
                "ca",
                "co",
                "cs",
                "cy",
                "da",
                "de",
                "el",
                "en",
                "en_GB",
                "eo",
                "es",
                "et",
                "eu",
                "fa",
                "fi",
                "fr",
                "fy",
                "gd",
                "gl",
                "hr",
                "hu",
                "in",
                "is",
                "it",
                "iw",
                "ja",
                "ko",
                "lt",
                "lv",
                "ml",
                "nb",
                "nl",
                "pl",
                "pt_BR",
                "pt_PT",
                "ro",
                "ru",
                "sk",
                "sl",
                "sq",
                "sr",
                "sv",
                "tr",
                "uk",
                "vi",
                "zh_CN",
                "zh_TW",
            ),
        )

        buildConfigField("String", "CLIENT_INFO_APP_NAME", "\"Easy Mail\"")
    }

    signingConfigs {
        if (project.hasProperty("swiftmail.keyAlias") &&
            project.hasProperty("swiftmail.keyPassword") &&
            project.hasProperty("swiftmail.storeFile") &&
            project.hasProperty("swiftmail.storePassword")
        ) {
            create("release") {
                keyAlias = project.property("swiftmail.keyAlias") as String
                keyPassword = project.property("swiftmail.keyPassword") as String
                storeFile = file(project.property("swiftmail.storeFile") as String)
                storePassword = project.property("swiftmail.storePassword") as String
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isShrinkResources = true
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            signingConfig = signingConfigs.findByName("release")
            enableUnitTestCoverage = testCoverageEnabled
            enableAndroidTestCoverage = testCoverageEnabled

            isMinifyEnabled = true
            isShrinkResources = false
            isDebuggable = true

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packaging {
        jniLibs {
            excludes += listOf("kotlin/**")
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) {
        it.packaging.resources.excludes.addAll(
            "META-INF/*.kotlin_module",
            "META-INF/*.version",
            "META-INF/**",
            "kotlin/**",
            "DebugProbesKt.bin",
        )
    }
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
