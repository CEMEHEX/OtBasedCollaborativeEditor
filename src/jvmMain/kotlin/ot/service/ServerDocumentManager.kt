package ot.service

import ot.Operation
import server.entity.Document

/**
 * Generic OT based server engine for collaborative editing
 *
 * @param T document content type
 * @param O operations type
 */
interface ServerDocumentManager<T, O : Operation<T>> {

    /**
     * Process operation from client
     *
     * @param operation operation from client
     * @return transformed operation
     */
    fun receiveOperation(documentId: Long, operation: O): O

    /**
     * @return latest version of document
     */
    fun getDocument(documentId: Long): Document<T>

    /**
     * @return latest document revision
     */
    fun getRevision(documentId: Long): Int

}