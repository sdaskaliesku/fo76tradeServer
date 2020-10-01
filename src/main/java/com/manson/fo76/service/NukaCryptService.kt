package com.manson.fo76.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.nuka.NukaRequest
import com.manson.domain.nuka.NukaResponse
import java.io.BufferedReader
import java.io.InputStream
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType
import org.glassfish.jersey.media.multipart.FormDataMultiPart
import org.springframework.stereotype.Service

@Service
class NukaCryptService(objectMapper: ObjectMapper) : BaseRestClient(objectMapper) {

    fun performRequest(request: NukaRequest): NukaResponse? {
        val webResource: WebTarget = client
                .target("https://nukacrypt.com/queries/searchdata.php")
        val multiPart = FormDataMultiPart()
        val map = request.toMultiPartMap()
        map.forEach { (k, v) ->
            multiPart.field(k, v)
        }
        val response = webResource.request().accept(MediaType.MULTIPART_FORM_DATA).buildPost(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA)).invoke()
        val responseString = BufferedReader((response.entity as InputStream).reader()).readText()
        return objectMapper.readValue(responseString, NukaResponse::class.java)
    }

}