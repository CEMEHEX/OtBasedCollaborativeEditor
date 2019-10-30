package ot.repository

import ot.service.Operation

/**
 *  Storage for documents operations history
 *
 * @param O operation type
 */
interface OperationsRepository<O : Operation<*>> {

    fun operationsCount(documentUUID: String): Int

    fun addOperation(documentUUID: String, operation: O)

    /**
     * Obtain all operations with revision greater then or equal to specified
     */
    fun getOperationsWithRevisionGte(documentUUID: String, revision: Int): Collection<O>

    fun removeDocumentOperations(documentUUID: String)

}