package server.service

import ot.Operation

/**
 *  Storage for documents operations history
 *
 * @param O operation type
 */
interface DocumentOperationsHistoryManager<O : Operation<*>> {

    fun operationsCount(documentId: Long): Int

    fun addOperation(documentId: Long, operation: O)

    /**
     * Obtain all operations received after specified revision
     *
     * @param documentId id of document from which to obtain concurrent operations
     * @param revision revision number from which to obtain concurrent operations
     *
     * @return concurrent operations
     */
    fun getConcurrentOperations(documentId: Long, revision: Int): Collection<O>

}