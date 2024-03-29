plugins {
    id("com.github.johnrengelman.shadow").version("8.1.1")
    id("java")
}

group = "me.oskar3123"
version = "SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
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
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.20-R0.2-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("com.discordsrv:discordsrv:1.25.1")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("org.bstats:bstats-bungeecord:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.shadowJar {
    relocate("org.bstats", "me.oskar3123.staffchat.bstats")
}

tasks.test {
    useJUnitPlatform()
}
