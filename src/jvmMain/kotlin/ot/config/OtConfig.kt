package ot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.entity.PlainTextDocument
import ot.repository.DocumentRepository
import ot.repository.OperationsRepository
import ot.repository.impl.InMemoryOperationsRepository
import ot.repository.impl.InMemoryPlainTextDocumentRepository
import ot.service.*
import ot.service.impl.*
import java.util.concurrent.ConcurrentHashMap

@Configuration
class OtConfig {

    @Bean
    fun uuidGenerator(): IdGenerator<String> = UuidGenerator()

    @Bean
    fun plainTextSingleCharacterOperationsManager(
        uuidGenerator: IdGenerator<String>
    ): PlainTextOperationsManager = PlainTextOperationsManager(uuidGenerator)

    @Bean
    fun plainTextDocumentStorage(): DocumentRepository<PlainTextDocument> = InMemoryPlainTextDocumentRepository(
        ConcurrentHashMap<String, PlainTextDocument>().apply {
            put("1", PlainTextDocument("1", 0, "", "Document 1"))
            put("2", PlainTextDocument("2", 0, "kek", "Document 2"))
        }
    )

    @Bean
    fun plainTextDocumentOperationsHistoryManager(
    ): OperationsRepository<PlainTextSingleCharacterOperation> =
        InMemoryOperationsRepository(ConcurrentHashMap())

    @Bean
    fun plainTextDocumentUpdater(): DocumentUpdater<String, PlainTextDocument> =
        PlainTextDocumentUpdater()

    @Bean
    fun documentStorageService(
        documentRepository: DocumentRepository<PlainTextDocument>,
        operationsRepository: OperationsRepository<PlainTextSingleCharacterOperation>,
        documentUpdater: DocumentUpdater<String, PlainTextDocument>
    ): DocumentStorageService<String, PlainTextSingleCharacterOperation, PlainTextDocument> =
        DocumentStorageServiceImpl(documentRepository, operationsRepository, documentUpdater)

    @Bean
    fun plainTextServerDocumentManager(
        operationsManager: OperationsManager<PlainTextSingleCharacterOperation>,
        documentStorageService: DocumentStorageService<String, PlainTextSingleCharacterOperation, PlainTextDocument>
    ): ServerDocumentManager<String, PlainTextSingleCharacterOperation, PlainTextDocument> =
        ServerDocumentManagerImpl(operationsManager, documentStorageService)

}