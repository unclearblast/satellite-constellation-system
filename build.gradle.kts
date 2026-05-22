// build.gradle.kts (корневой)
plugins {
    java
    id("org.springframework.boot") version "3.2.4" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
runtimeOnly("com.h2database:h2")

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("net.devh:grpc-client-spring-boot-starter:3.0.0.RELEASE")
        implementation("io.grpc:grpc-protobuf:1.62.2")
        implementation("io.grpc:grpc-stub:1.62.2")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation("io.rest-assured:rest-assured:5.3.1")
        testImplementation("io.rest-assured:json-path:5.3.1")
        testImplementation("io.qameta.allure:allure-rest-assured:2.24.0")
        testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
        compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
tasks.test {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "build/allure-results")
}

allure {
    version.set("2.24.0")
    autoconfigure.set(true)
    aspectjweaver.set(true)
}
}
