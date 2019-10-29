package ot.service.impl

import ot.service.IdGenerator
import ot.service.OperationsManager

class PlainTextOperationsManager(
    private val idGenerator: IdGenerator<String>
) : OperationsManager<PlainTextSingleCharacterOperation> {

    override fun transformAgainst(
        operation1: PlainTextSingleCharacterOperation,
        operation2: PlainTextSingleCharacterOperation
    ): PlainTextSingleCharacterOperation = when (operation1) {
        is InsertOperation -> when (operation2) {
            is InsertOperation -> transformInsertAgainstInsert(operation1, operation2)
            is DeleteOperation -> transformInsertAgainstDelete(operation1, operation2)
            is IdentityOperation -> operation1
        }
        is DeleteOperation -> when (operation2) {
            is InsertOperation -> transformDeleteAgainstInsert(operation1, operation2)
            is DeleteOperation -> transformDeleteAgainstDelete(operation1, operation2)
            is IdentityOperation -> operation1
        }
        is IdentityOperation -> operation1
    }

    override fun invert(operation: PlainTextSingleCharacterOperation): PlainTextSingleCharacterOperation =
        when (operation) {
            is InsertOperation -> {
                val (_, position, symbol) = operation
                DeleteOperation(idGenerator.generateId(), position, symbol)
            }
            is DeleteOperation -> {
                val (_, position, symbol) = operation
                InsertOperation(idGenerator.generateId(), position, symbol)
            }
            is IdentityOperation -> IdentityOperation(idGenerator.generateId())
        }

    private fun transformInsertAgainstInsert(
        operation1: InsertOperation,
        operation2: InsertOperation
    ): PlainTextSingleCharacterOperation = when {
        operation1.position < operation2.position -> operation1
        else -> operation1.copy(position = operation1.position + 1)
    }

    private fun transformInsertAgainstDelete(
        operation1: InsertOperation,
        operation2: DeleteOperation
    ): PlainTextSingleCharacterOperation = when {
        operation1.position <= operation2.position -> operation1
        else -> InsertOperation(operation1.uuid, operation1.position - 1, operation1.symbol)
    }

    private fun transformDeleteAgainstInsert(
        operation1: DeleteOperation,
        operation2: InsertOperation
    ): PlainTextSingleCharacterOperation = when {
        operation1.position < operation2.position -> operation1
        else -> operation1.copy(position = operation1.position + 1)
    }

    private fun transformDeleteAgainstDelete(
        operation1: DeleteOperation,
        operation2: DeleteOperation
    ): PlainTextSingleCharacterOperation = when {
        operation1.position < operation2.position -> operation1
        operation1.position > operation2.position -> operation1.copy(position = operation1.position - 1)
        else -> IdentityOperation(operation1.uuid)
    }

}