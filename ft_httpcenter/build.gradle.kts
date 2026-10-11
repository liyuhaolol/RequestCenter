import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.time.Duration
import java.util.Base64
import java.util.Properties
import org.gradle.api.GradleException

plugins {
    id("com.android.library")
    `maven-publish`
    signing
}

android {
    namespace = "spa.lyh.cn.ft_httpcenter"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
    }

    buildTypes {
        release {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility =  JavaVersion.VERSION_25
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

}

dependencies {
    api("io.github.liyuhaolol:HttpUtils:1.6.0")
}

// 发布所需密钥，写在 local.properties 或环境变量中（同名）：
//   signingKeyId / signingPassword / secretKeyRingFile
//   ossrhUsername / ossrhPassword          —— Central Portal User Token
//   signingKey（可选）                      —— ASCII armored 私钥，优先于 secretKeyRingFile
fun secret(name: String): String? {
    val local = rootProject.file("local.properties")
    if (local.exists()) {
        val props = Properties()
        local.inputStream().bufferedReader(Charsets.UTF_8).use { props.load(it) }
        props.getProperty(name)?.takeIf { it.isNotBlank() }?.let { return it }
    }
    return (findProperty(name) as? String)?.takeIf { it.isNotBlank() }
        ?: System.getenv(name)?.takeIf { it.isNotBlank() }
}

val signingKeyId: String? = secret("signingKeyId")
val signingPassword: String? = secret("signingPassword")
val secretKeyRingFile: String? = secret("secretKeyRingFile")
val signingKeyAscii: String? = secret("signingKey")
val ossrhUsername: String? = secret("ossrhUsername")
val ossrhPassword: String? = secret("ossrhPassword")

val pomGroup = "io.github.liyuhaolol"
val pomArtifact = "RequestCenter"
val pomVersion = "1.4.8"

val emptyJavadocJar = tasks.register<Jar>("emptyJavadocJar") {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = pomGroup
            artifactId = pomArtifact
            version = pomVersion

            artifact(emptyJavadocJar)

            pom {
                name = "HttpUtils"
                description = "RequestCenter of HttpUtils"
                url = "https://github.com/liyuhaolol/RequestCenter"
                packaging = "aar"
                licenses {
                    license {
                        name = "The MIT License"
                        url = "https://github.com/liyuhaolol/RequestCenter/blob/master/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "liyuhao"
                        name = "liyuhao"
                        email = "liyuhaoid@sina.com"
                    }
                }
                scm {
                    connection = "scm:git@github.com/liyuhaolol/RequestCenter.git"
                    developerConnection = "scm:git@github.com/liyuhaolol/RequestCenter.git"
                    url = "https://github.com/liyuhaolol/RequestCenter"
                }
            }
        }
    }

    repositories {
        // 本地 Maven 仓库目录，作为 Central Portal 的 bundle 源
        maven {
            name = "centralBundle"
            url = uri(layout.buildDirectory.dir("central-bundle"))
        }
    }
}

// AGP 在 afterEvaluate 才注册 release 组件
afterEvaluate {
    publishing.publications.named<MavenPublication>("release") {
        from(components["release"])
    }
}

signing {
    val hasSigning =
        !signingPassword.isNullOrBlank() &&
                (!signingKeyAscii.isNullOrBlank() || !secretKeyRingFile.isNullOrBlank())
    isRequired = hasSigning

    if (!signingKeyAscii.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKeyId, signingKeyAscii, signingPassword)
    } else if (!secretKeyRingFile.isNullOrBlank()) {
        extra["signing.keyId"] = signingKeyId
        extra["signing.password"] = signingPassword
        extra["signing.secretKeyRingFile"] = secretKeyRingFile
    }

    sign(publishing.publications)
}


// ---------- Central Publisher API ----------

val centralZip = layout.buildDirectory.file("central-bundle.zip")
val centralDeploymentIdFile = layout.buildDirectory.file("central-deployment-id.txt")

fun centralToken(): String =
    Base64.getEncoder().encodeToString("$ossrhUsername:$ossrhPassword".toByteArray())

