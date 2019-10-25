package ot.ws

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.TopicProcessor

@Component
class SampleHandler : WebSocketHandler {
    private val processor = TopicProcessor.share<String>("shared", 1024)

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session
            .send(processor.map { msg ->
                session.textMessage("message: $msg")
            })
            .and(session.receive()
                .map { msg -> "received ${msg.payloadAsText}" }
                .doOnNext { ev -> processor.onNext(ev) }
            )
    }
}