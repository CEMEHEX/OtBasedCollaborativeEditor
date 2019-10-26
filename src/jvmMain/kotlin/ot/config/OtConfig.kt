package ot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ot.OperationsManager
import ot.impl.PlainTextOperationsManager
import ot.impl.PlainTextSingleCharacterOperation
import ot.service.DocumentOperationsHistoryManager
import ot.service.DocumentStorage
import ot.service.ServerDocumentManager
import ot.service.impl.InMemoryDocumentOperationsHistoryManager
import ot.service.impl.InMemoryDocumentStorage
import ot.service.impl.ServerDocumentManagerImpl

@Configuration
class OtConfig {

    @Bean
    fun plainTextSingleCharacterOperationsManager(): PlainTextOperationsManager = PlainTextOperationsManager()

    @Bean
    fun plainTextDocumentStorage(): DocumentStorage<String> = InMemoryDocumentStorage(mutableMapOf())

    @Bean
    fun plainTextDocumentOperationsHistoryManager(
    ): DocumentOperationsHistoryManager<PlainTextSingleCharacterOperation> =
        InMemoryDocumentOperationsHistoryManager(mutableMapOf())

    @Bean
    fun plainTextServerDocumentManager(
        operationsManager: OperationsManager<PlainTextSingleCharacterOperation>,
        documentOperationsHistoryManager: DocumentOperationsHistoryManager<PlainTextSingleCharacterOperation>,
        documentStorage: DocumentStorage<String>
    ): ServerDocumentManager<String, PlainTextSingleCharacterOperation> =
        ServerDocumentManagerImpl(documentOperationsHistoryManager, operationsManager, documentStorage)

}