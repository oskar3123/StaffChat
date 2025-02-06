plugins {
    id("com.github.johnrengelman.shadow").version("8.1.1")
    id("java")
}

group = "me.oskar3123"
version = "SNAPSHOT"

// Main sourceSet remains on Java 8
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

// Create a sourceSet for Velocity code that uses Java 17
sourceSets {
    create("velocity") {
        java {
            srcDir("src/velocity/java")
            compileClasspath += sourceSets.main.get().output + sourceSets.main.get().compileClasspath
            runtimeClasspath += sourceSets.main.get().output + sourceSets.main.get().runtimeClasspath
        }
    }
}

// Configure the automatically created Velocity compilation task to use Java 17
tasks.named<JavaCompile>("compileVelocityJava") {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "m2-dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }
    maven {
        url = uri("https://nexus.scarsz.me/content/groups/public/")
    }
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.20-R0.2-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("com.discordsrv:discordsrv:1.25.1")
    "velocityCompileOnly"("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    "velocityAnnotationProcessor"("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("org.bstats:bstats-bungeecord:3.0.2")
    implementation("org.bstats:bstats-velocity:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.shadowJar {
    relocate("org.bstats", "me.oskar3123.staffchat.bstats")
    dependsOn("compileVelocityJava")
    from(sourceSets["velocity"].output)
}

tasks.test {
    useJUnitPlatform()
}