fun requireCentralCredentials() {
    if (ossrhUsername.isNullOrBlank() || ossrhPassword.isNullOrBlank()) {
        throw GradleException(
            "缺少 ossrhUsername / ossrhPassword，请写入 local.properties 或设置环境变量。" +
                    "Token 在 https://central.sonatype.com/account 生成。"
        )
    }
}

fun requireSigning() {
    if (signingPassword.isNullOrBlank() ||
        (signingKeyAscii.isNullOrBlank() && secretKeyRingFile.isNullOrBlank())
    ) {
        throw GradleException(
            "缺少 GPG 签名配置：signingPassword +（signingKey 或 secretKeyRingFile），" +
                    "以及 signingKeyId。"
        )
    }
}

val packageCentralBundle = tasks.register<Zip>("packageCentralBundle") {
    group = "publishing"
    description = "把 central-bundle 目录打成 Maven 仓库布局的 zip"
    dependsOn("publishReleasePublicationToCentralBundleRepository")
    from(layout.buildDirectory.dir("central-bundle")) {
        exclude("**/maven-metadata.xml*")
    }
    archiveFileName.set("central-bundle.zip")
    destinationDirectory.set(layout.buildDirectory)
}

val publishToCentral = tasks.register("publishToCentral") {
    group = "publishing"
    description = "签名并打包后，上传到 Sonatype Central Portal（AUTOMATIC 发布）"
    dependsOn("publishReleasePublicationToCentralBundleRepository", packageCentralBundle)

    doLast {
        requireCentralCredentials()
        requireSigning()

        val zipPath = centralZip.get().asFile.toPath()
        val name = "$pomArtifact-$pomVersion"
        val url = URI.create(
            "https://central.sonatype.com/api/v1/publisher/upload" +
                    "?name=${URLEncoder.encode(name, Charsets.UTF_8)}" +
                    "&publishingType=AUTOMATIC"
        )

        val boundary = "----CentralBundle${System.nanoTime()}"
        val fileBytes = Files.readAllBytes(zipPath)
        val preamble = "--$boundary\r\n" +
                "Content-Disposition: form-data; name=\"bundle\"; filename=\"central-bundle.zip\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n"
        val epilogue = "\r\n--$boundary--\r\n"

        val body = ByteArrayOutputStream()
        body.write(preamble.toByteArray(Charsets.UTF_8))
        body.write(fileBytes)
        body.write(epilogue.toByteArray(Charsets.UTF_8))

        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build()
        val request = HttpRequest.newBuilder(url)
            .timeout(Duration.ofMinutes(5))
            .header("Authorization", "Bearer ${centralToken()}")
            .header("Content-Type", "multipart/form-data; boundary=$boundary")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body.toByteArray()))
            .build()

        println("上传 $name 到 Central Portal…")
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val code = response.statusCode()
        val payload = response.body().trim()

        if (code !in 200..299) {
            throw GradleException("上传失败 HTTP $code：$payload")
        }

        centralDeploymentIdFile.get().asFile.writeText(payload)
        println("deploymentId = $payload")
        println("查看进度：https://central.sonatype.com/publishing/deployments")
        println("或执行 ./gradlew :lib_https:centralStatus")
    }
}

val centralStatus = tasks.register("centralStatus") {
    group = "publishing"
    description = "查询 publishToCentral 上传的 deployment 状态"
    doLast {
        requireCentralCredentials()
        val idFile = centralDeploymentIdFile.get().asFile
        if (!idFile.exists()) {
            throw GradleException("未找到 ${idFile.name}，请先执行 publishToCentral")
        }
        val deploymentId = idFile.readText().trim()

        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build()
        val request = HttpRequest.newBuilder(
            URI.create("https://central.sonatype.com/api/v1/publisher/status?id=$deploymentId")
        )
            .timeout(Duration.ofSeconds(60))
            .header("Authorization", "Bearer ${centralToken()}")
            .POST(HttpRequest.BodyPublishers.noBody())
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println("HTTP ${response.statusCode()}")
        println(response.body())
    }
}