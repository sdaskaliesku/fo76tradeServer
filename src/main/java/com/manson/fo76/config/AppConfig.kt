package com.manson.fo76.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import javax.servlet.MultipartConfigElement
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.unit.DataSize

@Configuration
open class AppConfig {
    companion object {
        @get:Bean
        val objectMapper = ObjectMapper()
        const val ENABLE_AUTO_PRICE_CHECK = false
        private const val MONGO_URL_FORMAT = "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority"

        init {
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        }
    }

    @Value("#{systemProperties['mongo.user']}")
    private val mongoUser: String? = null

    @Value("#{systemProperties['mongo.password']}")
    private val mongoPassword: String? = null

    @Value("#{systemProperties['mongo.db']}")
    private val mongoDb: String? = null

    @Value("#{systemProperties['mongo.url']}")
    val mongoUrl: String? = null

    open fun createMongoUrl(): String {
        return String.format(MONGO_URL_FORMAT, mongoUser, mongoPassword, mongoUrl, mongoDb)
    }

    @Bean
    open fun mongoClient(): MongoClient {
        return MongoClients.create(createMongoUrl())
    }

    @Bean
    open fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()
        factory.setMaxFileSize(DataSize.ofBytes(512000000L))
        factory.setMaxRequestSize(DataSize.ofBytes(512000000L))
        return factory.createMultipartConfig()
    }
}