package ot.config.jackson

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ot.service.impl.DeleteOperation
import ot.service.impl.IdentityOperation
import ot.service.impl.InsertOperation
import ot.service.impl.PlainTextSingleCharacterOperation


class PlainTextSingleCharacterOperationDeserializer
    : StdDeserializer<PlainTextSingleCharacterOperation>(
    PlainTextSingleCharacterOperation::class.java
) {

    private fun parsePositionAndSymbol(node: JsonNode, jp: JsonParser): Pair<Int, Char> {
        val position = node.get("position").asInt()
        val symbol = node
            .get("symbol")
            .asText()
            .let { if (it.length == 1) it[0] else throw JsonParseException(jp, "Invalid symbol: $it") }
        return position to symbol
    }

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): PlainTextSingleCharacterOperation {
        val node: JsonNode = jp.codec.readTree(jp)
        val type: String = node.get("type").asText()
        val uuid = node.get("uuid").asText()

        return when (type) {
            "INSERT" -> parsePositionAndSymbol(node, jp)
                .let { (position, symbol) -> InsertOperation(uuid, position, symbol) }
            "DELETE" -> parsePositionAndSymbol(node, jp)
                .let { (position, symbol) -> DeleteOperation(uuid, position, symbol) }
            "IDENTITY" -> IdentityOperation(uuid)
            else -> throw JsonParseException(jp, "Invalid operation type: $type")
        }
    }
}