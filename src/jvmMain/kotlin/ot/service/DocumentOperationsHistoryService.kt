package ot.service

/**
 *  Storage for documents operations history
 *
 * @param O operation type
 */
interface DocumentOperationsHistoryService<O : Operation<*>> {

    fun operationsCount(documentUUID: String): Int

    fun addOperation(documentUUID: String, operation: O)

    /**
     * Obtain all operations received after specified revision
     *
     * @param documentUUID id of document from which to obtain concurrent operations
     * @param revision revision number from which to obtain concurrent operations
     *
     * @return concurrent operations
     */
    fun getConcurrentOperations(documentUUID: String, revision: Int): Collection<O>

}