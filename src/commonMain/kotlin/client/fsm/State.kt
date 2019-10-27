package client.fsm

import client.command.OperationApplicationCommand
import ot.service.Operation

interface State<T, O : Operation<T>> {

    val clientFSM: ClientFSM<T, O>

    fun processLocalOperation(operation: O): OperationApplicationCommand<O>

    fun processRemoteOperation(operation: O): OperationApplicationCommand<O>

}