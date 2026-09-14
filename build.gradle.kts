import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
import io.izzel.taboolib.gradle.Basic
import io.izzel.taboolib.gradle.BukkitNMS
import io.izzel.taboolib.gradle.BukkitNMSItemTag
import io.izzel.taboolib.gradle.BukkitNMSUtil
import io.izzel.taboolib.gradle.BukkitUtil
import io.izzel.taboolib.gradle.Bukkit
import io.izzel.taboolib.gradle.Kether


plugins {
    java
    id("io.izzel.taboolib") version "2.0.38"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

taboolib {
    env {
        install(Basic)
        install(BukkitNMS)
        install(BukkitNMSItemTag)
        install(BukkitNMSUtil)
        install(BukkitUtil)
        install(Bukkit)
        install(Kether)
    }
    description {
        name = "CloudNebula"
        desc("高版本物品库")
        contributors {
            name("BalanceSea")
        }
        links {
            name("docs.balancesea.cn")
        }
    }
    version { taboolib = "6.3.0-75b18a2" }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JVM_1_8)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}