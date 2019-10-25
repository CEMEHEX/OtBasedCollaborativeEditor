package ot.ws

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class ChatController {

    @MessageMapping("/chat.sendMessage/{id}")
    @SendTo("/topic/public/{id}")
    fun sendMessage(
        @Payload chatMessage: String,
        @DestinationVariable id: String
        ): String {
        return chatMessage
    }

}