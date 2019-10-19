package server.service.impl

import ot.Operation
import ot.OperationsManager
import ot.util.transformAgainstEach
import server.entity.Document
import server.service.DocumentOperationsHistoryManager
import server.service.DocumentStorage
import server.service.ServerDocumentManager


class ServerDocumentManagerImpl<T, O : Operation<T>>(
    private val documentId: Long,
    private val operationsStorage: DocumentOperationsHistoryManager<O>,
    private val operationsManager: OperationsManager<O>,
    private val documentStorage: DocumentStorage<T>
) : ServerDocumentManager<T, O> {

    override val revision: Int
        get() = operationsStorage.operationsCount(documentId)

    override fun getDocument(): Document<T> = documentStorage.getDocumentById(documentId)

    override fun receiveOperation(operation: O): O {
        val concurrentOperations = operationsStorage.getConcurrentOperations(documentId, operation.revision)
        // transform received operation against each concurrent operation
        val transformedOperation = concurrentOperations.transformAgainstEach(operation, operationsManager)
        val document = getDocument()
        val updatedContent = transformedOperation.applyTo(document.content)
        val updatedDocument = document.copy(content = updatedContent, revision = revision + 1)
        documentStorage.save(updatedDocument)
        val updatedOperation = operationsManager.changeRevision(
            transformedOperation,
            revision
        )
        operationsStorage.addOperation(documentId, updatedOperation)
        return updatedOperation
    }
}