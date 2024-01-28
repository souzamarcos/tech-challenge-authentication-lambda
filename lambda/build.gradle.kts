plugins {
    id("java")
}

group = "com.fiap.burger"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.12.576")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:secretsmanager:2.21.10")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    compileOnly(group = "com.googlecode.json-simple", name = "json-simple", version = "1.1.1", ext = "jar")


    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    register("createZip", Zip::class) {
        from(processResources)
        from(compileJava)
        archiveFileName.set("lambda-customer-authentication.zip")
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

tasks.build {
    dependsOn(tasks.named("createZip"))
}