package ot.service.impl

import ot.service.DocumentStorage
import server.entity.Document
import server.exception.DocumentNotFoundException

class InMemoryDocumentStorage<T>(
        private val documents: MutableMap<Long, Document<T>>
) : DocumentStorage<T> {

    override fun getDocumentById(id: Long): Document<T> =
            documents[id] ?: throw DocumentNotFoundException(id)

    override fun save(document: Document<T>) {
        documents[document.id] = document
    }
}