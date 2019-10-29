package ot.service.impl

import ot.entity.Document
import ot.service.*
import ot.util.transformAgainstEach

class ServerDocumentManagerImpl<T, O : Operation<T>, D : Document<T>>(
    private val operationsStorage: DocumentOperationsHistoryService<O>,
    private val operationsManager: OperationsManager<O>,
    private val documentStorageService: DocumentStorageService<D>,
    private val documentUpdater: DocumentUpdater<T, D>
) : ServerDocumentManager<T, O, D> {

    override fun getRevision(documentId: Long) = operationsStorage.operationsCount(documentId)

    override fun getDocument(documentId: Long): D = documentStorageService.getDocumentById(documentId)

    override fun receiveOperation(
        documentId: Long,
        revision: Int,
        operation: O
    ): O {
        val concurrentOperations = operationsStorage.getConcurrentOperations(documentId, revision)
        // transform received operation against each concurrent operation
        val transformedOperation = concurrentOperations.transformAgainstEach(operation, operationsManager)
        val document = getDocument(documentId)
        val updatedContent = transformedOperation.applyTo(document.content)
        val updatedDocument = documentUpdater.updateContentAndRevision(
            document = document,
            content = updatedContent,
            revision = getRevision(documentId) + 1
        )
        documentStorageService.save(updatedDocument)
        operationsStorage.addOperation(documentId, transformedOperation)
        return transformedOperation
    }

}