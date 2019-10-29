package ot.config

import org.w3c.dom.HTMLTextAreaElement
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.fsm.ClientFSM
import ot.fsm.ClientFSMImpl
import ot.fsm.SynchronizedState
import ot.service.ClientDocumentManager
import ot.service.impl.*
import ot.util.validateNotNull
import kotlin.browser.document
import kotlin.js.Json


object DemoAppConfig : AppConfig<String, PlainTextSingleCharacterOperation, Long, Json> {

    override val textAreaElement = document.querySelector("textarea").validateNotNull() as HTMLTextAreaElement

    override val diffMatchPatch = diff_match_patch()
    override val idGenerator = LongSequentialIdGenerator()
    override val diffToOperationsDecomposer = PlainTextDiffToSingleCharOperationsDecomposer(diffMatchPatch, idGenerator)

    override val operationSerializer = PlainTextSingleCharacterOperationJsonSerializer()
    override val operationDeserializer = PlainTextSingleCharacterOperationJsonDeserializer()

    override val socket = SockJS("/ws")
    override val stompClient = Stomp.over(socket)

    override val operationsManager = PlainTextOperationsManager()
    override val clientFsm: ClientFSM<String, PlainTextSingleCharacterOperation> =
        ClientFSMImpl<String, PlainTextSingleCharacterOperation>(0)
            .apply { state = SynchronizedState(this, operationsManager) }

    override val clientDocumentManager: ClientDocumentManager<String, PlainTextSingleCharacterOperation> = ClientDocumentManagerImpl(
        clientFsm
    )
}