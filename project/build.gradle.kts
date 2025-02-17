import org.jetbrains.kotlin.com.intellij.util.text.VersionComparatorUtil
import xyz.jpenilla.runtask.task.AbstractRun
import java.util.Calendar

plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.release.axion)

    alias(libs.plugins.paperweight)

    alias(libs.plugins.paper.convention)
    alias(libs.plugins.paper.run)

    alias(libs.plugins.shadow)

    jacoco

    `maven-publish`

    alias(libs.plugins.dokka)
}

scmVersion {
    tag {
        prefix = ""
    }
}

version = scmVersion.version

paperPluginYaml {
    name = property("name.display") as String
    description = project.description

    authors = listOfNotNull((property("authors") as String).split(",").joinToString(transform = String::trim))

    setVersion(project.version)

    website = property("homepage.url") as String

    apiVersion = property("minecraft.version") as String

    main = "${project.group}.${property("package")}.${name.get()}Plugin"
    bootstrapper = "${project.group}.${property("package")}.bootstrap.${name.get()}PluginBootstrap"
}

repositories {
    mavenCentral()

    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("${paperPluginYaml.apiVersion.get()}-R0.1-${VersionComparatorUtil.VersionTokenType.SNAPSHOT.name}")

    implementation(libs.utility)

    testRuntimeOnly(libs.junit.platform)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jetbrains)

    testImplementation(libs.kotlin.mock)

    testImplementation(libs.bukkit.mock)
}

paperweight {
    addServerDependencyTo = configurations.named(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).map { setOf(it) }
}

// Apply a specific Java toolchain to ease working on different environments.
kotlin {
    jvmToolchain((property("java.version") as String).toInt())
}

java {
    withSourcesJar()
}

tasks {
    withType<Jar> {
        archiveBaseName = rootProject.name
    }

    shadowJar {
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }

    withType<AbstractRun> {
        javaLauncher = project.javaToolchains.launcherFor {
            vendor = JvmVendorSpec.JETBRAINS
            languageVersion = JavaLanguageVersion.of((project.property("java.version") as String).toInt())
        }

        jvmArgs("-XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=core")
    }

    jacocoTestCoverageVerification {
        dependsOn(test)

        violationRules {
            rule {
                limit {
                    minimum = 1.0.toBigDecimal()
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = rootProject.name
            version = version.toString()
        }
    }
}

dokka {
    moduleName = property("name.display") as String

    dokkaSourceSets.main {
        sourceLink {
            val remoteVersion = if ((version as String).endsWith(VersionComparatorUtil.VersionTokenType.SNAPSHOT.name)) "main" else version

            localDirectory.set(File(property("kotlin.directory") as String))
            remoteUrl("${property("source.url.prefix")}${remoteVersion}/${localDirectory.get().asFile.relativeTo(rootProject.rootDir)}")
        }
    }

    pluginsConfiguration.html {
        homepageLink = property("homepage.url") as String

        footerMessage.set("© ${Calendar.getInstance().get(Calendar.YEAR)} ${property("authors")}")
    }
}
