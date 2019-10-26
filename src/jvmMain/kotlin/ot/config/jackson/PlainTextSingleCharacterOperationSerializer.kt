package ot.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import ot.impl.DeleteOperation
import ot.impl.IdentityOperation
import ot.impl.InsertOperation
import ot.impl.PlainTextSingleCharacterOperation


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
        jgen.writeNumberField("id", value.id)
        jgen.writeNumberField("revision", value.revision)
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
        jgen.writeNumberField("id", value.id)
        jgen.writeNumberField("revision", value.revision)
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
        jgen.writeNumberField("id", value.id)
        jgen.writeNumberField("revision", value.revision)
        jgen.writeEndObject()
    }
}