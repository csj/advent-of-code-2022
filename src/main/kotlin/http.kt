import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.FileInputStream
import java.util.*

suspend fun getInputForDay(day: Int): String {
    val client = HttpClient(CIO) {
//        install(Logging)
    }

    val props = Properties()
    props.load(FileInputStream("src/main/resources/application.properties"))
    val sessionId = props.getProperty("sessionId")

    val response: HttpResponse = client
        .get("https://adventofcode.com/2022/day/${day}/input") {
            headers {
                append(HttpHeaders.Host, "adventofcode.com")
                append(HttpHeaders.Cookie, "session=${sessionId}")
            }
        }
    val data = response.receive() as String
    client.close()
    return data
}
