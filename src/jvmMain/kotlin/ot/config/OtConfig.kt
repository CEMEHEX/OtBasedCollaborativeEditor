package ot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.OperationsManager
import ot.impl.PlainTextOperationsManager
import ot.impl.PlainTextSingleCharacterOperation
import ot.service.DocumentOperationsHistoryService
import ot.service.DocumentStorageService
import ot.service.ServerDocumentManager
import ot.service.impl.InMemoryDocumentOperationsHistoryService
import ot.service.impl.InMemoryDocumentStorageService
import ot.service.impl.ServerDocumentManagerImpl
import server.entity.Document

@Configuration
class OtConfig {

    @Bean
    fun plainTextSingleCharacterOperationsManager(): PlainTextOperationsManager = PlainTextOperationsManager()

    @Bean
    fun plainTextDocumentStorage(): DocumentStorageService<String> = InMemoryDocumentStorageService(mutableMapOf(
        1L to Document(1, 0, "abc")
    ))

    @Bean
    fun plainTextDocumentOperationsHistoryManager(
    ): DocumentOperationsHistoryService<PlainTextSingleCharacterOperation> =
        InMemoryDocumentOperationsHistoryService(mutableMapOf())

    @Bean
    fun plainTextServerDocumentManager(
        operationsManager: OperationsManager<PlainTextSingleCharacterOperation>,
        documentOperationsHistoryService: DocumentOperationsHistoryService<PlainTextSingleCharacterOperation>,
        documentStorageService: DocumentStorageService<String>
    ): ServerDocumentManager<String, PlainTextSingleCharacterOperation> =
        ServerDocumentManagerImpl(documentOperationsHistoryService, operationsManager, documentStorageService)

}