import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	id("com.google.cloud.tools.jib") version "3.1.4"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "io.freevariable"
version = "0.0.1-SNAPSHOT"

val buildNumber by extra("0")

java.sourceCompatibility = JavaVersion.VERSION_17

jib {
	to {
		image = "auktion"
		tags = setOf("$version", "$version.${extra["buildNumber"]}")
	}
}

repositories {
	mavenCentral()
}

val asciidoctorExt: Configuration by configurations.creating

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.security:spring-security-crypto")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

val snippetsDir = file("${buildDir}/generated-snippets")


tasks.asciidoctor {
	inputs.dir(snippetsDir)
	dependsOn(tasks.test)
	configurations(asciidoctorExt.name)
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	outputs.dir(snippetsDir)
	useJUnitPlatform()
}
