plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.0"
    id("io.micronaut.aot") version "4.0.0"
}

version = "0.1"
group = "com.example"

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        mavenContent { snapshotsOnly() }
    }
    mavenCentral()
}

dependencies {
    runtimeOnly("org.yaml:snakeyaml")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    annotationProcessor("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")

    implementation("io.swagger.core.v3:swagger-annotations")

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")

    // Email
    implementation("io.micronaut.email:micronaut-email-template")
    implementation("io.micronaut.email:micronaut-email-amazon-ses")
    implementation("io.micronaut.email:micronaut-email-template")

    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut:micronaut-http-client")

    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:localstack")
    testImplementation("org.testcontainers:testcontainers")
    // Workaround until Test Containers 2.0
    testImplementation("com.amazonaws:aws-java-sdk-core:1.12.372")
}


application {
    mainClass.set("com.example.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("lambda_provided")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }

    aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(true)
        convertYamlToJava.set(true)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}



