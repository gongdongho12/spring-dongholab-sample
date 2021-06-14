import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.expediagroup.graphql") version "4.1.1"
    war
    java
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
    kotlin("plugin.jpa") version "1.4.31"
}

val packageGroup = "com.dongholab"
group = packageGroup
version = "${System.currentTimeMillis()}-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
val swaggerVersion = "3.0.0"
val graphqlVersion = "4.1.1"

repositories {
    mavenCentral()
}

graphql {
    schema {
        packages = listOf("com.dongholab")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("org.springframework.plugin:spring-plugin-core:2.0.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("io.springfox:springfox-boot-starter:${swaggerVersion}") {
        // spring-boot-starter-data-rest의 버전이 호환되지 않기에 exclude 처리
        exclude("org.springframework.boot", "spring-boot-starter-data-rest")
        // 여기서 모듈 중첩
        exclude("io.github.classgraph", "classgraph")
    }
    implementation("io.github.classgraph:classgraph:4.8.103")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.mariadb:r2dbc-mariadb:1.0.1")
    implementation("mysql:mysql-connector-java")

    implementation("com.expediagroup:graphql-kotlin-spring-server:${graphqlVersion}")
    implementation("com.expediagroup:graphql-kotlin-hooks-provider:${graphqlVersion}")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

graphql {
    schema {
        packages = listOf(packageGroup)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<War> {
    enabled = true
}