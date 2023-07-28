import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    kotlin("plugin.jpa") version "1.9.0"
    jacoco
}

subprojects {
    apply(plugin = "jacoco")
    jacoco {
        toolVersion = "0.8.10"
    }
}

group = "com.practice"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val asciidoctorExt: Configuration by configurations.creating
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    implementation {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
}

tasks {
    named("jar") {
        enabled = false
    }
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.data:spring-data-rest-hal-explorer")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    implementation("com.auth0:java-jwt:4.4.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.github.f4b6a3:ulid-creator:5.2.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.oracle.database.jdbc:ojdbc10:19.19.0.0")
    runtimeOnly("com.oracle.database.security:osdt_cert")
    runtimeOnly("com.oracle.database.security:oraclepki")
    runtimeOnly("com.oracle.database.security:osdt_core")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("io.rest-assured:rest-assured")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

val snippetsDir: File = file(path = "build/generated-snippets")

tasks.withType<Test> {
    useJUnitPlatform()
    outputs.dir(snippetsDir)
}

tasks {
    val asciidoctor by existing(AsciidoctorTask::class) {
        inputs.dir(snippetsDir)
        configurations(asciidoctorExt)
        dependsOn("test")
        attributes(mapOf("doctype" to "book"))
    }
    val copyDocument by registering(Copy::class) {
        dependsOn(asciidoctor)
        from(asciidoctor.get().outputDir)
        into("static/docs")
    }
    bootJar {
        dependsOn(copyDocument)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        csv.required.set(false)
        xml.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "200".toBigDecimal()
            }
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
