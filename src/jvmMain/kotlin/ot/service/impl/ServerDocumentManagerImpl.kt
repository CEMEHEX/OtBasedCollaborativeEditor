package ot.service.impl

import ot.entity.Document
import ot.service.DocumentStorageService
import ot.service.Operation
import ot.service.OperationsManager
import ot.service.ServerDocumentManager
import ot.util.transformAgainstEach

class ServerDocumentManagerImpl<T, O : Operation<T>, D : Document<T>>(
    private val operationsManager: OperationsManager<O>,
    private val documentStorageService: DocumentStorageService<T, O, D>
) : ServerDocumentManager<T, O, D> {

    override fun getRevision(documentUUID: String) = documentStorageService.getRevision(documentUUID)

    override fun getDocument(documentUUID: String): D = documentStorageService.getDocumentByUUID(documentUUID)

    override fun receiveOperation(
        documentUUID: String,
        revision: Int,
        operation: O
    ): O {
        val concurrentOperations = documentStorageService.getConcurrentOperations(documentUUID, revision)
        // transform received operation against each concurrent operation
        val transformedOperation = concurrentOperations.transformAgainstEach(operation, operationsManager)
        val document = getDocument(documentUUID)
        val updatedContent = transformedOperation.applyTo(document.content)
        documentStorageService.updateDocumentWithOperation(document, updatedContent, transformedOperation)
        return transformedOperation
    }

}