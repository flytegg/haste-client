package gg.flyte.hasteclient

import de.jensklingenberg.ktorfit.Ktorfit
import gg.flyte.hasteclient.model.Document
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.gson.*
import io.ktor.util.*
import java.net.URL

object HasteClient {

    private lateinit var ktorfit: Ktorfit
    private lateinit var hasteApi: HasteApi
    lateinit var client: HttpClient
    private lateinit var userAgent: String

    fun getKtorfit(): Ktorfit {
        if (!HasteClient::ktorfit.isInitialized) {
            throw IllegalStateException("Ktorfit has not been initialized! Please use HasteClient.Build() first!")
        }

        return ktorfit
    }

    suspend fun getDocument(id: String, url: String? = null): Document {
        return if (url == null) {
            hasteApi.getDocument(id)
        } else {
            client.get(URL(url)).body()
        }
    }

    suspend fun getRawDocument(id: String, url: String? = null): String {
        return if (url == null) {
            hasteApi.getRawDocument(id)
        } else {
            client.get(URL(url)).body()
        }
    }

    suspend fun createDocument(data: String) = hasteApi.createDocument(data)

    class Builder(init: Builder.() -> Unit) {
        private var userAgent = "flytegg/haste-client"
        private var url = "https://paste.learnspigot.com/"

        var logging = false
        var loggingLevel = LogLevel.NONE
        var logger = Logger.DEFAULT
        var customLogger: Logger? = null

        fun build() {
            client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson {
                        setPrettyPrinting()
                    }
                }
                if (logging) {
                    install(Logging) {
                        this.logger = logger
                        this.level = loggingLevel

                        if (customLogger != null) {
                            this.logger = customLogger!!
                        }
                    }
                }
                install(UserAgent) {
                    userAgent.apply {
                        HasteClient.userAgent = this
                        agent = this
                    }
                }
            }

            ktorfit = Ktorfit.Builder()
                .baseUrl(url)
                .httpClient(client)
                .build()

            hasteApi = ktorfit.create()
        }

        init {
            apply(init)
        }
    }
}