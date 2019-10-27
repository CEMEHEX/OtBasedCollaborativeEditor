package ot.config.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.entity.PlainTextDocument
import ot.service.impl.PlainTextSingleCharacterOperation


@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule().apply {
            addDeserializer(
                PlainTextSingleCharacterOperation::class.java,
                PlainTextSingleCharacterOperationDeserializer()
            )
            addDeserializer(
                PlainTextDocument::class.java,
                PlainTextDocumentDeserializer()
            )
            addSerializer(
                PlainTextSingleCharacterOperation::class.java,
                PlainTextSingleCharacterOperationSerializer()
            )
        }
        mapper.registerModule(module)

        return mapper
    }
}