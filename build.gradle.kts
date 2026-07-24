plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.spring") version "2.4.0"
    // mappie插件，插件版本号采用 {kotlin.version}-{mappie.version} 的结构，mappie版本前缀必须与kotlin版本保持一致
    id("tech.mappie.plugin") version "2.4.0-2.4.2"
    id("org.springframework.boot") version "4.1.0"
    // 等价于 maven 中的 dependencyManagement BOM，让dependencies中引入的库不用显示指定版本号（如果BOM提供）
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.mao"
version = "1.0.0"
description = "mao-app"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // kotlin协程构建器扩展，让它更好用
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // kotlin协程构建器，与webflux桥梁
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // 让 Jackson 能正确识别 kotlin 的 data class
    implementation("tools.jackson.module:jackson-module-kotlin")
    // mappie api
    implementation("tech.mappie:mappie-api:2.4.0-2.4.2")
    // runtimeOnly("org.postgresql:postgresql")
    // runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.asyncer:r2dbc-mysql")
    testImplementation("org.springframework.boot:spring-boot-starter-data-r2dbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
