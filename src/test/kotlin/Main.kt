import gg.flyte.hasteclient.HasteClient
import io.ktor.client.plugins.logging.*

suspend fun main() {
    HasteClient.Builder {
        logger = Logger.SIMPLE
        loggingLevel = LogLevel.ALL
    }.build()
}