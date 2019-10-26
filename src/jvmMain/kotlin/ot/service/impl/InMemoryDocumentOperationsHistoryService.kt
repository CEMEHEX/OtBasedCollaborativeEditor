package ot.service.impl

import ot.Operation
import ot.service.DocumentOperationsHistoryService

class InMemoryDocumentOperationsHistoryService<O : Operation<*>>(
    private val operations: MutableMap<Long, MutableList<O>>
) : DocumentOperationsHistoryService<O> {

    override fun operationsCount(documentId: Long): Int = operations[documentId]?.size ?: 0

    override fun addOperation(documentId: Long, operation: O) {
        operations[documentId]?.add(operation) ?: operations.put(documentId, mutableListOf(operation))
    }

    override fun getConcurrentOperations(
        documentId: Long,
        revision: Int
    ): Collection<O> = operations[documentId]?.drop(revision) ?: emptyList()
}