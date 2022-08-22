plugins {
    id("java")
}

group = "com.wry"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("cn.hutool:hutool-all:5.8.5")
    implementation("org.dom4j:dom4j:2.1.3")

//    implementation("org.mybatis:mybatis:3.4.5")
    implementation("com.alibaba:druid:1.2.11")
    implementation("mysql:mysql-connector-java:8.0.21")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
