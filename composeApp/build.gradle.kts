import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "1.9.10"
}

kotlin {
    jvm()

    sourceSets {
        val ktorVersion = "3.0.0"

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.androidx.navigation.runtime.desktop)

            implementation("io.ktor:ktor-client-cio:$ktorVersion")

            implementation("org.slf4j:slf4j-simple:2.0.13")
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.warehouse.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)

            packageName = "WarehouseApp"
            packageVersion = "1.0.0"
            description = "Aplicativo de gerenciamento de estoque"
            copyright = "© 2025"

            windows {
                iconFile.set(project.file("src/jvmMain/resources/icons/appIcon.ico"))
                menuGroup = "Warehouse Software"
                shortcut = true
                includeAllModules = true
            }

            // macOS específico
            macOS {
                iconFile.set(project.file("src/desktopMain/resources/icons/appIcon.icns"))
                packageName = "WarehouseApp"
            }

            // Linux específico
            linux {
                iconFile.set(project.file("src/desktopMain/resources/icons/appIcon.png"))
                packageName = "WarehouseApp"
            }
        }
    }
}
