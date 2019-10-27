package ot.service

import ot.entity.Document

/**
 *  Storage for documents
 *
 * @param D document type
 */
interface DocumentStorageService<D : Document<*>> {

    fun getDocumentById(id: Long): D

    fun save(document: D)

}