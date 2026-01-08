import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
    id("org.openapi.generator") version "7.2.0"
}

group = "org.bazar"
version = "1.0.0"
description = "bazar-persona"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.38")
    implementation("io.swagger.core.v3:swagger-models:2.2.38")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/bazar-persona.yaml")
    outputDir.set("${layout.buildDirectory.locationOnly.get()}/generated/openapi")

    // Packages for generated code
    apiPackage.set("org.bazar.persona.api")
    modelPackage.set("org.bazar.persona.model")
    typeMappings.set(mapOf(
        "DateTime" to "java.time.Instant"
    ))
    configOptions.set(mapOf(
        // 1. Fixes javax -> jakarta (CRITICAL for Spring Boot 3)
        "useSpringBoot3" to "true",

        // 2. Generates 'interface UsersApi' instead of 'class UsersApiController'
        "interfaceOnly" to "true",

        // 3. Removes DefaultExceptionHandler and ApiException classes
        "exceptionHandler" to "false",

        // 4. Removes the "Default" impl methods in the interface
        "skipDefaultInterface" to "true",
        "dateLibrary" to "java8",
        // Standard settings
        "useTags" to "true",
        "useBeanValidation" to "true",
        "documentationProvider" to "springdoc",
        "gradleBuildFile" to "false"
    ))
}

// 4. TELL KOTLIN WHERE TO FIND THE GENERATED CODE
sourceSets {
    main {
        kotlin.srcDir("${layout.buildDirectory.locationOnly.get()}/generated/openapi/src/main/kotlin")
    }
}

// 5. ENSURE CODE IS GENERATED BEFORE COMPILING
tasks.withType<KotlinCompile> {
    dependsOn("openApiGenerate")
}