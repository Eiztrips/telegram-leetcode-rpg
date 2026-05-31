plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "8.6.0"
}

group = "dev.eiztrips"
version = "0.0.1-SNAPSHOT"
description = "telegram-leetcode-rpg"

/*
    АВТОРЕФАКТОРИНГ
    ./gradlew spotlessApply - ЮЗАТЬ ПЕРЕД КАЖДЫМ КОММИТОМ!!!
 */
spotless {
    java {
        target("src/main/java/**/*.java")
        eclipse()
        removeUnusedImports()
        endWithNewline()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

/*
    todo: переместить версии в libs.versions.toml
     - https://docs.gradle.org/current/userguide/platforms.html#version-catalog
      - https://docs.gradle.org/current/userguide/platforms.html#version-catalog-usage
      - https://docs.gradle.org/current/userguide/platforms.html#version-catalog-dependencies
      - https://docs.gradle.org/current/userguide/platforms.html#version-catalog-plugins
      - https://docs.gradle.org/current/userguide/platforms.html#version-catalog-plugins
 */
val springDocVersion = "3.0.3"
val archUnit = "1.4.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("com.h2database:h2")
    testImplementation("com.tngtech.archunit:archunit-junit5:$archUnit")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
