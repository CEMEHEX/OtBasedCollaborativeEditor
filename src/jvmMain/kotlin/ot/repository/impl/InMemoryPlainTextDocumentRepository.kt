package ot.repository.impl

import ot.entity.PlainTextDocument
import ot.exception.DocumentNotFoundException
import ot.repository.DocumentRepository
import java.util.concurrent.ConcurrentMap

class InMemoryPlainTextDocumentRepository(
        private val documents: ConcurrentMap<String, PlainTextDocument>
) : DocumentRepository<PlainTextDocument> {
    override fun getDocumentByUUID(uuid: String): PlainTextDocument =
            documents[uuid] ?: throw DocumentNotFoundException(uuid)

    override fun save(document: PlainTextDocument) {
        documents[document.uuid] = document
    }

    override fun getDocumentsList(): List<PlainTextDocument> = documents.values.toList()

    override fun remove(documentUUID: String) {
        documents.remove(documentUUID)
    }
}