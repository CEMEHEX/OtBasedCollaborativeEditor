package ot.service.impl

import ot.service.OperationSerializer
import kotlin.js.Json
import kotlin.js.json

class PlainTextSingleCharacterOperationJsonSerializer :
    OperationSerializer<PlainTextSingleCharacterOperation, Json> {

    override fun serialize(operation: PlainTextSingleCharacterOperation): Json = when (operation) {
        is InsertOperation -> {
            val (id, position, symbol) = operation
            json(
                "type" to "INSERT",
                "id" to id.toString(),
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is DeleteOperation -> {
            val (id, position, symbol) = operation
            json(
                "type" to "DELETE",
                "id" to id.toString(),
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is IdentityOperation -> json(
            "type" to "IDENTITY",
            "id" to operation.id.toString()
        )

    }

}