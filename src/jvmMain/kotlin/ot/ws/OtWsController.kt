package ot.ws

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import ot.entity.PlainTextDocument
import ot.service.ServerDocumentManager
import ot.service.impl.PlainTextSingleCharacterOperation
import java.util.concurrent.ExecutorService

val logger: Logger = LoggerFactory.getLogger(OtWsController::class.java)

@Controller
class OtWsController(
    private val serverDocumentManager: ServerDocumentManager<String, PlainTextSingleCharacterOperation, PlainTextDocument>,
    @Qualifier("SingleThreadExecutor") private val singleThreadExecutor: ExecutorService
) {

    @MessageMapping("/document/{documentId}/revision/{revision}")
    @SendTo("/topic/public/operation/{documentId}")
    fun sendMessage(
        @Payload operation: PlainTextSingleCharacterOperation,
        @DestinationVariable documentId: Long,
        @DestinationVariable revision: Int
    ): PlainTextSingleCharacterOperation =
        singleThreadExecutor.submit<PlainTextSingleCharacterOperation> {
            logger.debug(
                """Operation received:
                $operation
                Client revision: $revision
                Document: ${serverDocumentManager.getDocument(documentId)}
            """
            )
            return@submit serverDocumentManager.receiveOperation(documentId, revision, operation).also {
                logger.debug(
                    """Transformed operation:
                        $it
                        Document after operation applied: ${serverDocumentManager.getDocument(documentId)}
                    """
                )
            }
        }.get() // TODO use reactive websockets

}