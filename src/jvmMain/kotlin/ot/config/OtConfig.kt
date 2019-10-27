package ot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.service.OperationsManager
import ot.service.impl.PlainTextOperationsManager
import ot.service.impl.PlainTextSingleCharacterOperation
import ot.service.DocumentOperationsHistoryService
import ot.service.DocumentStorageService
import ot.service.DocumentUpdater
import ot.service.ServerDocumentManager
import ot.service.impl.InMemoryDocumentOperationsHistoryService
import ot.service.impl.InMemoryPlainTextDocumentStorageService
import ot.service.impl.PlainTextDocumentUpdater
import ot.service.impl.ServerDocumentManagerImpl
import ot.entity.PlainTextDocument

@Configuration
class OtConfig {

    @Bean
    fun plainTextSingleCharacterOperationsManager(): PlainTextOperationsManager = PlainTextOperationsManager()

    @Bean
    fun plainTextDocumentStorage(): DocumentStorageService<PlainTextDocument> =
        InMemoryPlainTextDocumentStorageService(
            mutableMapOf(
                1L to PlainTextDocument(1, 0, "abc")
            )
        )

    @Bean
    fun plainTextDocumentOperationsHistoryManager(
    ): DocumentOperationsHistoryService<PlainTextSingleCharacterOperation> =
        InMemoryDocumentOperationsHistoryService(mutableMapOf())

    @Bean
    fun plainTextDocumentUpdater() : DocumentUpdater<String, PlainTextDocument> =
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