package ot.config

import org.w3c.dom.HTMLTextAreaElement
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.fsm.ClientFSM
import ot.fsm.ClientFSMImpl
import ot.fsm.SynchronizedState
import ot.service.ClientDocumentManager
import ot.service.IdGenerator
import ot.service.impl.*
import ot.util.validateNotNull
import kotlin.browser.document


object DemoAppConfig {

    val textAreaElement = document.querySelector("textarea").validateNotNull() as HTMLTextAreaElement

    val diffMatchPatch = diff_match_patch()
    val idGenerator: IdGenerator<String> = UuidGenerator()
    val diffToOperationsDecomposer = PlainTextDiffToSingleCharOperationsDecomposer(diffMatchPatch, idGenerator)

    val operationSerializer = PlainTextSingleCharacterOperationJsonSerializer()
    val operationDeserializer = PlainTextSingleCharacterOperationJsonDeserializer()

    val socket = SockJS("/ws")
    val stompClient = Stomp.over(socket)

    val operationsManager = PlainTextOperationsManager(idGenerator)
    val clientFsm: ClientFSM<String, PlainTextSingleCharacterOperation> =
        ClientFSMImpl<String, PlainTextSingleCharacterOperation>(0)
            .apply { state = SynchronizedState(this, operationsManager) }

    val clientDocumentManager: ClientDocumentManager<String, PlainTextSingleCharacterOperation> = ClientDocumentManagerImpl(
        clientFsm
    )
}