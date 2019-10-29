package ot

import org.w3c.dom.HTMLTextAreaElement
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.service.impl.LongSequentialIdGenerator
import ot.service.impl.PlainTextDiffToSingleCharOperationsDecomposer
import ot.service.impl.PlainTextSingleCharacterOperation
import ot.service.impl.PlainTextSingleCharacterOperationJsonSerializer
import kotlin.browser.document

fun <T> T?.validateNotNull(): T =
    this ?: throw IllegalStateException("required html elements not initialized")

val textAreaElement = document.querySelector("textarea").validateNotNull() as HTMLTextAreaElement

val diffMatchPatch = diff_match_patch()
val idGenerator = LongSequentialIdGenerator()
val diffToOperationsDecomposer = PlainTextDiffToSingleCharOperationsDecomposer(diffMatchPatch, idGenerator)
val operationSerializer = PlainTextSingleCharacterOperationJsonSerializer()
val socket = SockJS("/ws")
val stompClient = Stomp.over(socket)

fun setupWebSocket() {
    fun onError(error: dynamic) {
        console.error("Websocket client error: $error")
    }

    fun onMessageReceived(payload: dynamic) {
        val message = JSON.parse<PlainTextSingleCharacterOperation>(payload.body as String)
        console.log("Message received: ${JSON.stringify(message)}")
    }

    fun onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe("/topic/public/operation/1", ::onMessageReceived)
    }

    stompClient.connect(object {}, { onConnected() }, ::onError)
}

fun setupTextArea() {
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
        operations.forEach { sendOperation(it) }
        Unit
    }
}

fun main() {
    setupTextArea()
    setupWebSocket()
}