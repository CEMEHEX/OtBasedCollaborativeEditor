package ot.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import ot.service.impl.DeleteOperation
import ot.service.impl.IdentityOperation
import ot.service.impl.InsertOperation
import ot.service.impl.PlainTextSingleCharacterOperation


class PlainTextSingleCharacterOperationSerializer
    : StdSerializer<PlainTextSingleCharacterOperation>(PlainTextSingleCharacterOperation::class.java) {

    override fun serialize(
        value: PlainTextSingleCharacterOperation,
        jgen: JsonGenerator,
        provider: SerializerProvider
    ) = when (value) {
        is InsertOperation -> serializeInsertOperation(value, jgen)
        is DeleteOperation -> serializeDeleteOperation(value, jgen)
        is IdentityOperation -> serializeIdentityOperation(value, jgen)
    }

    private fun serializeInsertOperation(
        value: InsertOperation,
        jgen: JsonGenerator
    ) {
        jgen.writeStartObject()
        jgen.writeStringField("type", "INSERT")
        jgen.writeStringField("uuid", value.uuid)
        jgen.writeNumberField("position", value.position)
        jgen.writeStringField("symbol", value.symbol.toString())
        jgen.writeEndObject()
    }

    private fun serializeDeleteOperation(
        value: DeleteOperation,
        jgen: JsonGenerator
    ) {
        jgen.writeStartObject()
        jgen.writeStringField("type", "DELETE")
        jgen.writeStringField("uuid", value.uuid)
        jgen.writeNumberField("position", value.position)
        jgen.writeStringField("symbol", value.symbol.toString())
        jgen.writeEndObject()
    }

    private fun serializeIdentityOperation(
        value: IdentityOperation,
        jgen: JsonGenerator
    ) {
        jgen.writeStartObject()
        jgen.writeStringField("type", "IDENTITY")
        jgen.writeStringField("uuid", value.uuid)
        jgen.writeEndObject()
    }
}