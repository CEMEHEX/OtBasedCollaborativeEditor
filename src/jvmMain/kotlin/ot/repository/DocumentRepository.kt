package ot.repository

import ot.entity.Document

/**
 *  Storage for documents
 *
 * @param D document type
 */
interface DocumentRepository<D : Document<*>> {

    fun getDocumentByUUID(uuid: String): D

    fun getDocumentsList(): List<D>

    fun save(document: D)

    fun remove(documentUUID: String)

}