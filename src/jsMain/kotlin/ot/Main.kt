package ot

import ot.command.ApplyOperationLocally
import ot.command.NoCommand
import ot.command.OperationApplicationCommand
import ot.command.SendOperationToServer
import ot.config.DemoAppConfig.clientDocumentManager
import ot.config.DemoAppConfig.diffToOperationsDecomposer
import ot.config.DemoAppConfig.operationDeserializer
import ot.config.DemoAppConfig.operationSerializer
import ot.config.DemoAppConfig.stompClient
import ot.config.DemoAppConfig.textAreaElement
import ot.service.impl.DeleteOperation
import ot.service.impl.IdentityOperation
import ot.service.impl.InsertOperation
import ot.service.impl.PlainTextSingleCharacterOperation
import kotlin.js.Json

fun onOperationApplicationCommand(
    operationApplicationCommand: OperationApplicationCommand<PlainTextSingleCharacterOperation>
) {
    when (operationApplicationCommand) {
        is ApplyOperationLocally -> applyOperationLocally(operationApplicationCommand.operation)
        is SendOperationToServer -> sendOperation(operationApplicationCommand.operation)
        is NoCommand -> Unit
    }
}

fun setupWebSocket() {
    fun onError(error: dynamic) {
        console.error("Websocket client error: $error")
    }

    fun onMessageReceived(payload: dynamic) {
        val operationJson = JSON.parse<Json>(payload.body as String)
        val operation = operationDeserializer.deserialize(operationJson)
        console.log("Message received: ${JSON.stringify(operation)}")
        onOperationApplicationCommand(clientDocumentManager.processRemoteOperation(operation))
    }

    fun onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe("/topic/public/operation/1", ::onMessageReceived)
    }

    stompClient.connect(object {}, { onConnected() }, ::onError)
}

var REMOVE_IT_tmp_revision = 0
fun sendOperation(operation: PlainTextSingleCharacterOperation) {
    val serializedMessage = operationSerializer.serialize(operation)
    console.log("Sending message: ${JSON.stringify(serializedMessage)}")
    stompClient.send(
        "/app/document/1/revision/${REMOVE_IT_tmp_revision++}",
        {},
        JSON.stringify(serializedMessage)
    )
}

fun transformCursorPosition(
    cursorPosition: Int,
    operation: PlainTextSingleCharacterOperation
): Int = when (operation) {
    is InsertOperation -> if (operation.position <= cursorPosition) cursorPosition + 1 else cursorPosition
    is DeleteOperation -> if (operation.position < cursorPosition) cursorPosition - 1 else cursorPosition
    is IdentityOperation -> cursorPosition
}

fun applyOperationLocally(operation: PlainTextSingleCharacterOperation) {
    console.log("transforming cursor against ${JSON.stringify(operation)}")
    val selectionStartPos = textAreaElement.selectionStart?.let { transformCursorPosition(it, operation) }
    val selectionEndPos = textAreaElement.selectionEnd?.let { transformCursorPosition(it, operation) }
    val modifiedContent = operation.applyTo(textAreaElement.value)
    textAreaElement.value = modifiedContent
    textAreaElement.selectionStart = selectionStartPos
    textAreaElement.selectionEnd = selectionEndPos
}

fun setupTextArea() {
    var previousTextAreaData: String = textAreaElement.value
    textAreaElement.oninput = { inputEvent ->
        val cur = textAreaElement.value
        console.log("Prev text: $previousTextAreaData")
        console.log("Cur text: $cur")
        val operations = diffToOperationsDecomposer.diffToOperationList(previousTextAreaData, cur)
        console.log("Operations: $operations")
        val test = operations.fold(previousTextAreaData) { result, current -> current.applyTo(result) }
        console.log("Test: $test")
        previousTextAreaData = textAreaElement.value
        operations.forEach { onOperationApplicationCommand(clientDocumentManager.processLocalOperation(it)) }
        Unit
    }
}

fun main() {
    setupTextArea()
    setupWebSocket()
}