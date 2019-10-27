package ot.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ot.entity.PlainTextDocument

class PlainTextDocumentDeserializer : StdDeserializer<PlainTextDocument>(
    PlainTextDocument::class.java
) {

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): PlainTextDocument {
        val node: JsonNode = jp.codec.readTree(jp)
        val id = node.get("id").asLong()
        val revision = node.get("revision").asInt()
        val content: String = node.get("content").asText()

        return PlainTextDocument(id, revision, content)
    }
}