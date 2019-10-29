package ot.service.impl

import ot.service.DocumentOperationsHistoryService
import ot.service.Operation
import java.util.concurrent.ConcurrentMap

class InMemoryDocumentOperationsHistoryService<O : Operation<*>>(
    private val operations: ConcurrentMap<String, List<O>>
) : DocumentOperationsHistoryService<O> {

    override fun operationsCount(documentUUID: String): Int = operations[documentUUID]?.size ?: 0

    override fun addOperation(documentUUID: String, operation: O) {
        operations[documentUUID]?.plus(operation) ?: operations.put(documentUUID, listOf(operation))
    }

    override fun getConcurrentOperations(
        documentUUID: String,
        revision: Int
    ): Collection<O> = operations[documentUUID]?.drop(revision) ?: emptyList()
}