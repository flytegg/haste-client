package gg.flyte.hasteclient

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import gg.flyte.hasteclient.model.Document
import gg.flyte.hasteclient.model.KeyResponse

interface HasteApi {

    @POST("documents")
    suspend fun createDocument(
        @Body data: String,
    ): KeyResponse

    @GET("documents/{id}")
    suspend fun getDocument(
        @Path id: String,
    ): Document

    @GET("raw/{id}")
    suspend fun getRawDocument(
        @Path id: String,
    ): String

}