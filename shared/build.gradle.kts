plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        tvosX64(),
        tvosSimulatorArm64(),
        tvosArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
//            implementation(libs.androidx.lifecycle.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        iosMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        val nonJvmMain by creating {
            dependsOn(commonMain.get())
            dependsOn(nativeMain.get())
            dependencies {
            }
        }

        tvosMain {
            dependsOn(nonJvmMain)
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.options.freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }
}

android {
    namespace = "dev.tonholo.lifecycle_viewmodel_tvos_not_working.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
