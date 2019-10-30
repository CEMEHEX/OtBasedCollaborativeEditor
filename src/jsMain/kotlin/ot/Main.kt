package ot

import org.w3c.dom.HTMLTextAreaElement
import org.w3c.xhr.XMLHttpRequest
import ot.command.ApplyOperationLocally
import ot.command.NoCommand
import ot.command.OperationApplicationCommand
import ot.command.SendOperationToServer
import ot.config.DemoAppConfig
import ot.config.DemoAppConfig.diffToOperationsDecomposer
import ot.config.DemoAppConfig.documentTitleElement
import ot.config.DemoAppConfig.documentsListElement
import ot.config.DemoAppConfig.operationDeserializer
import ot.config.DemoAppConfig.operationSerializer
import ot.config.DemoAppConfig.stompClient
import ot.config.DemoAppConfig.textAreaElement
import ot.entity.PlainTextDocument
import ot.fsm.ClientFSM
import ot.fsm.ClientFSMImpl
import ot.fsm.SynchronizedState
import ot.service.ClientDocumentManager
import ot.service.impl.*
import kotlin.browser.document
import kotlin.js.Json

var clientFsm: ClientFSM<String, PlainTextSingleCharacterOperation>? = null

var clientDocumentManager: ClientDocumentManager<String, PlainTextSingleCharacterOperation>? = null

fun onOperationApplicationCommand(
    operationApplicationCommand: OperationApplicationCommand<PlainTextSingleCharacterOperation>
) {
    when (operationApplicationCommand) {
        is ApplyOperationLocally -> applyOperationLocally(operationApplicationCommand.operation)
        is SendOperationToServer -> sendOperation(operationApplicationCommand.operation)
        is NoCommand -> Unit
    }
}

fun setupWebSocket(documentUUID: String) {

//    stompClient.disconnect { console.log("Stomp client disconnected") }

    fun onError(error: dynamic) {
        console.error("Websocket client error: $error")
    }

    fun onMessageReceived(payload: dynamic) {
        val operationJson = JSON.parse<Json>(payload.body as String)
        val operation = operationDeserializer.deserialize(operationJson)
        console.log("Message received: ${payload.body}")
        onOperationApplicationCommand(clientDocumentManager?.processRemoteOperation(operation) ?: TODO())
    }

    fun onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe("/topic/public/operation/$documentUUID", ::onMessageReceived)
    }

    stompClient.connect(object {}, { onConnected() }, ::onError)
}

fun sendOperation(operation: PlainTextSingleCharacterOperation) {
    val serializedMessage = operationSerializer.serialize(operation)
    console.log("Sending message: ${JSON.stringify(serializedMessage)}")
    stompClient.send(
        "/app/document/$curDocumentUUID/revision/${clientDocumentManager?.revision ?: TODO()}",
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
    textAreaElement.oninput = { inputEvent ->
        val cur = textAreaElement.value
        console.log("Prev text: $previousTextAreaData")
        console.log("Cur text: $cur")
        val operations = diffToOperationsDecomposer.diffToOperationList(previousTextAreaData, cur)
        console.log("Operations: $operations")
        previousTextAreaData = textAreaElement.value
        operations.forEach { onOperationApplicationCommand(clientDocumentManager?.processLocalOperation(it) ?: TODO()) }
        Unit
    }
}

var previousTextAreaData: String = textAreaElement.value
fun setupTextAreaPreviousContentSaver() {
    textAreaElement.addEventListener("beforeinput", {
        console.log("Before input: ${(it.target as HTMLTextAreaElement).value}")
        previousTextAreaData = (it.target as HTMLTextAreaElement).value
    }
    )
}

fun initializeDocumentsList() {
    val xhr = XMLHttpRequest()
    xhr.open("GET", "document/all", false)
    xhr.send()
    val documents = JSON.parse<Array<PlainTextDocument>>(xhr.responseText)
    documents.forEach {
        val li = document.createElement("li")
        li.appendChild(document.createTextNode(it.title))
        li.setAttribute("id", it.uuid)
        documentsListElement.appendChild(li)
    }
}

var curDocumentUUID: String? = null // TODO
fun loadDocument(uuid: String) {
    curDocumentUUID = uuid
    val xhr = XMLHttpRequest()
    xhr.open("GET", "document/$uuid", false)
    xhr.send()
    val document = JSON.parse<PlainTextDocument>(xhr.responseText)
    documentTitleElement.textContent = document.title
    textAreaElement.value = document.content
    clientFsm = ClientFSMImpl<String, PlainTextSingleCharacterOperation>(document.revision)
        .apply { state = SynchronizedState(this, DemoAppConfig.operationsManager) }

    clientDocumentManager = clientFsm?.let { ClientDocumentManagerImpl(it)}
    setupWebSocket(uuid)
}

fun main() {
    initializeDocumentsList()
    setupTextArea()
    setupTextAreaPreviousContentSaver()
    loadDocument("1")
}