package ot.service

import ot.entity.Document

/**
 *  Storage for documents
 *
 * @param D document type
 */
interface DocumentStorageService<D : Document<*>> {

    fun getDocumentByUUID(uuid: String): D

    fun save(document: D)

    fun getDocumentsList(): List<D>

}