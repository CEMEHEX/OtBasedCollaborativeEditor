package ot.service.impl

import ot.service.OperationSerializer
import kotlin.js.Json
import kotlin.js.json

class PlainTextSingleCharacterOperationJsonSerializer :
    OperationSerializer<PlainTextSingleCharacterOperation, Json> {

    override fun serialize(operation: PlainTextSingleCharacterOperation): Json = when(operation) {
        is InsertOperation -> {
            val (id, revision, position, symbol) = operation
            json(
                "type" to "INSERT",
                "id" to id.toString(),
                "revision" to revision,
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is DeleteOperation -> {
            val (id, revision, position, symbol) = operation
            json(
                "type" to "DELETE",
                "id" to id.toString(),
                "revision" to revision,
                "position" to position,
                "symbol" to symbol.toString()
            )
        }
        is IdentityOperation -> {
            val (id, revision) = operation
            json(
                "type" to "IDENTITY",
                "id" to id.toString(),
                "revision" to revision
            )
        }
    }

}