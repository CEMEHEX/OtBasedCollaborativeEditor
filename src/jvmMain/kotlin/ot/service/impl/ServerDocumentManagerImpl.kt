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

    override fun getRevision(documentUUID: String) = operationsStorage.operationsCount(documentUUID)

    override fun getDocument(documentUUID: String): D = documentStorageService.getDocumentByUUID(documentUUID)

    override fun receiveOperation(
        documentUUID: String,
        revision: Int,
        operation: O
    ): O {
        val concurrentOperations = operationsStorage.getConcurrentOperations(documentUUID, revision)
        // transform received operation against each concurrent operation
        val transformedOperation = concurrentOperations.transformAgainstEach(operation, operationsManager)
        val document = getDocument(documentUUID)
        val updatedContent = transformedOperation.applyTo(document.content)
        val updatedDocument = documentUpdater.updateContentAndRevision(
            document = document,
            content = updatedContent,
            revision = getRevision(documentUUID) + 1
        )
        documentStorageService.save(updatedDocument)
        operationsStorage.addOperation(documentUUID, transformedOperation)
        return transformedOperation
    }

}