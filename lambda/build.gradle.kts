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
    implementation("com.amazonaws:aws-lambda-java-events:3.11.1")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.5.1")

    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws:4.0.5")

    //spring data
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.3")
    implementation("com.mysql:mysql-connector-j:8.0.33")
    implementation("org.hibernate.orm:hibernate-core:6.2.4.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.2.4.Final")

    compileOnly(group = "com.googlecode.json-simple", name = "json-simple", version = "1.1.1", ext = "jar")


    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:2.1.0")
}


tasks.test {
    useJUnitPlatform()
}


tasks {
   register("createZip", Zip::class) {
        from(processResources)
        from(compileJava)
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