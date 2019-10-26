package ot.service

import server.entity.Document

/**
 *  Storage for documents
 *
 * @param T document content type
 */
interface DocumentStorage<T> {

    fun getDocumentById(id: Long): Document<T>

    fun save(document: Document<T>)

}