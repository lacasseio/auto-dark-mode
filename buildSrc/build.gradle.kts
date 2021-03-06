plugins {
    `java-gradle-plugin`
    groovy
}

dependencies {
    implementation("dev.nokee:platformJni:0.4.0-00ed537")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://dl.bintray.com/nokeedev/distributions") }
    maven { url = uri("https://dl.bintray.com/nokeedev/distributions-snapshots") }
}

gradlePlugin {
    plugins {
        create("uber-jni-jar") {
            id = "uber-jni-jar"
            implementationClass = "UberJniJarPlugin"
        }
    }
}
