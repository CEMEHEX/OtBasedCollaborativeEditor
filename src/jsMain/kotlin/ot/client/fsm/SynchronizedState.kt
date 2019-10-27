package ot.client.fsm

import ot.client.command.ApplyOperationLocally
import ot.client.command.OperationApplicationCommand
import ot.client.command.SendOperationToServer
import ot.service.Operation
import ot.service.OperationsManager

class SynchronizedState<T, O : Operation<T>>(
    override val clientFSM: ClientFSM<T, O>,
    private val operationsManager: OperationsManager<O>,
    val revision: Int
) : State<T, O> {

    override fun processLocalOperation(operation: O): OperationApplicationCommand<O> {
        clientFSM.state = AwaitConfirmationState(clientFSM, operationsManager, operation)
        return SendOperationToServer(operation)
    }

    override fun processRemoteOperation(operation: O): OperationApplicationCommand<O> {
        clientFSM.state = SynchronizedState(clientFSM, operationsManager, operation.revision)
        return ApplyOperationLocally(operation)
    }
}