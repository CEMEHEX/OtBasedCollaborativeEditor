package ot.service.impl

import ot.service.DocumentStorageService
import ot.entity.PlainTextDocument
import ot.exception.DocumentNotFoundException

class InMemoryPlainTextDocumentStorageService(
        private val documents: MutableMap<Long, PlainTextDocument>
) : DocumentStorageService<PlainTextDocument> {

    override fun getDocumentById(id: Long): PlainTextDocument =
            documents[id] ?: throw DocumentNotFoundException(id)

    override fun save(document: PlainTextDocument) {
        documents[document.id] = document
    }
}