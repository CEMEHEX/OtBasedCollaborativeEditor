package ot.external

external class StompClient {

    fun connect(
        unknown: dynamic,
        onConnected: (Unit) -> Unit,
        onError: (dynamic) -> Unit
    )

    fun subscribe(
        topic: String,
        onMessageReceived: (dynamic) -> Unit
    )

    fun send(
        destination: String,
        unknown: dynamic,
        payload: String
    )

    fun disconnect(onDisconnected: (Unit) -> Unit)
}