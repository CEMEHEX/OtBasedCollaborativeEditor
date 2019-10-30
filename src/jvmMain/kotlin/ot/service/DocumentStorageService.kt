package ot.service

import ot.entity.Document

interface DocumentStorageService<T, O: Operation<T>, D : Document<T>> {

    /**
     * Obtain all operations received after specified revision
     *
     * @param documentUUID id of document from which to obtain concurrent operations
     * @param revision revision number from which to obtain concurrent operations
     *
     * @return concurrent operations
     */
    fun getConcurrentOperations(documentUUID: String, revision: Int): Collection<O>

    fun getRevision(documentUUID: String): Int

    fun updateDocumentWithOperation(
        document: D,
        modifiedContent: T,
        operation: O
    )

    fun getDocumentByUUID(uuid: String): D

    fun getDocumentsList(): List<D>

    fun save(document: D)

    fun remove(documentUUID: String)
}