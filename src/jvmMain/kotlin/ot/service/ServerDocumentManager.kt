package ot.service

/**
 * Generic OT based server engine for collaborative editing
 *
 * @param T document content type
 * @param O operations type
 * @param D document type
 */
interface ServerDocumentManager<T, O : Operation<T>, D> {

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
    fun getDocument(documentId: Long): D

    /**
     * @return latest document revision
     */
    fun getRevision(documentId: Long): Int

}