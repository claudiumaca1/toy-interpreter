plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")   // includes @ParameterizedTest
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "interpreter.Main"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`   // keyboard input for the text menu
    workingDir = rootDir          // Lab 5 (A3): test.in and the log files stay in the project root
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
