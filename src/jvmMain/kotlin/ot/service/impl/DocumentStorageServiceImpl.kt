package ot.service.impl

import ot.entity.Document
import ot.repository.DocumentRepository
import ot.repository.OperationsRepository
import ot.service.DocumentStorageService
import ot.service.DocumentUpdater
import ot.service.Operation

class DocumentStorageServiceImpl<T, O : Operation<T>, D : Document<T>>(
    private val documentRepository: DocumentRepository<D>,
    private val operationsStorage: OperationsRepository<O>,
    private val documentUpdater: DocumentUpdater<T, D>
) : DocumentStorageService<T, O, D> {

    override fun getConcurrentOperations(documentUUID: String, revision: Int): Collection<O> =
        operationsStorage.getOperationsWithRevisionGte(documentUUID, revision)

    override fun getRevision(documentUUID: String): Int = operationsStorage.operationsCount(documentUUID)

    override fun updateDocumentWithOperation(document: D, modifiedContent: T, operation: O) {
        val updatedDocument = documentUpdater.updateContentAndRevision(
            document = document,
            content = modifiedContent,
            revision = document.revision + 1
        )
        documentRepository.save(updatedDocument)
        operationsStorage.addOperation(document.uuid, operation)
    }

    override fun getDocumentByUUID(uuid: String): D = documentRepository.getDocumentByUUID(uuid)

    override fun getDocumentsList(): List<D> = documentRepository.getDocumentsList()

    override fun save(document: D) = documentRepository.save(document)

    override fun remove(documentUUID: String) {
        documentRepository.remove(documentUUID)
        operationsStorage.removeDocumentOperations(documentUUID)
    }
}
