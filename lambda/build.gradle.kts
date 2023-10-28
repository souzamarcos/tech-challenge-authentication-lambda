plugins {
    application
    id("java")
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.fiap.burger"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.fiap.burger.application.boot.BurgerApplication")
    applicationDefaultJvmArgs = listOf(
            "-Duser.timezone=America/Sao_Paulo"
    )
}

dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    compileOnly(group = "com.googlecode.json-simple", name = "json-simple", version = "1.1.1", ext = "jar")


    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    val zipTask by register("createZip", Zip::class) {
        from(processResources)
        from(compileJava)
        archiveFileName.set("lambda-client-authentication.zip")
        into("lib") {
            from(configurations.runtimeClasspath)
        }
    }
}

tasks.jar {
    manifest.attributes["Main-Class"] = "com.fiap.burger.handler.LambdaHandler"
    manifest.attributes["Class-Path"] = configurations
            .runtimeClasspath
            .get()
            .joinToString(separator = " ") { file ->
                "libs/${file.name}"
            }
}
