package ot.service.impl

import ot.service.DocumentOperationsHistoryService
import ot.service.Operation
import java.util.concurrent.ConcurrentMap

class InMemoryDocumentOperationsHistoryService<O : Operation<*>>(
    private val operations: ConcurrentMap<Long, List<O>>
) : DocumentOperationsHistoryService<O> {

    override fun operationsCount(documentId: Long): Int = operations[documentId]?.size ?: 0

    override fun addOperation(documentId: Long, operation: O) {
        operations[documentId]?.plus(operation) ?: operations.put(documentId, listOf(operation))
    }

    override fun getConcurrentOperations(
        documentId: Long,
        revision: Int
    ): Collection<O> = operations[documentId]?.drop(revision) ?: emptyList()
}