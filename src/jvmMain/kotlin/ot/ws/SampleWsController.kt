package ot.ws

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import ot.impl.PlainTextSingleCharacterOperation
import ot.service.ServerDocumentManager

@Controller
class OtWsController(
    val serverDocumentManager: ServerDocumentManager<String, PlainTextSingleCharacterOperation>
) {

    @MessageMapping("/{documentId}/operation")
    @SendTo("/topic/public/operation/{documentId}")
    fun sendMessage(
        @Payload operation: String,
        @DestinationVariable documentId: String
    ): PlainTextSingleCharacterOperation = TODO()/*serverDocumentManager.receiveOperation(
        documentId.toLong(),
        operation
    )*/

}