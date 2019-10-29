package ot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.entity.PlainTextDocument
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
    fun plainTextDocumentStorage(): DocumentStorageService<PlainTextDocument> {
        val uuid = "1"
        return InMemoryPlainTextDocumentStorageService(
            ConcurrentHashMap<String, PlainTextDocument>().apply {
                put(uuid, PlainTextDocument(uuid, 0, ""))
            }
        )
    }

    @Bean
    fun plainTextDocumentOperationsHistoryManager(
    ): DocumentOperationsHistoryService<PlainTextSingleCharacterOperation> =
        InMemoryDocumentOperationsHistoryService(ConcurrentHashMap())

    @Bean
    fun plainTextDocumentUpdater(): DocumentUpdater<String, PlainTextDocument> =
        PlainTextDocumentUpdater()

    @Bean
    fun plainTextServerDocumentManager(
        operationsManager: OperationsManager<PlainTextSingleCharacterOperation>,
        documentOperationsHistoryService: DocumentOperationsHistoryService<PlainTextSingleCharacterOperation>,
        documentStorageService: DocumentStorageService<PlainTextDocument>,
        documentUpdater: DocumentUpdater<String, PlainTextDocument>
    ): ServerDocumentManager<String, PlainTextSingleCharacterOperation, PlainTextDocument> =
        ServerDocumentManagerImpl(
            documentOperationsHistoryService,
            operationsManager,
            documentStorageService,
            documentUpdater
        )

}