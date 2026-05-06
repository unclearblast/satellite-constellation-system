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
        compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
