package ot.service

import ot.command.OperationApplicationCommand

interface ClientDocumentManager<T, O : Operation<T>> {

    fun processLocalOperation(operation: O): OperationApplicationCommand<O>

    fun processRemoteOperation(operation: O): OperationApplicationCommand<O>
}