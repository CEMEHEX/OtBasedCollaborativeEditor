package ot

import org.w3c.dom.events.Event
import ot.external.SockJS
import ot.external.Stomp
import ot.service.impl.PlainTextSingleCharacterOperation

fun <T> T?.validateNotNull(): T =
    this ?: throw IllegalStateException("required html elements not initialized")


fun main() {
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

//    stompClient.connect(object {}, { onConnected() }, ::onError)
}