package ot.client

import ot.client.command.OperationApplicationCommand
import ot.service.Operation

interface ClientDocumentManager<T, O : Operation<T>> {

    fun processLocalOperation(operation: O): OperationApplicationCommand<O>

    fun processRemoteOperation(operation: O): OperationApplicationCommand<O>
}