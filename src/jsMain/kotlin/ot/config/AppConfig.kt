package ot.config

import org.w3c.dom.HTMLTextAreaElement
import ot.external.SockJS
import ot.external.StompClient
import ot.external.diff_match_patch
import ot.fsm.ClientFSM
import ot.service.*
import ot.service.impl.PlainTextSingleCharacterOperation

interface AppConfig<T, O: Operation<T>, ID, R> {
    val textAreaElement: HTMLTextAreaElement

    val diffMatchPatch: diff_match_patch
    val idGenerator: IdGenerator<ID>
    val diffToOperationsDecomposer: DiffToOperationsDecomposer<T, O>

    val operationSerializer: OperationSerializer<O, R>
    val operationDeserializer: OperationDeserializer<O, R>

    val socket: SockJS
    val stompClient: StompClient

    val operationsManager: OperationsManager<O>
    val clientFsm: ClientFSM<String, PlainTextSingleCharacterOperation>
    val clientDocumentManager: ClientDocumentManager<String, PlainTextSingleCharacterOperation>
}