package ot

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import ot.external.SockJS
import ot.external.Stomp
import ot.service.impl.PlainTextSingleCharacterOperation
import kotlin.browser.document

fun <T> T?.validateNotNull(): T =
    this ?: throw IllegalStateException("required html elements not initialized")

val messageForm = document.querySelector("#messageForm").validateNotNull() as HTMLFormElement
val messageInput = document.querySelector("#message").validateNotNull() as HTMLInputElement
val messageArea = document.querySelector("#messageArea").validateNotNull()
val connectingElement = document.querySelector(".connecting").validateNotNull()

fun main() {
    val socket = SockJS("/ws")
    val stompClient = Stomp.over(socket)

    fun onError(error: dynamic) {
        connectingElement.textContent =
            "Could not connect to WebSocket server. Please refresh this page to try again!"
    }


    fun sendMessage(event: Event) {
        val messageContent = messageInput.value.trim()
        val chatMessage = JSON.parse<PlainTextSingleCharacterOperation>(messageInput.value)
        stompClient.send("/app/1/operation", {}, JSON.stringify(chatMessage))
        messageInput.value = ""

        event.preventDefault()
    }

    fun onMessageReceived(payload: dynamic) {
        val message = JSON.parse<PlainTextSingleCharacterOperation>(payload.body as String)
        console.log("Message received: ${JSON.stringify(message)}")
    }

    fun onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe("/topic/public/operation/1", ::onMessageReceived)

        connectingElement.classList.add("hidden")
    }

    stompClient.connect(object {}, { onConnected() }, ::onError)
    messageForm.addEventListener("submit", ::sendMessage, true)
}