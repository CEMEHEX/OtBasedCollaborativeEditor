package client

import client.command.OperationApplicationCommand
import ot.Operation

interface ClientDocumentManager<T, O : Operation<T>> {

    fun processLocalOperation(operation: O): OperationApplicationCommand<O>

    fun processRemoteOperation(operation: O): OperationApplicationCommand<O>
}