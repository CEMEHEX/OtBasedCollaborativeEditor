package ot.ws

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import ot.impl.PlainTextSingleCharacterOperation
import ot.service.ServerDocumentManager

val logger = LoggerFactory.getLogger(OtWsController::class.java)

@Controller
class OtWsController(
    val serverDocumentManager: ServerDocumentManager<String, PlainTextSingleCharacterOperation>
) {

    @MessageMapping("/{documentId}/operation")
    @SendTo("/topic/public/operation/{documentId}")
    fun sendMessage(
        @Payload operation: PlainTextSingleCharacterOperation,
        @DestinationVariable documentId: Long
    ): PlainTextSingleCharacterOperation = serverDocumentManager.receiveOperation(documentId, operation)
        .also { logger.info("Current document: ${serverDocumentManager.getDocument(documentId)}") }

}