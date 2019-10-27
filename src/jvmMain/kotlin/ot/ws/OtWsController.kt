package ot.ws

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import ot.impl.PlainTextSingleCharacterOperation
import ot.service.ServerDocumentManager

val logger: Logger = LoggerFactory.getLogger(OtWsController::class.java)

@Controller
class OtWsController(
    private val serverDocumentManager: ServerDocumentManager<String, PlainTextSingleCharacterOperation>
) {

    @MessageMapping("/{documentId}/operation")
    @SendTo("/topic/public/operation/{documentId}")
    fun sendMessage(
        @Payload operation: PlainTextSingleCharacterOperation,
        @DestinationVariable documentId: Long
    ): PlainTextSingleCharacterOperation {
        logger.debug(
            """Operation received: 
                $operation
                Document: ${serverDocumentManager.getDocument(documentId)}
            """
        )
        return serverDocumentManager.receiveOperation(documentId, operation).also {
            logger.debug(
                """Transformed operation: 
                    $it
                    Document after operation applied: ${serverDocumentManager.getDocument(documentId)}
                """
            )
        }
    }

}