package ot.service.impl

import ot.entity.PlainTextDocument
import ot.exception.DocumentNotFoundException
import ot.service.DocumentStorageService
import java.util.concurrent.ConcurrentMap

class InMemoryPlainTextDocumentStorageService(
        private val documents: ConcurrentMap<String, PlainTextDocument>
) : DocumentStorageService<PlainTextDocument> {

    override fun getDocumentByUUID(uuid: String): PlainTextDocument =
            documents[uuid] ?: throw DocumentNotFoundException(uuid)

    override fun save(document: PlainTextDocument) {
        documents[document.uuid] = document
    }
}