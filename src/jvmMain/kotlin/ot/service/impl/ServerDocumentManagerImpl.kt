package ot.service.impl

import ot.Operation
import ot.OperationsManager
import ot.service.DocumentOperationsHistoryManager
import ot.service.DocumentStorage
import ot.service.ServerDocumentManager
import ot.util.transformAgainstEach
import server.entity.Document

class ServerDocumentManagerImpl<T, O : Operation<T>>(
    private val operationsStorage: DocumentOperationsHistoryManager<O>,
    private val operationsManager: OperationsManager<O>,
    private val documentStorage: DocumentStorage<T>
) : ServerDocumentManager<T, O> {

    override fun getRevision(documentId: Long) = operationsStorage.operationsCount(documentId)

    override fun getDocument(documentId: Long): Document<T> = documentStorage.getDocumentById(documentId)

    override fun receiveOperation(documentId: Long, operation: O): O {
        val concurrentOperations = operationsStorage.getConcurrentOperations(documentId, operation.revision)
        // transform received operation against each concurrent operation
        val transformedOperation = concurrentOperations.transformAgainstEach(operation, operationsManager)
        val document = getDocument(documentId)
        val updatedContent = transformedOperation.applyTo(document.content)
        val updatedDocument = document.copy(content = updatedContent, revision = getRevision(documentId) + 1)
        documentStorage.save(updatedDocument)
        val updatedOperation = operationsManager.changeRevision(
            transformedOperation,
            getRevision(documentId)
        )
        operationsStorage.addOperation(documentId, updatedOperation)
        return updatedOperation
    }
}