package ot.service.impl

import ot.service.OperationDeserializer

// TODO replace dirty serialization workarounds with kotlinx.serialization
class PlainTextSingleCharacterOperationJsonDeserializer :
    OperationDeserializer<PlainTextSingleCharacterOperation, dynamic> {
    override fun deserialize(rawData: dynamic): PlainTextSingleCharacterOperation {
        console.log("DESERIALIZE: (type: ${rawData.type}, id: ${rawData.uuid}, position: ${rawData.position}, symbol: ${rawData.symbol})}")
        return when (rawData.type as String) {
            "INSERT" -> InsertOperation(
                rawData.uuid.toString(),
                rawData.position as Int,
                rawData.symbol.toString()[0]
            )
            "DELETE" -> DeleteOperation(
                rawData.uuid.toString(),
                rawData.position as Int,
                rawData.symbol.toString()[0]
            )
            "IDENTITY" -> IdentityOperation(rawData.uuid.toString())
            else -> throw IllegalArgumentException("Invalid operation type: ${rawData.type}")
        }
    }
}