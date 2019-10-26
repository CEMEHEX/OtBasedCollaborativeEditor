package ot.service.impl

import ot.service.DocumentStorageService
import server.entity.Document
import server.exception.DocumentNotFoundException

class InMemoryDocumentStorageService<T>(
        private val documents: MutableMap<Long, Document<T>>
) : DocumentStorageService<T> {

    override fun getDocumentById(id: Long): Document<T> =
            documents[id] ?: throw DocumentNotFoundException(id)

    override fun save(document: Document<T>) {
        documents[document.id] = document
    }
}