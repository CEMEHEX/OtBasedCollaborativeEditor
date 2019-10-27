package ot.config.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.service.impl.PlainTextSingleCharacterOperation


@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(
            PlainTextSingleCharacterOperation::class.java,
            PlainTextSingleCharacterOperationDeserializer()
        )
        module.addSerializer(
            PlainTextSingleCharacterOperation::class.java,
            PlainTextSingleCharacterOperationSerializer()
        )
        mapper.registerModule(module)

        return mapper
    }
}