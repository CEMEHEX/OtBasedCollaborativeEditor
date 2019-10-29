package ot

import org.w3c.dom.HTMLTextAreaElement
import ot.command.ApplyOperationLocally
import ot.command.NoCommand
import ot.command.OperationApplicationCommand
import ot.command.SendOperationToServer
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.fsm.ClientFSM
import ot.fsm.ClientFSMImpl
import ot.fsm.SynchronizedState
import ot.service.ClientDocumentManager
import ot.service.impl.*
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

val operationsManager = PlainTextOperationsManager()
val clientFsm: ClientFSM<String, PlainTextSingleCharacterOperation> =
    ClientFSMImpl<String, PlainTextSingleCharacterOperation>(0)
        .apply { state = SynchronizedState(this, operationsManager) }

val clientDocumentManager: ClientDocumentManager<String, PlainTextSingleCharacterOperation> = ClientDocumentManagerImpl(
    clientFsm
)

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
        val operation = JSON.parse<PlainTextSingleCharacterOperation>(payload.body as String)
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
    else -> {
        console.log("WTF operation: ${JSON.stringify(operation)}")
        throw IllegalStateException()
    }
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
        operations.forEach { sendOperation(it) }
        Unit
    }
}

fun main() {
    setupTextArea()
    setupWebSocket()
}