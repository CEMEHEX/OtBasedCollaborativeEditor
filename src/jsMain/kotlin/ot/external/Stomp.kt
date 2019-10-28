package ot.external

external class Stomp {
    companion object {
        fun over(webSocket: SockJS): StompClient
    }
}