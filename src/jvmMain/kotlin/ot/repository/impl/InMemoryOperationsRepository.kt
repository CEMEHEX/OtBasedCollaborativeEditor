package ot.repository.impl

import ot.repository.OperationsRepository
import ot.service.Operation
import java.util.concurrent.ConcurrentMap

class InMemoryOperationsRepository<O : Operation<*>>(
    private val operations: ConcurrentMap<String, List<O>>
) : OperationsRepository<O> {
    override fun operationsCount(documentUUID: String): Int = operations[documentUUID]?.size ?: 0

    override fun addOperation(documentUUID: String, operation: O) {
        operations.compute(documentUUID) { _, list -> list?.let { it + operation } ?: listOf(operation) }
    }

    override fun getOperationsWithRevisionGte(
        documentUUID: String,
        revision: Int
    ): Collection<O> = operations[documentUUID]?.drop(revision) ?: emptyList()

    override fun removeDocumentOperations(documentUUID: String) {
        operations.remove(documentUUID)
    }
}