package ot.service.impl

import ot.entity.PlainTextDocument
import ot.exception.DocumentNotFoundException
import ot.service.DocumentStorageService
import java.util.concurrent.ConcurrentMap

class InMemoryPlainTextDocumentStorageService(
        private val documents: ConcurrentMap<Long, PlainTextDocument>
) : DocumentStorageService<PlainTextDocument> {

    override fun getDocumentById(id: Long): PlainTextDocument =
            documents[id] ?: throw DocumentNotFoundException(id)

    override fun save(document: PlainTextDocument) {
        documents[document.id] = document
    }
}