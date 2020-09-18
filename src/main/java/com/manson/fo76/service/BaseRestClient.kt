package com.manson.fo76.service

import com.fasterxml.jackson.databind.ObjectMapper
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class BaseRestClient
@Autowired constructor(protected val objectMapper: ObjectMapper) {

    protected val client: Client

    init {
        val config = ClientConfig()
        config.register(JacksonJsonProvider(objectMapper))
        config.register(MultiPartFeature::class.java)
        config.register(MultiPartWriter::class.java)
        this.client = ClientBuilder.newClient(config)
    }
}