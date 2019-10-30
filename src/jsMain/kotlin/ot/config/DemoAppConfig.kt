package ot.config

import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.HTMLUListElement
import ot.external.SockJS
import ot.external.Stomp
import ot.external.diff_match_patch
import ot.service.IdGenerator
import ot.service.impl.*
import ot.util.validateNotNull
import kotlin.browser.document


object DemoAppConfig {

    val textAreaElement = document.querySelector("#textarea").validateNotNull() as HTMLTextAreaElement
    val documentsListElement = document.querySelector("#documentsList").validateNotNull() as HTMLUListElement
    val documentTitleElement = document.querySelector("#documentTitle").validateNotNull() as HTMLLIElement

    val diffMatchPatch = diff_match_patch()
    val idGenerator: IdGenerator<String> = UuidGenerator()
    val diffToOperationsDecomposer = PlainTextDiffToSingleCharOperationsDecomposer(diffMatchPatch, idGenerator)

    val operationSerializer = PlainTextSingleCharacterOperationJsonSerializer()
    val operationDeserializer = PlainTextSingleCharacterOperationJsonDeserializer()

    val socket = SockJS("/ws")
    val stompClient = Stomp.over(socket)

    val operationsManager = PlainTextOperationsManager(idGenerator)
}