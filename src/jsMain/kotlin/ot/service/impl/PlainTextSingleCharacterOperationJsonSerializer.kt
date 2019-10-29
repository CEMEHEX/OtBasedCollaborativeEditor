package ot.service.impl

import ot.service.OperationSerializer
import kotlin.js.Json
import kotlin.js.json

class PlainTextSingleCharacterOperationJsonSerializer :
    OperationSerializer<PlainTextSingleCharacterOperation, Json> {

    override fun serialize(operation: PlainTextSingleCharacterOperation): Json = when (operation) {
        is InsertOperation -> {
            val (uuid, position, symbol) = operation
            json(
                "type" to "INSERT",
                "uuid" to uuid,
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is DeleteOperation -> {
            val (uuid, position, symbol) = operation
            json(
                "type" to "DELETE",
                "uuid" to uuid,
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is IdentityOperation -> json(
            "type" to "IDENTITY",
            "uuid" to operation.uuid
        )

    }

}