package ot.impl

import ot.OperationsManager

class PlainTextOperationsManager : OperationsManager<PlainTextSingleCharacterOperation> {

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

    // TODO id must change, use id generator or something
    override fun invert(operation: PlainTextSingleCharacterOperation): PlainTextSingleCharacterOperation = when(operation) {
        is InsertOperation -> {
            val (id, revision, position, symbol) = operation
            DeleteOperation(id, revision, position, symbol) // TODO
        }
        is DeleteOperation -> {
            val (id, revision, position, symbol) = operation
            InsertOperation(id, revision, position, symbol) // TODO
        }
        is IdentityOperation -> operation // TODO
    }

    override fun changeRevision(
            operation: PlainTextSingleCharacterOperation,
            newRevision: Int
    ): PlainTextSingleCharacterOperation = when (operation) {
        is InsertOperation -> operation.copy(revision = newRevision)
        is DeleteOperation -> operation.copy(revision = newRevision)
        is IdentityOperation -> operation.copy(revision = newRevision)
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
        else -> InsertOperation(operation1.id, operation1.revision, operation1.position - 1, operation1.symbol)
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
        else -> IdentityOperation(operation1.id, operation1.revision)
    }

}