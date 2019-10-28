package ot

import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.service.PlainTextDiffToSingleCharOperationsDecomposer
import ot.service.impl.LongSequentialIdGenerator
import ot.service.impl.PlainTextSingleCharacterOperation
import kotlin.browser.document

fun <T> T?.validateNotNull(): T =
    this ?: throw IllegalStateException("required html elements not initialized")

val textAreaElement = document.querySelector("textarea").validateNotNull() as HTMLTextAreaElement

fun main() {
    val diffMatchPatch = diff_match_patch()
    diffMatchPatch.diff_main("abc", "bcx").forEach { diffEntry ->
        console.log("(${diffEntry[0]}, ${diffEntry[1]})")
    }
    val idGenerator = LongSequentialIdGenerator()
    val diffToOperationsDecomposer = PlainTextDiffToSingleCharOperationsDecomposer(diffMatchPatch, idGenerator)


    val socket = SockJS("/ws")
    val stompClient = Stomp.over(socket)

    fun onError(error: dynamic) {
        console.error("Websocket client error: $error")
    }

    fun sendMessage(event: Event) {
        val chatMessage = JSON.parse<PlainTextSingleCharacterOperation>(TODO())
        stompClient.send("/app/1/operation", {}, JSON.stringify(chatMessage))

        event.preventDefault()
    }

    fun onMessageReceived(payload: dynamic) {
        val message = JSON.parse<PlainTextSingleCharacterOperation>(payload.body as String)
        console.log("Message received: ${JSON.stringify(message)}")
    }

    fun onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe("/topic/public/operation/1", ::onMessageReceived)
    }

    var previousTextAreaData: String = textAreaElement.value
    textAreaElement.oninput = { inputEvent ->
        val cur = textAreaElement.value
        console.log("Prev text: $previousTextAreaData")
        console.log("Cur text: $cur")
        console.log("Operations: ${diffToOperationsDecomposer.diffToOperationList(previousTextAreaData, cur)}")
        previousTextAreaData = textAreaElement.value
        Unit
    }

    stompClient.connect(object {}, { onConnected() }, ::onError)

}